package myproject.tests;

import org.junit.jupiter.api.Test;

import myproject.entities.Tema;
import myproject.validator.TemaValidator;
import myproject.validator.ValidationException;

class TemaValidatorTest {


    @Test
    void validate() {
        TemaValidator tval=new TemaValidator();
        Tema t=new Tema(-2,"",19);
        try{
            tval.validate(t);
            assert false;
        }
        catch (ValidationException v){
            assert true;
        }
        Tema t1=new Tema(1,"sws",1);
        try{
            tval.validate(t1);
            assert false;
        }
        catch (ValidationException v){
            assert true;
        }
        Tema t2=new Tema(1,"sws",13);
        try{
            tval.validate(t2);
            assert true;
        }
        catch (ValidationException v){
            assert true;
        }
    }
}