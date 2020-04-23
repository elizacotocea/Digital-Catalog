package myproject.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Nota extends Entity<String> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Tema tema;
    private Student student;
    private LocalDate data;
    private String profesor;
    private float nota;
    private String feedback;
    private StructuraSemestru str=new StructuraSemestru();

    public Nota(Student student, Tema tema,String data, String profesor) {
        super(Integer.toString(student.getId())+Integer.toString(tema.getId()));
        this.student=student;
        this.tema=tema;
        this.data = LocalDate.parse(data,formatter);
        this.profesor = profesor;
        this.nota=0;
        this.feedback="nenotat";
    }
    public Nota(Student student, Tema tema,String data, String profesor,Float nota,String feedback) {
        super(Integer.toString(student.getId())+Integer.toString(tema.getId()));
        this.student=student;
        this.tema=tema;
        this.data = LocalDate.parse(data,formatter);
        this.profesor = profesor;
        this.nota=nota;
        this.feedback=feedback;

    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public float setNota(float Nota,int motivari,int deadline){
        this.nota=Nota;
        if (motivari!=99 && getWeek()>deadline) {
            this.nota -= str.getSemestru(this.data).getSaptamana() - deadline;
            this.nota += motivari;
        }
        if (nota<=0)
            nota=1;
        return nota;

    }
    public void updNota(float Nota){
        this.nota=Nota;
    }

    public float getNota(){
        return this.nota;
    }

    public Tema getTema(){
        return this.tema;
    }

    public Student getStudent(){
        return this.student;
    }

    public String getData() {
        return data.format(formatter);
    }

    public int getWeek(){
        return str.getSemestru(this.data).getSaptamana();
    }

    public void setData(String data) {

        this.data = LocalDate.parse(data,formatter);
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    @Override
    public void update(Entity<String> entity) {
        var nota=(Nota) entity;
        this.setProfesor(nota.getProfesor());
        this.setData(nota.getData().toString());
        this.setFeedback(nota.getFeedback());
        this.updNota(nota.getNota());
    }

    @Override
    public String toString() {
        return this.getId()+";"+this.getStudent().getId()+";"+this.getTema().getId()+";"+this.getData().toString()+";"+this.getProfesor()+";"
                +this.getNota()+";"+this.getFeedback();
    }
}
