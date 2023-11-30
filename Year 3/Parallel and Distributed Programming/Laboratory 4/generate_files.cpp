#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <ctime>

using namespace std;

void generateResultsFile(const string &country, int countryId, int problem, int numParticipants)
{
    string filename = "Inputs\\Rezultate" + country + "_P" + to_string(problem) + ".txt";
    ofstream file(filename);

    if (!file.is_open())
    {
        cerr << "Error opening file: " << filename << endl;
        return;
    }

    for (int participant = (countryId - 1) * numParticipants; participant < countryId * numParticipants; ++participant)
    {
        int score = rand() % 102 - 1;
        if (score != 0)
        {
            file << participant << " " << score << "\n";
        }
    }

    file.close();
}

void generateAllFiles(int numCountries, int numParticipantsPerCountry, int numProblems)
{
    srand(static_cast<unsigned>(time(nullptr)));

    for (int country = 1; country <= numCountries; ++country)
    {
        for (int problem = 1; problem <= numProblems; ++problem)
        {
            generateResultsFile("C" + to_string(country), country, problem, numParticipantsPerCountry);
        }
    }
}

int main()
{
    int numCountries = 5;
    int numParticipantsPerCountry = 100;
    int numProblems = 10;

    generateAllFiles(numCountries, numParticipantsPerCountry, numProblems);

    return 0;
}
