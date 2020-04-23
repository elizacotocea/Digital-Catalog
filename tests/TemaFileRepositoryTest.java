package myproject.tests;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import myproject.databases.TemaDatabase;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.validator.TemaValidator;
import myproject.validator.ValidationException;

class TemaFileRepositoryTest {
    String urlT = "jdbc:postgresql://localhost:5432/temaTest";
    String user = "postgres";
    String password = "eliza";
    TemaDatabase trepo;

    private TemaDatabase generate(){
        trepo=new TemaDatabase(new TemaValidator(),user,password,urlT);
        return trepo;
    }
    @Test
    void findOne() {
        TemaDatabase trepo=generate();
        Tema t=(Tema)trepo.findOne(10);
        Assert.assertEquals(t.getDescriere(),"t");
        Assert.assertNull (trepo.findOne(35));
        try {
            Tema t1=(Tema)trepo.findOne(null);
        }
        catch (IllegalArgumentException i){

        }
    }

    @Test
    void findAll() {
        TemaDatabase trepo=generate();
        Iterable<Tema> tema=trepo.findAll();
        Assert.assertEquals(tema.spliterator().getExactSizeIfKnown(),10);
    }

    @Test
    void save() {
        TemaDatabase trepo=generate();
       Tema t2=new Tema(9,"greu",8);
       Tema tttt=trepo.save(t2);
        try {
            Assert.assertNotNull(tttt);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
            try {
            Assert.assertNotNull(trepo.save(t2),null);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        try {
            trepo.save(null);
        }
        catch (IllegalArgumentException i){
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void delete() {
        TemaDatabase trepo=generate();
        Tema te=new Tema(2,"javaa",13);
        Tema t=(Tema)trepo.delete(2);
        Assert.assertEquals ((int)t.getId(),2);
        trepo.save(te);
        Assert.assertNull(trepo.delete(44));
    }

    @Test
    void update() {
        TemaDatabase trepo=generate();
        Student s=new Student(11,"Cotocea","Eliza","elizacotocea@gmail.com","Teofana","222");
        Tema t2=new Tema(11,"usor",10);
        Tema t22=new Tema(12,"usor",10);
        Tema t= null;
        try {
            t = (Tema)trepo.update(t2);
            Assert.assertNotNull(t);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertNotNull(trepo.update(t22));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        try {
            try {
                trepo.update(null);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        catch (IllegalArgumentException i){
        }
    }
}