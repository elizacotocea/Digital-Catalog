package myproject.databases;

import myproject.validator.TemaValidator;
import myproject.entities.Tema;
import myproject.repository.CrudRepository;
import myproject.validator.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemaDatabase implements CrudRepository<Integer, Tema> {
    private Connection conn;
    private TemaValidator validator;

    public TemaDatabase(TemaValidator validator,String user, String password, String url) {
        this.validator=validator;
        try {
            this.conn = DriverManager.getConnection(url,user,password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tema findOne(Integer id) {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            String strSelect = "Select * from teme where id = "+Integer.toString(id);
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                int    id1   = rset.getInt("id");
                String descriere = rset.getString("descriere");
                int    sweek   = rset.getInt("startweek");
                int    dweek   = rset.getInt("deadlineweek");
                int    s   = rset.getInt("sem");
                Tema t=new Tema(id1,descriere,sweek,dweek,s);
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Tema> findAll() {
        List<Tema> teme = new ArrayList<Tema>();
        try
        {
            Statement stmt = this.conn.createStatement();
            String strSelect = "Select * from teme";
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                int    id1   = rset.getInt("id");
                String descriere = rset.getString("descriere");
                int    sweek   = rset.getInt("startweek");
                int    dweek   = rset.getInt("deadlineweek");
                int    s   = rset.getInt("sem");
                Tema t=new Tema(id1,descriere,sweek,dweek,s);
                teme.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teme;
    }

    @Override
    public Tema save(Tema entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity null");
        validator.validate(entity);
        Statement stmt = null;
        String str=Integer.toString(entity.getId())+",'"+entity.getDescriere()+"',"+
                Integer.toString(entity.getStartWeek())+","
                +Integer.toString(entity.getDeadlineweek());
        if (findOne(entity.getId())!=null)
            return entity;
        else{
            try {
                stmt = this.conn.createStatement();
                String strSelect = "INSERT INTO teme(" +
                        "\"id\",\"descriere\", \"startweek\", \"deadlineweek\")" +
                        " VALUES ("+str+")";
                stmt.execute(strSelect);

            }catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Tema delete(Integer id) {
        if (id==null)
            throw new IllegalArgumentException("ID NULL");
        Statement stmt = null;
        Tema t=findOne(id);
        if (t==null)
            return null;
        try {
            stmt = this.conn.createStatement();
            String strSelect = "Delete from teme where id = "+Integer.toString(id);
            stmt.execute(strSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public Tema update(Tema entity) {
        if(entity == null) throw new IllegalArgumentException("entity cannot be null");
        validator.validate(entity);
        Statement stmt = null;
        if (findOne(entity.getId())==null)
            return entity;
        try {
            stmt = this.conn.createStatement();
            String strSelect = "UPDATE teme " +" SET " +"descriere='"+
                    entity.getDescriere()+"',startweek='"+Integer.toString(entity.getStartWeek())
                    +"',deadlineweek='"+
                    Integer.toString(entity.getDeadlineweek())+"' where id="+Integer.toString(entity.getId());
            stmt.execute(strSelect);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
