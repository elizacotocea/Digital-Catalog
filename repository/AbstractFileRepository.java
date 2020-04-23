package myproject.repository;

import myproject.validator.Validator;
import myproject.entities.Entity;
import myproject.validator.ValidationException;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository {

    String fileName;
    public AbstractFileRepository(Validator validator, String fileName) {
        super(validator);
        this.fileName=fileName;
    }

    protected abstract void writeData();
    protected abstract void loadData();

    @Override
    public E findOne(Object id) {
        return (E) super.findOne(id);
    }

    @Override
    public Iterable findAll() {
        return super.findAll();
    }

    @Override
    public E save(Entity entity) throws ValidationException {
        E e= (E) super.save(entity);
        if (e==null)
            writeData();
        return e;
    }

    @Override
    public E delete(Object o) {
        Entity e= super.delete(o);
        if (e!=null)
            writeData();
        return (E) e;
    }

    @Override
    public E update(Entity entity) {
        Entity e=super.update(entity);
        if (e==null)
            writeData();
        return (E) e;
    }
}
