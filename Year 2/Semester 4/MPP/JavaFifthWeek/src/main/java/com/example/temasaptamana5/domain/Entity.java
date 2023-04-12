package com.example.temasaptamana5.domain;

import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;
    private final ID id;

    public Entity(ID id) {
        this.id = id;
    }

    /**
     * Method that returns the id of the entity
     * @return ID
     */
    public ID getId() {
        return id;
    }

    /**
     * Determines the hash code of the user
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**
     * Method that verifies if the entity is equal to another object
     * @param obj Object
     * @return true if obj is equal with the instance and false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Entity<?>)) {
            return false;
        }

        Entity entity = (Entity) obj;
        return Objects.equals(this.id, entity.id);
    }
}
