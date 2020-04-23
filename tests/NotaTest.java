package myproject.tests;

import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

class NotaTest {

    private Nota[] generate(){
        Student s=new Student(1,"Eliza","Ania","eli@gmail.com","Ana","222");
        Tema t=new Tema(2,"Super",3);
        Nota n1=new Nota(s,t,"31/10/2019","Anca");
        Nota[] note={n1};
        return note;
    }
    @Test
    void getId() {
        Nota[] note=generate();
        Assert.assertEquals(note[0].getId().compareTo("12"),0);
    }

    @Test
    void setId() {
        Nota[] note=generate();
        note[0].setId("22");
        Assert.assertEquals(note[0].getId().compareTo("22"),0);
    }


    @Test
    void getData() {
        Nota[] note=generate();
      //  assert(note[0].getData().getMonthValue()==12);
       Assert.assertEquals (note[0].getData().compareTo("31/10/2019"),0);
    }

    @Test
    void setData() {
        Nota[] note=generate();
        note[0].setData("22/11/2019");
        Assert.assertEquals(note[0].getData().compareTo("22/11/2019"),0);
    }

    @Test
    void getProfesor() {
        Nota[] note=generate();
        Assert.assertEquals(note[0].getProfesor(),"Anca");
    }

    @Test
    void setProfesor() {
        Nota[] note=generate();
        note[0].setProfesor("Amalia");
        Assert.assertEquals(note[0].getProfesor(),"Amalia");
    }


    @Test
    void setNota() {
        Nota[] note=generate();
        note[0].setNota(10,0,9);
        Assert.assertEquals((int)note[0].getNota(),10);
    }

    @Test
    void getIDstudent() {
        Nota[] note=generate();
        Assert.assertEquals((int)note[0].getStudent().getId(),1);
    }

    @Test
    void getIDtema() {
        Nota[] note=generate();
        Assert.assertEquals((int)note[0].getTema().getId(),2);
    }
}