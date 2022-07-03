public class Main02 {
    int data;

    Main02(int data) {
        this.data = data;
    }

    void setData(int data) {
        this.data = data;
    }

    int getData() {
        return data;
    }

    public String toString() {
        return "data = " + data;
    }

    public static void main(String[] args) {
        Main02 a[] = new Main02[2];
        Main02 b[];

        a[0] = new Main02(1);
        a[1] = new Main02(10);

        b = a.clone();
        System.out.println("b[0] = " + b[0]);// 1
        b[0].setData(2);
        System.out.println("a[0] = " + a[0]);// 2
        System.out.println("b[0] = " + b[0]);// 2
        // 同じ配列なにも関わらず、intと自作定義classとで挙動が変わるかというとprimitive型と参照型の違い
    }
}
