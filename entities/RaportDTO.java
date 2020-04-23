package myproject.entities;

public class RaportDTO {
    private String name;
    private String prenume;
    private Double nota;
    private String profesor;

    public RaportDTO(String name, String prenume, Double nota,String profesor) {
        this.name = name;
        this.prenume = prenume;
        this.profesor=profesor;
        this.nota = (double) Math.round(nota * 100) / 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
