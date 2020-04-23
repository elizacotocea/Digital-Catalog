package myproject.utils.events;

import myproject.entities.Student;

public class StudentChangeEvent implements Event {
    private ChangeEventType type;
    private Student newData,oldData;

    public StudentChangeEvent(ChangeEventType type, Student newData, Student oldData) {
        super();
        this.type = type;
        this.newData = newData;
        this.oldData = oldData;
    }
    public StudentChangeEvent(ChangeEventType type, Student newData) {
        super();
        this.type = type;
        this.newData = newData;
    }
}
