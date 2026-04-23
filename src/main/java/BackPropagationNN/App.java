package BackPropagationNN;

import javafx.application.Platform;

public class App {

    public static void main(String[] args) {

        System.out.println("program starting...");

        // javaFX startup
        Platform.startup(() -> {});

        //setup
        int inputSize = 100;
        int outputSize = 10;
        int hiddenSize = 64;

        BackPropagationNN nn = BackPropagationNN.initializeNN(inputSize, outputSize, hiddenSize);

        LiveDigitGenerator generator = new LiveDigitGenerator();

        int epochs = 20000;
        int batchSize = 64;
        double learningRate = 0.2;
        double momentum = 0.5;

        System.out.println("starting online batching training...");

        // train
        for (int epoch = 1; epoch <= epochs; epoch++) {
            LiveDigitGenerator.Batch batch = generator.generateBatch(batchSize);
            nn.train(batch.inputs(), batch.outputs(), 1, learningRate, momentum);

            if (epoch % 1000 == 0) {
                System.out.printf("epoch %d / %d completed%n", epoch, epochs);
            }
        }

        System.out.println("training complete!");

        // widget
        NumberGuessWidget widget = new NumberGuessWidget();
        widget.widget();  //blocks thread until user finishes drawing

        //user input
        int[] rawInput = widget.getFlatInput();

        double[] normalized = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            normalized[i] = rawInput[i];
        }

        // predict
        double[] out = nn.predict(normalized);
        DigitLabel predicted = DigitLabel.fromOutput(out);

        System.out.printf("(prediction) you drew: %s%n", predicted);

        // javaFX shutdown
        Platform.exit();
    }
}
