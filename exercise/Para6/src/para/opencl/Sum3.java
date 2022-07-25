package para.opencl;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLProgram;

import easycl.*;
import java.nio.*;
import java.io.IOException;
import java.io.*;
import java.util.*;

import static java.lang.System.*;
import static com.jogamp.opencl.CLMemory.Mem.*;

/**
 * OpenCLを使用例を知るための簡単なデモ。
 * BufferAとBufferBとBufferCのそれぞれのi番目の要素を足してBufferDのi番目に格納する
 */
public class Sum3 {
  /**
   * OpenCLの初期化、データ初期化、演算、結果出力、OpenCL資源の開放をする
   */
  public Sum3() {
    // OpenCL演算環境の準備
    CLSetup cl = CLSetupCreator.createCLSetup();
    cl.initContext();

    FloatBuffer floatBuffer;
    // data a の読み取り
    floatBuffer = loadData("data/dataa.txt");
    CLBuffer<FloatBuffer> bufferA = cl.createBuffer(floatBuffer, READ_ONLY);
    floatBuffer.rewind();

    // data b の読み取り
    floatBuffer = loadData("data/datab.txt");
    CLBuffer<FloatBuffer> bufferB = cl.createBuffer(floatBuffer, READ_ONLY);
    floatBuffer.rewind();

    // data c の読み取り
    floatBuffer = loadData("data/datac.txt");
    CLBuffer<FloatBuffer> bufferC = cl.createBuffer(floatBuffer, READ_ONLY);
    floatBuffer.rewind();

    // a + b + c の計算の用意
    CLBuffer<FloatBuffer> bufferD = cl.createFloatBuffer(floatBuffer.limit(), WRITE_ONLY);

    int dataSize = floatBuffer.limit();
    CLCommandQueue queue = cl.createQueue();

    // OpenCLプログラム
    CLProgram program = cl.createProgramFromResource(this, "sum3.cl");
    CLKernel kernel = program.createCLKernel("Sum3");
    kernel.putArgs(bufferA, bufferB, bufferC, bufferD);
    kernel.setArgs(4, dataSize);

    bufferD.getBuffer().rewind();

    // 演算の実行
    queue.putWriteBuffer(bufferA, false)
        .putWriteBuffer(bufferB, false)
        .putWriteBuffer(bufferC, false)
        .putBarrier()
        .put1DRangeKernel(kernel, 0, dataSize, 1)
        .putBarrier()
        .putReadBuffer(bufferD, true);

    // 結果の出力
    FloatBuffer fb = bufferD.getBuffer();
    fb.rewind();
    for (int i = 0; i < fb.limit(); i++) {
      System.out.print(fb.get() + " ");
    }
    System.out.println();

    cl.release();
  }

  /**
   * 引数で指定されたファイルから浮動小数の数値列を読み込み、その値を格納したバッファを返す。
   * {@link CLBuffer}に組み込むBufferは
   * 
   * @param fname 読み込み対象となるファイルの名
   * @return ダイレクトバッファとして領域確保した後、読み込んだ値を書き込んだ{@link FloatBuffer}
   */
  static public FloatBuffer loadData(String fname) {
    ArrayList<Float> flt = new ArrayList<Float>();
    Scanner sc = null;
    try {
      sc = new Scanner(new File(fname));
    } catch (FileNotFoundException e) {
      System.err.println(e);
      System.exit(1);
    }
    while (sc.hasNextFloat()) {
      flt.add(sc.nextFloat());
    }
    FloatBuffer fb = ByteBuffer.allocateDirect(flt.size() * java.lang.Float.SIZE / 8)
        // java.lang.Float.SIZE はfloat型のbit数を意味する定数
        .order(ByteOrder.nativeOrder())// float型を表すバイト順序にnativeを指定
        .asFloatBuffer();
    int i = 0;
    while (fb.hasRemaining()) {
      fb.put(flt.get(i));
      i++;
    }
    fb.rewind();
    return fb;
  }

  public static void main(String[] argv) {
    new Sum3();
  }
}
