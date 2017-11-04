package io.dkozak.sfc.proj.editchartview;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.services.FuzzySetService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import io.dkozak.sfc.proj.utils.DataFunction;
import io.dkozak.sfc.proj.utils.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class EditchartPresenter implements Initializable, EventBusListener {

    private Logger logger = Logger.logger(this.getClass());

    @FXML
    private TextField triangleC;

    @FXML
    private TextField triangleB;

    @FXML
    private TextField triangleA;

    @FXML
    private TextField gaussianSigma;

    @FXML
    private TextField trapezoidA;


    @FXML
    private TextField trapezoidC;

    @FXML
    private TextField trapezoidB;

    @FXML
    private TextField gaussianMi;

    @FXML
    private TextField trapezoidD;

    @FXML
    private TextField nameField;

    private LineChart<Number, Number> controlledChart;

    @Inject
    private EventBus eventBus;

    @Inject
    private FuzzySetService fuzzySetService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register("editChartView" + (new Random().nextInt()), this);
    }

    @FXML
    public void gaussianReplace(ActionEvent event) {
        renderGaussian(true);
    }

    @FXML
    public void gaussianAppend(ActionEvent event) {
        renderGaussian(false);
    }

    private void renderGaussian(boolean clearViewBefore) {
        try {
            String name = getFuzzySetName();
            if (name.isEmpty())
                return;

            double mi = Double.parseDouble(gaussianMi.getText());
            double sigma = Double.parseDouble(gaussianSigma.getText());

            DataFunction gaussian = DataFunction.gaussian(mi, sigma);
            finishRendering(name, gaussian, clearViewBefore);

        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }

    private String getFuzzySetName() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            eventBus.unicast("appView", "error", "Please fill in the name of the set");
            return "";
        }
        return name;
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
            String name = getFuzzySetName();
            if (name.isEmpty())
                return;

            double a = Double.parseDouble(triangleA.getText());
            double b = Double.parseDouble(triangleB.getText());
            double c = Double.parseDouble(triangleC.getText());

            DataFunction triangle = DataFunction.triangle(a, b, c);
            finishRendering(name, triangle, clearViewBefore);

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
            String name = getFuzzySetName();
            if (name.isEmpty())
                return;

            double a = Double.parseDouble(trapezoidA.getText());
            double b = Double.parseDouble(trapezoidB.getText());
            double c = Double.parseDouble(trapezoidC.getText());
            double d = Double.parseDouble(trapezoidD.getText());

            DataFunction trapezoid = DataFunction.trapezoid(a, b, c, d);
            finishRendering(name, trapezoid, clearViewBefore);

        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }

    public void setControlledChart(LineChart<Number, Number> controlledChart) {
        this.controlledChart = controlledChart;
    }

    private void finishRendering(String name, DataFunction function, boolean clearViewBefore) {
        if (clearViewBefore)
            controlledChart.getData()
                           .clear();

        FuzzySet fuzzySet = new FuzzySet(name, function);
        fuzzySetService.addSet(fuzzySet);
        fuzzySet.visualize(controlledChart);

        eventBus.broadcast("newSet", fuzzySet);
        eventBus.unicast("appView", "info", name + " plotted");
        clearForm();
    }

    @Override
    public void onMessage(String messageID, Object content) {
        if ("clear".equals(messageID)) {
            controlledChart.getData()
                           .clear();
            clearForm();


        } else {
            logger.log("Unknown message " + messageID);
        }
    }

    private void clearForm() {
        nameField.clear();

        trapezoidA.clear();
        trapezoidB.clear();
        trapezoidC.clear();
        trapezoidD.clear();

        triangleA.clear();
        triangleB.clear();
        triangleC.clear();

        gaussianMi.clear();
        gaussianSigma.clear();
    }
}

