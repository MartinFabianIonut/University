#include <iostream>
#include <fstream>
#include <cstdlib>

using namespace std;

void generateConvolutionData(const string &convFileName, int rows, int cols)
{
    ofstream writer(convFileName);
    if (!writer.is_open())
    {
        cerr << "Error opening file: " << convFileName << endl;
        return;
    }

    writer << rows << " " << cols << endl;

    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            writer << rand() % 2 << " ";
        }
        writer << endl;
    }

    writer.close();
}

void generateRandomData(const string &fileName, int rows, int cols)
{
    ofstream writer(fileName);
    if (!writer.is_open())
    {
        cerr << "Error opening file: " << fileName << endl;
        return;
    }

    writer << rows << " " << cols << endl;

    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            writer << rand() % 100 << " ";
        }
        writer << endl;
    }

    writer.close();
}

int main()
{
    // generate data in file with name date+nrRows+nrCols
    string path = "Inputs/";
    string fileName = path + "data10x10.txt";
    generateRandomData(fileName, 10, 10);
    fileName = path + "data1000x1000.txt";
    generateRandomData(fileName, 1000, 1000);
    fileName = path + "data10000x10000.txt";
    generateRandomData(fileName, 10000, 10000);
    string convFileName = path + "convolution3x3.txt";
    generateConvolutionData(convFileName, 3, 3);
    return 0;
}
