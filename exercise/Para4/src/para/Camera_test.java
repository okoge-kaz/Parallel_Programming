package para;

import para.graphic.shape.Camera;
import para.graphic.target.JavaFXTarget;
import para.graphic.target.Target;

/** カメラを起動し、表示するデモ **/
public class Camera_test {

  public static void main(String[] args) {
    final Target target = new JavaFXTarget("Camera test", 320, 240);
    final Camera cam = new Camera(0, 0, 0);
    target.init();
    target.clear();
    target.flush();
    while (true) {
      target.clear();
      cam.draw(target);
      target.flush();
    }
  }
}