package io.dkozak.sfc.proj.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class EditedEntityService<T> {
    private final ObjectProperty<T> property = new SimpleObjectProperty<>();

    public T getProperty() {
        return property.get();
    }

    public ObjectProperty<T> propertyProperty() {
        return property;
    }

    public void unsetProperty() {
        property.set(null);
    }

    public void setProperty(T property) {
        this.property.set(property);
    }
}
