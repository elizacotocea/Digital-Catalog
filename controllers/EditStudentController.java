package myproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import myproject.entities.Student;
import myproject.service.StudentService;
import myproject.validator.ValidationException;

public class EditStudentController {

    @FXML
    private TextField textNume;
    @FXML
    private TextField textPrenume;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textProfesor;
    @FXML
    private TextField textFieldGr;

    private StudentService service;
    Stage dialogStage;
    Student student;
    StudentController studentController=new StudentController();

    @FXML
    private void initialize() {
    }

    @FXML
    public void handleReset() {
        setFields(student);
    }

    @FXML
    public void handleClearUpd() {
        clearFields();
    }

    @FXML
    public void handleUpdateFin() {
        String nume=textNume.getText();
        String prenume=textPrenume.getText();
        String email=textEmail.getText();
        String profesor=textProfesor.getText();
        String grupa=textFieldGr.getText();
        Student student1=new Student(student.getId(),nume,prenume,email,profesor,grupa);
       updateMessage(student1);

    }
    private void updateMessage(Student s)
    {
        try {
            Student r= this.service.update(s);
            if (r==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare student","Studentul a fost modificat");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare student","Studentul nu a fost modificat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();
    }

    public void setService(StudentService service,  Stage stage, Student s){
        this.service=service;
        this.dialogStage=stage;
        this.student=s;
        if (null != s) {
            setFields(s);
        }
    }
    private void clearFields() {
        textNume.setText("");
        textPrenume.setText("");
        textProfesor.setText("");
        textEmail.setText("");
        textFieldGr.setText("");
    }
    private void setFields(Student s)
    {
        textNume.setText(s.getNume());
        textPrenume.setText(s.getPrenume());
        textProfesor.setText(s.getCadruDidacticIndrumatorLab());
        textEmail.setText(s.getEmail());
        textFieldGr.setText(s.getGrupa());
    }
}
