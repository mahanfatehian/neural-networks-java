package BackPropagationNN;

public class App {

    static void main() {
        BackPropagationNN nn = BackPropagationNN.initializeNN(2, 1, 8 );

        //inputs
        double[][] trainingInputs = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };
        //outputs
        double[][] trainingOutputs = {
                {0},
                {1},
                {1},
                {0}
        };
        System.out.println("training in progress");

        nn.train(trainingInputs, trainingOutputs, 100000, 0.3, 0.6);

        nn.predict(trainingInputs);

    }
}
