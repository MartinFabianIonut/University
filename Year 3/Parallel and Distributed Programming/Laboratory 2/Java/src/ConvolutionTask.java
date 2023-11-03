import Singleton.Convolution;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ConvolutionTask extends Thread {
    private final int[][] inputMatrix;
    private final int[][] convolutionMatrix;
    private final int start;
    private final int end;
    private final CyclicBarrier barrier;
    private final Convolution convolution = Convolution.getInstance();

    public ConvolutionTask(int[][] inputMatrix, int[][] convolutionMatrix, int start, int end, CyclicBarrier barrier) {
        this.inputMatrix = inputMatrix;
        this.convolutionMatrix = convolutionMatrix;
        this.start = start;
        this.end = end;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int M = inputMatrix[0].length;
        int []buffFrontLineStart = new int[M];
        int []buffCurrentLine = new int[M];
        int []buffFrontLineEnd = new int[M];

        //COPY THE FRONT LINE START
        if (start == 0) {
            System.arraycopy(inputMatrix[0], 0, buffFrontLineStart, 0, M);
        } else {
            System.arraycopy(inputMatrix[start - 1], 0, buffFrontLineStart, 0, M);
        }

        //COPY THE FRONT LINE END
        if (end == inputMatrix.length) {
            System.arraycopy(inputMatrix[inputMatrix.length - 1], 0, buffFrontLineEnd, 0, M);
        } else {
            System.arraycopy(inputMatrix[end], 0, buffFrontLineEnd, 0, M);
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        //COPY THE CURRENT LINE
        System.arraycopy(inputMatrix[start], 0, buffCurrentLine, 0, M);

        //Create matrix of three lines
        int[][] buffer = new int[3][M];
        System.arraycopy(buffFrontLineStart, 0, buffer[0], 0, M);
        System.arraycopy(buffCurrentLine, 0, buffer[1], 0, M);
        System.arraycopy(inputMatrix[start + 1], 0, buffer[2], 0, M);

        for (int i = start; i < end - 1; ++i) {
            for (int j = 0; j < M; ++j) {
                inputMatrix[i][j] = convolution.applyConvolution(buffer, convolutionMatrix, j);
            }
            System.arraycopy(buffer[1], 0, buffer[0], 0, M);
            System.arraycopy(buffer[2], 0, buffer[1], 0, M);
            if (i + 2 == inputMatrix.length || i + 2 == end)
                System.arraycopy(buffFrontLineEnd, 0, buffer[2], 0, M);
            else
                System.arraycopy(inputMatrix[i + 2], 0, buffer[2], 0, M);
        }

        for (int j = 0; j < M; ++j) {
            inputMatrix[end - 1][j] = convolution.applyConvolution(buffer, convolutionMatrix, j);
        }
    }
}
