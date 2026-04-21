package PerceptronNN;

import java.util.Random;

public class PerceptronNN {

    private final ListMatrix<Double> weights;
    private final ListMatrix<Double> biases;
    private final int numInputs;
    private final int numOutputs;

    /***
     input matrix = 1 * inputNumbers
     weight matrix = inputNumbers * outputNumbers
     input matrix * weight matrix = Matrix of 1 * output numbers
     bias matrix = 1 * outputNumbers
     output matrix = 1 * outputNumbers
     ***/
    public PerceptronNN(int numInputs, int numOutputs) {
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        // Weights matrix: (numInputs x numOutputs)
        this.weights = ListMatrix.create(this.numInputs, this.numOutputs);

        // Biases row vector: (1 x this.numOutputs)
        this.biases = ListMatrix.create(1, this.numOutputs);
        initializeParameters();
    }

    private void initializeParameters() {

        Random random = new Random();

        // Initialize weights between [-0.5, 0.5)
        for (int i = 0; i < this.numInputs; i++) {
            for (int j = 0; j < this.numOutputs; j++) {
                double randomWeight = random.nextDouble() - 0.5;
                this.weights.set(i, j, randomWeight);
            }
        }

        // Initialize biases between [-0.5, 0.5)
        for (int j = 0; j < this.numOutputs; j++) {
            double randomBias = random.nextDouble() - 0.5;
            this.biases.set(0, j, randomBias);
        }
    }

    public void train(double learningRate, ListMatrix<Double> inputs, ListMatrix<Double> labels) {
        boolean hasError = true;
        int epoch = 0;
        int maxEpochs = 100;
        int numSamples = inputs.getNumRows(); // numOfCases

        while (hasError && epoch < maxEpochs) {
            hasError = false;

            // loop throughout rows
            for (int r = 0; r < numSamples; r++) {


                for (int j = 0; j < numOutputs; j++) {
                    double activation = biases.getMatrix().getFirst().get(j);

                    for (int i = 0; i < numInputs; i++) {
                        activation += inputs.getMatrix().get(r).get(i) * weights.getMatrix().get(i).get(j);
                    }

                    double prediction = Functions.heaviside(activation);
                    double target = labels.getMatrix().get(r).get(j);
                    double error = target - prediction;

                    if (error != 0.0) {
                        hasError = true;

                        // weights update
                        for (int i = 0; i < numInputs; i++) {
                            double currentWeight = weights.getMatrix().get(i).get(j);
                            double inputValue = inputs.getMatrix().get(r).get(i);
                            weights.set(i, j, currentWeight + (learningRate * error * inputValue));
                        }

                        // bias update
                        double currentBias = biases.getMatrix().getFirst().get(j);
                        biases.set(0, j, currentBias + (learningRate * error));
                    }
                }
            }
            epoch++;
        }

        if (hasError) {
            System.out.printf("problem needs non-linear solution (Stopped after %d epochs)%n", epoch);
        } else {
            System.out.printf("training completed in %d epochs.%n", epoch);
        }
    }

    public void predict(ListMatrix<Double> inputs) {
        System.out.println("--- predictions ---");
        int numSamples = inputs.getNumRows();

        for (int r = 0; r < numSamples; r++) {
            for (int j = 0; j < numOutputs; j++) {
                double activation = biases.getMatrix().getFirst().get(j);
                for (int i = 0; i < numInputs; i++) {
                    activation += inputs.getMatrix().get(r).get(i) * weights.getMatrix().get(i).get(j);
                }
                double prediction = Functions.heaviside(activation);
                System.out.printf("input row %d -> Predicted: %s%n", r, prediction);
            }
        }
    }
}

