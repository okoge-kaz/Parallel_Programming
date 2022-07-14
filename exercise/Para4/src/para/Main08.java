package para;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

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

  public Main08(String[] args) {
    final int PORT_NUMBER = 30000;

    ShapeManager shapeManager = new ShapeManager();
    shapeManager.put(new Camera(0, 0, 0));

    try (Socket socket = new Socket(args[0], PORT_NUMBER)) {

      PrintStream printStream = new PrintStream(socket.getOutputStream());
      Target target = new TextTarget(printStream);
      // Target target = new TextTarget(System.out);

      target.init();
      target.clear();

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
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }

  public static void main(String[] args) {
    new Main08(args);
  }

  class SendingThread extends Thread {
    final Socket socket;
    ShapeManager shapeManager;
    int threadIndex;

    public SendingThread(Socket socket, ShapeManager shapeManager, int threadIndex) {
      this.socket = socket;
      this.shapeManager = shapeManager;
      this.threadIndex = threadIndex;
    }

    @Override
    public void run() {
      try (Socket socket = this.socket) {
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        while (true) {
          synchronized (shapeManager) {
            printStream.println(shapeManager.toString());
          }
          try {
            Thread.sleep(100);
          } catch (InterruptedException ex) {
          }
        }
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }

  class RecievingThread extends Thread {
    final Socket socket;
    ShapeManager[] shapeManagerArray;
    int threadIndex;

    public RecievingThread(Socket socket, ShapeManager[] shapeManagerArray, int threadIndex) {
      this.socket = socket;
      this.shapeManagerArray = shapeManagerArray;
      this.threadIndex = threadIndex;
    }

    @Override
    public void run() {
      try (Socket socket = this.socket) {
        while (true) {
          BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          ShapeManager dummy = new ShapeManager();

          this.shapeManagerArray[threadIndex].clear();
          this.shapeManagerArray[threadIndex].put(new Rectangle(10000 * threadIndex, 320 * threadIndex, 0, 320, 240,
              new Attribute(0, 0, 0, true)));

          MainParser parser = new MainParser(new TranslateTarget(shapeManagerArray[threadIndex],
              new TranslationRule(10000 * threadIndex, new Vec2(320 * threadIndex, 0))),
              dummy);
          parser.parse(new Scanner(r));
        }
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}
