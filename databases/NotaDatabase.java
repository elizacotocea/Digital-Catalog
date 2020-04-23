package myproject.databases;

import myproject.entities.Student;
import myproject.validator.TemaValidator;
import myproject.entities.Nota;
import myproject.entities.Tema;
import myproject.repository.CrudRepository;
import myproject.validator.NotaValidator;
import myproject.validator.StudentValidator;
import myproject.validator.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaDatabase implements CrudRepository<String, Nota> {
    Connection conn;
    NotaValidator validator;
    StudentDatabase studentDatabase;
    TemaDatabase temaDatabase;
    public NotaDatabase(NotaValidator validator,String user, String password, String urlN,String urlS, String urlT) {
        this.validator=validator;
        studentDatabase=new StudentDatabase(new StudentValidator(),user,password,urlS);
        temaDatabase=new TemaDatabase(new TemaValidator(),user,password,urlT);
        try {
            conn = DriverManager.getConnection(urlN,user,password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Nota findOne(String idd) {
        if (idd==null)
            throw new IllegalArgumentException("ID NULL");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String strSelect = "Select * from note where idnota= '"+idd+"'";
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                int    ids   = rset.getInt("idstudent");
                int    idt   = rset.getInt("idtema");
                String    idn   = rset.getString("idnota");
                String data = rset.getString("data");
                String profesor=rset.getString("profesor");
                Float nota=rset.getFloat("nota");
                String feedback=rset.getString("feedback");
                Student s=studentDatabase.findOne(ids);
                Tema t=temaDatabase.findOne(idt);
                Nota n=new Nota(s,t,data,profesor,nota,feedback);
                return n;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Nota> findAll() {
        List<Nota> note= new ArrayList<Nota>();
        try
        {
            Statement stmt = conn.createStatement();
            String strSelect = "Select * from note";
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                int    ids   = rset.getInt("idstudent");
                int    idt   = rset.getInt("idtema");
                String    idn   = rset.getString("idnota");
                String data = rset.getString("data");
                String profesor=rset.getString("profesor");
                Float nota=rset.getFloat("nota");
                String feedback=rset.getString("feedback");
                Student s=studentDatabase.findOne(ids);
                Tema t=temaDatabase.findOne(idt);
                Nota n=new Nota(s,t,data,profesor,nota,feedback);
                note.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return note;
    }

    @Override
    public Nota save(Nota entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity null");
        validator.validate(entity);
        Statement stmt = null;
        String str=Integer.toString(entity.getStudent().getId())+",'"+Integer.toString(entity.getTema().getId())+"','"+
                entity.getId()+"','" +entity.getData()+"','"+entity.getProfesor()+"','"+
                Float.toString(entity.getNota())+"','"+ entity.getFeedback()+"'";
        if (findOne((String)entity.getId())!=null)
            return entity;
        else{
            try {
                stmt = conn.createStatement();
                String strSelect = "INSERT INTO note(\n" +
                        "\t\"idstudent\",\"idtema\",\"idnota\", \"data\", \"profesor\", \"nota\"," +
                        "\"feedback\")\n" +
                        "\t VALUES ("+str+")";
                stmt.execute(strSelect);

            }catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Nota delete(String id) {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
        Statement stmt = null;
        Nota t=findOne(id);
        if (t==null)
            return null;
        try {
            stmt = conn.createStatement();
            String strSelect = "Delete from note where idnota = '"+id+"'";
            stmt.execute(strSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public Nota update(Nota entity) {
        if(entity == null) throw new IllegalArgumentException("entity cannot be null");
        validator.validate(entity);
        Statement stmt = null;
        if (findOne(entity.getId())==null)
            return entity;
        try {
            stmt = conn.createStatement();
            String strSelect = "UPDATE note" +" SET " +"data='"+
                    entity.getData()+"',profesor='"+entity.getProfesor()
                    +"',nota='"+ Float.toString(entity.getNota())+
                    "',feedback='"+entity.getFeedback()+
                    "' where idnota='"+entity.getId()+"'";

            stmt.execute(strSelect);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
