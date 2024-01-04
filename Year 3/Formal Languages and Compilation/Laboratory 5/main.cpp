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
    void addRule(const string &nonTerminal, const vector<string> &production);
    bool parse(const string &input);
    void setStartSymbol(const string &startSymbol)
    {
        this->startSymbol = startSymbol;
    }
    string constructie_sir_prod(stack<string> &alphaStack);

private:
    string startSymbol;                                                // Variable for startSymbol
    unordered_map<string, vector<pair<vector<string>, string>>> rules; // Grammar rules (key is non-terminal) and value is a vector of pairs (production, indicativ_rp)
};

void Grammar::addRule(const string &nonTerminal, const vector<string> &production)
{
    // Form the indicativ_rp as nonTerminal + the number of productions for nonTerminal
    string indicativ_rp = nonTerminal + to_string(rules[nonTerminal].size() + 1);
    rules[nonTerminal].push_back(make_pair(production, indicativ_rp));
}

bool isNonterminal(const string &str)
{
    // str is a non-terminal if it starts with an uppercase letter
    return !str.empty() && isupper(str[0]);
}

bool isTerminal(const string &str)
{
    // str is a terminal if it starts with a lowercase letter
    return !str.empty() && !isupper(str[0]);
}

bool Grammar::parse(const string &input)
{
    stack<string> alphaStack;
    stack<string> betaStack;
    int i = 1;
    string s = "q";
    string t = "t";
    string e = "e";
    string q = "q";
    string r = "r";
    string S = startSymbol;

    // Add the start symbol to the beta stack
    betaStack.push(S);

    while ((s != t) && (s != e))
    {
        if (s == "q")
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
                if (isNonterminal(betaStack.top()))
                {
                    string A = betaStack.top();
                    betaStack.pop();
                    // Get indicativ_rp for the corresponding production
                    string indicativ_rp = rules[A][0].second;
                    // Add indicativ_rp to the alpha stack
                    alphaStack.push(indicativ_rp);
                    for (int j = rules[A][0].first.size() - 1; j >= 0; j--)
                    {
                        betaStack.push(rules[A][0].first[j]);
                    }
                }
                else if (isTerminal(betaStack.top()))
                {
                    if (betaStack.top() == input.substr(i - 1, betaStack.top().length()))
                    {
                        i += betaStack.top().length();
                        string betaTop = betaStack.top();
                        betaStack.pop();
                        alphaStack.push(betaTop);
                    }
                    else
                    {
                        s = r;
                    }
                }
                else
                {
                    s = r;
                }
            }
        }
        else if (s == "r")
        {
            if (!isNonterminal(alphaStack.top()))
            {
                i -= alphaStack.top().length();
                // Pop indicativ_rp from the alpha stack
                string neterminal = alphaStack.top();
                alphaStack.pop();
                // Push the corresponding production into the beta stack
                betaStack.push(neterminal);
            }
            else
            {
                // If there is a production that starts with indicativ_rp
                // Pop indicativ_rp from the alpha stack
                string indicativ_rp = alphaStack.top();
                string number = indicativ_rp.substr(indicativ_rp.length() - 1);
                int index = stoi(number);
                indicativ_rp = indicativ_rp.substr(0, indicativ_rp.length() - 1);

                vector<string> production = rules[indicativ_rp][index - 1].first;

                vector<string> symbols;
                int pos = 0;
                bool exists = true;
                while (!betaStack.empty() && pos < production.size())
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

                if (exists && index < rules[indicativ_rp].size())
                {
                    s = q;
                    // printf("Varful stivei alpha este: %s\n", alphaStack.top().c_str());
                    alphaStack.pop();
                    string indicativ_urm = rules[indicativ_rp][index].second;
                    alphaStack.push(indicativ_urm);
                    // printf("Varful stivei alpha este: %s\n", alphaStack.top().c_str());

                    // If it is epsilon, continue
                    if (rules[indicativ_rp][index].first[0] == string("ε"))
                    {
                        continue;
                    }
                    for (int k = rules[indicativ_rp][index].first.size() - 1; k >= 0; k--)
                    {
                        betaStack.push(rules[indicativ_rp][index].first[k]);
                    }
                }
                else
                {
                    if (i == 1 && indicativ_rp == S)
                    {
                        s = e;
                    }
                    else
                    {
                        alphaStack.pop();
                        betaStack.push(indicativ_rp);
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
        if (alphaTop.size() > 1 && isNonterminal(alphaTop))
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
        string nonTerminal;
        string symbol;
        vector<string> production;

        while (grammarFile >> nonTerminal >> symbol)
        {
            if (isStartSymbolSet)
            {
                grammar.setStartSymbol(nonTerminal);
                isStartSymbolSet = false;
            }

            // split the symbol string by the '_' delimiter
            string delimiter = "_";
            size_t pos = 0;
            string token;
            while ((pos = symbol.find(delimiter)) != string::npos)
            {
                token = symbol.substr(0, pos);
                production.push_back(token);
                symbol.erase(0, pos + delimiter.length());
            }
            production.push_back(symbol);

            grammar.addRule(nonTerminal, production);
            production.clear();
        }

        grammarFile.close();
    }
    else
    {
        cerr << "Nu s-a putut deschide fișierul de gramatică." << endl;
        return 1;
    }

    ifstream inputFile("input.txt");
    string inputSequence;
    string symbol;
    while (inputFile >> symbol)
    {
        inputSequence += symbol;
    }
    inputFile.close();

    if (grammar.parse(inputSequence))
    {
        cout << "Secvența de intrare este acceptată." << endl;
    }
    else
    {
        cout << "Secvența de intrare nu este acceptată." << endl;
    }
    return 0;
}
