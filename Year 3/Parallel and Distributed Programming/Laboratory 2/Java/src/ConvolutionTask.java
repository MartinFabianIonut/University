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
        int[][] buffer = new int[end - start + 1][M];

        for (int i = start; i < end; ++i) {
            for (int j = 0; j < M; ++j) {
                buffer[i - start][j] = convolution.applyConvolution(inputMatrix, convolutionMatrix, i, j);
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        for (int i = start; i < end; ++i)
        {
            System.arraycopy(buffer[i - start], 0, inputMatrix[i], 0, M);
        }
    }
}
