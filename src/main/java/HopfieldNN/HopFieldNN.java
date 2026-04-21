package HopfieldNN;

public class HopFieldNN {
    private Matrix weightMatrix;

    private HopFieldNN(int dimension) {
        this.weightMatrix = Matrix.create(dimension, dimension);

    }

    public static HopFieldNN create(int dimension) {
        return new HopFieldNN(dimension);
    }

    public void train(Matrix pattern) {
        Matrix p = Matrix.of(pattern); // COPY
        Matrix.applyFunction(p, Functions::transform);
        Matrix patternWeightMatrix = Matrix.matrixMultiplication(Matrix.reverse(p), p);
        Matrix.clearDiagonals(patternWeightMatrix);
        this.weightMatrix = Matrix.matrixAddition(patternWeightMatrix, weightMatrix);
    }

    public Matrix recall(Matrix pattern) {
        Matrix p = Matrix.of(pattern);
        Matrix.applyFunction(p, Functions::transform);
        Matrix result = Matrix.matrixMultiplication(p, weightMatrix);
        Matrix.applyFunction(result, Functions::stepFn);
        Matrix.applyFunction(result, Functions::reTransform);

        System.out.println(result);
        return result;
    }

    public Matrix completeRecall(Matrix inputPattern) {
        Matrix p = Matrix.of(inputPattern);
        Matrix.applyFunction(p, Functions::transform);

        Matrix currentState = Matrix.of(p);
        Matrix previousState;


        int maxIterations = 20;
        int iteration = 0;
        do {
            previousState = Matrix.of(currentState);


            Matrix.applyFunction(currentState, Functions::transform);
            Matrix result = Matrix.matrixMultiplication(currentState, weightMatrix);
            Matrix.applyFunction(result, Functions::stepFn);
            Matrix.applyFunction(result, Functions::reTransform);
            currentState = result;

            //printing
            System.out.println("Iteration " + iteration + ":");
            System.out.println(currentState);
            System.out.println();

            iteration++ ;

        } while (!currentState.equals(previousState) && iteration < maxIterations);
        return currentState ;
    }
}
