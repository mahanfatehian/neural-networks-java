package BackPropagationNN;

public class BackPropagationNN {

    private final Layer[] layers ;

    private BackPropagationNN(int inputSize , int outputSize , int... hiddenSizes ){
        int numHiddenLayers = hiddenSizes.length;

        layers = new Layer[numHiddenLayers + 1];


        //create layers from input layer as the output is the next layers input till the output layer
        int currentInputSize = inputSize;

        for (int i = 0; i < numHiddenLayers; i++) {
            layers[i] = new Layer(currentInputSize, hiddenSizes[i]);
            currentInputSize = hiddenSizes[i];
        }
        layers[numHiddenLayers] = new Layer(currentInputSize, outputSize);
    }

    public static BackPropagationNN initializeNN(int inputSize , int outputSize , int... hiddenLayersSizes){
        return new BackPropagationNN(inputSize , outputSize , hiddenLayersSizes);
    }

    public double[] run(double[] input){
        double[] activations = input ;
        for (Layer layer : layers) {
            activations = layer.run(activations);
        }
        return activations ;
    }

    private void train(double[] input , double[] taget , double learningRate , double momentum){
        double[] calculatedOutput = run(input) ;
        double[] error = new double[calculatedOutput.length];
        for(int i=0 ; i<error.length;i++){
            error[i] = taget[i] - calculatedOutput[i];
        }
        for(int i =layers.length-1 ; i>= 0;i--){
            error = layers[i].train(error , learningRate , momentum);
        }
    }
    public void train(double[][] trainingInputs, double[][] trainingOutputs, int epochs, double learningRate, double momentum) {
        for (int i = 0; i < epochs; i++) {
            for (int j = 0; j < trainingInputs.length; j++) {
                train(trainingInputs[j], trainingOutputs[j], learningRate, momentum);
            }
        }
    }
    public void predict(double[][] inputs) {
        System.out.println("testing results :");
        for (double[] input : inputs) {
            double[] result = run(input);

            System.out.print("Input: [");
            for (int j = 0; j < input.length; j++) {
                System.out.print(input[j] + (j < input.length - 1 ? ", " : ""));
            }
            System.out.print("] -> Output: [");
            for (int j = 0; j < result.length; j++) {
                System.out.printf("%.4f", result[j]);
                System.out.print(j < result.length - 1 ? ", " : "");
            }
            System.out.println("]");
        }
    }


}
