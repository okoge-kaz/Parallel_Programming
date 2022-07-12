// 藤井一喜 20B30790
package para.graphic.target;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import para.graphic.camera.CameraDevice;
import para.graphic.camera.CameraJavaCV;

/** 撮影装置のインスタンスを複数のターゲットで共有するためのクラス */
class SingleCamera {
  static private CameraDevice cameraDevice = null;

  private SingleCamera() {
  };

  static private boolean isStarted = false;

  /**
   * {@link SingleCamera}のインスタンスを作る
   * 
   * @param width  撮影画像の幅
   * @param height 撮影画像の高さ
   * @return 作成されたインスタンス
   */
  static synchronized SingleCamera create(int width, int height) {
    if (cameraDevice == null) {
      cameraDevice = new CameraJavaCV(width, height);
    }
    return new SingleCamera();
  }

  /**
   * 撮影装置を撮影可能状態にする
   * 
   * @return 撮影可能状態になれば true を返す
   */
  public boolean start() {
    synchronized (this) {
      if (isStarted) {
        return true;
      }
      isStarted = true;
    }
    return cameraDevice.start();
  }

  /**
   * 撮影装置を撮影停止状態にする
   * 
   * @return 撮影装置の状態を撮影状態から停止状態に変更できれば true もともと止まっていたり、
   *         停止できなければ false
   */
  public boolean stop() {
    synchronized (this) {
      if (!isStarted) {
        return false;
      }
      isStarted = false;
    }
    return cameraDevice.stop();
  }

  /**
   * 撮影画像の幅を返す
   * 
   * @return 幅
   */
  public int getWidth() {
    return cameraDevice.getWidth();
  }

  /**
   * 撮影画像の高さを返す
   * 
   * @return 高さ
   */
  public int getHeight() {
    return cameraDevice.getHeight();
  }

  /**
   * 撮影画像の1フレーム分のデータをコピーするために適した{@link BufferedImage}
   * を用意して返す
   * 
   * @return 新たに用意された{@link BufferedImage}
   */
  public BufferedImage createBufferedImage() {
    return cameraDevice.createBufferedImage();
  }

  /**
   * 撮影画像の1フレーム分のデータを返す
   * 
   * @param img 1フレーム分のデータがコピーされる {@link BufferedImage}
   *            getメソッドの中で一番使用を推奨。引数に渡す{@link BufferedImage}の作成には
   *            {@link createBufferedImage}が便利。
   */
  public void get(BufferedImage img) {
    start();
    cameraDevice.getBufferedImage(img);
  }

  /**
   * 撮影画像の1フレーム分のデータを返す
   * 
   * @param buffer 1フレーム分のデータがコピーされる {@link ByteBuffer}
   */
  public void get(ByteBuffer buffer) {
    start();
    cameraDevice.getByteBuffer(buffer);
  }

  /**
   * 撮影画像の1フレーム分のデータを返す
   * OS依存のデータ形式なため、使用は非推奨
   * 
   * @return 1フレーム分のデータ
   */
  public ByteBuffer get() {
    start();
    return cameraDevice.getRawByteBuffer();
  }
}
