package Singleton;

public class Convolution {
    private static Convolution instance = null;
    private Convolution() {
    }
    public static Convolution getInstance() {
        if (instance == null) {
            instance = new Convolution();
        }
        return instance;
    }
    public int applyConvolution(int[][] inputMatrix, int[][] convolutionMatrix, int j) {
        int result = 0;
        int n = inputMatrix.length;
        int m = inputMatrix[0].length;
        int k = convolutionMatrix.length;
        for (int p = 0; p < k; p++) {
            for (int q = 0; q < k; q++) {
                int x = 1 - k/2 + p;
                int y;
                if (j == 0)
                    y = Math.max(j - k/2 + q, 0);
                else if (j == m - 1)
                    y = Math.min(j - k/2 + q, m - 1);
                else
                    y = j - k/2 + q;
                result += inputMatrix[x][y] * convolutionMatrix[p][q];
            }
        }
        return result;
    }
}
