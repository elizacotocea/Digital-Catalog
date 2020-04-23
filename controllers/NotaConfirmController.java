package myproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import myproject.entities.Nota;
import myproject.service.NotaService;


public class NotaConfirmController {
    NotaService service;
    Stage dialogStage;
    Nota nota;
    NotaController notaController=new NotaController();

    @FXML
    TextField notaText;
    @FXML
    TextField profesorText;
    @FXML
    TextField studentText;
    @FXML
    TextField temaText;
    @FXML
    TextField dataText;
    @FXML
    TextArea feedbackText;
    public void setService(NotaService service, Stage stage, Nota n){
        this.service=service;
        this.dialogStage=stage;
        this.nota=n;
        if (null != n) {
            setFields(n);
        }
    }





    @FXML
    private void initialize() {
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
    @FXML
    public void handleConfirm() {
        Nota r = this.service.save(nota);
        if (r == null)
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare nota", "Nota a fost salvata!");
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare student", "Studentul nu a fost salvat! ID deja existent");
    }
    private void setFields(Nota n)
    {
        profesorText.setText(n.getProfesor());
        dataText.setText(n.getData());
        studentText.setText(n.getStudent().getNume()+" "+n.getStudent().getPrenume());
        notaText.setText(Float.toString(n.getNota()));
        feedbackText.setText(n.getFeedback());
        temaText.setText(Integer.toString(n.getTema().getId()));
    }
}
