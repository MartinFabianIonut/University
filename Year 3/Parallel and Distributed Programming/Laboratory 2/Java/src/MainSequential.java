import Paths.CONVOLUTION_PATHS;
import Paths.INPUT_PATHS;
import Singleton.ConvolutionClassic;
import Singleton.IOHandler;

import java.io.IOException;

public class MainSequential {
    private static final String OUTPUT_PATH = "src\\Outputs\\sequential.txt";
    private static final IOHandler ioHandler = IOHandler.getInstance();

    private static final ConvolutionClassic convolution = ConvolutionClassic.getInstance();

    public static void main(String[] args) throws IOException {
        String whatMatrix = INPUT_PATHS.values()[Integer.parseInt(args[0])].getPath();
        String whatConvolution = CONVOLUTION_PATHS.values()[Integer.parseInt(args[1])].getPath();

        int[][] convolutionMatrix = ioHandler.readMatrixFromFile(whatConvolution);
        int[][] inputMatrix = ioHandler.readMatrixFromFile(whatMatrix);
        int N = inputMatrix.length;
        int M = inputMatrix[0].length;

        long startTime = System.nanoTime();

        int[] buffer1 = new int[M];
        int[] buffer2 = new int[M];

        for (int j = 0; j < M; ++j) {
            buffer1[j] = convolution.applyConvolution(inputMatrix, convolutionMatrix,0,j);
        }

        for (int i = 1; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                buffer2[j] = convolution.applyConvolution(inputMatrix, convolutionMatrix, i,j);
            }

            System.arraycopy(buffer1, 0, inputMatrix[i - 1], 0, M);
            System.arraycopy(buffer2, 0, buffer1, 0, M);
        }

        System.arraycopy(buffer2, 0, inputMatrix[N - 1], 0, M);

        long endTime = System.nanoTime();
        System.out.println((double)(endTime - startTime)/1E6);//ms
        ioHandler.writeMatrixToFile(inputMatrix, OUTPUT_PATH);
    }
}
