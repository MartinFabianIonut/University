using SeventhWeekCSharpServerClient.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.repository.interfaces
{
    public interface IEmployeeRepository : IRepository<int, Employee<int>>
    {
        Employee<int> AuthenticateEmployee(string username, string password);
    }
}
