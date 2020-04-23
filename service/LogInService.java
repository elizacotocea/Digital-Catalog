package myproject.service;

import myproject.databases.LogInDatabase;
import myproject.entities.Student;

public class LogInService {
    LogInDatabase logInDatabase;

    public LogInService(String user, String passwd, String url,String urlS) {
        logInDatabase=new LogInDatabase(new StudentService(user,passwd,urlS),user,passwd,url);
    }
    public Student findOneStudent(String email,String parola) {
        return (Student) logInDatabase.findOneStudent(email,parola);
    }

    public String findTeacher(String email,String parola) {
        return logInDatabase.findTeacher(email,parola);
    }
    public String findAdministrator(String email,String parola) {
        return logInDatabase.findAdministrator(email,parola);
    }

}
