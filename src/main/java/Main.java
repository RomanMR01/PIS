import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Vikno on 17.11.2015.
 */

/*
Main клас для запуску програми.

Спочатку завантажується форма для введення логіна та паролю
Якщо введено коректні дані, то відкривається основна форма mainWindow.fxml
*/

public class Main extends Application {

    /*
    Точка входу програми
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        /*
        Основні параметри для відобредення стартового вікна.
        Розмітка GUI завантажується з файлу logIn.fxml
         */
        String fxmlFile = "/fxml/logIn.fxml";//шлях до файлу розмітки
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Вхід в систему");
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        stage.show();
    }
}