package BackPropagationNN;

import java.util.Arrays;
import java.util.Random;

class Layer {
    private final double[] inputs ;
    private final double[] outputs;
    private final double[] weights;
    private final double[] dWeights;
    private final Random random ;

    protected Layer(int inputSize , int outputSize){
        this.inputs = new double[inputSize+1];
        this.outputs = new double[outputSize];
        this.weights = new  double[(inputSize+1)*(outputSize)] ;
        this.dWeights = new  double[weights.length] ;
        this.random = new Random();
        initWeights();
    }


    private void initWeights(){
        for (int i = 0 ; i< weights.length ; i++){
            weights[i] = (random.nextDouble() - 0.5) * 0.5;
        }
    }

    protected double[] run(double[] inputs){
        System.arraycopy(inputs , 0 ,this.inputs , 0 ,  inputs.length );
        this.inputs[this.inputs.length - 1] = 1.0; // FIXED: Set bias on instance array


        int offset=0 ;
        for (int i = 0 ; i<outputs.length ; i++){
            outputs[i] = 0;
            for(int j = 0 ; j< this.inputs.length ; j++){
                outputs[i] += weights[offset+j] * this.inputs[j];
            }
            outputs[i] = Functions.sigmoid(outputs[i]) ;
            offset += this.inputs.length;
        }
        return Arrays.copyOf(outputs , outputs.length);
    }

    protected double[] train(double[] errors , double learningRate , double momentum){
        int offset = 0 ;
        double[] nextError = new double[inputs.length] ;

        for(int i = 0 ; i<outputs.length ; i++){
            double delta = errors[i] * Functions.dSigmoid(outputs[i]);

            for(int j = 0 ;j<inputs.length ; j++){
                int weightIndex = offset + j ;
                nextError[j] = nextError[j] + weights[weightIndex] * delta ;
                double dw = inputs[j] * delta *learningRate ;
                weights[weightIndex]+= dWeights[weightIndex] * momentum + dw ;
                dWeights[weightIndex] = dw ;
            }
            offset += inputs.length ;
        }
        return Arrays.copyOf(nextError, nextError.length - 1);
    }
}
