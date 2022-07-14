// 20B30790 藤井一喜
package para;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import para.graphic.parser.MainParser;
import para.graphic.shape.Attribute;
import para.graphic.shape.Camera;
import para.graphic.shape.Rectangle;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Vec2;
import para.graphic.target.Target;
import para.graphic.target.TextTarget;
import para.graphic.target.TranslateTarget;
import para.graphic.target.TranslationRule;

/**
 * カメラを起動し、サーバに送るデモ
 */
public class Main08 {
  /**
   * メインメソッド
   * 
   * @param args args[0]は相手先のホスト
   */
  ExecutorService threadPool;
  final int MAX_CONNECTION = 3;
  Socket socket;

  public Main08() {

    // スレッドプールの作成
    threadPool = Executors.newFixedThreadPool(MAX_CONNECTION);
  }

  public void start(String[] args) {
    final int PORT_NUMBER = 30000;

    ShapeManager shapeManager = new ShapeManager();
    shapeManager.put(new Camera(0, 0, 0));

    try (Socket socket = new Socket(args[0], PORT_NUMBER)) {

      PrintStream printStream = new PrintStream(socket.getOutputStream());
      Target target = new TextTarget(printStream);
      // Target target = new TextTarget(System.out);

      new SendThread(socket, shapeManager, printStream, target).start();

      target.init();
      target.clear();
      new Thread(() -> {
        while (true) {
          // 描画
          target.draw(shapeManager);
          target.flush();
          try {
            Thread.sleep(100);
          } catch (InterruptedException ex) {
          }
          // error handling
          if (printStream.checkError()) {
            break;
          }
        }
      }).start();
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }

  class SendThread extends Thread {
    final Socket socket;
    final ShapeManager shapeManager;
    final PrintStream printStream;
    final Target target;

    public SendThread(Socket socket, ShapeManager shapeManager, PrintStream printStream, Target target) {
      this.socket = socket;
      this.shapeManager = shapeManager;
      this.printStream = printStream;
      this.target = target;
    }

    @Override
    public void run() {
      try (Socket socket = this.socket) {
        target.init();
        target.clear();

        while (true) {
          target.draw(shapeManager);
          target.flush();
          try {
            Thread.sleep(100);
          } catch (InterruptedException ex) {
          }
          if (printStream.checkError()) {
            System.err.println("print steam check error");
            break;
          }
        }
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }

  class ReceiveThread extends Thread {
    /*
     * curl -v telnet://localhost:30000 で接続することで shapeManager を更新することができる。
     */
    final Socket socket;
    ShapeManager shapeManager;
    int threadIndex;

    public ReceiveThread(Socket socket, ShapeManager shapeManager, int threadIndex) {
      this.socket = socket;
      this.shapeManager = shapeManager;
      this.threadIndex = threadIndex;
    }

    @Override
    public void run() {
      try (Socket socket = this.socket) {
        while (true) {
          BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          ShapeManager dummy = new ShapeManager();

          this.shapeManager.clear();
          this.shapeManager.put(new Rectangle(10000 * threadIndex, 320 * threadIndex, 0, 320, 240,
              new Attribute(0, 0, 0, true)));

          MainParser parser = new MainParser(new TranslateTarget(shapeManager,
              new TranslationRule(10000 * threadIndex, new Vec2(320 * threadIndex, 0))),
              dummy);
          parser.parse(new Scanner(r));
        }
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }

  public static void main(String[] args) {
    Main08 m = new Main08();
    m.start(args);
  }
}
