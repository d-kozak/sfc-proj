package io.dkozak.sfc.proj.components.chartline;

import io.dkozak.sfc.proj.components.setdetails.SetDetailsView;
import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.fuzzy.MemberFunction;
import io.dkozak.sfc.proj.services.EditedFuzzySetService;
import io.dkozak.sfc.proj.services.InferenceResultService;
import io.dkozak.sfc.proj.services.SettingsService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import io.dkozak.sfc.proj.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static io.dkozak.sfc.proj.utils.Utils.bindChartLimits;
import static io.dkozak.sfc.proj.utils.Utils.showSetInAChart;

public class ChartLinePresenter implements EventBusListener, Initializable {
    @FXML
    private LineChart<Number, Number> chartRight;
    @FXML
    private LineChart<Number, Number> chartMiddle;
    @FXML
    private LineChart<Number, Number> chartLeft;

    @Inject
    private Stage mainStage;

    @Inject
    private EditedFuzzySetService editedFuzzySetService;

    @Inject
    private InferenceResultService inferenceResultService;

    @Inject
    private EventBus eventBus;

    @Inject
    private SettingsService settingsService;

    private Map<String, FuzzySet> sets = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register("chartLinePresenter " + (new Random().nextInt()), this);

        bindChartLimits(chartLeft, settingsService);
        bindChartLimits(chartMiddle, settingsService);
        bindChartLimits(chartRight, settingsService);
    }


    @FXML
    public void onAntecedent1(ActionEvent event) {
        getEditedSetAndShow("Antecedent 1", chartLeft);
    }

    @FXML
    public void onFact1(ActionEvent event) {
        getEditedSetAndShow("Fact 1", chartLeft);

    }

    @FXML
    public void onAntecedent2(ActionEvent event) {
        getEditedSetAndShow("Antecedent 2", chartMiddle);
    }

    @FXML
    public void onFact2(ActionEvent event) {
        getEditedSetAndShow("Fact 2", chartMiddle);

    }

    @FXML
    public void onConsequent(ActionEvent event) {
        getEditedSetAndShow("Consequent", chartRight);
    }

    private void getEditedSetAndShow(String name, LineChart<Number, Number> chart) {
        Utils.openModalDialog(mainStage, name, new SetDetailsView());
        FuzzySet set = editedFuzzySetService.getProperty();
        sets.put(name, set);
        editedFuzzySetService.unsetProperty();
        set.setName(name);
        showSetInAChart(set, chart, settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX());
    }

    @Override
    public void onMessage(String messageID, Object content) {
        switch (messageID) {
            case "compute":
                removeLastResults();
                computeInference();
                break;
            default:
                System.err.println("Unknown message " + messageID);
        }
    }

    private void removeLastResults() {
        chartLeft.getData()
                 .removeIf(series -> "Min(a1,f1)".equals(series.getName()));
        chartMiddle.getData()
                   .removeIf(series -> "Min(a2,f2)".equals(series.getName()));
        chartRight.getData()
                  .removeIf(series -> "Inference for this line".equals(series.getName()));
    }

    private void computeInference() {
        try {
            FuzzySet antecedent1 = sets.get("Antecedent 1");
            Objects.requireNonNull(antecedent1);
            FuzzySet fact1 = sets.get("Fact 1");
            Objects.requireNonNull(fact1);

            FuzzySet minLeft = antecedent1.intersect(fact1), minRight = null;
            minLeft.setName("Min(a1,f1)");
            showSetInAChart(minLeft, chartLeft, settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX());

            FuzzySet antecedent2 = sets.get("Antecedent 2");
            FuzzySet fact2 = sets.get("Fact 2");
            if (antecedent2 != null && fact2 != null) {
                minRight = antecedent2.intersect(fact2);
                minRight.setName("Min(a2,f2)");
                showSetInAChart(minRight, chartMiddle, settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX());
            }

            double maxValLeft = minLeft.getMaxValueFromInterval(settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX());
            if (minRight != null) {
                maxValLeft = Math.min(maxValLeft, minRight.getMaxValueFromInterval(settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX()));
            }

            FuzzySet consequent = sets.get("Consequent");
            Objects.requireNonNull(consequent);

            Function<Number, Number> consequentMemberFunction = consequent.getMemberFunction()
                                                                          .getFunction();

            final double finalMaxVal = maxValLeft;
            Function<Number, Number> resultMemberFunction = x -> Math.min(consequentMemberFunction.apply(x)
                                                                                                  .doubleValue(), finalMaxVal);

            FuzzySet result = new FuzzySet("Inference for this line", new MemberFunction(MemberFunction.Type.UNKNOWN, resultMemberFunction));
            showSetInAChart(result, chartRight, settingsService.getGraphMinimumX(), settingsService.getGraphMaximumX());
            inferenceResultService.add(result);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }
}
