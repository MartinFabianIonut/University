package Inputs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateMatrix {
    public static void main(String[] args) {
        // generate data in file with name date+nrRows+nrCols
        String path = "src/Inputs/";
        String fileName = path+"data10x10.txt";
        generateRandomData(fileName,10, 10);
        fileName = path+"data1000x1000.txt";
        generateRandomData(fileName,1000, 1000);
        fileName = path+"data10x10000.txt";
        generateRandomData(fileName,10, 10000);
        fileName = path+"data10000x10.txt";
        generateRandomData(fileName,10000, 10);
        String convFileName = path+"convolution3x3.txt";
        generateConvolutionData(convFileName, 3,3);
        convFileName = path+"convolution5x5.txt";
        generateConvolutionData(convFileName, 5,5);
    }

    private static void generateConvolutionData(String convFileName, int rows, int cols) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(convFileName))) {
            Random random = new Random();
            writer.write(rows + " " + cols);
            writer.newLine();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(random.nextInt(2) + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateRandomData(String fileName, int rows, int cols) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            writer.write(rows + " " + cols);
            writer.newLine();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(random.nextInt(100) + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
