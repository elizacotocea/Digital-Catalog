package myproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myproject.entities.Student;
import myproject.service.LogInService;
import myproject.service.NotaService;
import myproject.service.StudentService;
import myproject.service.TemaService;
import myproject.utils.events.StudentChangeEvent;
import myproject.utils.observer.Observer;
import myproject.validator.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static myproject.controllers.MessageAlert.showErrorMessage;

public class StudentController implements Observer<StudentChangeEvent> {
    ObservableList<Student> modelGrade = FXCollections.observableArrayList();
    private StudentService service;
    private NotaService notaService;
    private TemaService temaService;
    private NotaController notaController;

    @FXML
    ToggleButton notaButton;
    @FXML
    AnchorPane rootPane;
    @FXML
    TextField textFilterNume;
    @FXML
    TextField textFilterPrenume;
    @FXML
    Button updateButton;
    @FXML
    Button deleteButton;
    @FXML
    Button logOutButton;
    @FXML
    TextField textFieldID;
    @FXML
    TextField textFieldNume;
    @FXML
    TextField textFieldPrenume;
    @FXML
    TextField textFieldEmail;
    @FXML
    TableColumn<Student, String> numeColumn;
    @FXML
    TableColumn<Student, String> prenumeColumn;
    @FXML
    TableColumn<Student, Integer> idColumn;
    @FXML
    TableColumn<Student, String> emailColumn;
    @FXML
    TableColumn<Student, String> profColumn;
    @FXML
    TableColumn<Student, String> grupaColumn;
    @FXML
    TableView<Student> tableViewStudents;
    @FXML
    ComboBox<String> grupaComboBox;
    @FXML
    ComboBox<String> profComboBox;


