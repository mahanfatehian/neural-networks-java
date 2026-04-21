package HopfieldNN;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public class Matrix {

    private final int numRows;

    private final int numColumns;

    private final int[][] matrix;

    public int[][] getMatrix() {
        return matrix;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    private Matrix(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.matrix = new int[numRows][numColumns];

    }
    public static Matrix of(Matrix m){
        if (m.matrix == null || m.numRows == 0 || m.numColumns == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int rows = m.numRows;
        int columns = m.numColumns;
        Matrix result = Matrix.create(rows, columns);

        for (int i = 0; i < rows; i++) {
            System.arraycopy(m.matrix[i], 0, result.matrix[i], 0, columns);
        }
        return result;
    }
    public static Matrix create(int numRows, int numColumns) {
        return new Matrix(numRows, numColumns);
    }

    public static Matrix of(int[][] array2d) {
        if (array2d == null || array2d.length == 0 || array2d[0].length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int rows = array2d.length;
        int columns = array2d[0].length;
        Matrix result = Matrix.create(rows, columns);

        for (int i = 0; i < rows; i++) {
            System.arraycopy(array2d[i], 0, result.matrix[i], 0, columns);
        }
        return result;
    }

    public static Matrix matrixMultiplication(Matrix matrix1, Matrix matrix2) {
        if (matrix1.numColumns != matrix2.numRows) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }
        Matrix result = Matrix.create(matrix1.numRows, matrix2.numColumns);
        for (int row = 0; row < matrix1.numRows; row++) {
            for (int column = 0; column < matrix2.numColumns; column++) {
                int sum = 0;
                for (int k = 0; k < matrix1.numColumns; k++) {
                    sum += matrix1.matrix[row][k] * matrix2.matrix[k][column];
                }
                result.matrix[row][column] = sum;
            }

        }
        return result;
    }

    public static Matrix reverse(Matrix m) {
        Matrix result = Matrix.create(m.numColumns, m.numRows);

        for (int i = 0; i < m.numRows; i++) {
            for (int j = 0; j < m.numColumns; j++) {
                result.matrix[j][i] = m.matrix[i][j];
            }
        }
        return result;


    }

    public static Matrix matrixAddition(Matrix matrix1, Matrix matrix2) {
        if (!(matrix1.numRows == matrix2.numRows && matrix1.numColumns == matrix2.numColumns)) {
            throw new IllegalArgumentException("Matrix columns and rows must be equal");
        }
        Matrix result = Matrix.create(matrix1.numRows, matrix1.numColumns);

        for (int i = 0; i < matrix1.numRows; i++) {
            for (int j = 0; j < matrix1.numColumns; j++) {
                result.matrix[i][j] = matrix1.matrix[i][j] + matrix2.matrix[i][j];
            }
        }
        return result;
    }

    public static void clearDiagonals(Matrix matrix) {
        if (matrix.numRows != matrix.numColumns) {
            throw new IllegalArgumentException("Matrix columns and rows must be equal");
        }
        for (int i = 0; i < matrix.numRows; i++) {
            matrix.matrix[i][i] = 0;
        }
    }

    public static void applyFunction(Matrix matrix, IntUnaryOperator function) {
        for (int i = 0; i < matrix.numRows; i++) {
            for (int j = 0; j < matrix.numColumns; j++) {
                //applying lambda function to each value
                matrix.matrix[i][j] = function.applyAsInt(matrix.matrix[i][j]);
            }
        }
    }

    public static Matrix flatten(Matrix m){

        int[][] flatData = new int[1][m.numRows * m.numColumns];
        int index = 0;
        for (int r = 0; r < m.numRows; r++) {
            for (int c = 0; c < m.numColumns; c++) {
                flatData[0][index++] = m.matrix[r][c];
            }
        }
        return Matrix.of(flatData);
    }

    public static Matrix compress(Matrix m, int compressValue, Predicate<int[]> p) {
        System.out.println("BEFORE compress " + Arrays.deepToString(m.matrix));
        if (m.numRows % compressValue != 0 || m.numColumns % compressValue != 0) {
            throw new IllegalArgumentException("number of columns and rows must be divisible by " + compressValue);
        }

        int newRows = m.numRows / compressValue;
        int newCols = m.numColumns / compressValue;
        int[][] compressedData = new int[newRows][newCols];

        // iteration through the new compressed matrix
        for (int i = 0; i < newRows; i++) {
            for (int j = 0; j < newCols; j++) {

                // array to hold values
                int[] block = new int[compressValue * compressValue];
                int index = 0;

                //
                for (int r = 0; r < compressValue; r++) {
                    for (int c = 0; c < compressValue; c++) {
                        int originalRow = (i * compressValue) + r;
                        int originalCol = (j * compressValue) + c;
                        block[index++] = m.matrix[originalRow][originalCol];
                    }
                }
                // Apply function
                compressedData[i][j] = p.test(block) ? 1 : 0;
            }
        }

        System.out.println("AFTER compress " + Arrays.deepToString(compressedData));
        return Matrix.of(compressedData);
    }

    public static Matrix findMostIdentical(Matrix targetMatrix, List<Matrix> matrixList) {
        if (matrixList == null || matrixList.isEmpty()) {
            return null;
        }

        Matrix bestMatch = null;
        int maxMatches = -1;

        for (Matrix candidate : matrixList) {
            int matches = 0;

            // compare element by element
            for (int i = 0; i < targetMatrix.numRows; i++) {
                for (int j = 0; j < targetMatrix.numColumns; j++) {
                    if (targetMatrix.matrix[i][j] == candidate.matrix[i][j]) {
                        matches++;
                    }
                }
            }

            if (matches > maxMatches) {
                maxMatches = matches;
                bestMatch = candidate;
            }
        }

        return bestMatch;
    }


    @Override
    //effective java implementation suggestion
    public boolean equals(Object o) {

        if (this == o) return true;


        if(!(o instanceof Matrix other)) return false ;

        if (this.numRows != other.numRows) return false;
        if (this.numColumns != other.numColumns) return false;

       //deepEqual to check values
        return Arrays.deepEquals(this.matrix, other.matrix);
    }


    @Override
    //effective java implementation suggestion
    public int hashCode() {

        int result = numRows;


        result = 31 * result + numColumns;


        result = 31 * result + Arrays.deepHashCode(matrix);

        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1*N vector print
        if (numRows == 1) {
            int N = numColumns;
            int n = (int) Math.round(Math.sqrt(N));
            if (n * n == N) {
                // Pretty print as n x n
                for (int i = 0; i < N; i++) {
                    sb.append(matrix[0][i]);
                    if (i % n == n - 1) sb.append('\n');
                    else sb.append(' ');
                }
                return sb.toString();
            }
        }

        // general print
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                sb.append(matrix[r][c]);
                if (c < numColumns - 1) sb.append(' ');
            }
            if (r < numRows - 1) sb.append('\n');
        }
        return sb.toString();
    }


}
