package myproject.tests;

import myproject.entities.Tema;
import myproject.service.TemaService;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

class TemaServiceTest {
    String urlT = "jdbc:postgresql://localhost:5432/temaTest";
    String user = "postgres";
    String password = "eliza";
    TemaService t=new TemaService(user,password,urlT);
    @Test
    void findOne() {
        //Assert.assertEquals(t.findOne(1).getDeadlineweek(),9);
        Assert.assertEquals(t.findOne(97),null);
    }

    @Test
    void findAll() {
        Assert.assertEquals(t.findAll().spliterator().getExactSizeIfKnown(),10);
    }

    @Test
    void save() {
        t.delete(6);
        Tema t1=new Tema(6,"Java project2",9);
        Assert.assertNull(t.save(t1));
        Tema t2=new Tema(6,"Java project2",9);
        Assert.assertNotNull(t.save(t2));
    }

    @Test
    void delete() {
       // Assert.assertEquals(t.delete(1).getDeadlineweek(),9);
        Assert.assertNull(t.delete(97));
    }

    @Test
    void update() {
        Tema t1=new Tema(222,"Java project2",8);
        Tema t2=new Tema(2,"Java project2",8);
        Assert.assertNull(t.update(t2));
        Assert.assertNotNull(t.update(t1));
    }
}