package myproject.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends Entity<Integer> {
    private String nume;
    private String prenume;
    private String email;
    private String cadruDidacticIndrumatorLab;
    private String grupa;
    private List<Tema> teme=new ArrayList<>();;

    public Student(int ID,String name, String prenume, String email, String cadruDidacticIndrumatorLab, String grupa) {
        super(ID);
        this.nume = name;
        this.prenume = prenume;
        this.email = email;
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
        this.grupa = grupa;
    }

    public List<Tema> getTeme() {
        return teme;
    }

    public void setTeme(Tema tema) {
        if (!this.teme.contains(tema))
        this.teme.add(tema);
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String name) {
        this.nume = name;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCadruDidacticIndrumatorLab() {
        return cadruDidacticIndrumatorLab;
    }

    public void setCadruDidacticIndrumatorLab(String cadruDidacticIndrumatorLab) {
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    @Override
    public void update(Entity<Integer> entity) {
        var student=(Student) entity;
        this.setGrupa(student.getGrupa());
        this.setNume(student.getNume());
        this.setPrenume(student.getPrenume());
        this.setCadruDidacticIndrumatorLab(student.getCadruDidacticIndrumatorLab());
        this.setEmail(student.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(getNume(), student.getNume()) &&
                Objects.equals(getPrenume(), student.getPrenume()) &&
                Objects.equals(getEmail(), student.getEmail()) &&
                Objects.equals(getCadruDidacticIndrumatorLab(), student.getCadruDidacticIndrumatorLab()) &&
                Objects.equals(getGrupa(), student.getGrupa());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNume(), getPrenume(), getEmail(), getCadruDidacticIndrumatorLab(), getGrupa());
    }

    @Override
    public String toString() {
        return this.getId()+";"+ nume +
                ";" + prenume +
                ";" + email +
                ";" + cadruDidacticIndrumatorLab +
                ";" + grupa
                ;
    }

    //@Override
    //public int compareTo(Student s1){
      //  return -(int)(this.getMedie()-s1.getMedie());
  //  }
}
