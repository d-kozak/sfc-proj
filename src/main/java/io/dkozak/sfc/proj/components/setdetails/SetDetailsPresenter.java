package io.dkozak.sfc.proj.components.setdetails;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.fuzzy.MemberFunction;
import io.dkozak.sfc.proj.services.EditedFuzzySetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static io.dkozak.sfc.proj.utils.Utils.closeWindow;

public class SetDetailsPresenter implements Initializable {

    @FXML
    private Button confirmButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField gaussianSigma;
    @FXML
    private TextField gaussianMi;
    @FXML
    private TextField triangleA;
    @FXML
    private TextField triangleB;
    @FXML
    private TextField triangleC;
    @FXML
    private TextField trapezoidA;
    @FXML
    private TextField trapezoidB;
    @FXML
    private TextField trapezoidC;
    @FXML
    private TextField trapezoidD;

    @FXML
    private Text infoText;

    @Inject
    private EditedFuzzySetService editedFuzzySetService;

    private FuzzySet editedSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editedSet = editedFuzzySetService.getProperty();
        if (editedSet == null) {
            confirmButton.setText("Add");
        } else
            confirmButton.setText("Edit");
    }

    @FXML
    public void onConfirm(ActionEvent event) {
        String functionType = tabPane.getSelectionModel()
                                     .getSelectedItem()
                                     .getText();

        switch (functionType) {
            case "Trapezoid":
                confirmTrapezoid(event);
                break;
            case "Triangle":
                confirmTriangle(event);
                break;
            case "Gaussian":
                confirmGaussian(event);
                break;
            default:
                throw new RuntimeException("Unknown member function type" + functionType);
        }
    }

    private void confirmTrapezoid(ActionEvent event) {
        try {
            double a = Double.parseDouble(trapezoidA.getText());
            double b = Double.parseDouble(trapezoidB.getText());
            double c = Double.parseDouble(trapezoidC.getText());
            double d = Double.parseDouble(trapezoidD.getText());

            MemberFunction trapezoid = MemberFunction.trapezoid(a, b, c, d);
            finishEditing(trapezoid, event);

        } catch (NumberFormatException ex) {
            showInvalidNumberFormatMessage();
        }
    }

    private void confirmTriangle(ActionEvent event) {
        try {
            double a = Double.parseDouble(triangleA.getText());
            double b = Double.parseDouble(triangleB.getText());
            double c = Double.parseDouble(triangleC.getText());

            MemberFunction triangle = MemberFunction.triangle(a, b, c);
            finishEditing(triangle, event);

        } catch (NumberFormatException ex) {
            showInvalidNumberFormatMessage();
        }
    }

    private void confirmGaussian(ActionEvent event) {
        try {
            double mi = Double.parseDouble(gaussianMi.getText());
            double sigma = Double.parseDouble(gaussianSigma.getText());

            MemberFunction gaussian = MemberFunction.gaussian(mi, sigma);
            finishEditing(gaussian, event);

        } catch (NumberFormatException ex) {
            showInvalidNumberFormatMessage();
        }
    }

    private void finishEditing(MemberFunction function, ActionEvent event) {
        FuzzySet fuzzySet;
        if (editedSet != null) {
            fuzzySet = editedSet;
            fuzzySet.setMemberFunction(function);
        } else
            fuzzySet = new FuzzySet(function);
        editedFuzzySetService.setProperty(fuzzySet);
        closeWindow(event);
    }

    private void showInvalidNumberFormatMessage() {
        infoText.setFill(Color.RED);
        infoText.setText("Please check the number format");
    }

    @FXML
    public void onCancel(ActionEvent event) {
        closeWindow(event);
    }
}
