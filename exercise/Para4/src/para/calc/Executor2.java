package para.calc;

import javafx.scene.control.*;
import javafx.application.Platform;

public class Executor2 extends ExecutorBase implements Executor{
  Label label;

  public Executor2(Label label){
    super();
    this.label = label;
  }

  public void writeState(String state){
    System.out.print(state);

    Platform.runLater(()->{
        label.setText(state);
      });
  }

  synchronized public String operation(String data){
    init(data);
    result = null;
    boolean isSuccess=true;
    while(isSuccess && scanner.hasNext()){
      isSuccess = oneStep();
    }
    return result;
  }
}
