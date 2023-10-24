package Convolutions;

import Singleton.Convolution;

public class ConvolutionTaskBlocks extends Thread {
    private final int[][] inputMatrix;
    private final int[][] convolutionMatrix;
    private final int[][] resultMatrix;
    private final int startIdx;
    private final int endIdx;
    private final Convolution convolution = Convolution.getInstance();

    public ConvolutionTaskBlocks(int[][] inputMatrix, int[][] convolutionMatrix, int[][] resultMatrix, int startIdx, int endIdx) {
        this.inputMatrix = inputMatrix;
        this.convolutionMatrix = convolutionMatrix;
        this.resultMatrix = resultMatrix;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }

    @Override
    public void run() {
        int N = inputMatrix.length;
        int M = inputMatrix[0].length;
        for (int idx = startIdx; idx < endIdx && idx < N * M; idx++) {
            int i = idx / M;
            int j = idx % M;
            resultMatrix[i][j] = convolution.applyConvolution(inputMatrix, convolutionMatrix, i, j);

        }
    }
}
