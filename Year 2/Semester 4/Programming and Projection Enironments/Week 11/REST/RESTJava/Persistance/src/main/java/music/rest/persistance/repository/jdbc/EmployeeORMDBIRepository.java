package music.rest.persistance.repository.jdbc;

import music.rest.persistance.repository.IEmployeeRepository;
import rest.domain.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class EmployeeORMDBIRepository implements IEmployeeRepository<Integer,Employee> {

    private  static SessionFactory sessionFactory;
    public EmployeeORMDBIRepository(){
        initialize();
    }
    public void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Exceptie " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    private static final Logger logger= LogManager.getLogger();

    @Override
    public Employee findById(Integer id) {
        logger.traceEntry("find an employee by id");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try {
                transaction=session.beginTransaction();
                Employee employee=session.createQuery("from Employee where id=:id",Employee.class)
                        .setParameter("id",id)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                logger.traceExit(employee);
                return employee;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for insert in EmployeeORMDBIRepository: " + ex);
            }

        }
        return null;
    }

    @Override
    public Iterable<Employee> getAll() {
        logger.traceEntry("find all employees");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                List<Employee> employees=session.createQuery("from Employee",Employee.class)
                        .list();
                transaction.commit();
                logger.traceExit(employees);
                return employees;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for insert in EmployeeORMDBIRepository: " + ex);
            }
        }
        return null;
    }

    @Override
    public boolean add(Employee employee) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                session.save(employee);
                transaction.commit();
                logger.trace("saved {} instance", employee);
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for insert in EmployeeORMDBIRepository: " + ex);
            }
        }
        logger.traceExit("inserted successfully");
        return true;
    }



    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete employee");
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String hql = "from Employee where id=:id";
                Employee employee = session.createQuery(hql, Employee.class)
                        .setParameter("id", id)
                        .setMaxResults(1)
                        .uniqueResult();
                System.out.println("Stergem mesajul "+employee.getId());
                session.delete(employee);
                tx.commit();
                logger.traceExit("deleted successfully");
                return true;
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
                logger.error("ERROR for delete in EmployeeORMDBIRepository: " + ex);
            }
        }
        return false;
    }

    @Override
    public boolean update(Employee show) {
        logger.traceEntry("updating employee");
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.update(show);
                tx.commit();
                logger.traceExit("updated successfully");
                return true;
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
                logger.error("ERROR for update in EmployeeORMDBIRepository: " + ex);
            }
        }
        return true;
    }

    @Override
    public Employee authenticateEmployee(String username, String password) {
        logger.traceEntry("find an employee by username and password");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try {
                transaction=session.beginTransaction();
                Employee employee=session.createQuery("from Employee where " +
                                "username=:username and password=:password",Employee.class)
                        .setParameter("username",username)
                        .setParameter("password",password)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                logger.traceExit(employee);
                return employee;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for insert in EmployeeORMDBIRepository: " + ex);
            }

        }
        logger.traceExit("authentication failed");
        return null;
    }
}
