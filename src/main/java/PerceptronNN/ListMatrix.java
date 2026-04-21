package PerceptronNN;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
//stupid implementation only for educational purposes
public class ListMatrix<T extends Number> {

    private final int numRows;
    private final int  numColumns;
    private final List<List<T>> matrix;

    private ListMatrix(int numRows , int numColumns ){
        this.numRows = numRows ;
        this.numColumns = numColumns ;
        matrix = new ArrayList<>(numRows);
    }
    public static <T extends Number> ListMatrix<T> create(int numRows, int numColumns) {
        ListMatrix<T> result = new ListMatrix<T>(numRows, numColumns);
        for (int i = 0; i < numRows; i++) {
            List<T> row = new ArrayList<>(numColumns);
            for (int j = 0; j < numColumns; j++) {
                row.add(null); //initial will null to build size
            }
            result.matrix.add(row);
        }
        return result;
    }
    public static <T extends Number> ListMatrix<T> of(ListMatrix<T> m) {
        if (m == null || m.getMatrix() == null || m.getNumRows() == 0 || m.getNumColumns() == 0) {
            throw new IllegalArgumentException("Matrix cannot be null or empty");
        }

        ListMatrix<T> result = new ListMatrix<T>(m.numRows, m.numColumns);
        for (int i = 0; i < m.numRows; i++) {
            List<T> rowCopy = new ArrayList<>(m.matrix.get(i));
            result.matrix.add(rowCopy);
        }
        return result;
    }

    public void set(int rowIndex, int colIndex, T value) {
        if (rowIndex < 0 || rowIndex >= this.numRows || colIndex < 0 || colIndex >= this.numColumns) {
            throw new IndexOutOfBoundsException("Matrix indices out of bounds");
        }
        this.matrix.get(rowIndex).set(colIndex, value);
    }


    public ListMatrix<Double> add(ListMatrix<? extends Number> other) {
        if (this.numRows != other.numRows || this.numColumns != other.numColumns) {
            throw new IllegalArgumentException("Matrix dimensions must match for addition");
        }

        ListMatrix<Double> result = ListMatrix.create(this.numRows, this.numColumns);

        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numColumns; j++) {
                double val1 = this.matrix.get(i).get(j).doubleValue();
                double val2 = other.matrix.get(i).get(j).doubleValue();
                result.matrix.get(i).set(j, val1 + val2);
            }
        }
        return result;
    }

    public ListMatrix<Double> multiply(ListMatrix<? extends Number> other) {
        if (this.numColumns != other.numRows) {
            throw new IllegalArgumentException("Columns of first matrix must equal rows of second matrix");
        }

        ListMatrix<Double> result = ListMatrix.create(this.numRows, other.numColumns);

        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < other.numColumns; j++) {
                double sum = 0.0;
                for (int k = 0; k < this.numColumns; k++) {
                    double val1 = this.matrix.get(i).get(k).doubleValue();
                    double val2 = other.matrix.get(k).get(j).doubleValue();
                    sum += val1 * val2;
                }
                result.matrix.get(i).set(j, sum);
            }
        }
        return result;
    }

    public void applyFunction(UnaryOperator<T> function) {
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numColumns; j++) {
                T currentValue = this.matrix.get(i).get(j);
                T newValue = function.apply(currentValue);
                this.matrix.get(i).set(j, newValue);
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public List<List<T>> getMatrix() {
        return matrix;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1*N vector print
        if (numRows == 1) {
            int N = numColumns;
            int n = (int) Math.round(Math.sqrt(N));
            if (n * n == N) {
                // Pretty print as n x n
                for (int i = 0; i < N; i++) {
                    sb.append(matrix.getFirst().get(i));
                    if (i % n == n - 1) sb.append('\n');
                    else sb.append(' ');
                }
                return sb.toString();
            }
        }

        // general print
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                sb.append(matrix.get(r).get(c));
                if (c < numColumns - 1) sb.append(' ');
            }
            if (r < numRows - 1) sb.append('\n');
        }
        return sb.toString();
    }
}
