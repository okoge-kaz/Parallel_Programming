// 藤井一喜 20B30790
package para;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import para.graphic.parser.MainParser;
import para.graphic.shape.Attribute;
import para.graphic.shape.OrderedShapeManager;
import para.graphic.shape.Rectangle;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Vec2;
import para.graphic.target.JavaFXTarget;
import para.graphic.target.Target;
import para.graphic.target.TranslateTarget;
import para.graphic.target.TranslationRule;

/**
 * クライアントからの通信を受けて描画するサーバプログラム。
 * 監視ポートは30000番
 */
public class Main07 {
  final public int PORT_NUMBER = 30000;
  final int MAX_CONNECTION = 3;
  final Target target;
  final ShapeManager[] shapeManagerArray;
  final ServerSocket serverSocket;
  ExecutorService executorService;
  // Executor executorService;

  /**
   * 受け付け用ソケットを開くこと、受信データの格納場所を用意すること
   * を行う
   */
  public Main07() {
    target = new JavaFXTarget("Server", 320 * MAX_CONNECTION, 240);
    // target = new TextTarget(System.out);
    ServerSocket tmp = null;
    executorService = Executors.newFixedThreadPool(MAX_CONNECTION);
    try {
      tmp = new ServerSocket(PORT_NUMBER);
    } catch (IOException ex) {
      System.err.println(ex);
      System.exit(1);
    }
    serverSocket = tmp;
    shapeManagerArray = new ShapeManager[MAX_CONNECTION];
    for (int i = 0; i < MAX_CONNECTION; i++) {
      shapeManagerArray[i] = new OrderedShapeManager();
    }
  }

  /**
   * 受け付けたデータを表示するウィンドウの初期化とそこに受信データを表示するスレッドの開始
   */
  public void init() {
    target.init();
    target.clear();
    target.flush();
    new Thread(() -> {
      while (true) {
        target.clear();
        for (ShapeManager sm : shapeManagerArray) {
          synchronized (sm) {
            target.draw(sm);
          }
        }
        target.flush();
        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
      }
    }).start();
  }

  /**
   * 受信の処理をする
   */
  public void start() {
    int threadIndex = 0;
    while (true) {
      while (true) {
        try {
          Socket socket = serverSocket.accept();
          executorService.execute(new MyThread(socket, shapeManagerArray[threadIndex], threadIndex));
          threadIndex = (threadIndex + 1) % MAX_CONNECTION;
        } catch (IOException ex) {
          System.err.print(ex);
        }
      }
    }
  }

  class MyThread extends Thread {
    final Socket socket;
    ShapeManager shapeManager;
    int threadIndex;

    public MyThread(Socket socket, ShapeManager shapeManager, int index) {
      this.socket = socket;
      this.shapeManager = shapeManager;
      this.threadIndex = index;
    }

    public void run() {
      try {
        BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ShapeManager dummy = new ShapeManager();

        shapeManager.clear();
        shapeManager.put(new Rectangle(10000 * threadIndex, 320 * threadIndex, 0, 320, 240,
            new Attribute(0, 0, 0, true)));

        MainParser parser = new MainParser(new TranslateTarget(shapeManager,
            new TranslationRule(10000 * threadIndex, new Vec2(320 * threadIndex, 0))),
            dummy);
        parser.parse(new Scanner(r));

      } catch (IOException ex) {
        System.err.print(ex);
      }
    }
  }

  public static void main(String[] args) {
    Main07 m = new Main07();
    m.init();
    m.start();
  }
}
