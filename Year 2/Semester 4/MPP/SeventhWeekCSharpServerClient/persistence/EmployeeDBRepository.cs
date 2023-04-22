using log4net;
using SeventhWeekCSharpServerClient.domain;
using SeventhWeekCSharpServerClient.repository.interfaces;
using System.Collections.Generic;
using System.Data.SQLite;


namespace SeventhWeekCSharpServerClient.repository
{
    public class EmployeeDBRepository : IEmployeeRepository
    {
        private static readonly ILog Logger = LogManager.GetLogger("EmployeeDBRepository");

        private readonly IDictionary<string, string> props;

        public EmployeeDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
            Logger.Info("Creating EmployeeDBRepository");
        }

        private static Employee<int> GetEmployeeFromResultSet(SQLiteDataReader resultSet)
        {
            var id = resultSet.GetInt32(0);
            var firstName = resultSet.GetString(1);
            var lastName = resultSet.GetString(2);
            var username = resultSet.GetString(3);
            var password = resultSet.GetString(4);
            return new Employee<int>(id, firstName, lastName, username, password);
        }

        public List<Employee<int>> GetAll()
        {
            Logger.Info("finding all employees");
            var connection = DBUtils.GetConnection(props);
            var list = new List<Employee<int>>();
            using (var cmd = new SQLiteCommand("select * from employee", connection as SQLiteConnection))
            using (var reader = cmd.ExecuteReader())
                while (reader.Read())
                    list.Add(GetEmployeeFromResultSet(reader));
            Logger.Info(list);
            return list;
        }


        public Employee<int> FindById(int id)
        {
            Logger.Info("find an employee by id");
            var connection = DBUtils.GetConnection(props);
            var cmd = new SQLiteCommand("select * from employee where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (!reader.Read()) return null;
                var employee = GetEmployeeFromResultSet(reader);
                Logger.Info(employee);
                return employee;
            }
        }

        public bool Add(Employee<int> employee)
        {
            Logger.Info("adding employee");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand
                ("insert into employee values (@id, @first_name, @last_name, @username, @password)",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", null);
                command.Parameters.AddWithValue("@first_name", employee.FirstName);
                command.Parameters.AddWithValue("@last_name", employee.LastName);
                command.Parameters.AddWithValue("@username", employee.Username);
                command.Parameters.AddWithValue("@password", employee.Password);
                command.ExecuteNonQuery();
                Logger.InfoFormat("inserted successfully {0}", employee);
                return true;
            }
        }


        public bool Delete(int id)
        {
            Logger.InfoFormat("delete employee with id = {0}", id);
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("delete from employee where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                Logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Employee<int> employee)
        {
            Logger.Info("updating employee");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("update employee set first_name = @first_name, " +
                "last_name = @last_name, username = @username, password = @password where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@first_name", employee.FirstName);
                command.Parameters.AddWithValue("@last_name", employee.LastName);
                command.Parameters.AddWithValue("@username", employee.Username);
                command.Parameters.AddWithValue("@password", employee.Password);
                command.Parameters.AddWithValue("@id", employee.Id);
                command.ExecuteNonQuery();
                Logger.Info("updated successfully");
                return true;
            }
        }

        public Employee<int> AuthenticateEmployee(string username, string password)
        {
            Logger.Info("find a employee by username and password");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("select * from employee where username = @username and password = @password",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@username", username);
                command.Parameters.AddWithValue("@password", password);
                using (var reader = command.ExecuteReader())
                    if (reader.Read())
                    {
                        var employee = GetEmployeeFromResultSet(reader);
                        Logger.Info(employee);
                        return employee;
                    }
            }
            Logger.Info("authentication failed");
            return null;
        }

    }
            
}

