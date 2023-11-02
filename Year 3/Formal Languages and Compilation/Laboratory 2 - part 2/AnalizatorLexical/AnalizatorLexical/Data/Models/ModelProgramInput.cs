using AnalizatorLexical.Data.Models;
using System.ComponentModel.DataAnnotations;

namespace AnalizatorLexical.Data.Entities
{
	public class ModelProgramInput
	{
		[Required]
		public string? Title { get; set; }
		[Required]
		public string? Input { get; set; }
		
		public bool ShowTable = false;
		public TableData? TableData { get; set; }
	}
}