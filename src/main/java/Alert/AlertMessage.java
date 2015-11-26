package Alert;

import javafx.scene.control.Alert;

/**
 * Created by Vikno on 26.11.2015.
 */
public class AlertMessage {


    public void showAlert(String message,String title,int type) {

        javafx.scene.control.Alert alert;

        switch (type)
        {
            case 0:
                alert = new javafx.scene.control.Alert(Alert.AlertType.ERROR);
                break;
            case 1:
                alert = new javafx.scene.control.Alert(Alert.AlertType.WARNING);
                break;
            case 2:
                alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
                break;
            default:
                alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        }

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}
