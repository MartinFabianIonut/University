using AnalizatorLexical.Data.Entities;
using AnalizatorLexical.Data.Interfaces;
using AnalizatorLexical.Data.Models;
using System.Text.RegularExpressions;

namespace AnalizatorLexical.Data.Services
{
	public class ProgramInputService : IProgramInputService
	{
		private List<ModelProgramInput> programInputs = new List<ModelProgramInput>();
		private List<Atom> atoms = new List<Atom>();
		private Regex regexIdentificator = new Regex(@"^[a-zA-Z_][a-zA-Z0-9_]*$");

		public ProgramInputService(IConfiguration configuration)
		{
			configuration.GetSection("Atoms").Bind(atoms);
			string caleDirector = "C:\\Users\\marti\\source\\repos\\AnalizatorLexical\\AnalizatorLexical\\Data\\Inputs";

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
		}

		public void AddProgramInput(ModelProgramInput programInput)
		{
			programInputs.Add(programInput);
		}

		public Task<List<ModelProgramInput>> GetProgramInputsAsync()
		{
			return Task.FromResult(programInputs);
		}

		public TableData AnalizorLexical(string program)
		{
			List<Atom> Atoms = new List<Atom>();
			List<string> ID = new List<string>();
			List<string> CONST = new List<string>();
			bool error = false;

			var lines = program.Split('\n');
			List<string>? atomiLexicali = null;

			for (int lineNumber = 0; lineNumber < lines.Length && error == false; lineNumber++)
			{
				var line = lines[lineNumber];
				var atomiLexicaliPerLinie = line.Split(' ', '\t', '\r')
									   .Where(a => !string.IsNullOrEmpty(a))
									   .ToList();

				if (line.Contains("\" \""))
					atomiLexicaliPerLinie.Add("\" \"");
				if (line.Contains("' '"))
					atomiLexicaliPerLinie.Add("' '");
				if (atomiLexicaliPerLinie.Contains("\""))
					atomiLexicaliPerLinie.RemoveAll(atomiLexicali => atomiLexicali == "\"");
				if (atomiLexicaliPerLinie.Contains("'"))
					atomiLexicaliPerLinie.RemoveAll(atomiLexicali => atomiLexicali == "'");

				foreach (var atom in atomiLexicaliPerLinie)
				{
					var result = AnalizeazaCuvant(atom, Atoms, ID, CONST, lineNumber + 1);

					if (atomiLexicali == null)
						atomiLexicali = new List<string>();
					if (result != "")
					{
						error = true;
						atomiLexicali.Add(result);
						break;
					}
					else
						atomiLexicali.Add(atom);
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

			if (atomiLexicali != null)
			{
				TableData tableData = new TableData
				{
					Atoms = Atoms,
					TS_ID = TS_IDListSymbol,
					TS_CONST = TS_CONSTListSymbol,
					LexicalAtoms = atomiLexicali.ToList(),
				};

				return tableData;
			}
			return null;
		}

		private string AnalizeazaCuvant(string atom, List<Atom> Atoms, List<string> ID, List<string> CONST, int lineNumber)
		{
			Atom? foundAtom = atoms.FirstOrDefault(a => a.Name == atom);
			if (foundAtom != null)
			{
				if (!Atoms.Any(a => a.Name == atom))
					Atoms.Add(foundAtom);
			}
			else if (regexIdentificator.IsMatch(atom) && atom.Length <= 250)
			{
				if (ID.Contains(atom) == false)
					ID.Add(atom);
				if (!Atoms.Any(a => a.Name == "ID"))
					Atoms.Add(atoms[0]);
			}
			else if (double.TryParse(atom, out _) ||
					atom.StartsWith("\"") && atom.EndsWith("\"") ||
					atom.StartsWith("'") && atom.EndsWith("'"))
			{
				if (CONST.Contains(atom) == false)
					CONST.Add(atom);
				if (!Atoms.Any(a => a.Name == "CONST"))
					Atoms.Add(atoms[1]);
			}
			else
			{
				if (atom.Length > 250)
					return $"Linia {lineNumber}: Lungimea atomului lexical este mai mare decat 250 de caractere";
				else
					return $"Linia {lineNumber}: {atom}";
			}
			return "";
		}
	}
}
