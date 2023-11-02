public class FiniteAutomaton
{
	private HashSet<string> states;
	private HashSet<string> alphabet;
	private string initialState;
	private HashSet<string> finalStates;
	private Dictionary<(string, string), HashSet<string>> delta;

	public FiniteAutomaton(HashSet<string> states, HashSet<string> alphabet, string initialState, HashSet<string> finalStates, Dictionary<(string, string), HashSet<string>> delta)
	{
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;
		this.finalStates = finalStates;
		this.delta = delta;
	}

	public bool Accepts(string input)
	{
		HashSet<string> currentStates = new HashSet<string> { initialState };

		foreach (char symbol in input)
		{
			if (!alphabet.Contains(symbol.ToString()))
			{
				return false;
			}

			HashSet<string> nextStates = new HashSet<string>();

			foreach (string currentState in currentStates)
			{
				var transitionKey = (currentState, symbol.ToString());
				if (delta.ContainsKey(transitionKey))
				{
					nextStates.UnionWith(delta[transitionKey]);
				}
			}

			if (nextStates.Count == 0)
			{
				return false;
			}

			currentStates = nextStates;
		}

		return currentStates.Any(state => finalStates.Contains(state));
	}

	public string LongestAcceptedPrefix(string input)
	{
		HashSet<string> currentStates = new HashSet<string> { initialState };
		HashSet<string> lastAcceptedStates = new HashSet<string>();
		int lastAcceptedIndex = -1;

		for (int i = 0; i < input.Length; i++)
		{
			char symbol = input[i];

			if (!alphabet.Contains(symbol.ToString()))
			{
				break;
			}

			HashSet<string> nextStates = new HashSet<string>();

			foreach (string currentState in currentStates)
			{
				var transitionKey = (currentState, symbol.ToString());
				if (delta.ContainsKey(transitionKey))
				{
					nextStates.UnionWith(delta[transitionKey]);
				}
			}

			if (nextStates.Count == 0)
			{
				break;
			}

			currentStates = nextStates;

			if (currentStates.Any(state => finalStates.Contains(state)))
			{
				lastAcceptedStates = new HashSet<string>(currentStates);
				lastAcceptedIndex = i;
			}
		}

		if (lastAcceptedIndex == -1)
		{
			if (finalStates.Contains(initialState))
			{
				return "epsilon";
			}

			return "";
		}

		return input.Substring(0, lastAcceptedIndex + 1);
	}

	public void PrintStates()
	{
		Console.WriteLine("States:");
		Console.WriteLine(string.Join(", ", states));
		Console.WriteLine();
	}

	public void PrintAlphabet()
	{
		Console.WriteLine("Alphabet:");
		Console.WriteLine(string.Join(", ", alphabet));
		Console.WriteLine();
	}

	public void PrintTransitions()
	{
		Console.WriteLine("Transitions:");
		foreach (var transition in delta)
		{
			Console.WriteLine($"{transition.Key.Item1} --({transition.Key.Item2})--> {string.Join(", ", transition.Value)}");
		}
		Console.WriteLine();
	}

	public void PrintFinalStates()
	{
		Console.WriteLine("Final States:");
		Console.WriteLine(string.Join(", ", finalStates));
		Console.WriteLine();
	}
}

class Program
{
	private static HashSet<string>? states;
	private static HashSet<string>? alphabet;
	private static string? initialState;
	private static HashSet<string>? finalStates;
	private static Dictionary<(string, string), HashSet<string>>? delta;

	static int ReadFromFile()
	{
		string filePath = "C:\\GIT\\University\\Year 3\\Formal Languages and Compilation\\Laboratory 2\\Laboratory 2\\automaton_input2.txt";
		string[] lines = File.ReadAllLines(filePath);

		// Read states
		states = new HashSet<string>(lines[0].Split(','));

		// Read alphabet
		alphabet = new HashSet<string>(lines[1].Split(','));

		// Read initial state
		initialState = lines[2];
		if (string.IsNullOrWhiteSpace(initialState) || !states.Contains(initialState))
		{
			Console.WriteLine("Error: Initial state must be a non-empty string and part of the set of states.");
			return 1;
		}

		// Read final states
		finalStates = new HashSet<string>(lines[3].Split(','));
		if (finalStates.Any(string.IsNullOrWhiteSpace) || !finalStates.All(states.Contains))
		{
			Console.WriteLine("Error: Final states must not be empty strings and must be part of the set of states.");
			return 1;
		}

		delta = new Dictionary<(string, string), HashSet<string>>();

		for (int i = 4; i < lines.Length; i++)
		{
			string[] transition = lines[i].Split(',');
			if (transition.Length == 3)
			{
				string currentState = transition[0];
				string symbol = transition[1];
				string nextState = transition[2];

				var transitionKey = (currentState, symbol);

				if (string.IsNullOrWhiteSpace(currentState) || string.IsNullOrWhiteSpace(nextState) || !alphabet.Contains(symbol) || !states.Contains(currentState) || !states.Contains(nextState))
				{
					Console.WriteLine("Error: Current state, next state, and symbol must be valid inputs.");
					return 1;
				}

				if (!delta.ContainsKey(transitionKey))
				{
					delta[transitionKey] = new HashSet<string>();
				}

				delta[transitionKey].Add(nextState);
			}
			else
			{
				Console.WriteLine("Error: Invalid format for transition.");
				return 1;
			}
		}

		return 0;
	}


