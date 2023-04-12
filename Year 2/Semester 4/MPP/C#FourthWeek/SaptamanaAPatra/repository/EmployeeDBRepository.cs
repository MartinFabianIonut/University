using log4net;
using SaptamanaAPatra.domain;
using SaptamanaAPatra.repository.interfaces;
using SaptamanaAPatra.utils;
using System.Collections.Generic;
using System.Data.SQLite;


namespace SaptamanaAPatra.repository
{
    internal class EmployeeDBRepository: IEmployeeRepository
    {
        private static readonly ILog logger = LogManager.GetLogger("EmployeeDBRepository");

        private IDictionary<string, string> _props;

        public EmployeeDBRepository(IDictionary<string, string> props)
        {
            this._props = props;
            logger.Info("Creating EmployeeDBRepository");
        }

        private static Employee<int> GetEmployeeFromResultSet(SQLiteDataReader resultSet)
        {
            int id = resultSet.GetInt32(0);
            string firstName = resultSet.GetString(1);
            string lastName = resultSet.GetString(2);
            string userame = resultSet.GetString(3);
            string password = resultSet.GetString(4);
            return new Employee<int>(id, firstName, lastName, userame, password);
        }

        public List<Employee<int>> GetAll()
        {
            logger.Info("finding all employees");
            var connection = DBUtils.GetConnection(_props);
            var list = new List<Employee<int>>();
            using (var cmd = new SQLiteCommand("select * from employee", connection as SQLiteConnection))
            using (var reader = cmd.ExecuteReader())
                while (reader.Read())
                    list.Add(GetEmployeeFromResultSet(reader));
            logger.Info(list);
            return list;
        }


        public Employee<int> FindById(int id)
        {
            logger.Info("find an employee by id");
            var connection = DBUtils.GetConnection(_props);
            var cmd = new SQLiteCommand("select * from employee where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (reader.Read())
                {
                    Employee<int> employee = GetEmployeeFromResultSet(reader);
                    logger.Info(employee);
                    return employee;
                }
            }
            return null;
        }

        public bool Add(Employee<int> employee)
        {
            logger.Info("adding employee");
            var connection = DBUtils.GetConnection(_props);
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
                logger.InfoFormat("inserted successfully {0}", employee);
                return true;
            }
        }


        public bool Delete(int id)
        {
            logger.InfoFormat("delete employee with id = {0}", id);
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("delete from employee where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Employee<int> employee)
        {
            logger.Info("updating employee");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("update employee set first_name = @first_name, " +
                "last_name = @last_name, username = @username, passord = @password where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@first_name", employee.FirstName);
                command.Parameters.AddWithValue("@last_name", employee.LastName);
                command.Parameters.AddWithValue("@username", employee.Username);
                command.Parameters.AddWithValue("@password", employee.Password);
                command.Parameters.AddWithValue("@id", employee.Id);
                command.ExecuteNonQuery();
                logger.Info("updated successfully");
                return true;
            }
        }
    }
}

