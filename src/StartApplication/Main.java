package StartApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("primaryStage.fxml"));
        root.setId("primary");
        primaryStage.setTitle("Tutor Buddy");
        primaryStage.initStyle(StageStyle.DECORATED);
    }
}
