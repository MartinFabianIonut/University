using AnalizatorLexical.Data.Entities;
using AnalizatorLexical.Data.Models;

namespace AnalizatorLexical.Data.Interfaces
{
	public interface IProgramInputService
	{
		public void AddProgramInput(ModelProgramInput programInput);
		public Task<List<ModelProgramInput>> GetProgramInputsAsync();
		public TableData AnalizorLexical(string program);
	}
}
