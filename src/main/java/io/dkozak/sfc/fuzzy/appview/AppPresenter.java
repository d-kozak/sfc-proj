package io.dkozak.sfc.fuzzy.appview;

import io.dkozak.sfc.fuzzy.editchartview.EditchartPresenter;
import io.dkozak.sfc.fuzzy.editchartview.EditchartView;
import io.dkozak.sfc.fuzzy.function.DataFunction;
import io.dkozak.sfc.fuzzy.utils.eventbus.EventBus;
import io.dkozak.sfc.fuzzy.utils.eventbus.EventBusListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AppPresenter implements Initializable, EventBusListener {


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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFunction constant = DataFunction.constant(0.35);
        DataFunction linear = DataFunction.trapezoid(30, 40, 50, 60);
//        DataFunction linear2 = DataFunction.normalDistribution(50,0.2);
        DataFunction linear2 = DataFunction.triangle(10, 30, 50);

        constant.visualizeData(leftChart, "Constant");
        linear.visualizeData(middleChart, "Linear 1");
        linear2.visualizeData(rightChart, "Gausian");

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
            EditchartView editchartView = new EditchartView();
            EditchartPresenter presenter = (EditchartPresenter) editchartView.getPresenter();
            presenter.setControlledChart(rightChart);

            gridPane.add(editchartView.getView(), 2, 1);

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
            System.err.println("Unknown message " + messageID);
    }
}
