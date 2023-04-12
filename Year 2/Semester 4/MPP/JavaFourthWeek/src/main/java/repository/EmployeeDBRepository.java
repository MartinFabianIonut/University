package repository;

import domain.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeDBRepository extends AbstractRepository<Integer, Employee> {

    public EmployeeDBRepository(Properties props) {
        super(props);
    }

    private Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        return new Employee(id, firstName, lastName, username, password);
    }

    @Override
    public Employee findById(Integer id) {
        logger.traceEntry("find a employee by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from employee where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Employee employee = getEmployeeFromResultSet(resultSet);
                    logger.traceExit(employee);
                    return employee;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in EmployeeDBRepository: " + exception);
        }
        return null;
    }

    @Override
    public Iterable<Employee> getAll() {
        logger.traceEntry("finding all tickets");
        Connection connection = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from employee")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(getEmployeeFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for getAll in EmployeeDBRepository: " + exception);
        }
        logger.traceExit(employees);
        return employees;
    }

    @Override
    public boolean add(Employee employee) {
        logger.traceEntry("adding employee {}", employee);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "insert into employee (first_name, last_name, username, password) values (?, ?, ?, ?)")) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getUsername());
            ps.setString(4, employee.getPassword());
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in EmployeeDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("delete from employee where id=?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("deleted {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for delete in EmployeeDBRepository: " + exception);
            return false;
        }
        logger.traceExit("deleted successfully");
        return true;
    }

    @Override
    public boolean update(Employee employee) {
        logger.traceEntry("updating ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "update employee set first_name = ?, last_name = ?, username = ?, password = ?" +
                " where id = ?")) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getUsername());
            ps.setString(4, employee.getPassword());
            ps.setInt(5, employee.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for update in EmployeeDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }
}
