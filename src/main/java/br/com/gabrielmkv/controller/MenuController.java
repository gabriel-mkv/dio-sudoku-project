package br.com.gabrielmkv.controller;

import br.com.gabrielmkv.AppFX;
import br.com.gabrielmkv.config.Config;
import br.com.gabrielmkv.model.GameBoardSizeEnum;
import br.com.gabrielmkv.model.GameDifficultEnum;
import br.com.gabrielmkv.util.SudokuAlerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
        SudokuAlerts.showWarning("Selecione seu Desafio",
                                "Defina as regras do jogo", 
                                "Para gerar o tabuleiro, precisamos saber o tamanho e a dificuldade que você deseja encarar.");
    }

}