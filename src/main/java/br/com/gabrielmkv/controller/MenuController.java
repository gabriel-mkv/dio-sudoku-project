package br.com.gabrielmkv.controller;

import br.com.gabrielmkv.config.Config;
import br.com.gabrielmkv.model.GameBoardSizeEnum;
import br.com.gabrielmkv.model.GameDifficultEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class MenuController {
    
    @FXML
    private Button startButton;
    
    @FXML
    private ComboBox<GameBoardSizeEnum> gameBoardSize;
    
    @FXML
    private ComboBox<GameDifficultEnum> gameDifficulty;

    @FXML
    private void initialize() {
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
        alert.setTitle("Configuração de jogo incompleta!");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, selecione o tamanho do tabuleiro e a dificuldade do jogo.");
        alert.showAndWait();  
    }

}