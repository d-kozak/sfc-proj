package io.dkozak.sfc.proj;

import com.airhacks.afterburner.injection.Injector;
import io.dkozak.sfc.proj.appview.AppView;
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
        AppView appView = new AppView();
        Scene scene = new Scene(appView.getView());

        // custom values for dependency injection
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("mainStage", stage);
        Injector.setConfigurationSource(customProperties::get);

        stage.setTitle("SFC - Project");
        final String uri = getClass().getResource("app.css")
                                     .toExternalForm();
        scene.getStylesheets()
             .add(uri);
        stage.setScene(scene);
        stage.show();
    }
}
