package myproject.entities;

public class EminentiDTO {
    private String name;
    private String prenume;
    private String data;
    private String sapt;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSapt() {
        return sapt;
    }

    public void setSapt(String sapt) {
        this.sapt = sapt;
    }

    public EminentiDTO(String name, String prenume, String data, String sapt) {
        this.name = name;
        this.prenume = prenume;
        this.data = data;
        this.sapt = sapt;
    }
}
