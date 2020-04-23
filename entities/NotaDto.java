package myproject.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotaDto extends Entity<String>{
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Tema tema;
    private Student student;
    private int studentID;
    private int temaID;
    private LocalDate data;
    private String stringdata;
    private String profesor;
    private float nota;
    private String feedback;
    private StructuraSemestru str=new StructuraSemestru();
    private String  nume;
    private String notaID;
    private String descriere;


    public NotaDto(Student student, Tema tema, String data, String profesor, Float nota, String feedback) {
        super(Integer.toString(student.getId())+Integer.toString(tema.getId()));
        this.student=student;
        this.nume=student.getNume()+" "+student.getPrenume();
        this.studentID=student.getId();
        this.tema=tema;
        this.temaID=tema.getId();
        this.descriere=this.tema.getDescriere();
        this.notaID=Integer.toString(studentID)+Integer.toString(temaID);
        this.data = LocalDate.parse(data,formatter);
        this.stringdata=data;
        this.profesor = profesor;
        this.nota=nota;
        this.feedback=feedback;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getTemaID() {
        return temaID;
    }

    public String getNRTema() {
        return "Tema "+Integer.toString(temaID)+"-Deadline sapt "+tema.getDeadlineweek();
    }

    public void setTemaID(int temaID) {
        this.temaID = temaID;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getStringdata() {
        return stringdata;
    }

    public void setStringdata(String stringdata) {
        this.stringdata = stringdata;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public StructuraSemestru getStr() {
        return str;
    }

    public void setStr(StructuraSemestru str) {
        this.str = str;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNotaID() {
        return notaID;
    }

    public void setNotaID(String notaID) {
        this.notaID = notaID;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String desc) {
        this.descriere = desc;
    }

    public String toString() {
        return this.getNume()+";"+this.getTemaID()+";"+this.getData().toString()+";"+this.getProfesor()+";"
                +this.getNota()+";"+this.getFeedback()+";"+this.getStudentID()+";"+this.getId();
    }



}
