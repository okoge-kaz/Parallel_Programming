package para;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import para.graphic.shape.Camera;
import para.graphic.target.Target;

/**
 * カメラ画像の図形を出力する際、減色処理を行うフィルタを実装したTargetの装飾クラス
 * 減色時の代表色をカメラ画像に応じて自動調整する
 */
public class TargetColorFilter2 extends TargetColorFilter {
  final int SCOUNT1;
  final int SCOUNT3;

  /**
   * @param target        装飾対象のTargetクラス
   * @param quantizeCount 3原色の1色当たりの量子化数
   */
  public TargetColorFilter2(Target target, int quantizeCount) {
    super(target, quantizeCount);
    SCOUNT1 = quantizeCount;
    SCOUNT3 = SCOUNT1 * SCOUNT1 * SCOUNT1;
  }

  @Override
  protected ByteBuffer process(ByteBuffer input) {
    byte[] tmp = new byte[3];
    input.rewind();
    for (int i = 0; i < input.limit() / 3; i++) {
      input.get(tmp, 0, 3);
      inimage[i] = ((tmp[2] & 0xff) << 16) | ((tmp[1] & 0xff) << 8) | (tmp[0] & 0xff);
    }
    ArrayList<RGB> averageRGBArray = new ArrayList<RGB>(SCOUNT3);
    for (int i = 0; i < SCOUNT3; i++) {
      averageRGBArray.add(new RGB());
    }
    // 1番目のStream処理
    IntStream.range(0, Camera.WIDTH * Camera.HEIGHT)
        .forEach(n -> {
          int min = 600000;
          int label = 0;
          for (int j = 0; j < sample.length; j++) {
            int dp = distancePow(inimage[n], sample[j]);
            if (dp < min) {
              label = j;
              min = dp;
            }
          }
          averageRGBArray.get(label).add(inimage[n]);
          /*
           * add のあたりで問題が起きている。共有しているので??
           * スレッドセーフ、同期をとるようにするなど...
           */
        });
    // 2番目のStream処理
    IntStream.range(0, SCOUNT3).forEach(n -> {
      RGB rgb = averageRGBArray.get(n);
      sample[n] = rgb.get();
    });

    // 3番目のStream処理
    Map<Integer, Integer> classified = IntStream.range(0, Camera.WIDTH * Camera.HEIGHT)
        .boxed()
        .collect(Collectors.toMap(n -> n,
            n -> {
              int min = 600000;
              int label = 0;
              for (int j = 0; j < sample.length; j++) {
                int dp = distancePow(inimage[n], sample[j]);
                if (dp < min) {
                  label = j;
                  min = dp;
                }
              }
              return label;
            }));

    classified.forEach((k, v) -> {
      outimage[k * 4 + 0] = (byte) 0xff;
      outimage[k * 4 + 1] = (byte) ((sample[v] >> 16) & 0xff);
      outimage[k * 4 + 2] = (byte) ((sample[v] >> 8) & 0xff);
      outimage[k * 4 + 3] = (byte) (sample[v] & 0xff);
    });
    /*
     * do no filtering, just through output
     * classified.forEach((k,v)->{outimage[k*4+0]=(byte)0xff;
     * outimage[k*4+1]=(byte)((inimage[k]>>16)&0xff);
     * outimage[k*4+2]=(byte)((inimage[k]>>8)&0xff);
     * outimage[k*4+3]=(byte)(inimage[k]&0xff);});
     */
    input.rewind();
    outbuffer.rewind();
    return outbuffer;
  }

  /** 平均色を計算するための補助クラス */
  protected class RGB {
    protected int r, g, b, c;

    protected RGB() {
      r = 0;
      g = 0;
      b = 0;
      c = 0;
    }

    /**
     * 色を加算する
     * 
     * @param color 色を表す4バイトのint型
     */
    protected void add(int color) {
      c++;
      r += ((color & 0xff0000) >> 16);
      g += ((color & 0x00ff00) >> 8);
      b += ((color & 0x0000ff));

      // * synchronized block で囲むとうの処理が必要??
    }

    /**
     * 平均色を返す
     * 
     * @return add()で加算されてきた色の平均色をint型の4バイト1つで表した値
     */
    protected int get() {
      int ret;
      if (c != 0) {
        ret = ((r / c) << 16) | ((g / c) << 8) | (b / c);
      } else {
        ret = (127 << 16) | (127 << 8) | 127;
      }
      return ret;
    }
  }
}
