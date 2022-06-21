package para.calc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * 逆ポーランド記法の数式文字列に対して数値計算をするための基本処理が定義されているクラス
 */
abstract public class ExecutorBase {
  protected Scanner scanner;
  protected String result;

  private String state;
  private Stack<Float> stack;

  protected ExecutorBase() {
  }

  /**
   * 処理対象文字列を設定し、処理のための初期化を行う
   * 
   * @param data 処理対象文字列
   */
  protected void init(String data) {

    scanner = new Scanner(data);
    scanner.useDelimiter(",");

    stack = new Stack<Float>();

    state = "";
  }

  /**
   * 処理の1ステップ分を行う
   * 
   * @return 1ステップが処理できれば true 処理対象の文字列の文法誤りで処理できなればfalse
   */
  protected boolean oneStep() {

    if (scanner.hasNext("\\-?+\\d++\\.?+\\d*")) {

      float ret = Float.parseFloat(scanner.next("\\-?+\\d++\\.?+\\d*"));
      stack.push(ret);
      result = Float.toString(ret);
      showProgress();
      return true;

    } else if (scanner.hasNext("[\\+\\-\\*/]")) {

      String operator = scanner.next("[\\+\\-\\*/]");
      showProgress(operator);
      
      if (stack.empty()) {
        return false;
      }
      float operandRight = stack.pop();
      if (stack.empty()) {
        return false;
      }
      float operandLeft = stack.pop();
      float res = 0;

      switch (operator) {
        case "+":
          res = operandLeft + operandRight;
          break;
        case "-":
          res = operandLeft - operandRight;
          break;
        case "*":
          res = operandLeft * operandRight;
          break;
        case "/":
          res = operandLeft / operandRight;
          break;
      }

      stack.push(res);
      result = Float.toString(res);
      showProgress();
      return true;

    } else {

      scanner.next();
      return false;

    }
  }

  public void showProgress(String op) {

    Stream<Float> st = stack.stream();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos);

    ps.print("(");
    st.forEach(f -> {
      ps.print(f + "|");
    });
    ps.print(op + ")");

    state = bos.toString();
    writeState(state);

  }

  // Overload
  public void showProgress() {
    showProgress("");
  }

  /**
   * 状態を出力する
   * 
   * @param state 状態
   */
  abstract void writeState(String state);

  /*
   * 処理対象文字列を設定し、処理のための初期化を行う
   * 
   * @param data 処理対象文字列
   */
  abstract String operation(String data);
}
