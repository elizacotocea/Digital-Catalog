package myproject.tests;

import org.junit.jupiter.api.Test;

import myproject.entities.Student;
import myproject.validator.StudentValidator;
import myproject.validator.ValidationException;

class StudentValidatorTest {

    @Test
    void validate() {
        StudentValidator stval=new StudentValidator();
        Student s= new Student(-3,"","","","","3232");
        try{
            stval.validate(s);
            assert false;
        }
        catch (ValidationException v){
            assert true;
        }
        Student s1= new Student(0,null,null,null,null,"e2w");
        try{
            stval.validate(s1);
            assert false;
        }
        catch (ValidationException v){
            assert true;
        }
    }
}