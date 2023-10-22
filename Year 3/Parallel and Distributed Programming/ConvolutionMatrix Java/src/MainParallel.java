import Convolutions.*;
import Paths.*;
import Singleton.IOHandler;

import java.io.IOException;

public class MainParallel {
    private static final String OUTPUT_PATH = "C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Outputs\\parallel.txt";
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

//        int totalElements = N * M;
//        int elementsPerThread = totalElements / P;

        long startTime = System.nanoTime();

        //Convolutions.ConvolutionTask[] threads = new Convolutions.ConvolutionTask[P];
        ConvolutionTaskCols[] threads = new ConvolutionTaskCols[P];
        // ConvolutionTaskBlocks[] threads = new ConvolutionTaskBlocks[P];
        for (int i = 0; i < P; i++) {
            end = start + quotient;
            if(remainder > 0) {
                end++;
                remainder--;
            }
//            threads[i] = new Convolutions.ConvolutionTask(inputMatrix, convolutionMatrix, resultMatrix, start, end);
            threads[i] = new ConvolutionTaskCols(inputMatrix, convolutionMatrix, resultMatrix, start, end);

//            int elementsForThread = elementsPerThread + (i < remainder ? 1 : 0);
//            end = start + elementsForThread;
//            threads[i] = new ConvolutionTaskBlocks(inputMatrix, convolutionMatrix, resultMatrix, start, end);
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

