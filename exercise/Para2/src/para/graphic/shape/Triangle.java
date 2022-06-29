// 20B30790 藤井一喜

package para.graphic.shape;

import para.graphic.target.Target;

public class Triangle extends Shape {
    /** 属性 */
    private Attribute attr;
    /** 底辺の中心のx座標 */
    private int x;
    /** 底辺の中心のy座標 */
    private int y;
    /** 底辺の長さ */
    private int width;
    /** 高さ */
    private int height;

    /**
     * 三角形を生成する．
     * 
     * @param id     識別子
     * @param x      底辺の中心のx座標
     * @param y      底辺の中心のy座標
     * @param width  底辺の長さ
     * @param height 高さ
     */
    public Triangle(int id, int x, int y, int width, int height) {
        this(id, x, y, width, height, null);
    }

    /**
     * 三角形を生成する．
     * 
     * @param id     識別子
     * @param x      底辺の中心のx座標
     * @param y      底辺の中心のy座標
     * @param width  底辺の長さ
     * @param height 高さ
     * @param attr   属性
     */
    public Triangle(int id, int x, int y, int width, int height, Attribute attr) {
        super(id, x, x + width, y, y + height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.attr = attr;
    }

    /**
     * 属性を取得する
     */
    @Override
    public Attribute getAttribute() {
        return attr;
    }

    /**
     * この三角形を出力する
     * 
     * @param target 出力先
     */
    @Override
    public void draw(Target target) {
        target.drawTriangle(id, x, y, width, height, attr);
    }

}
