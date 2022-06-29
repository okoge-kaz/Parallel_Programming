// 20B30790 藤井一喜

package para.graphic.parser;

import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Triangle;

class TriangleParser implements ShapeParser {
    TriangleParser() {
    }

    @Override
    public Triangle parse(Scanner s, int id) {
        int x = s.nextInt();
        int y = s.nextInt();
        int width = s.nextInt();
        int height = s.nextInt();
        Triangle ret;
        Attribute attr = null;
        if (s.hasNext("Attribute")) {
            attr = AttributeParser.parse(s);
        }
        ret = new Triangle(id, x, y, width, height, attr);
        return ret;
    }
}
