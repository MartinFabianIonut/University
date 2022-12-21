package socialnetwork.domain.validators;

import socialnetwork.domain.exceptions.ValidationException;

/**
 * Generic validator interface to be used...
 * @param <T>
 */
public interface Validator<T> {
    /**
     * Method validating the entity of type T
     * @param entity - entity to validate
     * @throws ValidationException
     */
    void validate(T entity) throws ValidationException;
}