    public void initialize() {
        numeColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        prenumeColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("prenume"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        profColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidacticIndrumatorLab"));
        grupaColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        tableViewStudents.setItems(modelGrade);

        tableViewStudents.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals(updateButton.isPressed()) && newVal.equals(deleteButton.isPressed())) {
                tableViewStudents.getSelectionModel().clearSelection();
            }
        });

        profComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                profComboBox.getItems().setAll(getProf());
            }
        });
        grupaComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                grupaComboBox.getItems().setAll(getGroups());
            }
        });
        //.textProperty().addListener((x,y,z)->handleFilter());
        textFilterNume.textProperty().addListener((x,y,z)->handleFilter());
       // textFilterPrenume.textProperty().addListener((x,y,z)->handleFilter());
       // grupaComboBox.getEditor().textProperty().addListener((x,y,z)->handleFilter());
       // profComboBox.getEditor().textProperty().addListener((x,y,z)->handleFilter());
        //textFieldEmail.textProperty().addListener((x,y,z)->handleFilter());
    }

    private void handleFilter() {
            Predicate<Student> byName= x -> (x.getNume()+" "+x.getPrenume()).toUpperCase().contains(textFilterNume.getText().toUpperCase());
           // Predicate<Student> byName = x -> (x.getNume().startsWith(textFilterNume.getText()) || x.getPrenume().startsWith(textFilterNume.getText()));
//
//            Predicate<Student> byProfesor;
//            if (profComboBox.getSelectionModel().getSelectedItem() != null)
//                byProfesor = x -> x.getCadruDidacticIndrumatorLab().startsWith(profComboBox.getSelectionModel().getSelectedItem().toString());
//            byProfesor = x -> x.getCadruDidacticIndrumatorLab().startsWith(profComboBox.getEditor().getCharacters().toString());
//            Predicate<Student> byGrupa;
//            if (grupaComboBox.getSelectionModel().getSelectedItem() != null)
//                byGrupa = x -> x.getGrupa().startsWith(grupaComboBox.getSelectionModel().getSelectedItem().toString());
//            byGrupa = x -> x.getGrupa().startsWith(grupaComboBox.getEditor().getCharacters().toString());
            List<Student> note = getStudents();
            modelGrade.setAll((note.stream().filter(byName)).collect(Collectors.toList()));

    }

    public Set<String> getGroups() {
        List<Student> st = getStudents();
        return st.stream().collect(Collectors.groupingBy(x -> x.getGrupa())).keySet();
    }

    public Set<String> getProf() {
        List<Student> st = getStudents();
        return st.stream()
                .collect(Collectors.groupingBy(x -> x.getCadruDidacticIndrumatorLab())).keySet();
    }

    private List<Student> getStudents() {
        List<Student> lst = new ArrayList<Student>();
        Iterable<Student> students = service.findAll();
        students.forEach(lst::add);
        return lst;
    }

    public void setService(StudentService serv, NotaService notaService, TemaService temaService) {
        this.service = serv;
        this.notaService=notaService;
        this.temaService=temaService;
        modelGrade.setAll(getStudents());
        service.addObserver(this);

        //grupaComboBox.getSelectionModel().select(getGroups().iterator().next());
        //grupaComboBox.isEditable();


    }

    public void changeSceneToGrade(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/notaView.fxml"));
        AnchorPane pane = loader.load();
        notaController = loader.getController();
        notaController.setService(notaService,service,temaService,"x");
        rootPane.getChildren().setAll(pane);
    }

    public void populateCombos(){
        grupaComboBox.getItems().setAll(getGroups());
        profComboBox.getItems().setAll(getProf());
    }

    public void showMessageTaskEditDialog(Student student) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/editStudentView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditStudentController editStudentController = loader.getController();
            editStudentController.setService(service, dialogStage, student);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(StudentChangeEvent studentChangeEvent) {
        modelGrade.setAll(getStudents());    }

    @FXML
    public void handleUpdate() {
        if (tableViewStudents.getSelectionModel().isEmpty()) {
            showErrorMessage(null, "Nu ati ales niciun student!");
        } else {
            String item = tableViewStudents.getSelectionModel().getSelectedItem().toString();
            String[] items = item.split(";");
            try {
                Student s = new Student(Integer.parseInt(items[0]),items[1],items[2],items[3],items[4],items[5]);
                showMessageTaskEditDialog(s);

            } catch (NumberFormatException e) {
                showErrorMessage(null, e.getMessage());
            }
        }
        tableViewStudents.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleSave(){
            //String id = textFieldID.getText();
        try {
            String nume = textFieldNume.getText();
            String prenume = textFieldPrenume.getText();
            String email = textFieldEmail.getText();
            String profesor = profComboBox.getSelectionModel().getSelectedItem().toString();
            String grupa = grupaComboBox.getSelectionModel().getSelectedItem().toString();
            Student student = new Student(0, nume, prenume, email, profesor, grupa);
            saveStudent(student);
        }
        catch (NullPointerException e){
            showErrorMessage(null,"Introduceti toate datele!");
        }

    }

    private void saveStudent(Student s)
    {
        try {
            if (service.findByname(s.getNume(),s.getPrenume())!=null){
                showErrorMessage(null,"Acest student exista deja!");
                handleClear();
            }
            else {
                Student r = this.service.save(s);
                if (r == null)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare student", "Student a fost salvat");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare student", "Studentul nu a fost salvat! ID deja existent");
                handleClear();
            }
        } catch (ValidationException e) {
            showErrorMessage(null,e.getMessage());
        }

    }

    @FXML
    public void handleDelete(){
        if (tableViewStudents.getSelectionModel().isEmpty()){
            showErrorMessage(null,"Nu ati ales niciun student!");
        }
        else {
            String item = tableViewStudents.getSelectionModel().getSelectedItem().toString();
            String[] items = item.split(";");
            try {
                Student r = this.service.delete(Integer.parseInt(items[0]));
                if (r != null)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Stergere student", "Studentul a fost sters");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Stergere student", "Studentul nu a fost sters! ID inexistent");
            } catch (ValidationException e) {
                showErrorMessage(null, e.getMessage());
            }
        }
    }

    @FXML
    public void handleClear(){
        textFieldNume.setText("");
        textFieldPrenume.setText("");
        textFieldEmail.setText("");
        grupaComboBox.setValue("");
        profComboBox.setValue("");
    }

    public void handleMouseEvent(javafx.scene.input.MouseEvent mouseEvent) {
        tableViewStudents.getSelectionModel().clearSelection();
    }

    public void logOutAction() throws IOException {
        Stage currentStage=(Stage)textFieldEmail.getScene().getWindow();
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

