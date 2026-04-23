package BackPropagationNN;

import java.util.Random;

public class LiveDigitGenerator {

    private static final int GRID = 10;
    private static final int INPUT_SIZE = GRID * GRID;
    private static final int OUTPUT_SIZE = 10;

    private final Random rnd;

    public LiveDigitGenerator() {
        this(System.currentTimeMillis());
    }

    public LiveDigitGenerator(long seed) {
        this.rnd = new Random(seed);
    }

    public record Batch(double[][] inputs, double[][] outputs) {}

    // batch
    public Batch generateBatch(int batchSize) {

        double[][] inputs = new double[batchSize][INPUT_SIZE];
        double[][] outputs = new double[batchSize][OUTPUT_SIZE];

        for (int i = 0; i < batchSize; i++) {

            int digit = rnd.nextInt(10);

            int[][] grid = drawStrokeDigit(digit);

            // augment
            grid = randomShift(grid);
            grid = randomThickenOrThin(grid);
            grid = dropout(grid, 0.10);
            grid = addNoise(grid, 0.03);

            double[] flat = flatten(grid);

            inputs[i] = flat;

            // label
            double[] label = new double[OUTPUT_SIZE];
            label[digit] = 1.0;
            outputs[i] = label;
        }

        return new Batch(inputs, outputs);
    }

    // realistic draw
    private int[][] drawStrokeDigit(int d) {

        int[][] g = new int[GRID][GRID];

        switch (d) {
            case 0 -> {
                line(g, 1,1, 8,1);
                line(g, 1,8, 8,8);
                line(g, 1,1, 1,8);
                line(g, 8,1, 8,8);
            }
            case 1 -> {
                line(g, 4,1 + jitter(), 4,8 + jitter());
            }
            case 2 -> {
                line(g, 1,1, 8,1);
                line(g, 8,1, 8,4);
                line(g, 8,4, 1,8);
                line(g, 1,8, 8,8);
            }
            case 3 -> {
                line(g, 1,1, 8,1);
                line(g, 8,1, 8,4);
                line(g, 8,4, 1,4);
                line(g, 8,4, 8,8);
                line(g, 1,8, 8,8);
            }
            case 4 -> {
                line(g, 1,1, 1,4);
                line(g, 1,4, 8,4);
                line(g, 8,1, 8,8);
            }
            case 5 -> {
                line(g, 8,1, 1,1);
                line(g, 1,1, 1,4);
                line(g, 1,4, 8,4);
                line(g, 8,4, 8,8);
                line(g, 1,8, 8,8);
            }
            case 6 -> {
                line(g, 8,1, 1,4);
                line(g, 1,4, 1,8);
                line(g, 1,8, 8,8);
                line(g, 8,4, 1,4);
            }
            case 7 -> {
                line(g, 1,1, 8,1);
                line(g, 8,1, 4,8);
            }
            case 8 -> {
                line(g, 1,1, 8,1);
                line(g, 1,8, 8,8);
                line(g, 1,1, 1,8);
                line(g, 8,1, 8,8);
                line(g, 1,4, 8,4);
            }
            case 9 -> {
                line(g, 1,8, 8,4);
                line(g, 1,1, 8,1);
                line(g, 8,1, 8,8);
                line(g, 1,4, 8,4);
            }
        }

        return g;
    }

    // basic stroke
    private void line(int[][] g, int x0, int y0, int x1, int y1) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (in(x0, y0)) g[y0][x0] = 1;

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    private boolean in(int x, int y) {
        return x >= 0 && x < GRID && y >= 0 && y < GRID;
    }

    private int jitter() {
        return rnd.nextInt(3) - 1; // -1, 0, 1
    }


    // augmentation

    private int[][] randomShift(int[][] g) {
        int sx = rnd.nextInt(3) - 1;
        int sy = rnd.nextInt(3) - 1;

        int[][] out = new int[GRID][GRID];

        for (int y = 0; y < GRID; y++) {
            for (int x = 0; x < GRID; x++) {

                int nx = x + sx;
                int ny = y + sy;

                if (in(nx, ny))
                    out[ny][nx] = g[y][x];
            }
        }
        return out;
    }

    private int[][] randomThickenOrThin(int[][] g) {

        double r = rnd.nextDouble();

        if (r < 0.33) return thicken(g);
        if (r < 0.66) return thin(g);

        return g;
    }

    private int[][] thicken(int[][] g) {
        int[][] o = new int[GRID][GRID];

        for (int y = 0; y < GRID; y++) {
            for (int x = 0; x < GRID; x++) {
                if (g[y][x] == 1) {
                    o[y][x] = 1;
                    if (x < GRID - 1) o[y][x + 1] = 1;
                    if (y < GRID - 1) o[y + 1][x] = 1;
                }
            }
        }
        return o;
    }

    private int[][] thin(int[][] g) {
        int[][] o = new int[GRID][GRID];

        for (int y = 1; y < GRID - 1; y++) {
            for (int x = 1; x < GRID - 1; x++) {

                int count = 0;
                for (int dy = -1; dy <= 1; dy++)
                    for (int dx = -1; dx <= 1; dx++)
                        count += g[y + dy][x + dx];

                if (g[y][x] == 1 && count >= 3)
                    o[y][x] = 1;
            }
        }

        return o;
    }

    private int[][] dropout(int[][] g, double prob) {
        int[][] o = new int[GRID][GRID];
        for (int y = 0; y < GRID; y++) {
            for (int x = 0; x < GRID; x++) {
                if (g[y][x] == 1 && rnd.nextDouble() < prob)
                    o[y][x] = 0;
                else
                    o[y][x] = g[y][x];
            }
        }
        return o;
    }

    private int[][] addNoise(int[][] g, double prob) {
        int[][] o = new int[GRID][GRID];
        for (int y = 0; y < GRID; y++) {
            for (int x = 0; x < GRID; x++) {
                int v = g[y][x];
                if (rnd.nextDouble() < prob) v = 1 - v;
                o[y][x] = v;
            }
        }
        return o;
    }

    // conversion

    private double[] flatten(int[][] grid) {
        double[] out = new double[INPUT_SIZE];
        int idx = 0;
        for (int y = 0; y < GRID; y++)
            for (int x = 0; x < GRID; x++)
                out[idx++] = grid[y][x];
        return out;
    }
}
