package para.calc;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Executor2 extends ExecutorBase implements Executor {
  Label label;
  MyThread last;

  public Executor2(Label label) {
    super();
    this.label = label;
  }

  public void writeState(String state) {
    System.err.println(Thread.currentThread().getName());// hint
    System.out.print(state);
    Platform.runLater(() -> {
      label.setText(state);
      System.err.println(Thread.currentThread().getName());// hint
    });
  }

  synchronized public String operation(String data) {
    MyThread th = new MyThread(data, last);
    last = th;
    last.start();
    return result;
  }

  class MyThread extends Thread {
    MyThread last;
    String data;

    MyThread(String data, MyThread last) {
      this.data = data;
      this.last = last;
    }

    public void run() {
      if (last != null) {
        try {
          last.join();
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