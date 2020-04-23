package myproject.service;

import myproject.databases.LogInDatabase;
import myproject.databases.StudentDatabase;
import myproject.entities.Student;
import myproject.utils.events.ChangeEventType;
import myproject.utils.events.StudentChangeEvent;
import myproject.utils.observer.Observable;
import myproject.utils.observer.Observer;
import myproject.validator.StudentValidator;
import myproject.validator.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService implements Observable<StudentChangeEvent>, Service<Integer, Student>
{
    StudentDatabase studRepo;
    LogInDatabase logInDatabase;
    public StudentService(String user, String passwd, String url) {
        studRepo=new StudentDatabase(new StudentValidator(),user,passwd,url);
    }

    public Student findOne(Integer id) {
        return (Student) studRepo.findOne(id);
    }

    public Iterable<Student> findAll() {
        return studRepo.findAll();
    }

    public Student save(Student entity) throws ValidationException {
        logInDatabase=new LogInDatabase(new StudentService("postgres","eliza","jdbc:postgresql://localhost:5432/Student"),
                "postgres","eliza","jdbc:postgresql://localhost:5432/loginDatabase");
        Student st=studRepo.save(entity);
        logInDatabase.save(entity.getEmail(),entity.getPrenume().toLowerCase()+"99");
        if(st == null) {
            notifyObservers(new StudentChangeEvent(ChangeEventType.ADD,st));
        }
        return st;
    }

    public Student delete(Integer id) {

        Student st= (Student) studRepo.delete(id);
        if(st != null) {
            notifyObservers(new StudentChangeEvent(ChangeEventType.DELETE,st));
        }
        return st;
    }

    public Student update(Student entity) throws ValidationException {
        Student st= studRepo.update(entity);
        if(st == null) {
            notifyObservers(new StudentChangeEvent(ChangeEventType.UPDATE,st));
        }
        return st;
    }
    public Student findByname(String nume, String prenume){
    Iterable<Student> students=studRepo.findAll();
    for (Student s : students) {
        if (s.getPrenume().equals(prenume) && s.getNume().equals(nume))
            return s;
    }
    return null;
}

    public Student findByEmail(String email){
        Iterable<Student> students=studRepo.findAll();
        for (Student s : students) {
            if (s.getEmail().equals(email))
                return s;
        }
        return null;
    }


    public List<Student> filter_by_group(String gr){
        Iterable<Student> students=findAll();
        List<Student> st = new ArrayList<Student>();
        students.forEach(st::add);
        return st.stream()
                .filter(x->x.getGrupa().equals(gr))
                .collect(Collectors.toList());
    }
    private List<Observer<StudentChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<StudentChangeEvent> e) {
        observers.add(e);
    }
    @Override
    public void removeObserver(Observer<StudentChangeEvent> e) {
        //observers.remove(e);
    }



    @Override
    public void notifyObservers(StudentChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
//
}
