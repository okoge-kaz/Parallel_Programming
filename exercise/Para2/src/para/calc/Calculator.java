package para.calc;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.Pos;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

/**
 * JavaFX 電卓アプリケーションのメインクラス
 */
public class Calculator extends Application {
  Label inputLabel;
  Label outputLabel;
  StringBuilder buff;
  Executor executor;

  public Calculator() {
    inputLabel = new Label();
    outputLabel = new Label();
    buff = new StringBuilder();
    executor = new Executor1();
  }

  String[] buttonName = { "9", "8", "7", "+",
      "6", "5", "4", "-",
      "3", "2", "1", "*",
      "0", ".", ",", "/" };

  public void start(Stage stage) {

    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);

    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200, 200);
    Button[] buttons = new Button[16];

    Button buttonCalculation = new Button("=");
    double tmph = buttonCalculation.getHeight();
    buttonCalculation.setPrefHeight(56);

    Button buttonDelete = new Button("<");
    buttonDelete.setPrefHeight(56);

    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140, 30, Color.WHITE));
    stack.getChildren().add(inputLabel);
    root.getChildren().addAll(stack, outputLabel);
    root.getChildren().add(grid);
    grid.setAlignment(Pos.CENTER);

    for (int i = 0; i < 16; i++) {
      buttons[i] = new Button(buttonName[i]);
      buttons[i].setPrefHeight(26);
      buttons[i].setPrefWidth(26);
      grid.add(buttons[i], i % 4, i / 4);
    }

    grid.add(buttonDelete, 4, 0, 1, 2);
    grid.add(buttonCalculation, 4, 2, 1, 2);

    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
