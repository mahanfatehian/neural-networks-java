package BackPropagationNN;

public class Functions {


    private Functions() {
    }

    public static double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));

    }

    public static double dSigmoid(double x) {

//        return sigmoid(x) * (1-sigmoid(x));

       return x * (1-x);
    }
}
