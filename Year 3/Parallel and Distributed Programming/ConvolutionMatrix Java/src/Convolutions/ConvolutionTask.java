package Convolutions;

import Singleton.Convolution;

public class ConvolutionTask extends Thread {
    private final int[][] inputMatrix;
    private final int[][] convolutionMatrix;
    private final int[][] resultMatrix;
    private final int start;
    private final int end;
    private final Convolution convolution = Convolution.getInstance();

    public ConvolutionTask(int[][] inputMatrix, int[][] convolutionMatrix, int[][] resultMatrix, int start, int end) {
        this.inputMatrix = inputMatrix;
        this.convolutionMatrix = convolutionMatrix;
        this.resultMatrix = resultMatrix;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        int M = inputMatrix[0].length;
        for (int i = start; i < end; i++) {
            for (int j = 0; j < M; j++) {
                resultMatrix[i][j] = convolution.applyConvolution(inputMatrix, convolutionMatrix, i, j);
            }
        }
    }
}
