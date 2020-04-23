package myproject.service;

import myproject.validator.ValidationException;
import myproject.entities.Entity;

public interface Service <ID, E extends Entity<ID>> {

    E findOne(ID id);

    Iterable<E> findAll();

    E save(E entity) throws ValidationException;

    E delete(ID id);

    E update(E entity) throws ValidationException;

}
