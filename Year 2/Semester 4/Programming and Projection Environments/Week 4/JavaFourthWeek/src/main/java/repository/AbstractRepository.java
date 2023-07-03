package repository;

import domain.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.util.Properties;

public abstract class AbstractRepository <ID, E extends Entity<ID>> implements Repository<ID, E>{
    protected JdbcUtils dbUtils;
    protected static final Logger logger= LogManager.getLogger();

    public AbstractRepository(Properties props) {
        logger.info("Initializing AbstractRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
}
