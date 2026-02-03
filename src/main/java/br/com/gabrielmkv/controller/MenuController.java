package br.com.gabrielmkv.controller;

import br.com.gabrielmkv.AppFX;
import br.com.gabrielmkv.config.Config;
import br.com.gabrielmkv.model.GameBoardSizeEnum;
import br.com.gabrielmkv.model.GameDifficultEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements ScreanController{
    
    @FXML
    private MainController mainController;

    @FXML
    private Button startButton;
    
    @FXML
    private ComboBox<GameBoardSizeEnum> gameBoardSize;
    
    @FXML
    private ComboBox<GameDifficultEnum> gameDifficulty;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        setStageWidthAndHeight();
        gameDifficulty.getItems().setAll(GameDifficultEnum.values());
        gameBoardSize.getItems().setAll(GameBoardSizeEnum.values());
    }

    @FXML
    private void onStartGame() {
        if (!isFormValid()) {
            showValidationError();
            return;
        }

        buildGameConfig();
        mainController.showGame();
    }

    private void setStageWidthAndHeight() {
        Stage stage = AppFX.getStage();
        stage.setWidth(450);
        stage.setHeight(600);
        stage.setResizable(false);
    }

    private boolean isFormValid() {
        return gameBoardSize.getValue() != null
               && gameDifficulty.getValue() != null;
    }

    private void buildGameConfig() {
        GameBoardSizeEnum selectedBoardSize = gameBoardSize.getValue();
        GameDifficultEnum selectedDifficulty = gameDifficulty.getValue();
        Config.setup(selectedBoardSize, selectedDifficulty);
    }

    private void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);
        
        alert.setTitle("Configuração de jogo incompleta!");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, selecione o tamanho do tabuleiro e a dificuldade do jogo.");
        alert.showAndWait();  
    }

}