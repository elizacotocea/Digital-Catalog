package myproject.repository;

import myproject.entities.Tema;
import myproject.validator.TemaValidator;
import myproject.validator.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TemaFileRepository extends InMemoryRepository<Integer, Tema> {
    private String filepath;

    public TemaFileRepository(String filepath)
    {
        super(new TemaValidator());
        this.filepath=filepath;
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
                    Tema tema = parseLine(line);
                    super.save(tema);
                }
            } catch (IOException | ValidationException ex) {
            }
        }
    }

    private Tema parseLine(String o)  {
        String[] s=o.split(";");
        int id=Integer.parseInt(s[0]);
        String descriere=s[1];
        int startweek=Integer.parseInt(s[2]);
        int deadline=Integer.parseInt(s[3]);
        Tema tema=new Tema(id,descriere,startweek,deadline,1);
        return tema;
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
    public Tema save(Tema entity) throws ValidationException {
        Tema s=super.save(entity);
        if (s==null)
            writeData();
        return s;
    }

    @Override
    public Tema delete(Integer id) {
        Tema s= super.delete(id);
        if (s!=null)
            writeData();
        return s;

    }

    @Override
    public Tema update(Tema entity) throws ValidationException {
        Tema s=super.update(entity);
        if (s!=null)
            writeData();
        return s;
    }

}
