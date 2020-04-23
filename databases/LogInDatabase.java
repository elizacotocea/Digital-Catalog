package myproject.databases;

import myproject.entities.Student;
import myproject.service.StudentService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LogInDatabase {
    private Connection conn;
    private StudentService studentService;
    public LogInDatabase(StudentService studentService,String user, String password, String url) {
        this.studentService=studentService;
        try {
            this.conn = DriverManager.getConnection(url,user,password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student findOneStudent(String email,String passwd) {
        Statement stmt = null;
        try {
            String hiddenPasswd=cryptWithMD5(passwd).toString();
            stmt = this.conn.createStatement();
            String strSelect = "Select * from studentlogin where email  ='"+email +"' and parola = '"+hiddenPasswd+"'";
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                var s=studentService.findByEmail(rset.getString("email"));
                    return s;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findTeacher(String email,String passwd) {
        Statement stmt = null;
        try {
            String hiddenPasswd=cryptWithMD5(passwd).toString();
            stmt = this.conn.createStatement();
            String strSelect = "Select * from profesorlogin where email ='"+email +"' and parola = '"+hiddenPasswd+"'";
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                return rset.getString("nume");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findAdministrator(String email,String passwd) {
        Statement stmt = null;
        try {
            String hiddenPasswd=cryptWithMD5(passwd);
            stmt = this.conn.createStatement();
            String strSelect = "Select * from administratorlogin where email ='"+email +"' and parola = '"+hiddenPasswd+"'";
            ResultSet rset = stmt.executeQuery(strSelect);
            while(rset.next()) {
                return "administrator";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String cryptWithMD5(String pass){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Eroare");
        }
        return null;


    }
//
//    public String decryptPassword(byte[] passwd){
//        try{
//            // Generate the key first
//            System.out.println("in");
//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(128);  // Key size
//            Key key = keyGen.generateKey();
//
//            // Create Cipher instance and initialize it to encrytion mode
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  // Transformation of the algorithm
//            // Reinitialize the Cipher to decryption mode
//            cipher.init(Cipher.DECRYPT_MODE,key, cipher.getParameters());
//            byte[] plainBytesDecrypted = cipher.doFinal(passwd);
//            System.out.println("DECRUPTED DATA : "+new String(plainBytesDecrypted));
//            return new String(plainBytesDecrypted);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }

    public void save(String email,String parola){
        Statement stmt = null;
        String str="'"+email+"','"+cryptWithMD5(parola).toString()+"'";
            try {
                stmt = conn.createStatement();
                String strSelect = "INSERT INTO studentlogin(\n" +
                        "\t\"email\", \"parola\")\n" +
                        "\t VALUES ("+str+")";
                stmt.execute(strSelect);

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }


    public void update(String parola) {
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
//            String strSelect = "UPDATE student " +" SET " +"nume='"+
//                    entity.getNume()+"',prenume='"+entity.getPrenume()+"',profesor='"+
//                    entity.getCadruDidacticIndrumatorLab()+"', email='"+entity.getEmail()+
//                    "',grupa='"+entity.getGrupa()+"' where id="+Integer.toString(entity.getId());

           // stmt.execute(strSelect);

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
