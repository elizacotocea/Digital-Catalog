package myproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import myproject.entities.RaportDTO;
import myproject.entities.Tema;
import myproject.service.NotaService;

import java.util.List;
import java.util.stream.Collectors;

public class RaportController {
    private NotaService notaService;
    private String profesor;
    @FXML
    TableView<RaportDTO> tableView;
    @FXML
    TableColumn<RaportDTO, String> column1;
    @FXML
    TableColumn<RaportDTO, String> column2;
    @FXML
    TableColumn<RaportDTO, Double> column3;


    @FXML
    Label labelRez;
    public void studentGrades(ActionEvent actionEvent){
        labelRez.setText("Mediile la laborator ale studentilor: ");
        tableView.getItems().clear();
        tableView.setVisible(true);
        List<RaportDTO> medii= notaService.get_average_for_students().stream().filter(m->m.getProfesor().compareTo(profesor)==0).collect(Collectors.toList());
        column1.setText("Nume");
        column2.setText("Prenume");
        column3.setText("Medie");
        column1.setCellValueFactory(new PropertyValueFactory<RaportDTO,String >("name"));
        column2.setCellValueFactory(new PropertyValueFactory<RaportDTO,String >("prenume"));
        column3.setCellValueFactory(new PropertyValueFactory<RaportDTO,Double>("nota"));

        tableView.getItems().addAll(medii);

    }

    public void studentsPassed(ActionEvent actionEvent){
        labelRez.setText("Studentii care pot intra in examen:");
        tableView.getItems().clear();
        tableView.setVisible(true);
        List<RaportDTO> medii= notaService.get_students_promoted().stream().filter(m->m.getProfesor().compareTo(profesor)==0).collect(Collectors.toList());
        column1.setText("Nume");
        column2.setText("Prenume");
        column3.setText("Medie");
        column1.setCellValueFactory(new PropertyValueFactory<RaportDTO,String >("name"));
        column2.setCellValueFactory(new PropertyValueFactory<RaportDTO,String >("prenume"));
        column3.setCellValueFactory(new PropertyValueFactory<RaportDTO,Double>("nota"));

        tableView.getItems().addAll(medii);

    }

    public void studentsEminenti(ActionEvent actionEvent){
        labelRez.setText("Studentii care au predat toate temele la timp: ");
        tableView.getItems().clear();
        tableView.setVisible(true);
        List<RaportDTO> medii1= notaService.get_students_with_all_assignments().stream().filter(m->m.getProfesor().compareTo(profesor)==0).collect(Collectors.toList());
        List<RaportDTO> medii=medii1.stream()
                .filter(m->m.getNota()>5)
                .collect(Collectors.toList());
        column1.setText("Nume");
        column2.setText("Prenume");
        column3.setText("Medie");
        column1.setCellValueFactory(new PropertyValueFactory<RaportDTO,String >("name"));
        column2.setCellValueFactory(new PropertyValueFactory<RaportDTO,String >("prenume"));
        column3.setCellValueFactory(new PropertyValueFactory<RaportDTO,Double>("nota"));
        tableView.getItems().addAll(medii);

    }
    public void hardestHw(ActionEvent actionEvent){
        tableView.setVisible(false);
        labelRez.setVisible(true);
        Tema t=notaService.get_hardest_assignment();
        labelRez.setText("Cea mai grea tema: "+t.getNRTema()+" "+t.getDescriere()+" ");

    }
    public void setService( NotaService serv,String prof) {
        this.notaService = serv;
        this.profesor=prof;
    }

}
