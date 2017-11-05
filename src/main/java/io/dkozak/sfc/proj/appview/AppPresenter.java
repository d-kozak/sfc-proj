package io.dkozak.sfc.proj.appview;

import io.dkozak.sfc.proj.commandview.CommandPresenter;
import io.dkozak.sfc.proj.commandview.CommandView;
import io.dkozak.sfc.proj.editchartview.EditchartPresenter;
import io.dkozak.sfc.proj.editchartview.EditchartView;
import io.dkozak.sfc.proj.services.eventbus.EventBus;
import io.dkozak.sfc.proj.services.eventbus.EventBusListener;
import io.dkozak.sfc.proj.settings.SettingsView;
import io.dkozak.sfc.proj.utils.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AppPresenter implements Initializable, EventBusListener {

    private Logger logger = Logger.logger(this.getClass());

    @FXML
    private GridPane gridPane;

    @FXML
    private LineChart<Number, Number> leftChart;

    @FXML
    private LineChart<Number, Number> middleChart;

    @FXML
    private LineChart<Number, Number> rightChart;

    @FXML
    private Text infoText;

    @Inject
    private EventBus eventBus;

    @Inject
    private Stage mainStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register("appView", this);


        {
            EditchartView editchartView = new EditchartView();
            EditchartPresenter presenter = (EditchartPresenter) editchartView.getPresenter();
            presenter.setControlledChart(leftChart);

            gridPane.add(editchartView.getView(), 0, 1);
        }

        {
            EditchartView editchartView = new EditchartView();
            EditchartPresenter presenter = (EditchartPresenter) editchartView.getPresenter();
            presenter.setControlledChart(middleChart);

            gridPane.add(editchartView.getView(), 1, 1);
        }
        {
            CommandView commandView = new CommandView();
            CommandPresenter commandPresenter = (CommandPresenter) commandView.getPresenter();
            commandPresenter.setChart(rightChart);

            gridPane.add(commandView.getView(), 2, 1);
        }

    }

    @Override
    public void onMessage(String messageID, Object content) {
        if ("info".equals(messageID)) {
            infoText.setText((String) content);
            infoText.setFill(Color.BLACK);
        } else if ("error".equals(messageID)) {
            infoText.setText((String) content);
            infoText.setFill(Color.RED);
        } else
            logger.log("Unknown message " + messageID);
    }

    public void clearAll(ActionEvent event) {
        // I get a copy as well, se the cleaning is done in onMessage
        eventBus.broadcast("clear");
        eventBus.unicast("appView", "info", "All data cleared");
    }

    @FXML
    public void openSettings(ActionEvent event) {
        SettingsView settings = new SettingsView();
        Stage stage = new Stage();
        Scene scene = new Scene(settings.getView());
        stage.setTitle("Settings");
        stage.setScene(scene);

        // make the dialog modal
        stage.initOwner(this.mainStage.getOwner());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }
}
