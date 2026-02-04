package br.com.gabrielmkv.util;

import java.util.Optional;

import br.com.gabrielmkv.AppFX;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public final class SudokuAlerts {
    
    private static void applyStyle(Alert alert, String styleClassName){
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(SudokuAlerts.class.getResource("/br/com/gabrielmkv/css/alert.css")
                                                          .toExternalForm());
        dialogPane.getStyleClass().addAll("sudoku-alert", styleClassName);
    }

    public static void showInformation(String title, String header, String content){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyStyle(alert, "alert-information");
        alert.showAndWait();
    }

    public static void showError(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle("Algo não está certo...");
        alert.setHeaderText("Conflito detectado!");
        alert.setContentText("Alguns números está desafiando as leis do Sudoku. Dê uma revisada nas linhas e colunas!");

        applyStyle(alert, "alert-error");
        alert.showAndWait();
    }

    public static void showWarning(String title, String header, String content){
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyStyle(alert, "alert-warning");
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String header, String content){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyStyle(alert, "alert-confirm");        
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
