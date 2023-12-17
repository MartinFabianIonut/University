#include <iostream>
#include <fstream>
#include <string>
#include <stack>
#include <unordered_map>
#include <vector>

using namespace std;

class Grammar
{
public:
    void addRule(char nonTerminal, const string &production);
    bool parse(const string &input);
    void setStartSymbol(char startSymbol)
    {
        this->startSymbol = startSymbol;
    }
    string constructie_sir_prod(stack<string> &alphaStack);

private:
    char startSymbol;                                        // Variabila privata pentru startSymbol
    unordered_map<char, vector<pair<string, string>>> rules; // Regulile gramaticii (cheia este non-terminalul) iar valoarea este un vector de perechi (productie, indicativ_rp)
};

void Grammar::addRule(char nonTerminal, const string &production)
{
    // formeaza indicativ_rp ca fiind nonTerminal + numarul de productii pentru nonTerminal
    string indicativ_rp = nonTerminal + to_string(rules[nonTerminal].size() + 1);
    rules[nonTerminal].push_back(make_pair(production, indicativ_rp));
}

bool isNonterminal(char ch)
{
    // ch este un non-terminal dacă este o literă mare
    return ch >= 'A' && ch <= 'Z';
}

bool isTerminal(char ch)
{
    // ch este un terminal dacă este o literă mică
    return ch >= 'a' && ch <= 'z';
}

bool Grammar::parse(const string &input)
{
    stack<string> alphaStack;
    stack<char> betaStack;
    int i = 1;
    char s = 'q';
    char t = 't';
    char e = 'e';
    char q = 'q';
    char r = 'r';
    char S = startSymbol;

    // Adaugă simbolul de start în stiva beta
    betaStack.push(S);

    while ((s != t) && (s != e))
    {
        if (s == 'q')
        {
            if (betaStack.empty())
            {
                if (i == input.length() + 1)
                {
                    s = t;
                }
                else
                {
                    s = r;
                }
            }
            else
            {
                if (isupper(betaStack.top()))
                {
                    char A = betaStack.top();
                    betaStack.pop();
                    // ia indicativ_rp pentru producția corespunzătoare
                    string indicativ_rp = rules[A][0].second;
                    // Adaugă indicativ_rp în stiva alpha
                    alphaStack.push(indicativ_rp);
                    for (int j = rules[A][0].first.length() - 1; j >= 0; j--)
                    {
                        betaStack.push(rules[A][0].first[j]);
                    }
                }
                else if (betaStack.top() == input[i - 1])
                {
                    i++;
                    char betaTop = betaStack.top();
                    betaStack.pop();
                    alphaStack.push(string(1, betaTop));
                }
                else
                {
                    s = r;
                }
            }
        }
        else
        {
            if (s == 'r')
            {
                if (!isupper(alphaStack.top()[0]))
                {
                    i--;
                    // Scoate indicativ_rp din stiva alpha
                    string neterminal = alphaStack.top();
                    alphaStack.pop();
                    // push productia corespunzatoare in stiva beta
                    betaStack.push(neterminal[0]);
                }
                else
                {
                    // daca exista o productie care incepe cu indicativ_rp
                    // scoate indicativ_rp din stiva alpha
                    string indicativ_rp = alphaStack.top();
                    string number = indicativ_rp.substr(1);
                    int index = stoi(number);

                    string production = rules[indicativ_rp[0]][index - 1].first;

                    vector<char> symbols;
                    int pos = 0;
                    bool exists = true;
                    while (!betaStack.empty() && pos < production.length())
                    {
                        if (betaStack.top() == production[pos])
                        {
                            symbols.push_back(betaStack.top());
                            betaStack.pop();
                            pos++;
                        }
                        else
                        {
                            for (int i = symbols.size() - 1; i >= 0; i--)
                            {
                                betaStack.push(symbols[i]);
                            }
                            exists = false;
                            break;
                        }
                    }

                    if (exists && index < rules[indicativ_rp[0]].size())
                    {
                        s = q;
                        alphaStack.pop();
                        string indicativ_urm = rules[indicativ_rp[0]][index].second;
                        alphaStack.push(indicativ_urm);
                        // if it is epsilon continue
                        if (rules[indicativ_rp[0]][index].first == "ε")
                        {
                            continue;
                        }
                        for (int k = rules[indicativ_rp[0]][index].first.length() - 1; k >= 0; k--)
                        {
                            betaStack.push(rules[indicativ_rp[0]][index].first[k]);
                        }
                    }
                    else
                    {
                        if (i == 1 && indicativ_rp[0] == S)
                        {
                            s = e;
                        }
                        else
                        {
                            alphaStack.pop();
                            betaStack.push(indicativ_rp[0]);
                        }
                    }
                }
            }
        }
    }
    if (s == t)
        cout << constructie_sir_prod(alphaStack) << endl;
    return s == t;
}

string Grammar::constructie_sir_prod(stack<string> &alphaStack)
{
    string sir_prod = "";
    while (!alphaStack.empty())
    {
        string alphaTop = alphaStack.top();
        alphaStack.pop();
        if (alphaTop.length() > 1 && isupper(alphaTop[0]))
        {
            sir_prod = alphaTop + " " + sir_prod;
        }
    }
    return sir_prod;
}

int main()
{
    Grammar grammar;
    bool isStartSymbolSet = true;
    ifstream grammarFile("gramatica.txt");
    if (grammarFile.is_open())
    {
        char nonTerminal;
        string production;

        while (grammarFile >> nonTerminal >> production)
        {
            if (isStartSymbolSet)
            {
                grammar.setStartSymbol(nonTerminal);
                isStartSymbolSet = false;
            }
            grammar.addRule(nonTerminal, production);
        }

        grammarFile.close();
    }
    else
    {
        cerr << "Nu s-a putut deschide fișierul de gramatică." << endl;
        return 1;
    }

    ifstream inputFile("input.txt");
    if (inputFile.is_open())
    {
        string inputSequence;
        inputFile >> inputSequence;

        if (grammar.parse(inputSequence))
        {
            cout << "Secvența de intrare este acceptată." << endl;
        }
        else
        {
            cout << "Secvența de intrare nu este acceptată." << endl;
        }

        inputFile.close();
    }
    else
    {
        cerr << "Nu s-a putut deschide fișierul de intrare." << endl;
        return 1;
    }

    return 0;
}
