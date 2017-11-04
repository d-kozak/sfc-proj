package io.dkozak.sfc.proj;

import io.dkozak.sfc.proj.appview.AppView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AppView appView = new AppView();
        Scene scene = new Scene(appView.getView());
        stage.setTitle("SFC - Projekt");
        final String uri = getClass().getResource("app.css")
                                     .toExternalForm();
        scene.getStylesheets()
             .add(uri);
        stage.setScene(scene);
        stage.show();
    }
}
