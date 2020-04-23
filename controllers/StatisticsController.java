package myproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import myproject.service.NotaService;
import myproject.utils.events.NotaChangeEvent;
import myproject.utils.observer.Observer;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StatisticsController implements Observer<NotaChangeEvent> {
    private NotaService service;
    private String numeProf;
    public void setService(NotaService service,String numeProf){
        this.service = service;
        this.numeProf=numeProf;
        this.service.addObserver(this);
        setUpRaport1PieChart();
        setUpRaport4PieChart();
        setUpRaport3PieChart();
    }

    @FXML
    PieChart raport4PieChart;
    @FXML
    PieChart raport3PieChart;
    @FXML
    PieChart raport1PieChart;
    @FXML
    Label caption;
    @FXML
    Label l1;
    @FXML
    Label l2;
    @FXML
    Label l3;
    @FXML
    Label l4;
    @FXML
    Label l5;
    @FXML
    Label l6;
    @FXML
    Label l7;
    @FXML
    Label l8;
    @FXML
    Label l9;
    @FXML
    Label l10;

    @FXML
    private void initialize() {

    }


    private void setUpRaport1PieChart() {
        var list =service.get_average_for_students().stream().filter(n->n.getProfesor().compareTo(numeProf)==0).collect(Collectors.toList()) ;
        var numberOfLowStudents = StreamSupport.stream(list.spliterator(), false).filter(n -> n.getNota() < 5).count();
        var numberOfBestStudents=StreamSupport.stream(list.spliterator(), false).filter(n -> n.getNota() >=9).count();
        var numberOfAv1Students=StreamSupport.stream(list.spliterator(), false).filter(n -> n.getNota() <9 && n.getNota()>=8).count();
        var numberOfAv2Students=StreamSupport.stream(list.spliterator(), false).filter(n -> n.getNota() <8 && n.getNota()>=7).count();
        var numberOfAv3Students=StreamSupport.stream(list.spliterator(), false).filter(n -> n.getNota() <7 && n.getNota()>=6).count();
        var numberOfAv4Students=StreamSupport.stream(list.spliterator(), false).filter(n -> n.getNota() <6 && n.getNota()>=5).count();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Studenti cu nota finala intre 0-5", numberOfLowStudents),
                        new PieChart.Data("Studenti cu nota finala intre 5-6", numberOfAv4Students),
                        new PieChart.Data("Studenti cu nota finala intre 6-7", numberOfAv3Students),
                        new PieChart.Data("Studenti cu nota finala intre 7-8", numberOfAv2Students),
                        new PieChart.Data("Studenti cu nota finala intre 8-9", numberOfAv1Students),
                        new PieChart.Data("Studenti cu nota finala intre 9-10", numberOfBestStudents)
                );
        raport1PieChart.setData(pieChartData);
        raport1PieChart.setTitle("Statistica note finale studenti");
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: " + "#30122d");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + "#870734");
        pieChartData.get(2).getNode().setStyle("-fx-pie-color: " + "#cb2d3e;");
        pieChartData.get(3).getNode().setStyle("-fx-pie-color: " + "#ef473a");
        pieChartData.get(4).getNode().setStyle("-fx-pie-color: " + "#ffd6bf");
        pieChartData.get(5).getNode().setStyle("-fx-pie-color: " + "#FF0000");
        Set<Node> items = raport1PieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        Color[] colors = { Color.web("#82FA58"), Color.web("yellow"), Color.web("#FFBF00"), Color.web("#80FF00"), Color.web("#F7FE2E"),Color.web("#FF0000") };
        for (Node item : items) {
            Label label = (Label) item;
            final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
            final Glow niceEffect = new Glow();
            niceEffect.setInput(new Reflection());
            rectangle.setEffect(niceEffect);
            label.setGraphic(rectangle);
            i++;
        }
        for (final PieChart.Data data : raport1PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(Math.round(data.getPieValue())));
                            caption.setVisible(true);
                        }
                    });
        }
        l1.setText("Studenti cu nota finala intre 9-10: "+ numberOfBestStudents);
        l2.setText("Studenti cu nota finala intre 8-9: "+ numberOfAv1Students);
        l7.setText("Studenti cu nota finala intre 7-8: "+ numberOfAv2Students);
        l8.setText("Studenti cu nota finala intre 6-7: "+ numberOfAv3Students);
        l9.setText("Studenti cu nota finala intre 5-6: "+ numberOfAv4Students);
        l10.setText("Studenti cu nota finala intre 0-5: "+ numberOfLowStudents);
    }

    private void setUpRaport3PieChart() {
        long numberOfPromotedStudents = service.get_students_promoted().stream().filter(n->n.getProfesor().compareTo(numeProf)==0).collect(Collectors.toList()).spliterator().getExactSizeIfKnown();
        long numberOfStudents=service.get_average_for_students().stream().filter(n->n.getProfesor().compareTo(numeProf)==0).collect(Collectors.toList()).spliterator().getExactSizeIfKnown();
        long numberOfStudentsNotPassed=numberOfStudents-numberOfPromotedStudents;
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Studenti care pot intra in examen",numberOfPromotedStudents),
                        new PieChart.Data("Studenti care nu pot intra in examen", numberOfStudentsNotPassed)
                );
        raport3PieChart.setData(pieChartData);
        raport3PieChart.setTitle("Statistica studenti acceptati in sesiune");
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: " + "yellow");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + "#40FF00");
        Set<Node> items = raport3PieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        Color[] colors = { Color.web("#FF0040"), Color.web("#F7819F"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f") };
        for (Node item : items) {
            Label label = (Label) item;
            final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
            final Glow niceEffect = new Glow();
            niceEffect.setInput(new Reflection());
            rectangle.setEffect(niceEffect);
            label.setGraphic(rectangle);
            i++;
        }
        for (final PieChart.Data data : raport3PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(Math.round(data.getPieValue())));
                            caption.setVisible(true);
                        }
                    });
        }
        l3.setText("Numar studenti care pot intra in examen: "+ numberOfPromotedStudents);
        l4.setText("Numar studenti care nu pot intra in examen: "+ numberOfStudentsNotPassed);
    }

    private void setUpRaport4PieChart() {
        var numberOfStudentsWithoutDelay = StreamSupport.stream(service.get_students_with_all_assignments()
                .stream().filter(n->n.getProfesor().compareTo(numeProf)==0).collect(Collectors.toList()).spliterator(), false).count();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Studenti care au predat toate temele la timp", numberOfStudentsWithoutDelay),
                        new PieChart.Data("Studenti care nu au predat toate temele la timp", StreamSupport.stream(service.get_average_for_students()
                                .stream().filter(n->n.getProfesor().compareTo(numeProf)==0).collect(Collectors.toList())
                                .spliterator(), false).count() - numberOfStudentsWithoutDelay)
                );
        raport4PieChart.setData(pieChartData);
        raport4PieChart.setTitle("Statistica intarzieri la predare");
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: " + "#CEECF5;");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + "#58D3F7");
        Set<Node> items = raport4PieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        Color[] colors = { Color.web("#FF0040"), Color.web("#F7819F"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f") };
        for (Node item : items) {
            Label label = (Label) item;
            final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
            final Glow niceEffect = new Glow();
            niceEffect.setInput(new Reflection());
            rectangle.setEffect(niceEffect);
            label.setGraphic(rectangle);
            i++;
        }
        for (final PieChart.Data data : raport4PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(Math.round(data.getPieValue())));
                            caption.setVisible(true);
                        }
                    });
        }
        var numberOfStudentsWithDelay=StreamSupport.stream(service.get_average_for_students()
                .stream().filter(n->n.getProfesor().compareTo(numeProf)==0).collect(Collectors.toList())
                .spliterator(), false).count() - numberOfStudentsWithoutDelay;
        l5.setText("Numar studenti care au predat toate temele la timp: "+ numberOfStudentsWithoutDelay);
        l6.setText("Numar studenti care au predat toate temele la timp: "+ numberOfStudentsWithDelay);
    }

    @Override
    public void update(NotaChangeEvent notaChangeEvent) {
        setUpRaport1PieChart();
        setUpRaport3PieChart();
        setUpRaport4PieChart();
    }
}
