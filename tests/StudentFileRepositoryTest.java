package myproject.tests;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import myproject.databases.StudentDatabase;
import myproject.entities.Student;
import myproject.validator.StudentValidator;
import myproject.validator.ValidationException;

class StudentFileRepositoryTest {
    String urlS = "jdbc:postgresql://localhost:5432/studentTest";
    String user = "postgres";
    String password = "eliza";
   StudentDatabase srepo=new StudentDatabase(new StudentValidator(),user,password,urlS);

    StudentDatabase generate(){
        Student s=new Student(11,"Cotocea","Eliza","elizacotocea@gmail.com","Teofana","222");
        Student s1=new Student(22,"Circiu","Andreea","candreea@gmail.com","Teofana","222");
        Student s2=new Student(33,"Coman","Octavian","comanog@gmail.com","Carmen","223");
        try {
            srepo.save(s);
        } catch (ValidationException e) {
           assert false;
        }
        try {
            srepo.save(s1);
        } catch (ValidationException e) {
            assert false;
        }
        return srepo;
    }

    @Test
    void findOne() {
        StudentDatabase srepo=generate();
        Student s=(Student)srepo.findOne(11);
        Assert.assertEquals(s.getGrupa(),"222");
        Assert.assertEquals(s.getCadruDidacticIndrumatorLab(),"Teofana");
        Assert.assertNull (srepo.findOne(8));
        try {
            Student s1=(Student)srepo.findOne(null);
        }
        catch (IllegalArgumentException i){
        }

    }

    @Test
    void findAll() {
        StudentDatabase srepo=generate();
        Iterable<Student> st=srepo.findAll();
        Assert.assertEquals(st.spliterator().getExactSizeIfKnown(),12);
    }

    @Test
    void save() {
        StudentDatabase srepo=generate();
        srepo.delete(35);
        Student s2=new Student(35,"Coman","Octavian","comanog@gmail.com","Carmen","223");
        try {
            Assert.assertNull(srepo.save(s2));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Student s=new Student(91,"Cotocea","Eliza","elizacotocea@gmail.com","Teofana","222");
        try {
            Assert.assertNotNull(srepo.save(s));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        try {
            srepo.save(null);
        }
        catch (IllegalArgumentException i){
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void delete() {
        StudentDatabase srepo=generate();
        Student s=(Student)srepo.delete(11);
        Assert.assertEquals((int)s.getId(),11);
        Assert.assertNull(srepo.delete(99));
    }

    @Test
    void update() {
        StudentDatabase srepo=generate();
        Student s1=new Student(9,"Caiac","Anton","candreea@gmail.com","Teofana","222");
        Student s2=new Student(11,"Coman","Octavian","comanog@gmail.com","Carmen","223");
        Student s= null;
        try {
            s = (Student)srepo.update(s1);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assert.assertNull(s);
        try {
            Assert.assertNull((srepo.update(s2)));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        try {
            try {
                srepo.update(null);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        catch (IllegalArgumentException i){
        }
    }
}