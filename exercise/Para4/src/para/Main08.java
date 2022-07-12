package para;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import para.graphic.shape.Camera;
import para.graphic.shape.ShapeManager;
import para.graphic.target.Target;
import para.graphic.target.TextTarget;

/**
 * カメラを起動し、サーバに送るデモ
 */
public class Main08 {
  /**
   * メインメソッド
   * 
   * @param args args[0]は相手先のホスト
   */
  public static void main(String[] args) {
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
        
        if (printStream.checkError()) {
          break;
        }
      }
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }
}
