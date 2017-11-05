package io.dkozak.sfc.proj.aboutview;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class AboutPresenter {

    @FXML
    public void onOK(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        ((Node) event.getSource()).getScene()
                                  .getWindow()
                                  .hide();
    }
}
