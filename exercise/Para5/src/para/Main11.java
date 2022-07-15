package para;

import java.util.Scanner;
import java.util.stream.IntStream;

import para.graphic.parser.MainParser;
import para.graphic.shape.Attribute;
import para.graphic.shape.CollisionChecker;
import para.graphic.shape.CollisionCheckerParallel2;
import para.graphic.shape.CollisionCheckerParallel3;
import para.graphic.shape.MathUtil;
import para.graphic.shape.OrderedShapeManager;
import para.graphic.shape.Rectangle;
import para.graphic.shape.Shape;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Vec2;
import para.graphic.target.JavaFXTarget;

public class Main11 {
  final JavaFXTarget javaFXTarget;
  final MainParser parser;
  final ShapeManager shapeManager, wall;

  Thread thread;

  final static String data = "shape 10 Circle 80 60 20 Attribute Color 225 105 0 Fill true\n" +
      "shape 11 Circle 1760 60 20 Attribute Color 225 105 0 Fill true\n" +
      "shape 12 Circle 80 900 20 Attribute Color 225 105 0 Fill true\n" +
      "shape 13 Circle 1760 900 20 Attribute Color 225 105 0 Fill true\n";

  Vec2 position;
  Vec2 velocity;

  volatile int bpos;
  final String selector;

  public Main11(String selector) {
    this.selector = selector;
    shapeManager = new OrderedShapeManager();
    wall = new OrderedShapeManager();
    javaFXTarget = new JavaFXTarget("Main11", 1840, 960);
    parser = new MainParser(javaFXTarget, shapeManager);
    parser.parse(new Scanner(data));
    Attribute wallattr = new Attribute(250, 230, 200, true, 0, 0, 0);
    wall.add(new Rectangle(0, 0, 0, 1840, 20, wallattr));
    wall.add(new Rectangle(1, 0, 0, 20, 960, wallattr));
    wall.add(new Rectangle(2, 1820, 0, 20, 960, wallattr));
    wall.add(new Rectangle(3, 0, 940, 1840, 20, wallattr));
    bpos = 150;
    position = new Vec2(200, 250);
    velocity = new Vec2(16 * 10, 61 * 10);
    // velocity = new Vec2(4, 61/4.0f);
  }

  public void start() {
    /*
     * コンストラクタの後にすぐに実行される。
     */
    IntStream.range(0, 445 * 225).forEach(n -> {
      int x = n % 445;
      int y = n / 445;
      shapeManager.add(new Rectangle(20 + n, 30 + x * 4, 30 + y * 4, 3, 3,
          new Attribute(250, 100, 250, true, 0, 0, 0)));
    });

    javaFXTarget.init();

    CollisionChecker collisionChecker;
    switch (selector) {
      case "SINGLE":
        collisionChecker = new CollisionCheckerParallel2(false);
        break;
      case "PARALLEL":
        collisionChecker = new CollisionCheckerParallel2(true);
        break;
      case "POOL":
        collisionChecker = new CollisionCheckerParallel3(20);
        break;
      default:
        collisionChecker = new CollisionCheckerParallel2(false);
    }

    thread = new Thread(new Runnable() {
      public void run() {
        int j = 0;
        float time;
        float[] stime = new float[] { 1.0f };
        float[] wtime = new float[] { 1.0f };
        long startTimeMS = System.currentTimeMillis();
        long count = -1;

        while (true) {
          count++;

          if (count == 100) {
            /*
             * 100単位時間で終了するので、経過時間を計算する。
             */
            long endTimeMS = System.currentTimeMillis();
            System.out.println((endTimeMS - startTimeMS) + "msec");
            System.exit(0);
          }

          j = (j + 2) % 120;

          javaFXTarget.clear();
          javaFXTarget.drawCircle(1000, (int) position.data[0], (int) position.data[1], 5,
              new Attribute(0, 0, 0, true, 0, 0, 0));
          javaFXTarget.draw(shapeManager);
          javaFXTarget.draw(wall);
          javaFXTarget.flush();

          time = 1.0f;
          while (0 < time) {
            stime[0] = time;
            wtime[0] = time;
            Vec2 tmpspos = new Vec2(position);
            Vec2 tmpsvel = new Vec2(velocity);
            Vec2 tmpwpos = new Vec2(position);
            Vec2 tmpwvel = new Vec2(velocity);
            Shape s = collisionChecker.check(shapeManager, tmpspos, tmpsvel, stime);
            Shape w = collisionChecker.check(wall, tmpwpos, tmpwvel, wtime);
            if (s != null) {
              shapeManager.remove(s);
              position = tmpspos;
              velocity = tmpsvel;
              time = stime[0];
            } else if (w != null) {
              position = tmpwpos;
              velocity = tmpwvel;
              time = wtime[0];
            } else {
              position = MathUtil.plus(position, MathUtil.times(velocity, time));
              time = 0;
            }
          }
        }

      }
    });
    thread.start();
  }

  public static void main(String[] args) {
    new Main11(args[0]).start();
  }
}
