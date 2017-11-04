package io.dkozak.sfc.proj.commandview;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.services.FuzzySetService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class CommandPresenter implements Initializable, EventBusListener {

    @Inject
    private FuzzySetService fuzzySetService;

    @Inject
    private EventBus eventBus;

    private LineChart<Number, Number> chart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register("commandView", this);
    }

    public void setChart(LineChart<Number, Number> chart) {
        this.chart = chart;
    }

    @FXML
    void showUnion(ActionEvent event) {
        List<FuzzySet> sets = prepareFuzzySetList();
        if (sets.isEmpty())
            return;

        FuzzySet buffer = sets.get(0);
        sets.remove(buffer);
        for (FuzzySet set : sets) {
            buffer = buffer.union(set);
        }

        chart.getData()
             .clear();
        buffer.visualize(chart, "Union");
    }

    @FXML
    void showIntersection(ActionEvent event) {
        List<FuzzySet> sets = prepareFuzzySetList();
        if (sets.isEmpty()) return;

        FuzzySet buffer = sets.get(0);
        sets.remove(buffer);
        for (FuzzySet set : sets) {
            buffer = buffer.intersect(set);
        }

        chart.getData()
             .clear();
        buffer.visualize(chart, "Intersection");
    }

    @FXML
    void showComplement(ActionEvent event) {
        List<FuzzySet> fuzzySets = fuzzySetService.getSets();
        if (fuzzySets.size() < 1) {
            eventBus.unicast("appView", "error", "At least one set needed");
            return;
        }

        FuzzySet complement = fuzzySets.get(0)
                                       .complement();

        chart.getData()
             .clear();
        complement.visualize(chart, "Complement");

    }


    private List<FuzzySet> prepareFuzzySetList() {
        List<FuzzySet> result = new ArrayList<>();
        List<FuzzySet> fromService = fuzzySetService.getSets();
        if (fromService.size() < 2) {
            eventBus.unicast("appView", "error", "At least two sets needed");
            return Collections.emptyList();
        }
        result.addAll(fromService);
        return result;
    }

    @Override
    public void onMessage(String messageID, Object content) {
        if ("clear".equals(messageID)) {
            chart.getData()
                 .clear();
        } else {
            System.err.println("Unknown message " + messageID);
        }
    }

    public void clearAll(ActionEvent event) {
        // I get a copy as well, se the cleaning is done in onMessage
        eventBus.broadcast("clear");
        eventBus.unicast("appView", "info", "All data cleared");
    }
}

