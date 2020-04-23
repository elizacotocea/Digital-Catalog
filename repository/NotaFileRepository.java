package myproject.repository;

import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.validator.ValidationException;
import myproject.validator.NotaValidator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class NotaFileRepository extends AbstractFileRepository<String, Nota> {
    String filepath;
    StudentXMLRepository srep;
    TemaXMLRepository trep;
    public NotaFileRepository(String fileName,String Sfname, String Tfname) {
        super(new NotaValidator(),fileName);
        this.filepath=fileName;
        srep=new StudentXMLRepository(Sfname);
        trep=new TemaXMLRepository(Tfname);
        loadData();
    }

    @Override
    public void loadData() {
        File file = new File(filepath);
        if(file.exists() && !file.isDirectory())
        {
            try {
                List<String> lines = Files.readAllLines(Paths.get(filepath));
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Nota nota = parseLine(line);
                    super.save(nota);
                }
            } catch (IOException | ValidationException ex) {
            }
        }
    }

    private Nota parseLine(String o)  {
        String[] s=o.split(";");
        String id=s[0];
        int idS=Integer.parseInt(s[1]);
        int idT=Integer.parseInt(s[2]);
        String data=s[3];
        String profesor=s[4];
        float nota1=Float.parseFloat(s[5]);
        String feedback=s[6];
        Student st= (Student) srep.findOne(idS);
        Tema tt=(Tema)trep.findOne(idT);
        Nota nota=new Nota(st,tt,data,profesor);
        nota.updNota(nota1);
        nota.setFeedback(feedback);
        return nota;
    }

    @Override
    protected void writeData(){
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

}
