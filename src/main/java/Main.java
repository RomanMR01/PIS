import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Vikno on 17.11.2015.
 */

/*
Клас запуску програми
 */
public class Main extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

//        String fxmlFile = "/fxml/mainWindow.fxml";//Шлях до основного вікна
//        FXMLLoader loader = new FXMLLoader();
//        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
//        stage.setTitle("PIS");
//        stage.setScene(new Scene(root));
//        stage.setResizable(false);
//        stage.show();

        String fxmlFile = "/fxml/logIn.fxml";//Шлях до основного вікна
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Вхід в PIS");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();


    }
}