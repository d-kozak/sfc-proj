package io.dkozak.sfc.proj.components.main;

import io.dkozak.sfc.proj.components.about.AboutView;
import io.dkozak.sfc.proj.components.chartline.ChartLineView;
import io.dkozak.sfc.proj.components.result.ResultView;
import io.dkozak.sfc.proj.components.settings.SettingsView;
import io.dkozak.sfc.proj.services.InferenceResultService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static io.dkozak.sfc.proj.utils.Utils.openModalDialog;

public class MainPresenter implements Initializable {
    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox centralVBox;

    @Inject
    private InferenceResultService inferenceResultService;

    @Inject
    private Stage mainStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultView resultView = new ResultView();
        borderPane.setRight(resultView.getView());
        ChartLineView chartLineView = new ChartLineView();
        centralVBox.getChildren()
                   .addAll(chartLineView.getView());
    }

    @FXML
    public void openSettings(ActionEvent event) {
        openModalDialog(mainStage, "Settings", new SettingsView());
    }

    @FXML
    public void closeApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void clearAll(ActionEvent event) {
        centralVBox.getChildren()
                   .clear();
        inferenceResultService.getResults()
                              .clear();
    }

    @FXML
    public void openAbout(ActionEvent event) {
        Stage stage = new Stage();
        AboutView aboutView = new AboutView();
        Scene scene = new Scene(aboutView.getView());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onAddRule(ActionEvent event) {
        ChartLineView chartLineView = new ChartLineView();
        centralVBox.getChildren()
                   .add(chartLineView.getView());
    }
}
