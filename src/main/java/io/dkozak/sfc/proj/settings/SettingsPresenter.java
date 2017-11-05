package io.dkozak.sfc.proj.settings;

import io.dkozak.sfc.proj.services.SettingsService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import lombok.val;

import javax.inject.Inject;

public class SettingsPresenter {

    @FXML
    private TextField graphMinimumX;

    @FXML
    private TextField graphMaximumX;

    @Inject
    private SettingsService settingsService;

    @Inject
    private EventBus eventBus;

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
        throw new RuntimeException("Not impl");
    }

    private void closeWindow(ActionEvent event) {
        ((Node) event.getSource()).getScene()
                                  .getWindow()
                                  .hide();
    }

}
