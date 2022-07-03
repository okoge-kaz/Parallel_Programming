public class Main01 {
    private int data;

    void setData(int data) {
        this.data = data;
    }

    int getData() {
        return data;
    }

    public static void main(String[] args) {

        System.out.println("参照代入test start");
        Main01 a, b;
        a = new Main01();
        a.setData(1);
        b = a;// 参照代入
        b.setData(2);
        System.out.println(a.getData());// 2 not 1
        System.out.println("参照代入test end\n");

        System.out.println("配列の参照挙動 test start");
        int d[] = new int[5];
        int e[];
        d[0] = 0; d[1] = 1; d[2] = 2; d[3] = 3; d[4] = 4;
        e = d.clone();// 配列のコピー
        System.out.println(e[0]);// 0
        System.out.println(e[1]);// 1
        System.out.println(e[2]);// 2

        e[0] = 2;
        System.out.println("d[0] = 0 is expected:" + d[0]);// 0
        System.out.println("e[0] = 2 is expected:" + e[0]);// 2

        System.out.println("配列の参照挙動 test end\n");
        

        Main01[] c = new Main01[5];
        c[0].setData(2);
        System.out.println(c[0].getData());// null
        // Exception in thread "main" java.lang.NullPointerException: Cannot invoke "Main01.setData(int)" because "<local3>[0]" is null at Main01.main(Main01.java:21)
    }
}
