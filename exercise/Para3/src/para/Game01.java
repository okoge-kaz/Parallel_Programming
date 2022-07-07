// 20B30790 藤井一喜
package para;

import java.util.Random;

import para.game.GameFrame;
import para.game.SynchronizedPoint;
import para.graphic.shape.Attribute;
import para.graphic.shape.Circle;
import para.graphic.shape.Digit;
import para.graphic.shape.InsideChecker;
import para.graphic.shape.Shape;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Vec2;
import para.graphic.target.JavaFXCanvasTarget;
import para.graphic.target.JavaFXTarget;
import para.graphic.target.Target;

/**
 * モグラたたきゲームの雛形
 */
public class Game01 extends GameFrame {
  final static int WIDTH = 400;
  final static int HEIGHT = 700;
  final int MCOUNT = 9;
  final int XCOUNT = 3;
  Target inputside;
  Target outputside;

  Thread thread;
  Thread thread2;

  ShapeManager osm;
  ShapeManager ism;
  private long prev;
  Random rand;
  private int last;
  private int[] slot;
  Attribute mogattr;

  private volatile int currentPoint = 0;

  private final JavaFXTarget javaFxTarget = new JavaFXTarget("score board", 320, 240);
  private final ShapeManager shapeProject = new ShapeManager();

  public Game01() {
    super(new JavaFXCanvasTarget(WIDTH, HEIGHT));
    canvas.init();

    title = "Mole";
    outputside = canvas;
    inputside = canvas;

    osm = new ShapeManager();
    ism = new ShapeManager();
    rand = new Random(System.currentTimeMillis());
    slot = new int[MCOUNT];
    mogattr = new Attribute(158, 118, 38);

  }

  @Override
  public void gamestart(int v) {
    javaFxTarget.init();

    if (thread != null) {
      return;
    }
    thread = new Thread(() -> {
      Attribute attr = new Attribute(250, 80, 80);
      this.shapeProject.put(new Digit(100, 230, 120, 30, this.currentPoint % 10, new Attribute(200, 200, 200)));
      this.shapeProject
          .put(new Digit(101, 160, 120, 30, (this.currentPoint % 100) / 10, new Attribute(200, 200, 200)));
      this.shapeProject.put(new Digit(102, 90, 120, 30, this.currentPoint / 100, new Attribute(200, 200, 200)));

      javaFxTarget.clear();
      javaFxTarget.draw(shapeProject);
      javaFxTarget.flush();

      while (true) {

        try {
          Thread.sleep(80);
        } catch (InterruptedException ex) {
        }
        SynchronizedPoint p = xy.copy();
        if (prev != p.getTime()) {
          prev = p.getTime();
          ism.put(new Circle(v, (int) p.getXY()[0], (int) p.getXY()[1],
              20, attr));
          Shape s = InsideChecker.check(osm,
              new Vec2(p.getXY()[0], p.getXY()[1]));
          if (s != null) {
            slot[(s.getID() - 10) / 10] = 0;
            System.out.println(p.getXY()[0] + " " + p.getXY()[1] + " " + p.getTime());

            this.addValue(10);
            this.shapeProject.put(new Digit(100, 230, 120, 30, this.currentPoint % 10, new Attribute(200, 200, 200)));
            this.shapeProject
                .put(new Digit(101, 160, 120, 30, (this.currentPoint % 100) / 10, new Attribute(200, 200, 200)));
            this.shapeProject.put(new Digit(102, 90, 120, 30, this.currentPoint / 100, new Attribute(200, 200, 200)));

            javaFxTarget.clear();
            javaFxTarget.draw(shapeProject);
            javaFxTarget.flush();
            try {
              Thread.sleep(70);
            } catch (InterruptedException ex) {
            }
          }
        } else if (300 < System.currentTimeMillis() - prev) {
          ism.remove(v);
        }
        inputside.clear();
        mole();
        inputside.draw(ism);
        inputside.flush();
      }
    });
    thread.start();
    thread2.start();

  }

  private void mole() {

    int appear = rand.nextInt(40);
    if (30 < appear) {
      int head = rand.nextInt(MCOUNT - 1) + 1;
      int duration = rand.nextInt(600) + 200;

      if (slot[(last + head) % MCOUNT] <= 0) {
        slot[(last + head) % MCOUNT] = duration;
      }
    }
    for (int i = 0; i < MCOUNT; i++) {
      slot[i] = slot[i] - 10;
    }
    for (int i = 0; i < MCOUNT; i++) {
      if (0 < slot[i]) {
        int delegateid = 10 + i * 10;
        int x = (i % XCOUNT) * 130 + 60;
        int y = (i / XCOUNT) * 130 + 60;
        int level = slot[i] / 25;
        Garden.setMole(delegateid, x, y, level, osm);
      } else {
        Garden.removeMole(10 + i * 10, osm);
      }
    }
    inputside.draw(osm);
  }

  private synchronized void addValue(long v) {
    this.currentPoint += v;
    if (this.currentPoint > 100) {
      this.thread2.interrupt();
      this.thread.interrupt();
    }
  }
}
