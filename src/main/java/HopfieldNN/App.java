package HopfieldNN;

public class App {
    public static void main(String[] args) {
        HopFieldNN hopFieldNN = HopFieldNN.create(9);
        //representation of C
        hopFieldNN.train(Matrix.of(new int[][]{
                {1,1,1,
                 1,0,0,
                 1,1,1}
        }));
        //representation of T
        hopFieldNN.train(Matrix.of(new int[][]{
                {1,1,1,
                 0,1,0,
                 0,1,0}
        }));
        //noisy representation of C
        hopFieldNN.recall(Matrix.of(new int[][]{
                {1,1,1,
                 1,0,0,
                 1,1,0}
        }));

    }
}
