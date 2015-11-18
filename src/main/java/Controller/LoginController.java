package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Created by Vikno on 18.11.2015.
 */

public class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class);//lll

    /*
     Пароль для адміністратора
     */
    final String loginField = "roman";
    final String passwordField = "11110";

    boolean loginFlag = false;

    @FXML
    AnchorPane loginPan;

    @FXML
    TextField loginTF;

    @FXML
    PasswordField passwordTF;

    @FXML
    Button logButton;

    @FXML
    public void checkLogin() throws Exception {
        if (loginTF.getText().equals(loginField)) {
            if (passwordTF.getText().equals(passwordField)) {
                loginFlag = true;
                log.info("Это информационное сообщение2!");//lll
            }
        }

        if (loginFlag) {
            start(new Stage());

            loginPan.setDisable(true);
        }
    }

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