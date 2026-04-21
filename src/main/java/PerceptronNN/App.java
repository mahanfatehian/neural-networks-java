package PerceptronNN;

public class App {
    //using perceptron nn to solve XOR & AND & OR operators
    public static void main(String[] args) {
    PerceptronNN perceptronNNXOR = new PerceptronNN(2 , 1);
        // 4 rows (samples), 2 columns (features)
        ListMatrix<Double> inputs = ListMatrix.create(4, 2);
        // 4 rows (samples), 1 column (output label)
        ListMatrix<Double> labels = ListMatrix.create(4, 1);
        /***
          XOR
         ***/
        // Case 1: 0, 0 -> 0
        inputs.set(0, 0, 0.0); inputs.set(0, 1, 0.0); labels.set(0, 0, 0.0);
        // Case 2: 0, 1 -> 1
        inputs.set(1, 0, 0.0); inputs.set(1, 1, 1.0); labels.set(1, 0, 1.0);
        // Case 3: 1, 0 -> 1
        inputs.set(2, 0, 1.0); inputs.set(2, 1, 0.0); labels.set(2, 0, 1.0);
        // Case 4: 1, 1 -> 0
        inputs.set(3, 0, 1.0); inputs.set(3, 1, 1.0); labels.set(3, 0, 0.0);

        System.out.println("--------- XOR ---------");
        perceptronNNXOR.train(0.2 , inputs , labels);
        perceptronNNXOR.predict(inputs);



        /***
         AND
         ***/
        PerceptronNN perceptronNNAND = new PerceptronNN(2 , 1);

        // Case 1: 0, 0 -> 0
        inputs.set(0, 0, 0.0); inputs.set(0, 1, 0.0); labels.set(0, 0, 0.0);
        // Case 2: 0, 1 -> 0
        inputs.set(1, 0, 0.0); inputs.set(1, 1, 1.0); labels.set(1, 0, 0.0);
        // Case 3: 1, 0 -> 0
        inputs.set(2, 0, 1.0); inputs.set(2, 1, 0.0); labels.set(2, 0, 0.0);
        // Case 4: 1, 1 -> 1
        inputs.set(3, 0, 1.0); inputs.set(3, 1, 1.0); labels.set(3, 0, 1.0);

        System.out.println("--------- AND ---------");
        perceptronNNAND.train(0.2 , inputs , labels);
        perceptronNNAND.predict(inputs);



        /***
         OR
         ***/
        PerceptronNN perceptronNNOR = new PerceptronNN(2 , 1);

        // Case 1: 0, 0 -> 0
        inputs.set(0, 0, 0.0); inputs.set(0, 1, 0.0); labels.set(0, 0, 0.0);
        // Case 2: 0, 1 -> 1
        inputs.set(1, 0, 0.0); inputs.set(1, 1, 1.0); labels.set(1, 0, 1.0);
        // Case 3: 1, 0 -> 1
        inputs.set(2, 0, 1.0); inputs.set(2, 1, 0.0); labels.set(2, 0, 1.0);
        // Case 4: 1, 1 -> 1
        inputs.set(3, 0, 1.0); inputs.set(3, 1, 1.0); labels.set(3, 0, 1.0);

        System.out.println("--------- OR ---------");
        perceptronNNOR.train(0.2 , inputs , labels);
        perceptronNNOR.predict(inputs);


    }

}
