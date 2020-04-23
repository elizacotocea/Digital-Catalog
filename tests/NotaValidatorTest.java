package myproject.tests;

import org.junit.jupiter.api.Test;

import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.validator.NotaValidator;
import myproject.validator.ValidationException;

class NotaValidatorTest {

    @Test
    void validate() {
        NotaValidator nval=new NotaValidator();
        Tema t=new Tema(1,"sw",13);
        Student s=new Student(1,"A","A","A","A","2232");
        Nota n=new Nota(s,t,"20/12/2019","A");
        try{
            nval.validate(n);
            assert true;
        }
        catch (ValidationException v){
            assert false;
        }


    }
}