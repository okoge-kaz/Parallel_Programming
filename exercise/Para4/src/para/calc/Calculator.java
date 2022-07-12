// 藤井一喜 20B30790
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
  /** 入力数式の表示部 */
  final Label inputLabel;
  /** 計算結果の表示部 */
  final Label outputLabel;
  /** 入力数式の文字列作成用バッファ */
  final StringBuilder buff;
  /** 数式解釈器 */
  final Executor interpreter;

  /**
   * 電卓の状態を保持するデータ領域、逆ポーランド記法解釈器の準備といった初期化行う.
   */
  public Calculator() {
    inputLabel = new Label();
    outputLabel = new Label();
    buff = new StringBuilder();
    // ex = new Executor1();
    interpreter = new Executor2(outputLabel);
  }

  /** ボタンのラベル */
  final String[] buttonNames = { "9", "8", "7", "+",
      "6", "5", "4", "-",
      "3", "2", "1", "*",
      "0", ".", ",", "/" };

  public void start(Stage stage) {

    // 大枠の作成
    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);
    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200, 300);

    // ボタンを定義
    Button[] buttons = new Button[16];
    Button calculationStartButton = new Button("=");
    Button oneFormerInputDeleteButton = new Button("<");

    calculationStartButton.setPrefHeight(56);
    oneFormerInputDeleteButton.setPrefHeight(56);

    // 見た目の調整
    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140, 30, Color.WHITE));
    stack.getChildren().add(inputLabel);

    for (int i = 0; i < 16; i++) {
      buttons[i] = new Button(buttonNames[i]);
      buttons[i].setPrefHeight(26);
      if (i % 4 == 3) {
        buttons[i].setOnAction(ev -> {
          buff.append(((Button) ev.getSource()).getText() + ",");
          inputLabel.setText(buff.toString());
        });
      } else {
        buttons[i].setOnAction(ev -> {
          buff.append(((Button) ev.getSource()).getText());
          inputLabel.setText(buff.toString());
        });
      }
    }
    // = ボタンを押すことで計算を実行する
    calculationStartButton.setOnAction(ev -> {
      System.out.println("[[" + buff.toString() + "]]");
      String mid;
      mid = interpreter.operation(buff.toString());
      outputLabel.setText(mid);
      buff.delete(0, buff.length());
    });
    // 1つ前の入力を削除するボタン
    oneFormerInputDeleteButton.setOnAction(ev -> {
      if (buff.length() != 0) {
        buff.deleteCharAt(buff.length() - 1);
      }
      inputLabel.setText(buff.toString());
    });

    // 出力ラベルとボタンを配置
    root.getChildren().addAll(stack, outputLabel);
    root.getChildren().add(grid);
    grid.setAlignment(Pos.CENTER);
    for (int i = 0; i < 16; i++) {
      grid.add(buttons[i], i % 4, i / 4);
    }
    grid.add(oneFormerInputDeleteButton, 4, 0, 1, 2);
    grid.add(calculationStartButton, 4, 2, 1, 2);

    stage.setWidth(200);
    stage.setHeight(200);
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
