package myproject.tests;

import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.service.NotaService;
import myproject.validator.ValidationException;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.List;

class NotaServiceTest {
    String urlS = "jdbc:postgresql://localhost:5432/studentTest";
    String urlT = "jdbc:postgresql://localhost:5432/temaTest";
    String urlN = "jdbc:postgresql://localhost:5432/notaTest";
    String user = "postgres";
    String password = "eliza";
    NotaService nserv=new NotaService(user,password,urlN,urlS,urlT);
    private NotaService generate(){
        return nserv;
    }
    @Test
    void findOne() {
        NotaService nserv=generate();
        Assert.assertEquals(nserv.findOne("94").getProfesor(),"Teofana");
        Assert.assertNull(nserv.findOne("33"));
    }

    @Test
    void findAll() {
        NotaService nserv=generate();
        Assert.assertEquals(nserv.findAll().spliterator().getExactSizeIfKnown(),6);
    }

    @Test
    void save() {
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Nota n1=new Nota(s,t,"30/10/2019","Paul");
        NotaService nserv=generate();
        nserv.delete("55");
        try {
            Assert.assertNull(nserv.save(n1));
        } catch (ValidationException e) {
            assert false;
        }
        try {
            Assert.assertEquals(nserv.save(n1),n1);
        } catch (ValidationException e) {
            assert false;
        }
        try {
           nserv.save(null);
        } catch (IllegalArgumentException | ValidationException e) {
            Assert.assertFalse(false);
        }
    }

    @Test
    void delete() {
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Nota n1=new Nota(s,t,"30/10/2019","Paul");
        NotaService nserv=generate();
        Assert.assertEquals(nserv.delete("55").getProfesor(),"Andreea");
        try{
            nserv.delete(null);
            Assert.assertFalse(false);
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        Assert.assertNull(nserv.delete("2"));
        nserv.save(n1);
    }

    @Test
    void update() {
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Tema t3=new Tema(7,"tema test",14);
        Nota n2=new Nota(s,t,"30/10/2019","Paul");
        NotaService nserv=generate();
        try {
            Assert.assertNull(nserv.update(n2));
        } catch (ValidationException e) {
            Assert.assertFalse(false);
        }
        Nota n3=new Nota(s,t3,"30/11/2019","Teofana");
        Nota nupd=nserv.update(n3);
        try {
            Assert.assertNotNull(nupd);
        } catch (ValidationException e) {
            Assert.assertFalse(false);
        }
        try {
            Assert.assertNull(nserv.update(null).getData());
        } catch (IllegalArgumentException | ValidationException e) {
            Assert.assertFalse(false);
        }
    }


    @Test
    void addGrade() {
        NotaService nserv=generate();
        Student s=new Student(9,"test","test","test@gmail.com","Teofana","222");
        Tema t=new Tema(5,"super",13);
        Tema t1=new Tema(4,"tema test",6);
        Nota n2=new Nota(s,t,"07/10/2019","Teofana");
        nserv.save(n2);
      ///  nserv.addGrade(s,t,10,"super",0);
        Assert.assertEquals(nserv.findOne("95").getNota(),(float) 10);

        Nota n3=new Nota(s,t1,"07/11/2019","Teofana");
        nserv.save(n3);
      //  nserv.addGrade(s,t1,10,"super",0);
        Assert.assertEquals(nserv.findOne("94").getNota(),(float) 10);

        Tema t2=new Tema(6,"super",5);
        Nota n4=new Nota(s,t2,"07/11/2019","Teofana");
        nserv.save(n4);
       // nserv.addGrade(s,t2,10,"super",1);
        Assert.assertEquals(nserv.findOne("96").getNota(),(float) 10);

        Tema t22=new Tema(8,"super",4);
        Nota n42=new Nota(s,t22,"07/11/2019","Teofana");
        nserv.save(n42);
     //   nserv.addGrade(s,t22,10,"super",2);
        Assert.assertEquals(nserv.findOne("98").getNota(),(float) 10);
        //nserv.delete("14");

        Tema t222=new Tema(10,"super",4);
        Nota n422=new Nota(s,t222,"07/11/2019","Teofana");
        nserv.save(n422);
      //  nserv.addGrade(s,t222,10,"super",2);
        Assert.assertEquals(nserv.findOne("910").getNota(),(float) 10);

    }

    @Test
    void filter_students_with_hw() {
        List<Student> l= nserv.filter_students_with_hw(5);
        Assert.assertTrue(!l.isEmpty());
        List<Student> l1= nserv.filter_students_with_hw(5);
        Assert.assertFalse(l1.isEmpty());
        Assert.assertEquals(l1.get(0).getNume(),"Caiac");
    }

    @Test
    void filter_students_with_hw_teacher() {
        List<Student> l= nserv.filter_students_with_hw_teacher(13,"Teofana");
        Assert.assertTrue(l.isEmpty());
        List<Student> l2= nserv.filter_students_with_hw_teacher(19,"Re2ana");
        Assert.assertTrue(l2.isEmpty());
        List<Student> l1= nserv.filter_students_with_hw_teacher(5,"Teofana");
        Assert.assertFalse(l1.isEmpty());
        Assert.assertEquals(l1.get(0).getNume(),"Caiac");
    }

    @Test
    void filter_grades_in_week_of_hw() {
        List<Float> l= nserv.filter_grades_in_week_of_hw(4,6);
        Assert.assertEquals(l.spliterator().getExactSizeIfKnown(),1);
        Assert.assertEquals(l.get(0),(float)10);
        List<Float> l1= nserv.filter_grades_in_week_of_hw(12,5);
        Assert.assertTrue(l1.isEmpty());
        List<Float> l2= nserv.filter_grades_in_week_of_hw(1,2);
        Assert.assertTrue(l2.isEmpty());
    }
}