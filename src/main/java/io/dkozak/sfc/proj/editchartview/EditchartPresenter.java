package io.dkozak.sfc.proj.editchartview;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.services.FuzzySetService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.utils.DataFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;

import javax.inject.Inject;

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

    @Inject
    private EventBus eventBus;

    @Inject
    private FuzzySetService fuzzySetService;


    @FXML
    public void gausianReplace(ActionEvent event) {
        renderGausian(true);
    }

    @FXML
    public void gausianAppend(ActionEvent event) {
        renderGausian(false);
    }

    private void renderGausian(boolean clearViewBefore) {
        try {
            double mi = Double.parseDouble(gausianMi.getText());
            double sigma = Double.parseDouble(gausianSigma.getText());

            DataFunction gaussian = DataFunction.gaussian(mi, sigma);
            if (clearViewBefore)
                controlledChart.getData()
                               .clear();
            gaussian.visualizeData(controlledChart, String.format("Gausian(%.2f,%.2f)", mi, sigma));
            eventBus.unicast("appView", "info", "Gausian graph plotted");
            fuzzySetService.addSet(new FuzzySet(gaussian));
        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }


    private void sendInvalidNumberFormatMessage() {
        eventBus.unicast("appView", "error", "Invalid number format, please correct it");
    }

    @FXML
    public void triangleReplace(ActionEvent event) {
        renderTriangle(true);
    }

    @FXML
    public void TriangleAppend(ActionEvent event) {
        renderTriangle(false);
    }

    private void renderTriangle(boolean clearViewBefore) {
        try {
            double a = Double.parseDouble(triangleA.getText());
            double b = Double.parseDouble(triangleB.getText());
            double c = Double.parseDouble(triangleC.getText());

            DataFunction triangle = DataFunction.triangle(a, b, c);
            if (clearViewBefore)
                controlledChart.getData()
                               .clear();
            triangle.visualizeData(controlledChart, String.format("Triangle(%.2f,%.2f,%.2f)", a, b, c));
            eventBus.unicast("appView", "info", "Triangle graph plotted");
            fuzzySetService.addSet(new FuzzySet(triangle));
        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }

    @FXML
    public void trapezoidReplace(ActionEvent event) {
        renderTrapezoid(true);
    }

    public void trapezoidAppend(ActionEvent event) {
        renderTrapezoid(false);
    }

    private void renderTrapezoid(boolean clearViewBefore) {
        try {
            double a = Double.parseDouble(trapezoidA.getText());
            double b = Double.parseDouble(trapezoidB.getText());
            double c = Double.parseDouble(trapezoidC.getText());
            double d = Double.parseDouble(trapezoidD.getText());

            DataFunction trapezoid = DataFunction.trapezoid(a, b, c, d);
            if (clearViewBefore)
                controlledChart.getData()
                               .clear();

            fuzzySetService.addSet(new FuzzySet(trapezoid));
            trapezoid.visualizeData(controlledChart, String.format("Trapezoid(%.2f,%.2f,%.2f,%.2f)", a, b, c, d));
            eventBus.unicast("appView", "info", "Trapezoid graph plotted");
        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }

    public LineChart<Number, Number> getControlledChart() {
        return controlledChart;
    }

    public void setControlledChart(LineChart<Number, Number> controlledChart) {
        this.controlledChart = controlledChart;
    }
}

