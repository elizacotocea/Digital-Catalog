package myproject.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

public class StructuraSemestru {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String filename="C:\\Users\\eliza\\IdeaProjects\\Proiect_map\\src\\Data\\StructuraSem.txt";
    Path path= Paths.get(filename);
    List<String> lines;
    private LocalDate inceput_sem1;
    private LocalDate inceput_v1;
    private LocalDate sfarsit_v1;
    private LocalDate inceput_sem2;
    private LocalDate inceput_v2;
    private LocalDate sfarsit_v2;
    private LocalDate sfarsit_sem1;
    private LocalDate sfarsit_sem2;
    private int nrsem;
    private int saptamana;

    private void read(){
    {
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }}
    public StructuraSemestru(){
        read();
        inceput_sem1=LocalDate.parse(lines.get(0),formatter);
        inceput_v1=LocalDate.parse(lines.get(1).split("-")[0],formatter);
        sfarsit_v1=LocalDate.parse(lines.get(1).split("-")[1],formatter);
        inceput_sem2=LocalDate.parse(lines.get(3),formatter);
        inceput_v2=LocalDate.parse(lines.get(4).split("-")[0],formatter);
        sfarsit_v2=LocalDate.parse(lines.get(4).split("-")[1],formatter);
        sfarsit_sem1=LocalDate.parse(lines.get(2),formatter);
        sfarsit_sem2=LocalDate.parse(lines.get(5),formatter);
    }

    public StructuraSemestru getSemestru(LocalDate date){
        int inceput=0;
        int sapt=0;
        if (date.isAfter(getInceput_sem1()) && date.isBefore(getInceput_v1())){
            inceput= getInceput_sem1().get(WeekFields.of( Locale.UK ).weekOfWeekBasedYear( ));
            sapt=date.get(WeekFields.of( Locale.UK).weekOfWeekBasedYear( ));
            return new StructuraSemestru(1,sapt-inceput+1);}
        else if (date.isAfter(getSfarsit_v1()) && date.isBefore(getSfarsit_sem1())){
            inceput= getInceput_sem1().get(WeekFields.of( Locale.UK ).weekOfWeekBasedYear( ));
            sapt=date.get(WeekFields.of( Locale.UK).weekOfWeekBasedYear( ));
            return new StructuraSemestru(1,12+sapt-1);
        }
        else if (date.isAfter(getInceput_sem2()) && date.isBefore(getInceput_v2())){
            inceput= getInceput_sem2().get(WeekFields.of( Locale.UK ).weekOfWeekBasedYear( ));
            sapt=date.get(WeekFields.of( Locale.UK).weekOfWeekBasedYear( ));
            return new StructuraSemestru(2,sapt-inceput+1);
        }
        else if (date.isAfter(getSfarsit_v2()) && date.isBefore(getSfarsit_sem2()))
        {
            inceput= getInceput_sem2().get(WeekFields.of( Locale.UK ).weekOfWeekBasedYear( ));
            sapt=date.get(WeekFields.of( Locale.UK).weekOfWeekBasedYear( ));
            return new StructuraSemestru(2,sapt-inceput+1);
        }
        return null;
    }

    public StructuraSemestru(int nrsem, int saptamana) {
        this.nrsem = nrsem;
        this.saptamana = saptamana;
    }

    public int getNrsem() {
        return nrsem;
    }

    public void setNrsem(int nrsem) {
        this.nrsem = nrsem;
    }

    public int getSaptamana() {
        return saptamana;
    }


    public LocalDate getInceput_sem1() {
        return inceput_sem1;
    }

    public LocalDate getInceput_v1() {
        return inceput_v1;
    }

    public LocalDate getSfarsit_v1() {
        return sfarsit_v1;
    }

    public LocalDate getInceput_sem2() {
        return inceput_sem2;
    }

    public LocalDate getInceput_v2() {
        return inceput_v2;
    }

    public LocalDate getSfarsit_v2() {
        return sfarsit_v2;
    }

    public LocalDate getSfarsit_sem1() {
        return sfarsit_sem1;
    }

    public LocalDate getSfarsit_sem2() {
        return sfarsit_sem2;
    }
}
