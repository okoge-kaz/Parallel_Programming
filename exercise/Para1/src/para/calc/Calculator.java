package para.calc;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * JavaFX 電卓アプリケーションのメインクラス
 */
public class Calculator extends Application {
  Label inputLabel;
  Label outputLabel;
  StringBuilder stringBuffer;
  Executor executor;

  public Calculator() {
    inputLabel = new Label();
    outputLabel = new Label();
    stringBuffer = new StringBuilder();
    executor = new Executor1();
  }

  String[] buttonName = { "9", "8", "7", "+",
      "6", "5", "4", "-",
      "3", "2", "1", "*",
      "0", ".", ",", "/" };

  public void start(Stage stage) {

    VBox root = new VBox();
    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200, 300);
    Button[] buttons = new Button[16];

    Button buttonCalculationEqual = new Button("=");
    Button buttonCalculationInequality = new Button("<");

    StackPane stack = new StackPane();

    root.setAlignment(Pos.TOP_CENTER);
    root.getChildren().addAll(stack, outputLabel);

    buttonCalculationEqual.setPrefHeight(56);
    buttonCalculationEqual.setPrefWidth(28);
    buttonCalculationInequality.setPrefHeight(56);
    buttonCalculationInequality.setPrefWidth(28);

    stack.getChildren().add(new Rectangle(140, 30, Color.WHITE));
    stack.getChildren().add(inputLabel);

    for (int i = 0; i < 16; i++) {
      buttons[i] = new Button(buttonName[i]);
      buttons[i].setPrefHeight(28);
      buttons[i].setPrefWidth(28);
      grid.add(buttons[i], i % 4, i / 4);
    }

    VBox vBox = new VBox();
    vBox.getChildren().addAll(buttonCalculationEqual, buttonCalculationInequality);

    HBox hBox = new HBox();
    hBox.getChildren().addAll(grid, vBox);
    hBox.setAlignment(Pos.CENTER);

    root.getChildren().add(hBox);

    for (int i = 0; i < 16; i++) {
      Button targetButton = buttons[i];
      targetButton.setOnAction(e -> {
        stringBuffer.append(targetButton.getText());
        inputLabel.setText(stringBuffer.toString());
      });
    }

    buttonCalculationEqual.setOnAction(e -> {
      outputLabel.setText(executor.operation(stringBuffer.toString()));
      stringBuffer.setLength(0);
      inputLabel.setText("");
    });
    buttonCalculationInequality.setOnAction(e -> {
      stringBuffer.append(buttonCalculationInequality.getText());
      inputLabel.setText(stringBuffer.toString());
    });

    stage.setWidth(200);
    stage.setHeight(200);
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
