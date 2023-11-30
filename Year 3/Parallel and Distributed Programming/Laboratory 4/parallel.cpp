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
    int numThreads;

public:
    // constructor with given number of threads
    MyQueue(int numThreads) : numThreads(numThreads) {}

    void insert(const Participant &participant)
    {
        lock_guard<mutex> lock(mtx);
        participants.push(participant);
    }

    pair<Participant, bool> get()
    {
        lock_guard<mutex> lock(mtx);
        if (participants.empty())
        {
            return {{}, false};
        }
        Participant participant = participants.front();
        participants.pop();
        if (participant.tara < 1)
        {
            cout << "In queue: Participantul " << participant.ID << " nu a participat la problema " << participant.punctaj << endl;
        }
        return {participant, true};
    }

    bool isEmpty()
    {
        lock_guard<mutex> lock(mtx);
        return participants.empty();
    }

    bool isOver()
    {
        lock_guard<mutex> lock(mtx);
        return numThreads == 0;
    }

    void decrementNumThreads()
    {
        lock_guard<mutex> lock(mtx);
        --numThreads;
    }
};

class Node
{
public:
    Participant participant;
    Node *next;
    Node *prev;

    Node(const Participant &participant) : participant(participant), next(nullptr), prev(nullptr) {}
};

class DoublyLinkedList
{
private:
    Node *head;
    Node *tail;
    vector<int> hashTable;
    mutex listMtx;

public:
    DoublyLinkedList() : head(nullptr), tail(nullptr) {}
    ~DoublyLinkedList();

    int hashFunction(const Participant &participant)
    {
        return participant.ID + participant.tara;
    }

    void insertSorted(Node *newNode);
    void addNode(const Participant &participant);
    void deleteNode(int participantID);
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

DoublyLinkedList::~DoublyLinkedList()
{
    Node *current = head;
    while (current)
    {
        Node *next = current->next;
        delete current;
        current = next;
    }
}

void DoublyLinkedList::insertSorted(Node *newNode)
{
    if (!head || newNode->participant.punctaj >= head->participant.punctaj)
    {
        newNode->next = head;
        newNode->prev = nullptr;
        if (head)
        {
            head->prev = newNode;
        }
        head = newNode;
        if (!tail)
        {
            tail = head;
        }
        return;
    }

    Node *current = head;
    while (current->next && current->next->participant.punctaj > newNode->participant.punctaj)
    {
        current = current->next;
    }

    newNode->next = current->next;
    newNode->prev = current;

    if (current->next)
    {
        current->next->prev = newNode;
    }
    else
    {
        tail = newNode;
    }

    current->next = newNode;
}

void DoublyLinkedList::addNode(const Participant &participant)
{
    lock_guard<mutex> lock(listMtx);
    Node *newNode = new Node(participant);

    int hash = hashFunction(participant);
    if (find(hashTable.begin(), hashTable.end(), hash) != hashTable.end())
    {
        delete newNode;
        return;
    }

    if (participant.punctaj < 0)
    {
        if (!head)
        {
            hashTable.push_back(hash);
        }
        else
        {
            deleteNode(participant.ID);
            hashTable.push_back(hash);
        }
        return;
    }

    Node *current = head;
    while (current)
    {
        if (current->participant.ID == participant.ID)
        {
            current->participant.punctaj += participant.punctaj;
            sortList();
            delete newNode;
            return;
        }
        current = current->next;
    }

    insertSorted(newNode);
}

void DoublyLinkedList::deleteNode(int participantID)
{
    Node *current = head;
    while (current)
    {
        if (current->participant.ID == participantID)
        {
            if (current->prev)
            {
                current->prev->next = current->next;
            }
            else
            {
                head = current->next;
            }

            if (current->next)
            {
                current->next->prev = current->prev;
            }
            else
            {
                tail = current->prev;
            }

            delete current;
            return;
        }
        current = current->next;
    }
}

void DoublyLinkedList::displayList()
{
    Node *current = head;
    int position = 1;
    while (current)
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

void workerThread(DoublyLinkedList &clasamentTari, MyQueue &queue)
{
    while (!queue.isOver())
    {
        while (!queue.isEmpty())
        {
            if (queue.isEmpty())
            {
                continue;
            }
            auto participant = queue.get();
            if (!participant.second)
            {
                continue;
            }
            clasamentTari.addNode(participant.first);
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc < 3)
    {
        cerr << "Usage: " << argv[0] << " <numThreads> <numReaders>" << endl;
        return 1;
    }

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

    int numReaders = stoi(argv[2]);

    auto startTime = chrono::high_resolution_clock::now();

    DoublyLinkedList clasamentTari;
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
    int numWorkers = stoi(argv[1]) - numReaders;
    for (int i = 1; i <= numWorkers; ++i)
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

    clasamentTari.displayList();

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime).count();

    cout << duration;

    return 0;
}
