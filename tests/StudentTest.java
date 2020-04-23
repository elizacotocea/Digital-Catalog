package myproject.tests;

import myproject.entities.Student;
import org.testng.Assert;

class StudentTest {

    private Student[] generateStudents(){
        Student s=new Student(1,"Cotocea","Eliza","elizacotocea@gmail.com","Teofana","222");
        Student s1=new Student(2,"Circiu","Andreea","candreea@gmail.com","Teofana","222");
        Student s2=new Student(3,"Coman","Octavian","comanog@gmail.com","Carmen","223");
        Student[] students={s,s1,s2};
    return students;
    }

    @org.junit.jupiter.api.Test
    void getName() {
        Student[] s=generateStudents();
        Assert.assertEquals(s[0].getNume().compareTo("Cotocea"), 0);
        Assert.assertEquals(s[1].getNume().compareTo("Circiu"),0);
        Assert.assertNotEquals(s[2].getNume().compareTo("Circiu"),0);
    }

    @org.junit.jupiter.api.Test
    void setName() {
        Student[] s=generateStudents();
        s[0].setNume("Balint");
        Assert.assertEquals (s[0].getNume().compareTo("Balint"),0);
    }

    @org.junit.jupiter.api.Test
    void getPrenume() {
        Student[] s=generateStudents();
        Assert.assertEquals (s[0].getPrenume().compareTo("Eliza"),0);
        Assert.assertNotEquals(s[1].getPrenume().compareTo("Eliza"),0);
    }

    @org.junit.jupiter.api.Test
    void setPrenume() {
        Student[] s=generateStudents();
        s[0].setPrenume("Bianca");
        Assert.assertEquals (s[0].getPrenume().compareTo("Bianca"),0);
    }

    @org.junit.jupiter.api.Test
    void getEmail() {
        Student[] s=generateStudents();
        Assert.assertEquals(s[0].getEmail().compareTo("elizacotocea@gmail.com"),0);
        Assert.assertNotEquals (s[1].getEmail().compareTo("elizacotocea@gmail.com"),0);
    }

    @org.junit.jupiter.api.Test
    void setEmail() {
        Student[] s=generateStudents();
        s[0].setEmail("balintbianca@gmail.com");
        Assert.assertEquals (s[0].getEmail().compareTo("balintbianca@gmail.com"),0);
    }

    @org.junit.jupiter.api.Test
    void getCadruDidacticIndrumatorLab() {
        Student[] s=generateStudents();
        Assert.assertEquals (s[0].getCadruDidacticIndrumatorLab().compareTo("Teofana"),0);
        Assert.assertNotEquals (s[2].getCadruDidacticIndrumatorLab().compareTo("Teofana"),0);
    }

    @org.junit.jupiter.api.Test
    void setCadruDidacticIndrumatorLab() {
        Student[] s=generateStudents();
        s[0].setCadruDidacticIndrumatorLab("Carmen");
        Assert.assertEquals (s[0].getCadruDidacticIndrumatorLab().compareTo("Carmen"),0);
    }

    @org.junit.jupiter.api.Test
    void getGrupa() {
        Student[] s=generateStudents();
        Assert.assertEquals (s[0].getGrupa().compareTo("222"),0);
        Assert.assertNotEquals (s[2].getGrupa().compareTo("222"),0);
    }

    @org.junit.jupiter.api.Test
    void setGrupa() {
        Student[] s=generateStudents();
        s[0].setGrupa("224");
        Assert.assertEquals (s[0].getGrupa().compareTo("224"),0);
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Student[] s=generateStudents();
        Assert.assertEquals(s[0].equals(s[0]),true);
        Assert.assertEquals(s[1].equals(s[0]),false);
    }

}