package io.dkozak.sfc.proj.components.settings;

import io.dkozak.sfc.proj.services.SettingsService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.val;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPresenter implements Initializable {

    @FXML
    private TextField graphMinimumX;

    @FXML
    private TextField graphMaximumX;

    @FXML
    private Text infoText;

    @Inject
    private SettingsService settingsService;

    @Inject
    private EventBus eventBus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphMinimumX.setText(Integer.toString(settingsService.getGraphMinimumX()));
        graphMaximumX.setText(Integer.toString(settingsService.getGraphMaximumX()));
    }

    @FXML
    void onConfirm(ActionEvent event) {

        try {
            val graphMinimumX = Integer.parseInt(this.graphMinimumX.getText());
            val graphMaximumX = Integer.parseInt(this.graphMaximumX.getText());

            if (graphMinimumX > graphMaximumX) {
                displayErrorMessage("MinimumX must be bigger than maximumX");
                return;
            }

            settingsService.setGraphMinimumX(graphMinimumX);
            settingsService.setGraphMaximumX(graphMaximumX);
            closeWindow(event);
            eventBus.broadcast("settingsUpdated");
        } catch (NumberFormatException ex) {
            displayErrorMessage("Invalid number format, please correct it");
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void displayErrorMessage(String message) {
        infoText.setFill(Color.RED);
        infoText.setText(message);
    }

    private void closeWindow(ActionEvent event) {
        ((Node) event.getSource()).getScene()
                                  .getWindow()
                                  .hide();
    }

}
