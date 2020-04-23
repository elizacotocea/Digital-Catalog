package myproject.utils.events;

import myproject.entities.Nota;

public class NotaChangeEvent implements Event {
    private ChangeEventType type;
    private Nota newData,oldData;

    public NotaChangeEvent(ChangeEventType type, Nota newData, Nota oldData) {
        super();
        this.type = type;
        this.newData = newData;
        this.oldData = oldData;
    }
    public NotaChangeEvent(ChangeEventType type, Nota newData) {
        super();
        this.type = type;
        this.newData = newData;
    }
}
