package io.dkozak.sfc.proj.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsPresenter {

    @FXML
    private TextField graphMaximumX;

    @FXML
    private TextField graphMinimumX;

    @FXML
    void onConfirm(ActionEvent event) {
        System.out.println("Confirm");
    }

    @FXML
    void onCancel(ActionEvent event) {
        System.out.println("Cancel");
    }

}
