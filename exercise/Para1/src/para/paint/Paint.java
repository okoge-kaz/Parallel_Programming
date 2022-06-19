package para.paint;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX お絵描きアプリケーションのメインクラス
 */
public class Paint extends Application {
  Canvas canvas;
  GraphicsContext gc;
  /** 直前のポインタのx座標 */
  double oldx;
  /** 直前のポインタのy座標 */
  double oldy;
  /** 描画領域の大きさ */
  final int SIZE = 600;
  Button clear;

  /**
   * お絵描きプログラムの準備をして、ウィンドウを開きます
   */
  public void start(Stage stage) {
    Group group = new Group();
    canvas = new Canvas(SIZE, SIZE);
    gc = canvas.getGraphicsContext2D();
    drawShapes(gc);
    canvas.setOnMousePressed(ev -> {
      oldx = ev.getX();
      oldy = ev.getY();
    });

    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
        new EventHandler<MouseEvent>() {
          public void handle(MouseEvent ev) {
            gc.strokeLine(oldx, oldy, ev.getX(), ev.getY());
            oldx = ev.getX();
            oldy = ev.getY();
          }
        });
    clear = new Button("clear");
    clear.setOnAction(e -> {
      gc.setFill(Color.WHITE);
      gc.fillRect(0, 0, SIZE, SIZE);
    });

    BorderPane bp = new BorderPane();
    VBox vb = new VBox();

    Slider sliderr = new Slider(0, 1, 0);
    Slider sliderg = new Slider(0, 1, 0);
    Slider sliderb = new Slider(0, 1, 1);

    sliderr.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      gc.setStroke(Color.RED.deriveColor(0, 1, 1, nv.doubleValue()));
    });

    sliderg.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      gc.setStroke(Color.GREEN.deriveColor(0, 1, 1, nv.doubleValue()));
    });

    sliderb.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      gc.setStroke(Color.BLUE.deriveColor(0, 1, 1, nv.doubleValue()));
    });

    vb.setAlignment(Pos.CENTER);
    vb.getChildren().add(sliderr);
    vb.getChildren().add(sliderg);
    vb.getChildren().add(sliderb);
    vb.getChildren().add(clear);
    bp.setTop(vb);
    bp.setCenter(canvas);
    Scene scene = new Scene(bp);
    stage.setScene(scene);
    stage.setTitle("JavaFX Draw");
    stage.show();
  }

  /**
   * 初期化メソッド、startメソッドの呼び出され方とは異なる呼び出され方をする。必要ならば定義する
   */
  public void init() {
  }

  /**
   * 図形を描きます。
   * 図形描画の実装サンプルです
   */
  private void drawShapes(GraphicsContext gc) {
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, SIZE, SIZE);
    gc.setFill(Color.GREEN);
    gc.setStroke(Color.BLUE);
    gc.setLineWidth(4);
    // gc.strokeLine(40, 10, 10, 40);
    // gc.fillOval(60, 10, 30, 30);
    // gc.strokeOval(110, 10, 30, 30);
    // gc.fillRoundRect(160, 10, 30, 30, 10, 10);
  }
}
