using AnalizatorLexical.Data.Entities;

namespace AnalizatorLexical.Data.Models
{
	public class TableData
	{
		public List<Atom>? Atoms { get; set; }
		public List<Symbol>? TS_ID { get; set; }
		public List<Symbol>? TS_CONST { get; set; }
		public List<string>? LexicalAtoms { get; set; }
		public List<string>? Errors { get; set; }
	}
}
