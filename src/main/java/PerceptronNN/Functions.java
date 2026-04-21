package PerceptronNN;

public class Functions {
    private Functions() {
    }

    public static double heaviside(double x) {
    return (x>0) ? 1 : 0 ;
    }
}
