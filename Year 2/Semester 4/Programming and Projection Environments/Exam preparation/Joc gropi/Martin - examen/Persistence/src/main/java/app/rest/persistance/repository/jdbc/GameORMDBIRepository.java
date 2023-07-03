package app.rest.persistance.repository.jdbc;

import app.rest.persistance.repository.IGameRepository;
import domain.Game;
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
public class GameORMDBIRepository implements IGameRepository<Integer, Game> {

    private  static SessionFactory sessionFactory;
    public GameORMDBIRepository(){
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
    public Game findById(Integer id) {
        logger.traceEntry("find an game by id");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try {
                transaction=session.beginTransaction();
                Game game =session.createQuery("from Game where id=:id", Game.class)
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
                logger.error("ERROR for findById, with id = " + id+", in GameORMDBIRepository: " + ex);
            }

        }
        return null;
    }

    @Override
    public Iterable<Game> getAll() {
        logger.traceEntry("find all games");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                List<Game> games =session.createQuery("from Game", Game.class)
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
    public boolean add(Game game) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                session.save(game);
                transaction.commit();
                logger.trace("saved {} instance", game);
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for insert in GameORMDBIRepository: " + ex);
            }
        }
        logger.traceExit("inserted successfully");
        return true;
    }


    @Override
    public boolean update(Game game) {
        logger.traceEntry("updating game");
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.update(game);
                tx.commit();
                logger.traceExit("updated successfully");
                return true;
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
                logger.error("ERROR for update in GameORMDBIRepository: " + ex);
            }
        }
        return true;
    }

    @Override
    public Integer getMaxId() {
        logger.traceEntry("get max id");
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                Integer maxId =session.createQuery("select max(id) from Game", Integer.class)
                        .uniqueResult();
                transaction.commit();
                logger.traceExit(maxId);
                return maxId;
            }
            catch (RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                logger.error("ERROR for getMaxId in GameORMDBIRepository: " + ex);
            }
        }
        return null;
    }
}
