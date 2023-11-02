import Paths.*;
import Singleton.IOHandler;

import java.io.IOException;
import java.util.concurrent.CyclicBarrier;

public class MainParallel {
    private static final String OUTPUT_PATH = "src\\Outputs\\parallel.txt";
    private static final IOHandler ioHandler = IOHandler.getInstance();

    public static void main(String[] args) throws IOException {
        int P = Integer.parseInt(args[0]);
        CyclicBarrier barrier = new CyclicBarrier(P);
        String whatMatrix = INPUT_PATHS.values()[Integer.parseInt(args[1])].getPath();
        String whatConvolution = CONVOLUTION_PATHS.values()[Integer.parseInt(args[2])].getPath();

        int[][] convolutionMatrix = ioHandler.readMatrixFromFile(whatConvolution);
        int[][] inputMatrix = ioHandler.readMatrixFromFile(whatMatrix);
        int N = inputMatrix.length;

        int start = 0, end;
        int quotient = N / P;
        int remainder = N % P;

        long startTime = System.nanoTime();

        ConvolutionTask[] threads = new ConvolutionTask[P];
        for (int i = 0; i < P; i++) {
            end = start + quotient + (i < remainder ? 1 : 0);
            threads[i] = new ConvolutionTask(inputMatrix, convolutionMatrix, start, end, barrier);
            threads[i].start();
            start = end;
        }

        try {
            for (int i = 0; i < P; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        long endTime = System.nanoTime();
        System.out.println((double)(endTime - startTime)/1E6);//ms
        ioHandler.writeMatrixToFile(inputMatrix, OUTPUT_PATH);
    }
}
