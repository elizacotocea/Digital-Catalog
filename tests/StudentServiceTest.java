package myproject.tests;

import myproject.entities.Student;
import myproject.service.StudentService;
import myproject.validator.ValidationException;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.List;

class StudentServiceTest {
    String urlS = "jdbc:postgresql://localhost:5432/studentTest";
    String user = "postgres";
    String password = "eliza";
    StudentService serv=new StudentService(user,password,urlS);
    StudentService generate(){
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\eliza\\IdeaProjects\\Proiect_map\\src\\Data\\StudentFileXMLTest.xml"));
//            writer.write("");
//            writer.close();
//        } catch (IOException e) {
//        }
        Student s=new Student(1,"Cotocea","Eliza","elizacotocea@gmail.com","Teofana","222");
        Student s1=new Student(2,"Circiu","Andreea","candreea@gmail.com","Teofana","222");
        Student s2=new Student(3,"Coman","Octavian","comanog@gmail.com","Carmen","223");
        try {
            serv.save(s);
        } catch (ValidationException e) {
            assert true;
        }
        try {
            serv.save(s1);
        } catch (ValidationException e) {
            assert true;
        }
        return serv;
    }
    @Test
    void findOne() {
        StudentService st=generate();
        Assert.assertEquals (st.findOne(1).getPrenume(),"Eliza");
        Assert.assertNull(st.findOne(6));
    }

    @Test
    void findAll() {
        StudentService st=generate();
        Assert.assertEquals(st.findAll().spliterator().getExactSizeIfKnown(),12);
    }

    @Test
    void save() {

        Student s=new Student(13,"Cotocea","Eliza","elizacotocea@gmail.com","Teofana","222");
        StudentService st=generate();
        st.delete(13);
        Assert.assertNull(st.save(s));
        Assert.assertNotNull(st.save(s));
    }

    @Test
    void delete() {
        StudentService st=generate();
        Assert.assertEquals(st.delete(1).getNume(),"Cotocea");
        Assert.assertEquals(st.delete(97),null);
    }

    @Test
    void update() {
        Student s1=new Student(8,"Carmin","Andreea","candreea@gmail.com","Teofana","222");
        StudentService st=generate();
        Assert.assertNotEquals(st.update(s1),null);
        Student s=new Student(2,"Carmin","Andreea","candreea@gmail.com","Teofana","222");
        Assert.assertNull(st.update(s));
    }

    @Test
    void findByname() {
        StudentService serv=generate();
        Assert.assertEquals(serv.findByname("Cotocea","Eliza").getGrupa(),"222");
        Assert.assertEquals(serv.findByname("Circiu","Andrd"),null);
    }

    @Test
    void filter_by_group() {
        List<Student> l=serv.filter_by_group("222");
        Assert.assertEquals(l.spliterator().getExactSizeIfKnown(),7);
        Assert.assertEquals(l.get(0).getNume(),"Cotocea");
        List<Student> l1=serv.filter_by_group("292");
        Assert.assertTrue(l1.isEmpty());
    }
}