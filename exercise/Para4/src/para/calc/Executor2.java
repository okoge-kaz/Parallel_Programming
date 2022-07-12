package para.calc;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Executor2 extends ExecutorBase implements Executor {
  Label label;
  MyThread currentThread;

  public Executor2(Label label) {
    super();
    this.label = label;
  }

  public void writeState(String state) {
    System.err.println("writeState Thread: " + Thread.currentThread().getName());// hint
    System.out.print(state);

    // 途中経過を表示するためのラベルに書き込む　JavaFX スレッド
    Platform.runLater(() -> {
      label.setText(state);
      System.err.println("setText Thread: " + Thread.currentThread().getName());// hint
    });
  }

  synchronized public String operation(String data) {
    MyThread newThread = new MyThread(data, currentThread);
    currentThread = newThread;
    currentThread.start();
    return result;
  }

  class MyThread extends Thread {
    MyThread lastThread;
    String data;

    MyThread(String data, MyThread currentThread) {
      this.data = data;
      this.lastThread = currentThread;
    }

    @Override
    public void run() {
      if (lastThread != null) {
        // すでに数式解釈用のスレッドが起動している場合は、そのスレッドを停止する
        // この処理をしないと無限にスレッドが起動し続けてしまう
        try {
          lastThread.join();
          System.out.println("last thread is finished");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      init(data);

      boolean isSuccess = true;
      while (isSuccess && scanner.hasNext()) {
        isSuccess = oneStep();
      }
      Platform.runLater(()->{
        label.setText(result);
      });

      try {
        Thread.sleep(2000);
      } catch (InterruptedException ex) {
      }
    }
  }
}