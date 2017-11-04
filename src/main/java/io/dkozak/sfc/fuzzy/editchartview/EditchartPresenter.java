package io.dkozak.sfc.fuzzy.editchartview;

import io.dkozak.sfc.fuzzy.function.DataFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;

public class EditchartPresenter {

    @FXML
    private TextField triangleC;

    @FXML
    private TextField triangleB;

    @FXML
    private TextField triangleA;

    @FXML
    private TextField gausianSigma;

    @FXML
    private TextField trapezoidA;


    @FXML
    private TextField trapezoidC;

    @FXML
    private TextField trapezoidB;

    @FXML
    private TextField gausianMi;

    @FXML
    private TextField trapezoidD;

    private LineChart<Number, Number> controlledChart;


    @FXML
    void gausianClicked(ActionEvent event) {
        try {
            double mi = Double.parseDouble(gausianMi.getText());
            double sigma = Double.parseDouble(gausianSigma.getText());

            DataFunction gausian = DataFunction.gausian(mi, sigma);
            controlledChart.getData()
                           .clear();
            gausian.visualizeData(controlledChart, "Gausian");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void triangleClicked(ActionEvent event) {
        try {
            double a = Double.parseDouble(triangleA.getText());
            double b = Double.parseDouble(triangleB.getText());
            double c = Double.parseDouble(triangleC.getText());

            DataFunction gausian = DataFunction.triangle(a, b, c);
            controlledChart.getData()
                           .clear();
            gausian.visualizeData(controlledChart, "Triangle");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void trapezoidClicked(ActionEvent event) {
        try {
            double a = Double.parseDouble(trapezoidA.getText());
            double b = Double.parseDouble(trapezoidB.getText());
            double c = Double.parseDouble(trapezoidC.getText());
            double d = Double.parseDouble(trapezoidD.getText());

            DataFunction gausian = DataFunction.trapezoid(a, b, c, d);
            controlledChart.getData()
                           .clear();
            gausian.visualizeData(controlledChart, "Trapezoid");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    public LineChart<Number, Number> getControlledChart() {
        return controlledChart;
    }

    public void setControlledChart(LineChart<Number, Number> controlledChart) {
        this.controlledChart = controlledChart;
    }
}

