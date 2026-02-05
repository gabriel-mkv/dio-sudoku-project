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

/**
 * Controlador responsável pela lógica da tela de Menu Principal.
 * <p>
 * Esta classe gerencia a interação do usuário com o formulário de configuração
 * do jogo, permitindo a seleção de dificuldade e tamanho do tabuleiro antes
 * de iniciar a partida.
 * </p>
 */
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

    /**
     * Inicializa os componentes da interface gráfica.
     * <p>
     * Configura as dimensões da janela para o menu e popula os {@link ComboBox}
     * com os valores dos enums {@link GameDifficultEnum} e {@link GameBoardSizeEnum}.
     * </p>
     */
    @FXML
    private void initialize() {
        setStageWidthAndHeight();
        gameDifficulty.getItems().setAll(GameDifficultEnum.values());
        gameBoardSize.getItems().setAll(GameBoardSizeEnum.values());
    }

    /**
     * Ação disparada pelo botão "INICIAR JOGO".
     * <p>
     * Valida as escolhas do usuário. Se válidas, configura o jogo globalmente
     * e solicita ao {@link MainController} a troca para a tela do tabuleiro.
     * Caso contrário, exibe um alerta de validação.
     * </p>
     */
    @FXML
    private void onStartGame() {
        if (!isFormValid()) {
            showValidationError();
            return;
        }

        buildGameConfig();
        mainController.showGame();
    }

    /**
     * Define as dimensões fixas da janela para a tela de menu.
     */
    private void setStageWidthAndHeight() {
        Stage stage = AppFX.getStage();
        stage.setWidth(450);
        stage.setHeight(600);
        stage.setResizable(false);
    }

    /**
     * Verifica se todos os campos obrigatórios foram preenchidos.
     * 
     * @return {@code true} se dificuldade e tamanho foram selecionados, {@code false} caso contrário.
     */
    private boolean isFormValid() {
        return gameBoardSize.getValue() != null
               && gameDifficulty.getValue() != null;
    }

    /**
     * Captura os valores selecionados na interface e configura o estado global do jogo.
     */
    private void buildGameConfig() {
        GameBoardSizeEnum selectedBoardSize = gameBoardSize.getValue();
        GameDifficultEnum selectedDifficulty = gameDifficulty.getValue();
        Config.setup(selectedBoardSize, selectedDifficulty);
    }

    /**
     * Exibe um alerta informando que o formulário está incompleto.
     */
    private void showValidationError() {
        SudokuAlerts.showWarning("Selecione seu Desafio",
                                "Defina as regras do jogo", 
                                "Para gerar o tabuleiro, precisamos saber o tamanho e a dificuldade que você deseja encarar.");
    }

}