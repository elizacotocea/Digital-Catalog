package myproject.validator;


public interface Validator<E> {
    void validate(E entity) throws ValidationException;

}

