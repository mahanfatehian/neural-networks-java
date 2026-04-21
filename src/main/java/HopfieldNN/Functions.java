package HopfieldNN;

public class Functions {
    private Functions() {
    }

    //sign function
    public static int stepFn(int x) {
        return (x < 0) ? -1 : 1;
    }

    public static int transform(int x) {
        return (x == 0) ? -1 : x;
    }

    public static int reTransform(int x) {
        return (x == -1) ? 0 : x;
    }

}
