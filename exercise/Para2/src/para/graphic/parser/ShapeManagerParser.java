// 20B30790 藤井一喜
package para.graphic.parser;

import java.util.HashMap;
import java.util.Scanner;

import para.graphic.shape.ShapeManager;

class ShapeManagerParser implements MetaParser {
  private final ShapeManager shapemanager;
  private final HashMap<String, ShapeParser> map;

  ShapeManagerParser(ShapeManager sm) {
    shapemanager = sm;
    map = new HashMap<String, ShapeParser>();
    map.put("Circle", new CircleParser());
    map.put("Image", new ImageParser());
    map.put("Rectangle", new RectangleParser());
    map.put("Triangle", new TriangleParser());
  }

  @Override
  public void parse(Scanner s) {
    int id = s.nextInt();
    ShapeParser sp = map.get(s.next());
    shapemanager.put(sp.parse(s, id));
  }

  class ClearParser implements MetaParser {
    @Override
    public void parse(Scanner s) {
      shapemanager.clear();
    }
  }
}
