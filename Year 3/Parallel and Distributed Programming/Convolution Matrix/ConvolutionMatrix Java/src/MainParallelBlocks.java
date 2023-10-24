import Convolutions.ConvolutionTaskBlocks;
import Paths.CONVOLUTION_PATHS;
import Paths.INPUT_PATHS;
import Singleton.IOHandler;

import java.io.IOException;

public class MainParallelBlocks {
    private static final String OUTPUT_PATH = "src\\Outputs\\parallel.txt";
    private static final IOHandler ioHandler = IOHandler.getInstance();
    public static void main(String[] args) throws IOException {
        int P = Integer.parseInt(args[0]);
        String whatMatrix = INPUT_PATHS.values()[Integer.parseInt(args[1])].getPath();
        String whatConvolution = CONVOLUTION_PATHS.values()[Integer.parseInt(args[2])].getPath();

        int[][] convolutionMatrix = ioHandler.readMatrixFromFile(whatConvolution);
        int[][] inputMatrix = ioHandler.readMatrixFromFile(whatMatrix);
        int N = inputMatrix.length;
        int M = inputMatrix[0].length;
        int[][] resultMatrix = new int[N][M];

        int start = 0, end;
        int quotient = N / P;
        int remainder = N % P;

        int totalElements = N * M;
        int elementsPerThread = totalElements / P;

        long startTime = System.nanoTime();

        ConvolutionTaskBlocks[] threads = new ConvolutionTaskBlocks[P];
        for (int i = 0; i < P; i++) {
            end = start + elementsPerThread + (i < remainder ? 1 : 0);
            threads[i] = new ConvolutionTaskBlocks(inputMatrix, convolutionMatrix, resultMatrix, start, end);
            threads[i].start();
            start = end;
        }
        for (int i = 0; i < P; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        System.out.println((double)(endTime - startTime)/1E6);//ms
        ioHandler.writeMatrixToFile(resultMatrix, OUTPUT_PATH);
    }
}

