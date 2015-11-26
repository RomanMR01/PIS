package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import Alert.AlertMessage;

/**
 * Created by Vikno on 18.11.2015.
 */

/*
Контролер для входу в систему
*/
public class LoginController {

    /*
    Логер для класу LoginController
     */
    private static final Logger log = Logger.getLogger(LoginController.class);

    AlertMessage alertMessage = new AlertMessage();

    /*
     -------Don't look on it--------
     Пароль для адміністратора
     */
    final String loginField = "login";
    final String passwordField = "1111";

    /*
    Прапорець для перевірки коректності введених даних
     */
    boolean loginFlag = false;

    /*
    Елементи інтерфейсу
     */
    @FXML
    AnchorPane loginPan; //Основна панель вікна

    @FXML
    TextField loginTF;//Поле логіну

    @FXML
    PasswordField passwordTF;//Поле паролю

    @FXML
    Button logButton;//Кнопка активації

    /*
    Перевірка коректності введених даних.
    Викликається при кліку на logButton.
     */
    @FXML
    public void checkLogin() throws Exception {
        /*
        Якщо введені дані співпадають з необхідними то відкриється основне вікно програми
         */
        if (loginTF.getText().equals(loginField)) {
            if (passwordTF.getText().equals(passwordField)) {
                loginFlag = true;
            }
        }

        /*
        Якщо дані авторизаії коректні, то відкриваємо нове вікно і блокуємо поле авторизації.
        (Треба закрити)
         */
        if (loginFlag) {
            start(new Stage());
            loginPan.setDisable(true);
        } else {
            /*
            Запис помилки в log файл та вивід інформації на екран
             */
            log.info("Введено некоректні дані!");
            errorAlert("Введено некоректні дані!","Помилка!");
        }
    }

    private void errorAlert(String message,String title) {
          alertMessage.showAlert(message,title,0);
    }

    /*
    Метод для запуску mainWindow
     */
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/mainWindow.fxml";//Шлях до основного вікна
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("PIS");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}