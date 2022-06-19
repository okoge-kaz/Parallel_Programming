package para.calc;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * JavaFX 電卓アプリケーションのメインクラス
 */
public class Calculator extends Application {
  Label input;
  Label output;
  StringBuilder buff;
  Executor ex;

  public Calculator() {
    input = new Label();
    output = new Label();
    buff = new StringBuilder();
    ex = new Executor1();
  }

  String[] buttonname = { "9", "8", "7", "+",
      "6", "5", "4", "-",
      "3", "2", "1", "*",
      "0", ".", ",", "/" };

  public void start(Stage stage) {
    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);
    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200, 300);
    Button[] buttons = new Button[16];
    Button buttoncal = new Button("=");
    buttoncal.setPrefHeight(56);
    Button buttondel = new Button("<");
    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140, 30, Color.WHITE));
    stack.getChildren().add(input);
    root.getChildren().addAll(stack, output);
    for (int i = 0; i < 16; i++) {
      buttons[i] = new Button(buttonname[i]);
    }

    stage.setWidth(200);
    stage.setHeight(200);
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
