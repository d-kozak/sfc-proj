package io.dkozak.sfc.proj.components.result;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.fuzzy.MemberFunction;
import io.dkozak.sfc.proj.services.InferenceResultService;
import io.dkozak.sfc.proj.services.SettingsService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static io.dkozak.sfc.proj.utils.Utils.bindChartLimits;
import static io.dkozak.sfc.proj.utils.Utils.showSetInAChart;

public class ResultPresenter implements Initializable, EventBusListener {

    @FXML
    private LineChart<Number, Number> resultChart;

    @Inject
    private InferenceResultService inferenceResultService;

    @Inject
    private EventBus eventBus;

    @Inject
    private SettingsService settingsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register("resultView", this);
        bindChartLimits(resultChart, settingsService);
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
        showSetInAChart(result, resultChart, settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX());

    }

    @Override
    public void onMessage(String messageID, Object content) {
        switch (messageID) {
            case "clear":
                clearView();
                break;
            default:
                System.err.println("Unknown message");
        }
    }

    private void clearView() {
        resultChart.getData()
                   .clear();
    }

    public LineChart<Number, Number> getResultChart() {
        return resultChart;
    }
}
