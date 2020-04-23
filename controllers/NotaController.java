package myproject.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import myproject.entities.*;
import myproject.service.LogInService;
import myproject.service.NotaService;
import myproject.service.StudentService;
import myproject.service.TemaService;
import myproject.utils.events.NotaChangeEvent;
import myproject.utils.observer.Observer;
import myproject.validator.ValidationException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static myproject.controllers.MessageAlert.showErrorMessage;

public class NotaController implements Observer<NotaChangeEvent> {
    ObservableList<NotaDto> modelGrade = FXCollections.observableArrayList();
    private NotaService service;
    private TemaService temaService;
    private StudentController studentController;
    private StudentService studentService;
    private StructuraSemestru structuraSemestru = new StructuraSemestru();
    private String numeProf;
    Stage dialogStage = new Stage();
    @FXML
    ToggleButton studentButton;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    Button updateButton;

    @FXML
    AnchorPane rootPane;
    @FXML
    TableColumn<NotaDto, String> studentColumn;
    @FXML
    TableColumn<NotaDto, Integer> nrtemaColumn;
    @FXML
    TableColumn<NotaDto, String> dataColumn;
    @FXML
    TableColumn<NotaDto, String> profesorColumn;
    @FXML
    TableColumn<NotaDto, Float> notaColumn;
    @FXML
    TableColumn<NotaDto, String> feedbackColumn;
    @FXML
    TableColumn<NotaDto, Integer> studentidColumn;
    @FXML
    TableColumn<NotaDto, String> notaidColumn;
    @FXML
    TableView<NotaDto> tableViewNota;
    @FXML
    ComboBox<String> nrtemaComboBox;
    @FXML
    ComboBox<String> studentsComboBox;
    @FXML
    TextArea feedbackTextField;
    @FXML
    TextField notaTextField;
    @FXML
    DatePicker datePicker;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    ComboBox<String> profComboBox;
    @FXML
    CheckBox motivare1;
    @FXML
    CheckBox motivare2;
    @FXML
    Label start;
    @FXML
    Label end;
    @FXML
    Button logOutButton;
    @FXML
    Label temaLabel;
    @FXML
    Label profLabel;
    @FXML
    Label studentLabel;

    public void initialize() {
        studentColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("nume"));
        nrtemaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, Integer>("temaID"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("stringdata"));
        profesorColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("profesor"));
        notaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, Float>("nota"));
        feedbackColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("feedback"));
        studentidColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, Integer>("studentID"));
        notaidColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("notaID"));
        tableViewNota.setItems(modelGrade);

        studentsComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                studentsComboBox.getItems().setAll(getStudents());
            }
        });

        nrtemaComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                nrtemaComboBox.getItems().setAll(getHw());
            }
        });


    }


    public Set<String> getHw() {
        Iterable<Tema> note1 = temaService.findAll();
        List<Tema> note = new ArrayList<Tema>();
        Iterable<Tema> n = temaService.findAll();
        n.forEach(note::add);
        return note.stream().collect(Collectors.groupingBy(x -> x.getNRTema())).keySet();
    }

    public List<String> getStudents() {
        List<Student> stud = new ArrayList<Student>();
        Iterable<Student> n = studentService.findAll();
        n.forEach(stud::add);
        String numeprof=numeProf;
        stud.sort((e1, e2) -> e1.getNume().compareTo(e2.getNume()));
        if (numeprof != null)
            return stud
                    .stream()
                    .filter(x -> x.getCadruDidacticIndrumatorLab().compareTo(numeprof) == 0)
                    .map(x -> x.getNume() + " " + x.getPrenume())
                    .collect(Collectors.toList());
        else
            return stud
                    .stream()
                    .map(x -> x.getNume() + " " + x.getPrenume())
                    .collect(Collectors.toList());

    }

