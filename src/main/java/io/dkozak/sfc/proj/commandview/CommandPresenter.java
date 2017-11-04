package io.dkozak.sfc.proj.commandview;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.services.FuzzySetService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import io.dkozak.sfc.proj.utils.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class CommandPresenter implements Initializable, EventBusListener {

    private Logger logger = Logger.logger(this.getClass());

    @FXML
    private ChoiceBox<String> set1ChoiceBox;

    @FXML
    private ChoiceBox<String> set2ChoiceBox;

    @Inject
    private FuzzySetService fuzzySetService;

    @Inject
    private EventBus eventBus;

    private LineChart<Number, Number> chart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set1ChoiceBox.getSelectionModel()
                     .selectedIndexProperty()
                     .addListener((observable, oldValue, newValue) -> {
                         String name = set1ChoiceBox.getItems()
                                                    .get(newValue.intValue());
                         eventBus.unicast("appView", "info", "Set 1 selected: " + name);
                     });
        set2ChoiceBox.getSelectionModel()
                     .selectedIndexProperty()
                     .addListener((observable, oldValue, newValue) -> {
                         String name = set2ChoiceBox.getItems()
                                                    .get(newValue.intValue());
                         eventBus.unicast("appView", "info", "Set 2 selected: " + name);
                     });
        set1ChoiceBox.setItems(FXCollections.observableArrayList());
        set2ChoiceBox.setItems(FXCollections.observableArrayList());

        eventBus.register("commandView", this);
    }

    public void setChart(LineChart<Number, Number> chart) {
        this.chart = chart;
    }

    @FXML
    void showUnion(ActionEvent event) {
        performOver2Sets(FuzzySet::union);
    }

    private void performOver2Sets(BiFunction<FuzzySet, FuzzySet, FuzzySet> operation) {
        FuzzySet set1 = getFuzzySetFrom(set1ChoiceBox);
        FuzzySet set2 = getFuzzySetFrom(set2ChoiceBox);
        if (FuzzySet.NULL.equals(set1)) {
            eventBus.unicast("appView", "error", "Please specify set 1");
            return;
        } else if (FuzzySet.NULL.equals(set2)) {
            eventBus.unicast("appView", "error", "Please specify set 2");
            return;
        }

        FuzzySet result = operation.apply(set1, set2);

        chart.getData()
             .clear();
        result.visualize(chart);

        eventBus.unicast("appView", "info", result.getName() + " plotted");
    }

    @FXML
    public void showIntersection(ActionEvent event) {
        performOver2Sets(FuzzySet::intersect);
    }

    @FXML
    void showComplement(ActionEvent event) {

        FuzzySet set1 = getFuzzySetFrom(set1ChoiceBox);
        if (FuzzySet.NULL.equals(set1)) {
            eventBus.unicast("appView", "error", "Please specify set 1");
            return;
        }
        FuzzySet complement = set1.complement();

        chart.getData()
             .clear();
        complement.visualize(chart);
        eventBus.unicast("appView", "info", complement.getName() + " plotted");
    }

    private FuzzySet getFuzzySetFrom(ChoiceBox<String> choiceBox) {
        String name = choiceBox.getValue();
        return fuzzySetService.getSet(name);
    }

    @Override
    public void onMessage(String messageID, Object content) {
        if ("clear".equals(messageID)) {
            chart.getData()
                 .clear();
            set1ChoiceBox.getItems()
                         .clear();
            set2ChoiceBox.getItems()
                         .clear();
        } else if ("newSet".equals(messageID)) {
            FuzzySet newSet = (FuzzySet) content;
            String name = newSet.getName();
            set1ChoiceBox.getItems()
                         .add(name);
            set2ChoiceBox.getItems()
                         .add(name);

        } else {
            logger.log("Unknown message " + messageID);
        }
    }
}

