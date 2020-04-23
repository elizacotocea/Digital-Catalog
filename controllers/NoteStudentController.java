package myproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import myproject.entities.Nota;
import myproject.entities.NotaDto;
import myproject.entities.Student;
import myproject.service.LogInService;
import myproject.service.NotaService;
import myproject.utils.events.NotaChangeEvent;
import myproject.utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoteStudentController implements Observer<NotaChangeEvent> {
    ObservableList<NotaDto> modelGrade = FXCollections.observableArrayList();
    private NotaService service;
    private Student student;
    @FXML
    TableColumn<NotaDto, Integer> nrTemaColumn;
    @FXML
    TableColumn<NotaDto, String> descriereColumn;
    @FXML
    TableColumn<NotaDto, Float> notaColumn;
    @FXML
    TableColumn<NotaDto, String> feedbackColumn;

    @FXML
    TableView<NotaDto> table;
    @FXML
    Label label;
    @FXML
    Label logLabel;
    @FXML
    Button logOutButton;

    public void setService(NotaService serv,Student st) {
        this.service = serv;
        this.student=st;
        modelGrade.setAll(getNotaDTOList());
        service.addObserver(this);
        Double nota=(this.service.get_average_for_a_student(this.student.getId()));
        Double nota2=(double)Math.round(nota * 100) / 100;
        label.setText(Double.toString(nota2));
        logLabel.setText("Logged in as "+student.getNume()+" "+student.getPrenume());
    }
    public void initialize() {
        nrTemaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, Integer>("temaID"));
        notaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, Float>("nota"));
        feedbackColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("feedback"));
        descriereColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("descriere"));
        table.setItems(modelGrade);
    }

    private List<NotaDto> getNotaDTOList() {
        List<Nota> lst = new ArrayList<Nota>();
        Iterable<Nota> note = service.findAll();
        note.forEach(lst::add);
        return lst
                .stream()
                .map(n -> new NotaDto(n.getStudent(), n.getTema(), n.getData(), n.getProfesor(), n.getNota(), n.getFeedback()))
                .filter(n->n.getStudentID()==this.student.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void update(NotaChangeEvent notaChangeEvent) {
        modelGrade.setAll(getNotaDTOList());
    }

    public void logOutAction() throws IOException {
                Stage currentStage=(Stage)table.getScene().getWindow();
                currentStage.close();
                FXMLLoader loader=new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/logView.fxml"));
                AnchorPane root=loader.load();


                LogInController ctrl=loader.getController();
                ctrl.setService(new LogInService("postgres","eliza","jdbc:postgresql://localhost:5432/loginDatabase",
                        "jdbc:postgresql://localhost:5432/Student"));

                Scene scene=new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
        }

}
