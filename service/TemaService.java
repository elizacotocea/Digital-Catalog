package myproject.service;

import myproject.databases.TemaDatabase;
import myproject.entities.Tema;
import myproject.validator.TemaValidator;
import myproject.validator.ValidationException;

public class TemaService implements Service<Integer, Tema> {
    private TemaDatabase temaRepo;
    public TemaService(String user, String passwd, String url) {
        temaRepo=new TemaDatabase(new TemaValidator(),user,passwd,url);
    }

    @Override
    public Tema findOne(Integer id) {
        return (Tema) this.temaRepo.findOne(id);
    }

    @Override
    public Iterable<Tema> findAll() {
        return this.temaRepo.findAll();
    }

    @Override
    public Tema save(Tema entity) throws ValidationException {
        return (Tema) this.temaRepo.save(entity);
    }

    @Override
    public Tema delete(Integer id) {
        return (Tema) temaRepo.delete(id);
    }

    @Override
    public Tema update(Tema entity) throws ValidationException {
        return (Tema) temaRepo.update(entity);
    }

}
