package myproject.repository;

import myproject.validator.Validator;
import myproject.entities.Entity;
import myproject.validator.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID,E extends Entity<ID>> implements CrudRepository<ID,E> {
    protected Map<ID,E> entities=new HashMap<>();
    protected Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
    }

    public E findOne(ID id)
    {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
       return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity null");
        validator.validate(entity);
        return entities.put(entity.getId(),entity);

    }

    @Override
    public E delete(ID id) {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
        E oldValue=entities.get(id);
            if (oldValue!=null){
                oldValue=entities.get(id);
                entities.remove(id);
            }
        return oldValue;
    }

    @Override
    public E update(E entity) {
        if(entity == null) throw new IllegalArgumentException("entity cannot be null");
        validator.validate(entity);
        E e = findOne(entity.getId());
        if (e!=null){
            e.update(entity);
            entities.replace(entity.getId(), e);
            return null;
        }
        return entity;
    }
}