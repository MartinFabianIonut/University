package Singleton;

public class ConvolutionClassic {
    private static ConvolutionClassic instance = null;
    private ConvolutionClassic() {
    }
    public static ConvolutionClassic getInstance() {
        if (instance == null) {
            instance = new ConvolutionClassic();
        }
        return instance;
    }
    public int applyConvolution(int[][] inputMatrix, int[][] convolutionMatrix, int i, int j) {
        int result = 0;
        int n = inputMatrix.length;
        int m = inputMatrix[0].length;
        int k = convolutionMatrix.length;
        for (int p = 0; p < k; p++) {
            for (int q = 0; q < k; q++) {
                int x = Math.min(Math.max(i - k / 2 + p, 0), n - 1);
                int y = Math.min(Math.max(j - k / 2 + q, 0), m - 1);
                result += inputMatrix[x][y] * convolutionMatrix[p][q];
            }
        }
        return result;
    }
}
