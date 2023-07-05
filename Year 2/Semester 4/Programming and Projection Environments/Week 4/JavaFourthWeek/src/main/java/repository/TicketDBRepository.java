package repository;

import domain.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketDBRepository extends AbstractRepository<Integer, Ticket> {

    public TicketDBRepository(Properties props) {
        super(props);
    }

    private Ticket getTicketFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int idShow = resultSet.getInt("id_show");
        String nameOfCostumer = resultSet.getString("name_of_costumer");
        return new Ticket(id, idShow, nameOfCostumer);
    }

    @Override
    public Ticket findById(Integer id) {
        logger.traceEntry("find a ticket by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from ticket where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Ticket ticket = getTicketFromResultSet(resultSet);
                    logger.traceExit(ticket);
                    return ticket;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in TicketDBRepository: " + exception);
        }
        return null;
    }

    @Override
    public Iterable<Ticket> getAll() {
        logger.traceEntry("finding all tickets");
        Connection connection = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from ticket")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    tickets.add(getTicketFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for getAll in TicketDBRepository: " + exception);
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public boolean add(Ticket ticket) {
        logger.traceEntry("Buying ticket {}", ticket);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "insert into ticket (id_show, name_of_costumer) values (?, ?)")) {
            ps.setInt(1, ticket.getIdShow());
            ps.setString(2, ticket.getNameOfCostumer());
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in TicketDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("delete from ticket where id=?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("deleted {} instance", result);

        } catch (SQLException exception) {
            logger.error("ERROR for delete in TicketDBRepository: " + exception);
            return false;
        }
        logger.traceExit("deleted successfully");
        return true;
    }

    @Override
    public boolean update(Ticket ticket) {
        logger.traceEntry("updating ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "update ticket set id_show = ?, name_of_costumer = ?" +
                " where id = ?")) {
            ps.setInt(1, ticket.getIdShow());
            ps.setString(2, ticket.getNameOfCostumer());
            ps.setInt(3, ticket.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for update in TicketDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }
}