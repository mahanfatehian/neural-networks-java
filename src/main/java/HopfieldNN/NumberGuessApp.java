package HopfieldNN;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;


public class NumberGuessApp {
    public static void main(String[] args) {

        System.out.println("program is starting");

        // javafx startup
        Platform.startup(() -> {
        });

        NumberGuessWidget widget = new NumberGuessWidget();

        HopFieldNN nn = HopFieldNN.create(100);
        List<Matrix> numbersMatrix = new ArrayList<>();
        for (NumbersRepresentation number : NumbersRepresentation.values()) {
            Matrix m = Matrix.of(number.getMatrix());
            nn.train(m);
            numbersMatrix.add(m);
        }

        // blocks the main thread until the widget closes
        widget.widget();
        Matrix result = nn.recall(Matrix.flatten(widget.getFinalMatrix()));

        Matrix finalNumber = Matrix.findMostIdentical(result, numbersMatrix);
        boolean found = false;

        for (NumbersRepresentation number : NumbersRepresentation.values()) {
            if (finalNumber.equals(number.getMatrix())) {
                System.out.println("matched Number is : " + number);
                found = true;
            }
        }
        //threshold is only 1 bit(value) noise
        if (!found) {
            System.out.println("pattern was not found");


            // shuts down javafx
            Platform.exit();
        }
    }
}
