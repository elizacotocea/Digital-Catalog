package myproject.tests;

import myproject.databases.NotaDatabase;
import myproject.databases.StudentDatabase;
import myproject.databases.TemaDatabase;
import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.validator.StudentValidator;
import myproject.validator.TemaValidator;
import myproject.validator.ValidationException;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import myproject.validator.NotaValidator;

class NotaFileRepositoryTest {
    String urlS = "jdbc:postgresql://localhost:5432/studentTest";
    String urlT = "jdbc:postgresql://localhost:5432/temaTest";
    String urlN = "jdbc:postgresql://localhost:5432/notaTest";
    String user = "postgres";
    String password = "eliza";
    StudentDatabase srep=new StudentDatabase(new StudentValidator(),user,password,urlS);
    TemaDatabase trep=new TemaDatabase(new TemaValidator(),user,password,urlT);
    NotaDatabase nrepo=new NotaDatabase(new NotaValidator(),user,password,urlN,urlS,urlT);

    private NotaDatabase generate(){
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Nota n=new Nota(s,t,"30/10/2019","Paul");
        try {
            nrepo.save(n);
        } catch (ValidationException e) {
            assert false;
        }
        return nrepo;
    }
    @Test
    void findOne() {
        NotaDatabase nrep=generate();
        Assert.assertEquals(((Nota)nrep.findOne("55")).getId().compareTo("11"),4);
        Assert.assertNull(nrep.findOne("35"));
    }

    @Test
    void findAll() {
        NotaDatabase nrep=generate();
        Assert.assertEquals(nrep.findAll().spliterator().getExactSizeIfKnown(),6);
    }

    @Test
    void save() {
        NotaDatabase nrepo=generate();
        nrepo.delete("55");
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Nota n2=new Nota(s,t,"30/10/2019","Andreea");
        try {
            Assert.assertNull(nrepo.save(n2));
            assert true;
        }
        catch (ValidationException e) {
            assert true;
        }
        try {
            Assert.assertNotNull(nrepo.save(n2));
            assert true;
        }
        catch (ValidationException e) {
            assert true;
        }
        try {
            nrepo.save(null);
            assert false;
        }
        catch (IllegalArgumentException i){
        } catch (ValidationException e) {
            assert true;
        }
    }

    @Test
    void delete() {
        NotaDatabase nrepo=generate();
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Nota n2=new Nota(s,t,"30/10/2019","Andreea");
        Assert.assertNotNull(nrepo.delete("55"));
        Assert.assertNull(nrepo.delete("32"));
        nrepo.save(n2);
    }

    @Test
    void update() {
        NotaDatabase nrepo=generate();
        Student s=new Student(5,"Pop","Ion","pion@gmail.com","Paul","273");
        Tema t=new Tema(5,"tema test",14);
        Nota n2=new Nota(s,t,"24/10/2019","Daniela");
        try {
            Assert.assertNull(nrepo.update(n2));
            Assert.assertEquals(((Nota)nrepo.findOne("55")).getProfesor().compareTo("Daniela"),0);
            assert true;
        }
        catch (ValidationException e) {
            assert true;
        }
        Tema t1=new Tema(7,"test tema 2",14);
        Nota n3=new Nota(s,t1,"24/10/2019","Daniela");
        try {
            Assert.assertNotNull(nrepo.update(n3));
            assert true;
        }
        catch (ValidationException e) {
            assert true;
        }
        try {
            nrepo.update(null);
            assert false;
        }
        catch (IllegalArgumentException i){
        } catch (ValidationException e) {
            assert true;
        }
    }
}