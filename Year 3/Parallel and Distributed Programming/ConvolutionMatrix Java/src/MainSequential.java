
import Paths.*;
import Singleton.*;

import java.io.File;
import java.io.IOException;

public class MainSequential {
    private static final String OUTPUT_PATH = "C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Outputs\\sequential.txt";
    private static final IOHandler ioHandler = IOHandler.getInstance();
    private static final Convolution convolution = Convolution.getInstance();
    public static void main(String[] args) throws IOException {
        String whatMatrix = INPUT_PATHS.values()[Integer.parseInt(args[0])].getPath();
        String whatConvolution = CONVOLUTION_PATHS.values()[Integer.parseInt(args[1])].getPath();

        int[][] convolutionMatrix = ioHandler.readMatrixFromFile(whatConvolution);
        int[][] inputMatrix = ioHandler.readMatrixFromFile(whatMatrix);
        int N = inputMatrix.length;
        int M = inputMatrix[0].length;

        long startTime = System.nanoTime();
        int[][] resultMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                resultMatrix[i][j] = convolution.applyConvolution(inputMatrix, convolutionMatrix, i, j);
            }
        }
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime)/1E6);//ms
        ioHandler.writeMatrixToFile(resultMatrix, OUTPUT_PATH);
    }
}
