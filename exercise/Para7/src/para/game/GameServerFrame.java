package para.game;

import java.net.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.AbstractQueue;
import java.util.concurrent.ArrayBlockingQueue;
import para.graphic.parser.MainParser;
import para.graphic.target.Target;
import para.graphic.target.TextTarget;
import para.graphic.shape.ShapeManager;

public class GameServerFrame extends Thread {
  public static final int PORTNO = 30001;
  public final AbstractQueue<GameInputThread> queue;
  private final GameTextTarget[] useroutput;
  public final ArrayList<GameInputThread> array;

  private final int maxconnection;
  private int users;
  private ServerSocket ss = null;

  public GameServerFrame(int maxconnection) {
    this.maxconnection = maxconnection;
    queue = new ArrayBlockingQueue<GameInputThread>(maxconnection);
    useroutput = new GameTextTarget[maxconnection];
    array = new ArrayList<GameInputThread>(maxconnection);
    for (int i = 0; i < maxconnection; i++) {
      useroutput[i] = null;
    }
    users = 0;
  }

  public void init() throws IOException {
    ss = new ServerSocket(PORTNO);
  }

  public void welcome() {
    start();
  }

  public void run() {
    loop();
  }

  private void loop() {
    while (true) {
      Socket s;
      synchronized (this) {
        while ((maxconnection - 1) < users) {
          /*
           * 接続数可能最大数 < users + 1 ならば、もう１人接続することはできないので、まつ
           * この機能がないと、接続可能数を超えて socket accept してしまうことになるため
           */
          try {
            wait();
          } catch (InterruptedException ex) {
          }
        }
        users++;
      }
      try {
        // accept
        s = ss.accept();
        int id;
        synchronized (this) {
          id = decideUserID();// 勝手に user 0か user 1かが割り振られる。（空いている方が割り当てられる）
          useroutput[id] = new GameTextTarget(s.getOutputStream());
          InputStream is = s.getInputStream();
          GameInputThread th = new GameInputThread(is, id, this);
          array.add(th);
          queue.add(th);
        }
      } catch (IOException ex) {
        System.err.print(ex);
      }
    }
  }

  public synchronized GameTextTarget getUserOutput(int id) {
    return useroutput[id];
  }

  private synchronized int decideUserID() {
    for (int i = 0; i < maxconnection; i++) {
      if (useroutput[i] == null) {
        return i;
      }
    }
    return -1;
  }

  public synchronized void removeUser(int num) {
    users--;
    useroutput[num] = null;
    for (GameInputThread th : array) {
      if (th.userID == num) {
        array.remove(th);
        break;
      }
    }
    notifyAll();
  }
}
