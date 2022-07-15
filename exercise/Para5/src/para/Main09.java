package para;

import para.graphic.shape.Camera;
import para.graphic.shape.OrderedShapeManager;
import para.graphic.shape.ShapeManager;
import para.graphic.target.JavaFXTarget;
import para.graphic.target.Target;

public class Main09 {
  public static void main(String[] args) {
    ShapeManager sm = new OrderedShapeManager();
    // Target target = new JavaFXTarget("color filter", 320,240);
    Target target = new TargetColorFilter(new JavaFXTarget("color filter",
        320, 240), 4);

    target.init();
    sm.put(new Camera(0, 0, 0));

    /*
     * 80 ms ごとの再レンダリング
     */
    while (true) {
      target.clear();
      target.draw(sm);
      target.flush();
      try {
        Thread.sleep(80);
      } catch (InterruptedException e) {
      }
    }
  }
}
