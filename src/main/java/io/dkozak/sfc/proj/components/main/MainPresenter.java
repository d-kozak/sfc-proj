package io.dkozak.sfc.proj.components.main;

import io.dkozak.sfc.proj.components.chartline.ChartLineView;
import io.dkozak.sfc.proj.components.result.ResultView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPresenter implements Initializable {
    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox centralVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultView resultView = new ResultView();
        borderPane.setRight(resultView.getView());
        ChartLineView chartLineView = new ChartLineView();
        ChartLineView chartLineView1 = new ChartLineView();
        centralVBox.getChildren()
                   .addAll(chartLineView.getView(), chartLineView1.getView());
    }

    @FXML
    public void openSettings(ActionEvent event) {

    }

    @FXML
    public void closeApp(ActionEvent event) {

    }

    @FXML
    public void clearAll(ActionEvent event) {

    }

    @FXML
    public void openAbout(ActionEvent event) {

    }
}
