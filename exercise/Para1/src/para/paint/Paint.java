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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

// JavaFX お絵描きアプリケーションのメインクラス
public class Paint extends Application {
  double oldX; // 直前のポインタのx座標
  double oldY; // 直前のポインタのy座標
  final int SIZE = 600; // 描画領域の大きさ

  // お絵描きプログラムの準備をして、ウィンドウを開きます
  public void start(Stage stage) {
    Group group = new Group();

    Canvas canvas = new Canvas(SIZE, SIZE);
    canvas.setOnMousePressed(mouseEvent -> {
      oldX = mouseEvent.getX();
      oldY = mouseEvent.getY();
    });

    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    drawShapes(graphicsContext);

    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
        new EventHandler<MouseEvent>() {
          public void handle(MouseEvent ev) {
            graphicsContext.strokeLine(oldX, oldY, ev.getX(), ev.getY());
            oldX = ev.getX();
            oldY = ev.getY();
          }
        });

    Button buttonClear = new Button("clear");
    buttonClear.setOnAction(e -> {
      graphicsContext.setFill(Color.WHITE);
      graphicsContext.fillRect(0, 0, SIZE, SIZE);
    });

    BorderPane borderPane = new BorderPane();
    VBox vBox = new VBox();

    Slider sliderRed = new Slider(0, 1, 0);
    Slider sliderGreen = new Slider(0, 1, 0);
    Slider sliderBlue = new Slider(0, 1, 1);
    Slider sliderTransparency = new Slider(0, 1, 0.5);
    Slider sliderLineWidth = new Slider(0, 10, 1);

    sliderRed.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      graphicsContext.setFill(Color.RED.deriveColor(0, 1, 1, nv.doubleValue()));

    });

    sliderGreen.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      graphicsContext.setFill(Color.GREEN.deriveColor(0, 1, 1, nv.doubleValue()));
    });

    sliderBlue.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      graphicsContext.setFill(Color.BLUE.deriveColor(0, 1, 1, nv.doubleValue()));
    });

    sliderTransparency.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      graphicsContext.setFill(Color.BLACK.deriveColor(0, 1, 1, nv.doubleValue()));
    });

    sliderLineWidth.valueProperty().addListener((ObservableValue<? extends Number> ov,
        Number oldv, Number nv) -> {
      graphicsContext.setLineWidth(nv.doubleValue());
    });

    Rectangle rectangle = new Rectangle(25, 25);
    rectangle.setFill(Color.BLACK);

    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER);
    hBox.getChildren().addAll(buttonClear, rectangle);

    vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(sliderRed, sliderGreen, sliderBlue, sliderTransparency, sliderLineWidth);
    vBox.getChildren().add(hBox);

    borderPane.setTop(vBox);
    borderPane.setCenter(canvas);

    Scene scene = new Scene(borderPane);

    stage.setScene(scene);
    stage.setTitle("JavaFX Draw");
    stage.show();
  }

  // 初期化メソッド、startメソッドの呼び出され方とは異なる呼び出され方をする。必要ならば定義する

  public void init() {
  }

  private void drawShapes(GraphicsContext graphicsContext) {
    graphicsContext.setFill(Color.WHITE);
    graphicsContext.fillRect(0, 0, SIZE, SIZE);

    graphicsContext.setFill(Color.GREEN);
    graphicsContext.setStroke(Color.BLUE);

    graphicsContext.setLineWidth(4);

    graphicsContext.strokeLine(40, 10, 10, 40);
    graphicsContext.strokeOval(110, 10, 30, 30); // 丸外枠

    graphicsContext.fillOval(60, 10, 30, 30); // 丸
    graphicsContext.fillRoundRect(160, 10, 30, 30, 10, 10); // 四角
  }
}
