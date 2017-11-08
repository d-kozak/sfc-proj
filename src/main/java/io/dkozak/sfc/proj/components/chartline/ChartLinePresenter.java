package io.dkozak.sfc.proj.components.chartline;

import io.dkozak.sfc.proj.components.setdetails.SetDetailsView;
import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.services.EditedFuzzySetService;
import io.dkozak.sfc.proj.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.stage.Stage;

import javax.inject.Inject;

public class ChartLinePresenter {
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
        set.setName(name);
        set.visualizeOn(chart);
    }
}
