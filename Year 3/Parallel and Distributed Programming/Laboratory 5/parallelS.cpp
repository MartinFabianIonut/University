#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <chrono>
#include <queue>
#include <mutex>
#include <thread>
#include <condition_variable>

using namespace std;

ofstream g("Outputs\\ClasamentParalel.txt");

struct Participant
{
    int ID;
    int punctaj;
    int tara;
};

class MyQueue
{
    queue<Participant> participants;
    mutex mtx;
    condition_variable insertCV;
    condition_variable getCV;
    int numThreads;
    const int MAX_CAPACITY = 50;

public:
    MyQueue(int numThreads) : numThreads(numThreads) {}

    void insert(const Participant &participant)
    {
        unique_lock<mutex> lock(mtx);
        insertCV.wait(lock, [this]
                      { return participants.size() < MAX_CAPACITY; });
        participants.push(participant);
        getCV.notify_one();
    }

    pair<Participant, bool> get()
    {
        unique_lock<mutex> lock(mtx);
        getCV.wait(lock, [this]
                   { return !participants.empty() || numThreads == 0; });
        if (participants.empty())
        {
            return {{}, false};
        }

        Participant participant = participants.front();
        participants.pop();
        insertCV.notify_one();
        return {participant, true};
    }

    bool canContinue()
    {
        lock_guard<mutex> lock(mtx);
        return !participants.empty() || numThreads > 0;
    }

    void decrementNumThreads()
    {
        lock_guard<mutex> lock(mtx);
        --numThreads;
        getCV.notify_all();
    }
};

class Node
{
public:
    Participant participant;
    Node *next;
    mutex nodeMtx;

    Node(const Participant &participant) : participant(participant), next(nullptr) {}
};

class SinglyLinkedList
{
private:
    Node *head;
    vector<int> hashTable;
    mutex hashTableMutex;

public:
    SinglyLinkedList() : head(new Node({-1, 0, 0})) {}

    ~SinglyLinkedList()
    {
        Node *current = head;
        while (current)
        {
            Node *next = current->next;
            delete current;
            current = next;
        }
    }

    int hashFunction(const Participant &participant)
    {
        return participant.ID + participant.tara;
    }

    void addNode(const Participant &participant);
    void displayList();
    void sortList()
    {
        Node *current = head;
        while (current)
        {
            Node *next = current->next;
            while (next)
            {
                if (current->participant.punctaj < next->participant.punctaj ||
                    (current->participant.punctaj == next->participant.punctaj &&
                     current->participant.ID > next->participant.ID))
                {
                    swap(current->participant, next->participant);
                }
                next = next->next;
            }
            current = current->next;
        }
    }
};

void SinglyLinkedList::addNode(const Participant &participant)
{
    bool negativeScore = participant.punctaj < 0;
    int hash = hashFunction(participant);

    hashTableMutex.lock();
    if (find(hashTable.begin(), hashTable.end(), hash) != hashTable.end())
    {
        hashTableMutex.unlock();
        return;
    }

    if (negativeScore)
    {
        hashTable.push_back(hash);
    }

    Node *prev = head;
    prev->nodeMtx.lock();
    Node *current = head->next;

    hashTableMutex.unlock();
    bool alreadyExists = false;
    while (current != nullptr && !alreadyExists)
    {
        current->nodeMtx.lock();
        if (current->participant.ID == participant.ID)
        {
            alreadyExists = true;
            if (participant.punctaj > 0)
            {
                current->participant.punctaj += participant.punctaj;
            }
            else
            {
                prev->next = current->next;
            }
        }
        prev->nodeMtx.unlock();
        prev = current;
        current = current->next;
    }

    if (!alreadyExists && participant.punctaj > 0)
    {
        Node *newNode = new Node(participant);
        prev->next = newNode;
    }
    prev->nodeMtx.unlock();
    if (alreadyExists && participant.punctaj < 0)
    {
        delete prev;
    }
}

void SinglyLinkedList::displayList()
{
    Node *current = head;
    int position = 1;
    while (current->participant.ID != -1)
    {
        g << "Pozitie: " << position << ", ID: " << current->participant.ID << ", Punctaj: " << current->participant.punctaj << ", Tara: " << current->participant.tara << endl;
        current = current->next;
        ++position;
    }
    g.close();
}

void readerThread(const vector<pair<string, int>> &filenames, MyQueue &queue)
{
    for (auto [filename, country] : filenames)
    {
        ifstream inFile(filename);

        if (!inFile.is_open())
        {
            cerr << "Eroare la deschiderea fisierului " << filename << endl;
            continue;
        }

        int ID, punctaj;
        while (inFile >> ID >> punctaj)
        {
            queue.insert({ID, punctaj, country});
        }

        inFile.close();
    }
    queue.decrementNumThreads();
}

void workerThread(SinglyLinkedList &clasamentTari, MyQueue &queue)
{
    while (queue.canContinue())
    {
        auto participant = queue.get();
        if (participant.second)
        {
            clasamentTari.addNode(participant.first);
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc < 3)
    {
        cerr << "Usage: " << argv[0] << " <numWorkers> <numReaders>" << endl;
        return 1;
    }

    int numWorkers = stoi(argv[1]);
    int numReaders = stoi(argv[2]);
    ofstream k("Outputs\\parametri.txt");
    k << numWorkers << " " << numReaders;
    k.close();

    int numProblems = 10;
    int numCountries = 5;

    vector<pair<string, int>> filenames;
    for (int problem = 1; problem <= numProblems; ++problem)
    {
        for (int country = 1; country <= numCountries; ++country)
        {
            string tara = "C" + to_string(country);
            string filename = "Inputs\\Rezultate" + tara + "_P" + to_string(problem) + ".txt";
            filenames.emplace_back(filename, country);
        }
    }

    auto startTime = chrono::high_resolution_clock::now();

    SinglyLinkedList clasamentTari;
    MyQueue queue = MyQueue(numReaders);

    vector<thread> readerThreads;
    int start = 0, end;
    int quotient = filenames.size() / numReaders;
    int remainder = filenames.size() % numReaders;
    for (int i = 1; i <= numReaders; ++i)
    {
        end = start + quotient;
        if (remainder > 0)
        {
            ++end;
            --remainder;
        }
        vector<pair<string, int>> filesSubset(filenames.begin() + start, filenames.begin() + end);
        readerThreads.emplace_back(readerThread, filesSubset, ref(queue));
        start = end;
    }

    vector<thread> workerThreads;

    for (int i = 0; i < numWorkers; ++i)
    {
        workerThreads.emplace_back(workerThread, ref(clasamentTari), ref(queue));
    }

    for (auto &thread : readerThreads)
    {
        thread.join();
    }

    for (auto &thread : workerThreads)
    {
        thread.join();
    }

    clasamentTari.sortList();
    clasamentTari.displayList();

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime).count();

    auto durationString = to_string(duration);
    cout << durationString;

    return 0;
}
