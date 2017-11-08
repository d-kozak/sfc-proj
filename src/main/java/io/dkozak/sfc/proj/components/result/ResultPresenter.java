package io.dkozak.sfc.proj.components.result;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultPresenter implements Initializable {

    @FXML
    private LineChart resultChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onCompute(ActionEvent event) {

    }
}
