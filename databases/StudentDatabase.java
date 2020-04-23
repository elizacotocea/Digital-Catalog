package myproject.databases;

import myproject.entities.Student;
import myproject.repository.CrudRepository;
import myproject.validator.StudentValidator;
import myproject.validator.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDatabase implements CrudRepository<Integer, Student> {
    Connection conn;
    StudentValidator validator;

    public StudentDatabase(StudentValidator validator,String user, String password, String url) {
        this.validator=validator;
        try {
            conn = DriverManager.getConnection(url,user,password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student findOne(Integer id) {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String strSelect = "Select * from student where id = "+Integer.toString(id);
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                int    id1   = rset.getInt("ID");
                String nume = rset.getString("Nume");
                String prenume = rset.getString("Prenume");
                String profesor = rset.getString("Profesor");
                String email = rset.getString("Email");
                String grupa= rset.getString("Grupa");
                Student s=new Student(id1,nume,prenume,email,profesor,grupa);
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Student> findAll() {
        List<Student> st = new ArrayList<Student>();
        try
        {
        Statement stmt = conn.createStatement();
        String strSelect = "Select * from student";
        ResultSet rset = stmt.executeQuery(strSelect);
        while(rset.next()) {
            int    id   = rset.getInt("ID");
            String nume = rset.getString("Nume");
            String prenume = rset.getString("Prenume");
            String profesor = rset.getString("Profesor");
            String email = rset.getString("Email");
            String grupa= rset.getString("Grupa");
            Student s=new Student(id,nume,prenume,email,profesor,grupa);
            st.add(s);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return st;
    }

    @Override
    public Student save(Student entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity null");
        validator.validate(entity);
        Statement stmt = null;
        String str="'"+entity.getNume()+"','"+entity.getPrenume()+"','"
        +entity.getCadruDidacticIndrumatorLab()+"','"+entity.getEmail()+"','"+ entity.getGrupa()+"'";
        if (findOne(entity.getId())!=null)
            return entity;
        else{
        try {
            stmt = conn.createStatement();
            String strSelect = "INSERT INTO student(\n" +
                    "\t\"nume\", \"prenume\", \"profesor\", \"email\", \"grupa\")\n" +
                    "\t VALUES ("+str+")";
            stmt.execute(strSelect);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        }
    }

    @Override
    public Student delete(Integer id) {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
        Statement stmt = null;
        Student s=findOne(id);
        if (s==null)
            return null;
        try {
            stmt = conn.createStatement();
            String strSelect = "Delete from student where id = "+Integer.toString(id);
            stmt.execute(strSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public Student update(Student entity) {
        if(entity == null) throw new IllegalArgumentException("entity cannot be null");
        validator.validate(entity);
        Statement stmt = null;
        if (findOne(entity.getId())==null)
            return entity;
        try {
            stmt = conn.createStatement();
            String strSelect = "UPDATE student " +" SET " +"nume='"+
                    entity.getNume()+"',prenume='"+entity.getPrenume()+"',profesor='"+
                    entity.getCadruDidacticIndrumatorLab()+"', email='"+entity.getEmail()+
                    "',grupa='"+entity.getGrupa()+"' where id="+Integer.toString(entity.getId());

            stmt.execute(strSelect);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
