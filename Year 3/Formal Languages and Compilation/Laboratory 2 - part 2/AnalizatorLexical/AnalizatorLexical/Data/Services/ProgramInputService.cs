using AnalizatorLexical.Data.Entities;
using AnalizatorLexical.Data.Interfaces;
using AnalizatorLexical.Data.Models;
using System.Reflection;

namespace AnalizatorLexical.Data.Services
{
	public class ProgramInputService : IProgramInputService
	{
		private List<ModelProgramInput> programInputs = new List<ModelProgramInput>();
		private List<Atom> atoms = new List<Atom>();
		private FiniteAutomaton identifierAutomaton, realNrAutomaton, integerNrAutomaton;

		public ProgramInputService(IConfiguration configuration)
		{
			configuration.GetSection("Atoms").Bind(atoms);
			var currentDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
			var parentDirectory = Directory.GetParent(currentDirectory).Parent.Parent.FullName;
			var caleDirector = Path.Combine(parentDirectory, "Data", "Inputs");

			if (Directory.Exists(caleDirector))
			{
				try
				{
					string[] fisiere = Directory.GetFiles(caleDirector);
					foreach (var fisier in fisiere)
					{
						string numeFisier = Path.GetFileNameWithoutExtension(fisier);
						string continutFisier = File.ReadAllText(fisier);
						programInputs.Add(new ModelProgramInput { Title = numeFisier, Input = continutFisier });
					}
				}
				catch (IOException e)
				{
					Console.WriteLine($"Eroare la citirea fisierelor: {e.Message}");
				}
			}
			else
			{
				Console.WriteLine("Directorul nu exista.");
			}

			var automatonPath = Path.Combine(parentDirectory, "Data", "Automatons");
			var identifierAutomatonPath = Path.Combine(automatonPath, "identifier.txt");
			var realNrAutomatonPath = Path.Combine(automatonPath, "realNr.txt");
			var integerNrAutomatonPath = Path.Combine(automatonPath, "integerNr.txt");
			ReadFromFile(identifierAutomatonPath);
			identifierAutomaton = new FiniteAutomaton(alphabet, initialState, finalStates, delta);
			ReadFromFile(realNrAutomatonPath);
			realNrAutomaton = new FiniteAutomaton(alphabet, initialState, finalStates, delta);
			ReadFromFile(integerNrAutomatonPath);
			integerNrAutomaton = new FiniteAutomaton(alphabet, initialState, finalStates, delta);
		}

		private static HashSet<string>? states;
		private static HashSet<string>? alphabet;
		private static string? initialState;
		private static HashSet<string>? finalStates;
		private static Dictionary<(string, string), HashSet<string>>? delta;
		private static int ReadFromFile(string filePath)
		{
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

		public void AddProgramInput(ModelProgramInput programInput)
		{
			programInputs.Add(programInput);
		}

		public Task<List<ModelProgramInput>> GetProgramInputsAsync()
		{
			return Task.FromResult(programInputs);
		}

		public TableData? LexicalAnalyzer(string program)
		{
			List<Atom> Atoms = new List<Atom>();
			List<string> ID = new List<string>();
			List<string> CONST = new List<string>();
			bool error = false;

			var lines = program.Split('\n');
			List<string>? lexicalAtoms = new List<string>();

			for (int lineNumber = 0; lineNumber < lines.Length && error == false; lineNumber++)
			{
				var result = AnalyzeTheLine(lines[lineNumber], Atoms, ID, CONST, lineNumber + 1, lexicalAtoms);
				if (result != "")
				{
					error = true;
					lexicalAtoms.Add(result);
					break;
				}
			}

			HashTable TS_ID = new HashTable(ID.Count);
			HashTable TS_CONST = new HashTable(CONST.Count);

			ID.ForEach(id => TS_ID.Add(id));
			CONST.ForEach(constanta => TS_CONST.Add(constanta));

			var TS_IDListSymbol = ID.Select(id => new Symbol { Name = id, Code = TS_ID.Get(id) }).ToList();
			var TS_CONSTListSymbol = CONST.Select(constanta => new Symbol { Name = constanta, Code = TS_CONST.Get(constanta) }).ToList();
			TS_IDListSymbol.Sort((x, y) => x.Code.CompareTo(y.Code));
			TS_CONSTListSymbol.Sort((x, y) => x.Code.CompareTo(y.Code));
			Atoms.Sort((x, y) => x.Code.CompareTo(y.Code));

			if (lexicalAtoms != null)
			{
				TableData tableData = new TableData
				{
					Atoms = Atoms,
					TS_ID = TS_IDListSymbol,
					TS_CONST = TS_CONSTListSymbol,
					LexicalAtoms = lexicalAtoms.ToList(),
				};
				return tableData;
			}
			return null;
		}

		private string AnalyzeTheLine(string line, List<Atom> Atoms, List<string> ID, List<string> CONST, int lineNumber, List<string> lexicalAtoms)
		{

			while (line.StartsWith(" ") || line.StartsWith("\t"))
				line = line.Substring(1);
			while (!string.IsNullOrWhiteSpace(line))
			{
				var longestAcceptedPrefix = (identifierAutomaton.LongestAcceptedPrefix(line) ?? "")
					.Length > (realNrAutomaton.LongestAcceptedPrefix(line) ?? "").Length ?
					(identifierAutomaton.LongestAcceptedPrefix(line) ?? "") :
					(realNrAutomaton.LongestAcceptedPrefix(line) ?? "");
				if (longestAcceptedPrefix.Length == 0)
				{
					longestAcceptedPrefix = line[0].ToString();
				}
				if (longestAcceptedPrefix.Length > 0 && longestAcceptedPrefix != " " && longestAcceptedPrefix != "\r")
				{
					Atom? foundAtom = atoms.FirstOrDefault(a => a.Name == longestAcceptedPrefix);
					if (foundAtom != null)
					{
						if (!Atoms.Any(a => a.Name == longestAcceptedPrefix))
							Atoms.Add(foundAtom);
					}
					else if (longestAcceptedPrefix.Length < 250)
					{
						var isIntegerNumber = integerNrAutomaton.Accepts(longestAcceptedPrefix);
						var isRealNumber = realNrAutomaton.Accepts(longestAcceptedPrefix);
						if (isIntegerNumber || isRealNumber)
						{
							if (CONST.Contains(longestAcceptedPrefix) == false)
								CONST.Add(longestAcceptedPrefix);
							if (!Atoms.Any(a => a.Name == "CONST"))
								Atoms.Add(atoms[1]);
						}
						else
						{
							if (!identifierAutomaton.GetAlphabet().Contains(longestAcceptedPrefix) && longestAcceptedPrefix.Length == 1)
							{
								return $"Linia {lineNumber}: Atom lexical necunoscut: {longestAcceptedPrefix}";
							}
							else
							{
								if (ID.Contains(longestAcceptedPrefix) == false)
									ID.Add(longestAcceptedPrefix);
								if (!Atoms.Any(a => a.Name == "ID"))
									Atoms.Add(atoms[0]);
							}

						}
					}
					else
					{
						return $"Linia {lineNumber}: Lungimea atomului lexical este mai mare decat 250 de caractere";
					}
				}

				if (longestAcceptedPrefix != " " && longestAcceptedPrefix != "\r")
					lexicalAtoms.Add(longestAcceptedPrefix);

				line = line.Substring(longestAcceptedPrefix.Length);
			}
			return "";
		}
	}
}
