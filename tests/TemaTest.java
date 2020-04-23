package myproject.tests;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import myproject.entities.Tema;

class TemaTest {

    private Tema[] generateTema(){
      Tema t1=new Tema(1,"Superba",13);
        Tema t2=new Tema(2,"Grea",10);
        Tema[] teme={t1,t2};
        return teme;
    }

    @Test
    void getDescriere() {
        Tema[] teme=generateTema();
        Assert.assertEquals(teme[0].getDescriere(),"Superba");
    }

    @Test
    void setDescriere() {
        Tema[] teme=generateTema();
        teme[0].setDescriere("Suss");
        Assert.assertEquals(teme[0].getDescriere(),"Suss");
    }

    @Test
    void getStartWeek() {
        Tema[] teme=generateTema();
        Assert.assertEquals(teme[0].getStartWeek(),8);
    }


    @Test
    void getDeadlineweek() {
        Tema[] teme=generateTema();
        Assert.assertEquals(teme[0].getDeadlineweek(),13);

    }

    @Test
    void setDeadlineweek() {
        Tema[] teme=generateTema();
        teme[0].setDeadlineweek(11);
        Assert.assertEquals(teme[0].getDeadlineweek(),11);
    }
}