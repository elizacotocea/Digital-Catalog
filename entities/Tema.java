package myproject.entities;

import java.time.LocalDate;

public class Tema extends Entity<Integer> {
    private int ID;
    private String descriere;
    private int startWeek;
    private int deadlineweek;
    private StructuraSemestru str=new StructuraSemestru();
    private int sem;

    public Tema(int ID, String descriere, int startWeek, int deadlineweek,int sem){
        super(ID);
        this.descriere = descriere;
        this.startWeek=startWeek;
        this.deadlineweek=deadlineweek;
        this.sem=sem;
    }

    public Tema(int ID,String descriere,  int deadlineweek) {
        super(ID);
        this.descriere = descriere;
        this.startWeek = str.getSemestru(LocalDate.now()).getSaptamana();
        this.deadlineweek = deadlineweek;
    }
    public String getNRTema() {
        return "Tema "+Integer.toString(this.getId())+"-Deadline sapt "+this.getDeadlineweek();
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public int getDeadlineweek() {
        return deadlineweek;
    }
    public void setDeadlineweek(int deadlineweek) { this.deadlineweek=deadlineweek; }

    public int getSem() {
        return sem;
    }
    public void setSem(int sem) { this.sem=sem; }

    @Override
    public void update(Entity<Integer> entity) {
        var tema=(Tema) entity;
        this.setDeadlineweek(tema.getDeadlineweek());
        this.setDescriere(tema.getDescriere());
    }

    @Override
    public String toString() {
        return this.getId() +
                ";" + descriere +
                ";" + startWeek +
                ";" + deadlineweek ;
    }

    //public int getID() { return ID; }
}
