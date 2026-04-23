
package BackPropagationNN;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.util.concurrent.CountDownLatch;

public class NumberGuessWidget {

    private int[] flatInput = null;

    public int[] getFlatInput() {
        return flatInput;
    }

    public void widget() {

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("draw (10x10) digit");

            GridPane grid = new GridPane();
            grid.setHgap(2);
            grid.setVgap(2);
            grid.setAlignment(Pos.CENTER);

            int[][] state = new int[10][10];

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {

                    Rectangle rect = getRectangle(row, col, state);

                    grid.add(rect, col, row);
                }
            }

            Button finishBtn = new Button("Finish");
            finishBtn.setOnAction(e -> {
                flatInput = flatten(state);
                stage.close();
            });

            VBox root = new VBox(15, grid, finishBtn);
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> latch.countDown());

            stage.show();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Nonnull
    private static Rectangle getRectangle(int row, int col, int[][] state) {
        Rectangle rect = new Rectangle(30, 30);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);

        final int r = row;
        final int c = col;

        rect.setOnMouseClicked(e -> {
            if (state[r][c] == 0) {
                state[r][c] = 1;
                rect.setFill(Color.BLACK);
            } else {
                state[r][c] = 0;
                rect.setFill(Color.WHITE);
            }
        });
        return rect;
    }

    private static int[] flatten(int[][] arr) {

        int[] out = new int[100];
        int idx = 0;

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                out[idx++] = arr[r][c];
            }
        }

        return out;
    }
}
