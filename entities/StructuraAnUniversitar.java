package myproject.entities;

import java.time.LocalDate;

public class StructuraAnUniversitar extends Entity<Integer> {
    private int anUniversitar;
    private StructuraSemestru semestru=new StructuraSemestru();

    public StructuraAnUniversitar(int ID, int anUniversitar) {
        super(ID);
        this.anUniversitar =anUniversitar;
        this.semestru = semestru.getSemestru(LocalDate.now());
    }


    public StructuraSemestru getSem() {
        return this.semestru;
    }

    public int getAnUniversitar() {
        return anUniversitar;
    }

    public void setAnUniversitar(int anUniversitar) {
        this.anUniversitar = anUniversitar;
    }
}
