#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <chrono>
#include <queue>
#include <mutex>

using namespace std;

struct Participant
{
    int ID;
    int punctaj;
    int tara;
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
    ofstream g("Outputs\\Clasament.txt");
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

int main()
{
    int numProblems = 10;
    int numCountries = 5;

    auto startTime = chrono::high_resolution_clock::now();

    DoublyLinkedList clasamentTari;

    for (int country = 1; country <= numCountries; ++country)
    {
        for (int problem = 1; problem <= numProblems; ++problem)
        {
            string tara = "C" + to_string(country);
            string filename = "Inputs\\Rezultate" + tara + "_P" + to_string(problem) + ".txt";
            ifstream inFile(filename);

            if (!inFile.is_open())
            {
                cerr << "Eroare la deschiderea fisierului " << filename << endl;
                continue;
            }

            int ID, punctaj;
            while (inFile >> ID >> punctaj)
            {
                clasamentTari.addNode({ID, punctaj, country});
            }

            inFile.close();
        }
    }

    clasamentTari.displayList();

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime).count();

    cout << duration;

    return 0;
}
