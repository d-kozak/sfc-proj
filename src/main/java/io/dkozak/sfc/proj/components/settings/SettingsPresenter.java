package io.dkozak.sfc.proj.components.settings;

import io.dkozak.sfc.proj.services.SettingsService;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

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
        Bindings.bindBidirectional(graphMinimumX.textProperty(), settingsService.graphMinimumXProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(graphMaximumX.textProperty(), settingsService.graphMaximumXProperty(), new NumberStringConverter());
    }

    @FXML
    void onConfirm(ActionEvent event) {
        if (settingsService.getGraphMinimumX() > settingsService.getGraphMaximumX()) {
            displayErrorMessage("MinimumX must be bigger than maximumX");
            return;
        }
        closeWindow(event);
        eventBus.broadcast("settingsUpdated");

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