//    public List<String> getProf() {
//        List<Student> stud = new ArrayList<Student>();
//        Iterable<Student> n = studentService.findAll();
//        n.forEach(stud::add);
//        stud.sort((e1, e2) -> e1.getCadruDidacticIndrumatorLab().compareTo(e2.getCadruDidacticIndrumatorLab()));
//        return stud
//                .stream()
//                .map(x -> x.getCadruDidacticIndrumatorLab())
//                .distinct()
//                .collect(Collectors.toList());
//
//    }

    private List<NotaDto> getNotaDTOList() {
        List<Nota> lst = new ArrayList<Nota>();
        Iterable<Nota> note = service.findAll();
        note.forEach(lst::add);
        return lst
                .stream()
                .map(n -> new NotaDto(n.getStudent(), n.getTema(), n.getData(), n.getProfesor(), n.getNota(), n.getFeedback()))
                .filter(n->n.getProfesor().compareTo(this.numeProf)==0)
                .collect(Collectors.toList());
    }

    private String getClosestHw() {
        Iterable<Tema> note = temaService.findAll();
        int min = 100;
        String tema = "";
        for (Tema n : note) {
            if (Math.abs(n.getDeadlineweek() - structuraSemestru.getSemestru(LocalDate.now()).getSaptamana()) < min) {
                min = Math.abs(n.getDeadlineweek() - structuraSemestru.getSemestru(LocalDate.now()).getSaptamana());
                tema = "Tema " + Integer.toString(n.getId()) + "-Deadline sapt " + n.getDeadlineweek();
            }
        }
        return tema;
    }

    public void setService(NotaService serv, StudentService studentService, TemaService temaService,String prof) {
        this.numeProf=prof;
        this.profLabel.setText("Logged in as "+this.numeProf.split(" ")[0]);
        this.service = serv;
        this.studentService = studentService;
        this.temaService = temaService;
        modelGrade.setAll(getNotaDTOList());
        service.addObserver(this);
        datePicker.setValue(LocalDate.now());
        nrtemaComboBox.setValue(getClosestHw());
        notaTextField.setText("10");
        temaLabel.setText(getClosestHw());
        checkDate();
        // studentsComboBox.getSelectionModel().select(getStudents().iterator().next());
        //grupaComboBox.isEditable();


    }

    public void showRapoarte() throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/filterView.fxml"));
        AnchorPane root=loader.load();


        RaportController ctrl=loader.getController();
        ctrl.setService(new NotaService("postgres","eliza","jdbc:postgresql://localhost:5432/Nota",
                        "jdbc:postgresql://localhost:5432/Student","jdbc:postgresql://localhost:5432/Tema"),this.numeProf
               );

        Scene scene=new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void showPieChart() throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/statisticsView.fxml"));
        AnchorPane root=loader.load();


        StatisticsController ctrl=loader.getController();
        ctrl.setService(new NotaService("postgres","eliza","jdbc:postgresql://localhost:5432/Nota",
                        "jdbc:postgresql://localhost:5432/Student","jdbc:postgresql://localhost:5432/Tema"),numeProf);

        Scene scene=new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void showMessageTaskEditDialog(Nota nota) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, nota.getStudent().getNume() + " " + nota.getStudent().getPrenume() + ", \n Tema " +
                nota.getTema().getId() + ", " + nota.getData() + ", " + nota.getProfesor() + ", " + nota.getNota() + ",\n " +
                nota.getFeedback(),
                ButtonType.YES, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            service.save(nota);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare nota", "Nota a fost salvata!");
            handleClear();
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare nota", "Nota NU a fost salvata!");
        }

    }

    public void changeSceneToStudent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/studentView.fxml"));
        AnchorPane pane = loader.load();
        studentController = loader.getController();
        studentController.setService(studentService, service, temaService);
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void handleMotivari1() {
        if (motivare1.isVisible() && !motivare1.isSelected()) {
            notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) + 1));
            feedbackTextField.setText("");
        } else if (motivare1.isVisible() && motivare1.isSelected()) {
            notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) - 1));
            if (motivare1.isVisible() && !motivare1.isSelected())
                feedbackTextField.setText("");
            else
                feedbackTextField.setText("Ai intarziat o saptamana! -1 punct la nota! \n");
        }
    }

    @FXML
    public void handleMotivari2() {
        if (motivare2.isVisible() && !motivare2.isSelected()) {
            notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) + 1));
        } else if (motivare2.isVisible() && motivare2.isSelected()) {
            notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) - 1));
        }
    }

    @FXML
    public void handleDelete() {
        if (tableViewNota.getSelectionModel().isEmpty()) {
            showErrorMessage(null, "Nu ati ales nicio nota!");
        } else {
            String item = tableViewNota.getSelectionModel().getSelectedItem().toString();
            String[] items = item.split(";");
            try {
                Nota r = this.service.delete(items[7]);
                if (r != null)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Stergere nota", "Nota a fost stearsa");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Stergere nota", "Nota nu a fost stearsa! ID inexistent");
            } catch (ValidationException e) {
                showErrorMessage(null, e.getMessage());
            }
        }
    }


    @FXML
    public void handleSave() {
        try {
           // String nume = studentsComboBox.getSelectionModel().getSelectedItem().toString();
            String nume=studentLabel.getText();
            if (!studentLabel.getText().isEmpty() && !temaLabel.getText().isEmpty()){
            String[] numestud = nume.split(" ");
            Student st = studentService.findByname(numestud[0], numestud[1]);
          //  String tema = nrtemaComboBox.getSelectionModel().getSelectedItem().toString();

            String tema=temaLabel.getText();
            String[] item = tema.split(" ");
            String[] items = item[1].split("-");
            LocalDate data = datePicker.getValue();
            int temaID = Integer.parseInt(items[0]);
            Tema t = service.findTema(temaID);
            int penality = service.getPenality(t, data);
            if (penality == -100){
                notaTextField.setText("0");
                showErrorMessage(null, "Alegeti o data din timpul semestrului!");}
            else{
            String profesor = this.numeProf;
            String feedback = feedbackTextField.getText();
            Float nota = Float.parseFloat(notaTextField.getText());
            if (nota > (float) 10 || nota <= (float) 0)
                showErrorMessage(null, "Introduceti o nota intre 1 si 10!");
            else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String data_predare = data.format(formatter);
                Nota save_nota = new Nota(st, t, data_predare, profesor, nota, feedback);
                saveNota(save_nota);
            }
            handlehw();

            }}
            else{
                handlehw();
                    showErrorMessage(null, "Introduceti toate datele!");

                }
        } catch (NumberFormatException e) {
            showErrorMessage(null, "Nota trebuie sa fie numar!");
            handlehw();
        }

    }

    private void saveNota(Nota nota) {
        try {
            if (service.findOne(nota.getId()) != null) {
                showErrorMessage(null, "Aceasta tema a fost deja notata!");
                handleClear();
            } else {
                showMessageTaskEditDialog(nota);
                // handleClear();
            }
        } catch (ValidationException e) {
            showErrorMessage(null, e.getMessage());
        }

    }

    @FXML
    public void checkMotivari() {
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        LocalDate data = datePicker.getValue();
        //String tema = nrtemaComboBox.getSelectionModel().getSelectedItem().toString();
        String tema = temaLabel.getText();
        StructuraSemestru str = new StructuraSemestru();
        int temaID = Integer.parseInt(tema.split(" ")[1].split("-")[0]);
        Tema t = service.findTema(temaID);
        try {
            if (service.getPenality(t, data) == 1) {
                if (t.getDeadlineweek() >= str.getSemestru(start).getSaptamana() && t.getDeadlineweek() >= str.getSemestru(end).getSaptamana()
                        && t.getStartWeek() <= str.getSemestru(start).getSaptamana() && t.getStartWeek() <= str.getSemestru(end).getSaptamana()) {
                    feedbackTextField.setText("");
                    if ((Integer.parseInt(notaTextField.getText()) + 1) > 10) {
                        notaTextField.setText("10");
                    } else {
                        notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) + 1));
                    }
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Motivare", "Motivare valida!!");
                } else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Motivare", "Motivare invalida!!!!!");

            }
            if (service.getPenality(t, data) == 2) {
                if (t.getDeadlineweek() >= str.getSemestru(start).getSaptamana() && t.getDeadlineweek() + 1 >= str.getSemestru(end).getSaptamana()
                        && t.getStartWeek() <= str.getSemestru(start).getSaptamana() && t.getStartWeek() < str.getSemestru(end).getSaptamana()
                        && str.getSemestru(start).getSaptamana() != str.getSemestru(end).getSaptamana()) {
                    feedbackTextField.setText("");
                    if ((Integer.parseInt(notaTextField.getText()) + 2) > 10) {
                        notaTextField.setText("10");
                    } else {
                        notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) + 2));
                    }
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Motivare", "Motivare valida!!");
                } else if (t.getDeadlineweek() + 1 >= str.getSemestru(start).getSaptamana() && t.getDeadlineweek() + 1 >= str.getSemestru(end).getSaptamana()
                        && t.getStartWeek() <= str.getSemestru(start).getSaptamana() && t.getStartWeek() <= str.getSemestru(end).getSaptamana()
                        && str.getSemestru(start).getSaptamana() == str.getSemestru(end).getSaptamana()) {
                    feedbackTextField.setText("Ai intarziat o saptamana! -1 punct!");
                    if ((Integer.parseInt(notaTextField.getText()) + 1) > 10) {
                        notaTextField.setText("10");
                    } else {
                        notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) + 1));
                    }
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Motivare", "Motivare valida!!");
                } else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Motivare", "Motivare invalida!!!!!");
            }
        }
        catch (NullPointerException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Motivare", "Motivare invalida!! Date din vacanta!");
        }
    }

    @FXML
    public void checkHw() {
        if (!nrtemaComboBox.getSelectionModel().isEmpty()){
        temaLabel.setText(nrtemaComboBox.getSelectionModel().getSelectedItem().toString());
        feedbackTextField.setText("");
        checkDate();}
    }

    @FXML
    public void checkDate() {

        notaTextField.setEditable(true);
        end.setVisible(false);
        startDate.setVisible(false);
        startDate.getEditor().clear();
        endDate.getEditor().clear();
        endDate.setVisible(false);
        start.setVisible(false);
        notaTextField.setText("10");
        if (!temaLabel.getText().isEmpty()) {
            feedbackTextField.setText("");
            String tema = temaLabel.getText();
            LocalDate data = datePicker.getValue();
            int temaID = Integer.parseInt(tema.split(" ")[1].split("-")[0]);
            Tema t = service.findTema(temaID);
            int penality = service.getPenality(t, data);
            if (penality == -100){
                //datePicker.setValue(LocalDate.now());
                notaTextField.setText("0");
                showErrorMessage(null, "Alegeti o data din timpul semestrului!");}
            if (penality == 1) {
                startDate.setVisible(true);
                endDate.setVisible(true);
                end.setVisible(true);
                start.setVisible(true);
                notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) - 1));
                feedbackTextField.setText("Ai intarziat o saptamana! -1 punct la nota! \n");
            } else if (penality == 2) {
                startDate.setVisible(true);
                endDate.setVisible(true);
                end.setVisible(true);
                start.setVisible(true);
                notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) - 2));
                feedbackTextField.setText("Ai intarziat o saptamana! -2 puncte la nota! \n");
            } else if (penality == -1) {
                notaTextField.setText("1");
                feedbackTextField.setText("Ai depasit termenul de predare si ai primit nota 1! \n");
                notaTextField.setEditable(false);
            }
        }
        else{
            notaTextField.setText("10");
            String tema = getClosestHw();
            LocalDate data = datePicker.getValue();
            int temaID = Integer.parseInt(tema.split(" ")[1].split("-")[0]);
            Tema t = service.findTema(temaID);
            int penality = service.getPenality(t, data);
            if (penality == 1) {
                startDate.setVisible(true);
                endDate.setVisible(true);
                end.setVisible(true);
                start.setVisible(true);
                notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) - 1));
                feedbackTextField.setText("Ai intarziat o saptamana! -1 punct la nota! \n");
            } else if (penality == 2) {
                startDate.setVisible(true);
                endDate.setVisible(true);
                end.setVisible(true);
                start.setVisible(true);
                notaTextField.setText(Integer.toString(Integer.parseInt(notaTextField.getText()) - 2));
                feedbackTextField.setText("Ai intarziat o saptamana! -2 puncte la nota! \n");
            } else if (penality == -1) {
                notaTextField.setText("1");
                feedbackTextField.setText("Ai depasit termenul de predare si ai primit nota 1! \n");
                notaTextField.setEditable(false);
            }
        }
    }
    @FXML
    public void handleClear(){
        notaTextField.setText("");
        feedbackTextField.setText("");
        nrtemaComboBox.setValue(getClosestHw());
        notaTextField.setText("10");
        datePicker.setValue(LocalDate.now());
        end.setVisible(false);
        startDate.setVisible(false);
        endDate.setVisible(false);
        start.setVisible(false);
        studentsComboBox.getSelectionModel().clearSelection();
//        profComboBox.getSelectionModel().clearSelection();
        startDate.getEditor().clear();
        endDate.getEditor().clear();
        nrtemaComboBox.setValue(getClosestHw());
        temaLabel.setText(getClosestHw());
        studentLabel.setText("");

    }

    @FXML
    public void handlePDF() {
        service.saveRaportInPdf(numeProf);
        if (Desktop.isDesktopSupported()) {
            try {
                String path="C:\\Users\\eliza\\IdeaProjects\\Proiect_map\\src\\com\\company\\resources\\PDFs\\RaportsPDF"+numeProf+".pdf";
                File myFile = new File(path);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
            }
        }
    }

    @FXML
    public void handlehw(){
        Iterable<Tema> note1 = temaService.findAll();
        List<Tema> note = new ArrayList<Tema>();
        Iterable<Tema> n = temaService.findAll();
        n.forEach(note::add);
        if (!studentsComboBox.getSelectionModel().isEmpty()){
            checkDate();
            studentLabel.setText(studentsComboBox.getSelectionModel().getSelectedItem().toString());
            String nume = studentLabel.getText();
            String[] numestud=nume.split(" ");
            Student st=studentService.findByname(numestud[0],numestud[1]);

            Iterable<Nota> lnote1=service.findAll();
            List<Tema> lteme= new ArrayList<Tema>();;
            lnote1.forEach(x->{if (x.getStudent().getId()==st.getId()){
                lteme.add(x.getTema());}});
            List<Tema> finall=new ArrayList<>();
            for (Tema t:note){
                finall.add(t);
            }
            for(Tema te:note)
                for (Tema temuta:lteme)
                    if (te.getId()==temuta.getId())
                        finall.remove(te);
            int min=100;
            String tema="";
            for (Tema ni: finall) {
                if (Math.abs(ni.getDeadlineweek()-structuraSemestru.getSemestru(LocalDate.now()).getSaptamana())<min){
                    min=Math.abs(ni.getDeadlineweek()-structuraSemestru.getSemestru(LocalDate.now()).getSaptamana());
                    //System.out.println(min);
                    tema="Tema "+Integer.toString(ni.getId())+"-Deadline sapt "+ni.getDeadlineweek()+"Sem. "+ni.getSem();
                   // System.out.println(tema);
                }
            }
            nrtemaComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    nrtemaComboBox.getItems().setAll(finall.stream().collect(Collectors.groupingBy(x -> x.getNRTema())).keySet());
                }
            });
            temaLabel.setText(tema);
            nrtemaComboBox.setValue(temaLabel.getText());
        }
        else if (!studentLabel.getText().isEmpty()){
            String nume = studentLabel.getText();
            String[] numestud=nume.split(" ");
            Student st=studentService.findByname(numestud[0],numestud[1]);

            Iterable<Nota> lnote1=service.findAll();
            List<Tema> lteme= new ArrayList<Tema>();;
            lnote1.forEach(x->{if (x.getStudent().getId()==st.getId()){
                lteme.add(x.getTema());}});
            List<Tema> finall=new ArrayList<>();
            for (Tema t:note){
                finall.add(t);
            }
            for(Tema te:note)
                for (Tema temuta:lteme)
                    if (te.getId()==temuta.getId())
                        finall.remove(te);
            int min=100;
            String tema="";
            for (Tema ni: finall) {
                if (Math.abs(ni.getDeadlineweek()-structuraSemestru.getSemestru(LocalDate.now()).getSaptamana())<min){
                    min=Math.abs(ni.getDeadlineweek()-structuraSemestru.getSemestru(LocalDate.now()).getSaptamana());
                    tema="Tema "+Integer.toString(ni.getId())+"-Deadline sapt "+ni.getDeadlineweek()+"Sem. "+ni.getSem();

                }
            }
            nrtemaComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    nrtemaComboBox.getItems().setAll(finall.stream().collect(Collectors.groupingBy(x -> x.getNRTema())).keySet());
                }
            }
            );
            temaLabel.setText(tema);
            nrtemaComboBox.setValue(temaLabel.getText());
        }
        else{
            temaLabel.setText(getClosestHw());
            nrtemaComboBox.setValue(temaLabel.getText());
        }

    }

    public void logOutAction() throws IOException {
        Stage currentStage=(Stage)start.getScene().getWindow();
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

    @Override
    public void update(NotaChangeEvent notaChangeEvent) {
        modelGrade.setAll(getNotaDTOList());
    }
}
