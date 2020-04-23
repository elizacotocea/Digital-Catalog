package myproject.repository;

import myproject.entities.Student;
import myproject.validator.StudentValidator;
import myproject.validator.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StudentFileRepository extends InMemoryRepository<Integer, Student> {
   private String filepath;

    public StudentFileRepository(String fileName) {
        super(new StudentValidator());
        this.filepath= fileName;
        loadData();
    }

    private void loadData() {
        File file = new File(filepath);
        if(file.exists() && !file.isDirectory())
        {
            try {
                List<String> lines = Files.readAllLines(Paths.get(filepath));
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Student student = parseLine(line);
                    super.save(student);
                }
            } catch (IOException | ValidationException ex) {
            }
        }
    }

    private Student parseLine(String o)  {
        String[] s=o.split(";");
        int id=Integer.parseInt(s[0]);
        String name=s[1];
        String prenume=s[2];
        String email=s[3];
        String profesor=s[4];
        String grupa=s[5];
        Student student=new Student(id,name,prenume,email,profesor,grupa);
        return student;
    }
    private void writeData(){
         try{
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        for(var entity : super.findAll()){
            writer.write(entity.toString());
            writer.newLine();}
        writer.close();
    }
        catch(IOException e){
    }
}
    @Override
    public Student save(Student entity) throws ValidationException {
        Student s=super.save(entity);
        if (s==null)
            writeData();
        return s;
    }

    @Override
    public Student delete(Integer id) {
        Student s= super.delete(id);
        if (s!=null)
            writeData();
        return s;

    }

    @Override
    public Student update(Student entity) throws ValidationException {
        Student s=super.update(entity);
        if (s!=null)
            writeData();
        return s;
    }
}
