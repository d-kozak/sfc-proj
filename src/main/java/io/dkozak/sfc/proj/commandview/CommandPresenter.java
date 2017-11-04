package io.dkozak.sfc.proj.commandview;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.services.FuzzySetService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPresenter {

    @Inject
    private FuzzySetService fuzzySetService;

    @Inject
    private EventBus eventBus;

    private LineChart<Number, Number> chart;

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
        buffer.visualize(chart, "Intersect");
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
}

