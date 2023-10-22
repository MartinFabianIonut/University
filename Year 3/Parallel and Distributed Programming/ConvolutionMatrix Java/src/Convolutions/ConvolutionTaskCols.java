package Convolutions;

import Singleton.Convolution;

public class ConvolutionTaskCols extends Thread {
    private final int[][] inputMatrix;
    private final int[][] convolutionMatrix;
    private final int[][] resultMatrix;
    private final int startCol;
    private final int endCol;
    private final Convolution convolution = Convolution.getInstance();

    public ConvolutionTaskCols(int[][] inputMatrix, int[][] convolutionMatrix, int[][] resultMatrix, int startCol, int endCol) {
        this.inputMatrix = inputMatrix;
        this.convolutionMatrix = convolutionMatrix;
        this.resultMatrix = resultMatrix;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    @Override
    public void run() {
        int N = inputMatrix.length;
        int M = inputMatrix[0].length;
        for (int j = startCol; j < endCol && j < M; j++) {
            for (int i = 0; i < N; i++) {
                resultMatrix[i][j] = convolution.applyConvolution(inputMatrix, convolutionMatrix, i, j);
            }
        }
    }
}
