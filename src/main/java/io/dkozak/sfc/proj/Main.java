package io.dkozak.sfc.proj;

import com.airhacks.afterburner.injection.Injector;
import io.dkozak.sfc.proj.components.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getView());

        // custom values for dependency injection
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("mainStage", stage);
        Injector.setConfigurationSource(customProperties::get);

        stage.setTitle("SFC - Project");
        final String uri = getClass().getResource("app.css")
                                     .toExternalForm();
        scene.getStylesheets()
             .add(uri);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
}
