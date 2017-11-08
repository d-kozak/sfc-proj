package io.dkozak.sfc.proj.components.result;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.fuzzy.MemberFunction;
import io.dkozak.sfc.proj.services.InferenceResultService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class ResultPresenter implements Initializable {

    @FXML
    private LineChart<Number, Number> resultChart;

    @Inject
    private InferenceResultService inferenceResultService;

    @Inject
    private EventBus eventBus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onCompute(ActionEvent event) {
        resultChart.getData()
                   .clear();
        inferenceResultService.getResults()
                              .clear();

        eventBus.broadcast("compute");
        List<FuzzySet> results = inferenceResultService.getResults();
        if (results.isEmpty()) {
            throw new RuntimeException("Cannot compute, no partial results");
        }

        Function<Number, Number> resultingMemberFunction = input -> {
            double max = results.get(0)
                                .getMemberFunction()
                                .getFunction()
                                .apply(input)
                                .doubleValue();
            for (int i = 1; i < results.size(); i++) {
                double tmp = results.get(i)
                                    .getMemberFunction()
                                    .getFunction()
                                    .apply(input)
                                    .doubleValue();
                if (tmp > max) {
                    max = tmp;
                }
            }
            return max;
        };

        FuzzySet result = new FuzzySet("Inference result", new MemberFunction(MemberFunction.Type.UNKNOWN, resultingMemberFunction));
        result.visualizeOn(resultChart);

    }
}
