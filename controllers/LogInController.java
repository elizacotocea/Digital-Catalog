package myproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import myproject.entities.Student;
import myproject.service.LogInService;
import myproject.service.NotaService;
import myproject.service.StudentService;
import myproject.service.TemaService;

import java.io.IOException;

public class LogInController {

    LogInService service;
    @FXML
    Button teacherButton;
    @FXML
    Button studentButton;
    @FXML
    Button admButton;

    @FXML
    AnchorPane rootPane;
    @FXML
    AnchorPane logrootPane;
    @FXML
    TextField userTextField;

    @FXML
    Label label;
    @FXML
    CheckBox checkPasswd;
    @FXML
    PasswordField passwordField;

    public void setService(LogInService service) {
        this.service = service;

    }
    public void showCatalogNote() throws IOException {

        if (!userTextField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            String email=userTextField.getText();
            String passwd=passwordField.getText();
        if (service.findTeacher(email,passwd)!=null){
            Stage currentStage=(Stage)teacherButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/notaView.fxml"));
            AnchorPane root=loader.load();
            String numeProf=service.findTeacher(email,passwd).split(" ")[0];

            NotaController ctrl=loader.getController();
            ctrl.setService(new NotaService("postgres","eliza","jdbc:postgresql://localhost:5432/Nota",
                "jdbc:postgresql://localhost:5432/Student","jdbc:postgresql://localhost:5432/Tema"),
                new StudentService("postgres","eliza","jdbc:postgresql://localhost:5432/Student"),
                new TemaService("postgres","eliza","jdbc:postgresql://localhost:5432/Tema"),
                    numeProf);

            Scene scene=new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();}
        else{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Date gresite", "Email sau parola incorecte!");
        }}
        else{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Conexiune", "Completati toate campurile!");

        }

    }

    public void showAdaugareSt() throws IOException {

        if (!userTextField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            String email=userTextField.getText();
            String passwd=passwordField.getText();
            if (service.findAdministrator(email,passwd)!=null){
                Stage currentStage=(Stage)admButton.getScene().getWindow();
                currentStage.close();
                FXMLLoader loader=new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/studentView.fxml"));
                AnchorPane root=loader.load();


                StudentController ctrl=loader.getController();
                ctrl.setService(new StudentService("postgres","eliza","jdbc:postgresql://localhost:5432/Student"),
                        new NotaService("postgres","eliza","jdbc:postgresql://localhost:5432/Nota",
                                "jdbc:postgresql://localhost:5432/Student","jdbc:postgresql://localhost:5432/Tema"),
                        new TemaService("postgres","eliza","jdbc:postgresql://localhost:5432/Tema"));

                Scene scene=new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();}
            else{
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Date gresite", "Email sau parola incorecte!");
            }}
        else{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Conexiune", "Completati toate campurile!");

        }

    }

    public void showNoteStudenti() throws IOException {

        if (!userTextField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            String email=userTextField.getText();
            String passwd=passwordField.getText();
            Student st=service.findOneStudent(email,passwd);
            if (service.findOneStudent(email,passwd)!=null){
                Stage currentStage=(Stage)studentButton.getScene().getWindow();
                currentStage.close();
                FXMLLoader loader=new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/noteStudentView.fxml"));
                AnchorPane root=loader.load();


                NoteStudentController ctrl=loader.getController();
                ctrl.setService(new NotaService("postgres","eliza","jdbc:postgresql://localhost:5432/Nota",
                                "jdbc:postgresql://localhost:5432/Student","jdbc:postgresql://localhost:5432/Tema"),st);

                Scene scene=new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();}
            else{
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Date gresite", "Email sau parola incorecte!");
            }}
        else{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Conexiune", "Completati toate campurile!");

        }
    }

    @FXML
    public void checkPassword(){
        String password=passwordField.getText();
        if (checkPasswd.isSelected()){
            passwordField.setPromptText(passwordField.getText());
            passwordField.setText("");
            passwordField.setDisable(true);
    }
    else{
            passwordField.setText(passwordField.getPromptText());
            passwordField.setPromptText("");
            passwordField.setDisable(false);
        }
    }
}
