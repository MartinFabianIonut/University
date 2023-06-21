package app.rest.persistance.repository.jdbc;

import app.rest.persistance.repository.IPositionRepository;
import domain.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component()
public class PositionORMDBIRepository implements IPositionRepository<Integer, Position> {

    private  static SessionFactory sessionFactory;
    public PositionORMDBIRepository(){
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
    public Position findById(Integer id) {
        logger.traceEntry("find an position by id");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try {
                transaction=session.beginTransaction();
                Position game =session.createQuery("from Position where id=:id", Position.class)
                        .setParameter("id",id)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                logger.traceExit(game);
                return game;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for findById, with id = " + id+", in PositionORMDBIRepository: " + ex);
            }

        }
        return null;
    }

    @Override
    public Position findByRowCol(int row, int col){
        logger.traceEntry("find an position by row and col");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try {
                transaction=session.beginTransaction();
                Position game =session.createQuery("from Position where row=:row and col=:col", Position.class)
                        .setParameter("row",row)
                        .setParameter("col",col)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                logger.traceExit(game);
                return game;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for findByRowCol, with row = " + row + " and col = " + col + ", in PositionORMDBIRepository: " + ex);
            }

        }
        return null;
    }

    @Override
    public Iterable<Position> getAll() {
        logger.traceEntry("find all employees");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                List<Position> games =session.createQuery("from Position", Position.class)
                        .list();
                transaction.commit();
                logger.traceExit(games);
                return games;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for getAll in GameORMDBIRepository: " + ex);
            }
        }
        return null;
    }

    @Override
    public boolean add(Position position) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                session.save(position);
                transaction.commit();
                logger.trace("saved {} instance", position);
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for insert in PositionORMDBIRepository: " + ex);
            }
        }
        logger.traceExit("inserted successfully");
        return true;
    }


    @Override
    public boolean update(Position position) {
        logger.traceEntry("updating position");
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.update(position);
                tx.commit();
                logger.traceExit("updated successfully");
                return true;
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
                logger.error("ERROR for update in PositionORMDBIRepository: " + ex);
            }
        }
        return true;
    }

    @Override
    public Integer getMaxId() {
        logger.traceEntry("get max id for position");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                Integer maxId =session.createQuery("select max(id) from Position", Integer.class)
                        .uniqueResult();
                transaction.commit();
                logger.traceExit(maxId);
                return maxId;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for getMaxId in PositionORMDBIRepository: " + ex);
            }
        }
        return null;
    }
}
