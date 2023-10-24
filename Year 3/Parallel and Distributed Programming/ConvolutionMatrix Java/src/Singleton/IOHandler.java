package Singleton;

import java.io.*;
import java.util.Arrays;

public class IOHandler {
    private static IOHandler instance = null;
    private IOHandler() {
    }
    public static IOHandler getInstance() {
        if (instance == null) {
            instance = new IOHandler();
        }
        return instance;
    }

    private String getAbsolutePath(String fileName) {
        File resourceFile = new File(fileName);
        String path = resourceFile.getAbsolutePath();
        if (path.contains("out\\production\\ConvolutionMatrix Java")) {
            path = path.replace("src\\", "");
        }
        return path;
    }

    public int [][] readMatrixFromFile(String fileName) throws IOException{
        int[][] matrix;
        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePath(fileName)))) {
            String line = br.readLine();
            String[] dimensions = line.split(" ");
            int N = Integer.parseInt(dimensions[0]);
            int M = Integer.parseInt(dimensions[1]);

            matrix = new int[N][M];

            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                for (int col = 0; col < M; col++) {
                    matrix[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
        }
        return matrix;
    }

    public void writeMatrixToFile(int[][] matrix, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAbsolutePath(fileName)))) {
            for (int[] ints : matrix) {
                writer.write(Arrays.toString(ints).replaceAll("[\\[\\],]", ""));
                writer.newLine();
            }
        }
    }
}
