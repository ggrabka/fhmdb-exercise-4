package at.ac.fhcampuswien.fhmdb.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogUtil {

    public static void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}