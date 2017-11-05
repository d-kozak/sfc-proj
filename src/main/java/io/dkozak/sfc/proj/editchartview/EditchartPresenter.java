package io.dkozak.sfc.proj.editchartview;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.fuzzy.MemberFunction;
import io.dkozak.sfc.proj.services.FuzzySetService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import io.dkozak.sfc.proj.utils.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;
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

    @FXML
    public ChoiceBox<String> removeSetChoiceBox;

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
    public void gaussianAdd(ActionEvent event) {
        try {
            String name = getFuzzySetName();
            if (name.isEmpty())
                return;

            double mi = Double.parseDouble(gaussianMi.getText());
            double sigma = Double.parseDouble(gaussianSigma.getText());

            MemberFunction gaussian = MemberFunction.gaussian(mi, sigma);
            finishRendering(name, gaussian);

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
    public void triangleAdd(ActionEvent event) {
        try {
            String name = getFuzzySetName();
            if (name.isEmpty())
                return;

            double a = Double.parseDouble(triangleA.getText());
            double b = Double.parseDouble(triangleB.getText());
            double c = Double.parseDouble(triangleC.getText());

            MemberFunction triangle = MemberFunction.triangle(a, b, c);
            finishRendering(name, triangle);

        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }

    @FXML
    public void trapezoidAdd(ActionEvent event) {
        try {
            String name = getFuzzySetName();
            if (name.isEmpty())
                return;

            double a = Double.parseDouble(trapezoidA.getText());
            double b = Double.parseDouble(trapezoidB.getText());
            double c = Double.parseDouble(trapezoidC.getText());
            double d = Double.parseDouble(trapezoidD.getText());

            MemberFunction trapezoid = MemberFunction.trapezoid(a, b, c, d);
            finishRendering(name, trapezoid);

        } catch (NumberFormatException ex) {
            sendInvalidNumberFormatMessage();
        }
    }

    public void setControlledChart(LineChart<Number, Number> controlledChart) {
        this.controlledChart = controlledChart;
    }

    private void finishRendering(String name, MemberFunction function) {
        FuzzySet fuzzySet = new FuzzySet(name, function);
        fuzzySetService.addSet(fuzzySet);
        fuzzySet.visualizeOn(controlledChart);

        removeSetChoiceBox.getItems()
                          .add(name);
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

    @FXML
    public void onRemoveSet(ActionEvent event) {
        String fuzzySetName = removeSetChoiceBox.getValue();
        if (fuzzySetName == null || fuzzySetName.isEmpty()) {
            eventBus.unicast("appView", "error", "Please select set to remove");
            return;
        }


        if (!controlledChart.getData()
                            .removeIf(series -> fuzzySetName.equals(series.getName()))) {
            throw new RuntimeException("No set removed");
        }
        fuzzySetService.removeSet(fuzzySetName);
        removeSetChoiceBox.getItems()
                          .remove(fuzzySetName);
        eventBus.unicast("appView", "info", "Set " + fuzzySetName + " was removed successfully");
    }
}

