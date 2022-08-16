import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("application.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1037, 690);
        stage.setTitle("Dictionary Application");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        applicationLogic.setStage(stage); // Passing the stage object to applicationLogic class
    }

    public static void main(String[] args) {
        launch();
    }
}