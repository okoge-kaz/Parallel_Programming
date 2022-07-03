package para.graphic.shape;

import java.util.*;

/**
 * 描画順序をidの順とする図形集合
 */
public class OrderedShapeManager extends ShapeManager {
    /**
     * 描画順序をidの順とする図形集合を生成する（空集合）
     */
    public OrderedShapeManager() {
        super(new TreeSet<Shape>(new Comparator<Shape>() {
            @Override
            public int compare(Shape o1, Shape o2) {
                return o2.getID() - o1.getID();
            }
        }));
    }
}