	static void ReadFromConsole()
	{
		// Read states
		Console.WriteLine("Enter the states separated by comma:");
		states = new HashSet<string>(Console.ReadLine().Split(','));
		Console.WriteLine();

		// Read alphabet
		Console.WriteLine("Enter the alphabet separated by comma:");
		alphabet = new HashSet<string>(Console.ReadLine().Split(','));
		Console.WriteLine();

		// Read initial state
		do
		{
			Console.WriteLine("Enter the initial state:");
			initialState = Console.ReadLine();

			if (!states.Contains(initialState))
			{
				Console.WriteLine("Error: Initial state must be part of the set of states.");
			}
		} while (!states.Contains(initialState));

		Console.WriteLine();

		// Read final states
		do
		{
			Console.WriteLine("Enter the final states separated by comma:");
			finalStates = new HashSet<string>(Console.ReadLine().Split(','));

			if (!finalStates.All(state => states.Contains(state)))
			{
				Console.WriteLine("Error: Final states must be part of the set of states.");
			}
		} while (!finalStates.All(state => states.Contains(state)));

		Console.WriteLine();

		delta = new Dictionary<(string, string), HashSet<string>>();
		string input;
		do
		{
			Console.WriteLine("Enter the transitions in the format: currentState,symbol,nextState\n\t or Enter to stop");
			input = Console.ReadLine();

			if (!string.IsNullOrWhiteSpace(input))
			{
				string[] transition = input.Split(',');

				if (transition.Length == 3)
				{
					string currentState = transition[0];
					string symbol = transition[1];
					string nextState = transition[2];

					var transitionKey = (currentState, symbol);

					if (states.Contains(currentState) && states.Contains(nextState) && alphabet.Contains(symbol))
					{
						if (!delta.ContainsKey(transitionKey))
						{
							delta[transitionKey] = new HashSet<string>();
						}

						delta[transitionKey].Add(nextState);
					}
					else
					{
						Console.WriteLine("Error: Current state, next state, and symbol must be valid inputs.");
					}
				}
				else
				{
					Console.WriteLine("Error: Enter the transition in the correct format.");
				}
			}
		} while (!string.IsNullOrWhiteSpace(input));
	}

	static void Main()
	{
		Console.WriteLine("Choose if you want to read from file or from console:");
		Console.WriteLine("1. Read from file");
		Console.WriteLine("2. Read from console");

		if (Console.ReadLine() == "1")
		{
			if (ReadFromFile() != 0)
				return;
		}
		else
			ReadFromConsole();

		FiniteAutomaton automaton = new FiniteAutomaton(states, alphabet, initialState, finalStates, delta);

		Console.WriteLine("Enter the string to be checked:");
		string inputString = Console.ReadLine();

		bool accepted = automaton.Accepts(inputString);
		string longestAcceptedPrefix = automaton.LongestAcceptedPrefix(inputString);

		Console.WriteLine($"Is the string accepted? {accepted}");
		if (longestAcceptedPrefix != "")
			Console.WriteLine($"Longest accepted prefix: {longestAcceptedPrefix}\n");
		else
			Console.WriteLine("Longest accepted prefix: none\n");

		while (true)
		{
			Console.WriteLine("Choose an option:");
			Console.WriteLine("1. Print States");
			Console.WriteLine("2. Print Alphabet");
			Console.WriteLine("3. Print Transitions");
			Console.WriteLine("4. Print Final States");
			Console.WriteLine("5. Exit");

			switch (Console.ReadLine())
			{
				case "1":
					automaton.PrintStates();
					break;
				case "2":
					automaton.PrintAlphabet();
					break;
				case "3":
					automaton.PrintTransitions();
					break;
				case "4":
					automaton.PrintFinalStates();
					break;
				case "5":
					return;
				default:
					Console.WriteLine("Invalid choice. Please try again.");
					break;
			}
		}
	}
}
