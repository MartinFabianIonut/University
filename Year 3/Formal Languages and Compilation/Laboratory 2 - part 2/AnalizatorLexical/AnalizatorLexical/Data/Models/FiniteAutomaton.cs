namespace AnalizatorLexical.Data.Models
{
	public class FiniteAutomaton
	{
		private HashSet<string> alphabet;
		private string initialState;
		private HashSet<string> finalStates;
		private Dictionary<(string, string), HashSet<string>> delta;

		public FiniteAutomaton(HashSet<string> alphabet, string initialState, HashSet<string> finalStates, Dictionary<(string, string), HashSet<string>> delta)
		{
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

		public HashSet<string> GetAlphabet()
		{
			return alphabet;
		}
	}
}
