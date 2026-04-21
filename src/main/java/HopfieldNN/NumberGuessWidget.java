package HopfieldNN;

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

    private Matrix finalMatrix = null;

    public Matrix getFinalMatrix() {
        return finalMatrix;
    }

    public void widget() {
        // latch to block main thread
        CountDownLatch latch = new CountDownLatch(1);

        // ui thread
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Draw Pattern");

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
                finalMatrix = Matrix.of(state);
                stage.close();
            });

            VBox root = new VBox(15, grid, finishBtn);
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

            stage.setScene(new Scene(root));

            // release main thread (latch) when closed
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
}
