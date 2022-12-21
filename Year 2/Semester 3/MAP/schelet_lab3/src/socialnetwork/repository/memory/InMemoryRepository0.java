package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;


import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository0<ID, E extends Entity<ID>> implements Repository0<ID, E> {

    private Validator<E> validator;
    private Map<ID, E> entities;
    //Map <E, ArrayList<E>> showfriendships = new HashMap<E,ArrayList<E>>();

    public InMemoryRepository0(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }


    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        if (entities.get(id) == null)
            throw new RepoException("non existing user for id = " + id);
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * @param entity I want to add
     *               entity must be not null
     * @return
     */
    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        } else entities.put(entity.getId(), entity);//, showfriendships.put(entity,u.getFriends());
        return null;
    }

    /**
     * @param id of user I want to delete
     *           id must be not null
     * @return user I deleted if existing, else
     * throw RepoException
     */
    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        if (entities.get(id) != null) {
            E entity = entities.get(id);
            entities.remove(id);
            return entity;
        }
        throw new RepoException("non existing user");
    }

    /**
     * @param entity that I want to update
     *               entity must not be null
     * @return entity changed
     */
    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        entities.put(entity.getId(), entity);
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

}
