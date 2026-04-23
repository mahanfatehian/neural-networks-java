package BackPropagationNN;

public enum DigitLabel {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE;

    public static DigitLabel fromOutput(double[] out) {
        int index = 0;
        double max = out[0];

        for (int i = 1; i < out.length; i++) {
            if (out[i] > max) {
                max = out[i];
                index = i;
            }
        }
        return DigitLabel.values()[index];
    }
}
