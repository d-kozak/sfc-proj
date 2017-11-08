package io.dkozak.sfc.proj.services;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SettingsService {

    public static final int DEFAULT_GRAPH_MINIMUM_X = 0;
    public static final int DEFAULT_GRAPH_MAXIMUM_X = 100;

    private IntegerProperty graphMinimumX = new SimpleIntegerProperty(DEFAULT_GRAPH_MINIMUM_X);
    private IntegerProperty graphMaximumX = new SimpleIntegerProperty(DEFAULT_GRAPH_MAXIMUM_X);

    public int getGraphMinimumX() {
        return graphMinimumX.get();
    }

    public IntegerProperty graphMinimumXProperty() {
        return graphMinimumX;
    }

    public void setGraphMinimumX(int graphMinimumX) {
        this.graphMinimumX.set(graphMinimumX);
    }

    public int getGraphMaximumX() {
        return graphMaximumX.get();
    }

    public IntegerProperty graphMaximumXProperty() {
        return graphMaximumX;
    }

    public void setGraphMaximumX(int graphMaximumX) {
        this.graphMaximumX.set(graphMaximumX);
    }
}
