package io.dkozak.sfc.proj.utils;

import com.airhacks.afterburner.views.FXMLView;
import io.dkozak.sfc.proj.services.SettingsService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utils {

    private Utils() {
    }

    public static void openModalDialog(Stage primaryStage, String title, FXMLView fxmlView) {
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlView.getView());
        stage.setTitle(title);
        stage.setScene(scene);

        // make the dialog modal
        stage.initOwner(primaryStage.getOwner());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public static void closeWindow(ActionEvent event) {
        ((Node) event.getSource()).getScene()
                                  .getWindow()
                                  .hide();
    }

    public static void bindChartLimits(LineChart<Number, Number> chart, SettingsService settingsService) {
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        xAxis.setAutoRanging(false);
        settingsService.graphMinimumXProperty()
                       .bindBidirectional(xAxis.lowerBoundProperty());
        settingsService.graphMaximumXProperty()
                       .bindBidirectional(xAxis.upperBoundProperty());
    }
}
