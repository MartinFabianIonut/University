namespace AnalizatorLexical.Data.Models
{
	public class HashTable
	{
		private readonly int _size;
		private readonly List<string> _table;

		private int Hash(string key)
		{
			var hash = 0;
			for (var i = 0; i < key.Length; i++)
			{
				hash += key[i];
			}
			return hash % _size;
		}

		public HashTable(int size)
		{
			_size = size;
			_table = new List<string>(size);
			for (var i = 0; i < size; i++)
			{
				_table.Add("");
			}
		}

		public void Add(string key)
		{
			var index = Hash(key);
			while (index < _size && _table[index] != "")
			{
				index++;
			}
			if (index < _size)
			{
				_table[index] = key;
			}
			else {
				index = 0;
				while (index < _size && _table[index] != "")
				{
					index++;
				}
				if (index < _size)
				{
					_table[index] = key;
				}
			}
		}

		public int Get(string key)
		{
			return _table.IndexOf(key);
		}
	}
}
