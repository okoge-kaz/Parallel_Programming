// 藤井一喜 20B30790
package para;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import para.graphic.target.TextTarget;
import para.graphic.target.TranslateTarget;
import para.graphic.target.TranslationRule;

/**
 * クライアントからの通信を受けて描画するサーバプログラム。
 * 監視ポートは30000番
 */
public class Main09 {
  final public int PORT_NUMBER = 30000;
  final int MAX_CONNECTION = 3;
  final Target javaFXTarget;
  TextTarget textTarget;
  final ShapeManager[] shapeManagerArray;
  final ServerSocket serverSocket;
  ExecutorService threadPool;
  final BufferedReader[] bufferedReaderArray;
  final PrintWriter[] printWriterArray;

  /**
   * 受け付け用ソケットを開くこと、受信データの格納場所を用意すること
   * を行う
   */
  public Main09() {
    // target declaration
    javaFXTarget = new JavaFXTarget("Server", 320 * MAX_CONNECTION, 240);
    textTarget = new TextTarget(System.out);

    // server socket declaration
    ServerSocket tmp = null;
    try {
      // server socket を新しく作成する
      tmp = new ServerSocket(PORT_NUMBER);
    } catch (IOException ex) {
      // server socket の作成に失敗したときは、エラーを出力して終了する
      System.err.println(ex);
      System.exit(1);
    }
    serverSocket = tmp;

    // shape manager array declaration
    // クライアントからアクセスされた際に描画が行われる MAX_CONNECTION 個の ShapeManager を作成する
    shapeManagerArray = new ShapeManager[MAX_CONNECTION];
    for (int i = 0; i < MAX_CONNECTION; i++) {
      shapeManagerArray[i] = new OrderedShapeManager();
    }

    // print stream array declaration
    printWriterArray = new PrintWriter[MAX_CONNECTION];
    for (int i = 0; i < MAX_CONNECTION; i++) {
      printWriterArray[i] = new PrintWriter(System.out);
    }

    // buffered reader array declaration
    bufferedReaderArray = new BufferedReader[MAX_CONNECTION];
    for (int i = 0; i < MAX_CONNECTION; i++) {
      bufferedReaderArray[i] = new BufferedReader(new InputStreamReader(System.in));
    }

    // スレッドプールの作成
    threadPool = Executors.newFixedThreadPool(MAX_CONNECTION);
  }

  /**
   * 受け付けたデータを表示するウィンドウの初期化とそこに受信データを表示するスレッドの開始
   */
  public void init() {
    /*
     * Main09 が実行された時に、クライアントからのデータを受け取るためのスレッドを起動する
     * new Thread() start によって開始される無名のスレッドがこの役割を担う
     */

    // 描画する JavaFX のターゲットを初期化
    javaFXTarget.init();
    javaFXTarget.clear();
    javaFXTarget.flush();
    // クライアントに送るデータのターゲットを初期化
    textTarget.init();
    textTarget.clear();
    textTarget.flush();

    // 受信用スレッド
    new Thread(() -> {
      while (true) {
        javaFXTarget.clear();
        for (ShapeManager sm : shapeManagerArray) {
          /*
           * 1. 描画対象をclearする
           * 2. MAX_CONNECTION 個の ShapeManager の配列から順番に shapeManager を取り出す
           * 3. synchronized で shapeManager の lock を取得する
           * 4. shapeManager の shape の描画準備を行う
           * 5. flush で shape の描画を行う
           */
          synchronized (sm) {
            javaFXTarget.draw(sm);
          }
        }
        javaFXTarget.flush();
        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
      }
    }).start();

    // 送信用スレッド
    new Thread(() -> {
      while (true) {

        synchronized (bufferedReaderArray) {
          for (int i = 0; i < MAX_CONNECTION; i++) {
            if (bufferedReaderArray[i] == null) {
              System.out.println("bufferedReaderArray[" + i + "] is null");
            }
          }
        }
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
    /*
     * init() でクライアントからのデータを受け取る準備が完了した。
     * また、init() で起動したスレッドは一定間隔で shapeManagerArray の中身を再描画する処理を行う。
     * start() では スレッドがデータを socket より受け取ることができるようにする処理を行う。
     */
    int shapeManagerIndex = 0;
    while (true) {
      while (true) {
        try {
          Socket socket = serverSocket.accept();
          threadPool.execute(new MyThread(socket, shapeManagerArray[shapeManagerIndex], shapeManagerIndex));
          shapeManagerIndex = (shapeManagerIndex + 1) % MAX_CONNECTION;
        } catch (IOException ex) {
          System.err.print(ex);
        }
      }
    }
  }

  class MyThread extends Thread {
    private final Socket socket;
    private ShapeManager shapeManager;
    private int shapeManagerIndex;

    /*
     * socket: このスレッドが通信するソケット すでに connect されている
     * shapeManager: このスレッドが描画する対象の ShapeManager
     * threadIndex: このスレッドが描画する対象の shapeManager の index
     */

    public MyThread(Socket socket, ShapeManager shapeManager, int index) {
      this.socket = socket;
      this.shapeManager = shapeManager;
      this.shapeManagerIndex = index;
    }

    @Override
    public void run() {
      /*
       * Java は Thread.start() メソッドを呼び出すと、自動的に run() メソッドが呼び出される。
       * Thread.start() では、新しく開始したスレッド上で run() メソッドが呼び出される。
       * 逆に Thread.run()としてしまうと、新規にされたスレッドではなく、呼び出し元のスレッド上で run() メソッドが呼び出される。
       */
      try {
        /*
         * 1. socket.getInputStream() により クライアントデータを受け取るためのストリームを取得する。
         */
        final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 受信したデータを描画する準備のためにshapeManagerに受け取ったデータを解釈後追加する
        ShapeManager dummy = new ShapeManager();
        this.shapeManager.clear();
        this.shapeManager.put(new Rectangle(10000 * shapeManagerIndex, 320 * shapeManagerIndex, 0, 320, 240,
            new Attribute(0, 0, 0, true)));

        MainParser parser = new MainParser(new TranslateTarget(shapeManager,
            new TranslationRule(10000 * shapeManagerIndex, new Vec2(320 * shapeManagerIndex, 0))),
            dummy);
        parser.parse(new Scanner(bufferReader));

        // 受信したデータをまとめてクライアントへ再送信するために準備を行う。実際にデータを送信するのは 送信用スレッドである。
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        synchronized (bufferedReaderArray[shapeManagerIndex]) {
          bufferedReaderArray[shapeManagerIndex] = bufferReader;
        }
        synchronized (printWriterArray[shapeManagerIndex]) {
          printWriterArray[shapeManagerIndex] = printWriter;
        }

      } catch (IOException ex) {
        System.err.print(ex);
      }
    }
  }

  public static void main(String[] args) {
    Main09 m = new Main09();
    m.init();
    m.start();
  }
}
