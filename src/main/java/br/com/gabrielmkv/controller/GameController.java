package br.com.gabrielmkv.controller;

import br.com.gabrielmkv.generator.SudokuGenerator;
import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;
import br.com.gabrielmkv.util.SudokuAlerts;
import br.com.gabrielmkv.util.SymbolConverter;
import br.com.gabrielmkv.AppFX;
import br.com.gabrielmkv.config.Config;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController implements ScreanController {

    @FXML
    private MainController mainController;

    private Board board;

    @FXML
    private GridPane sudokuGrid;
    
    @FXML
    private Button verifyStatusGame;

    @FXML
    private Button cleanGame;

    @FXML
    private Button endGame;

    @FXML
    private void initialize() {
        board = SudokuGenerator.createSudoku(Config.getBoardSize(), Config.getDifficulty());
        renderBoard();
        setStageWidthAndHeight();
    }

    private void renderBoard() {

        sudokuGrid.getChildren().clear();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Space space = board.getSpaces().get(row).get(col);

                Node cell = createCell(space, row, col);
                sudokuGrid.add(cell, col, row);
            }
        }
    }

    private Node createCell(Space space, int row, int col) {
        
        TextField field = new TextField();

        field.setPrefSize(80, 80);
        field.setAlignment(Pos.CENTER);
        field.getStyleClass().add("sudoku-cell");

        if (board.getSize() == 16) field.getStyleClass().add("cell-16x16");

        if (space.getActualNum() != null) {
            field.setText(String.valueOf(space.getActualNum()));
        }

        if (space.isFixed()) {
            field.setEditable(false);
            field.setStyle("-fx-font-weight: bold");
        } else {
            field.setUserData(field);
            field.setOnKeyReleased(event -> handleInputUser(field));
        }

        applyBlockBoundaries(field, row, col);
        return field;
    }

    private void setStageWidthAndHeight() {
        Stage stage = AppFX.getStage();
        int boardSize = board.getSize();
        
        switch (boardSize) {
            case 4 -> {
                stage.setWidth(550);
                stage.setHeight(600);
            }
            case 9 -> {
                stage.setWidth(750);
                stage.setHeight(850);
            }
            case 16 -> {
                stage.setWidth(900);
                stage.setHeight(1000);
            }
        }

        stage.setResizable(false);
    }

    private void applyBlockBoundaries(TextField cell, int row, int col) {
        int size = board.getSize();
        int blockSize = (int) Math.sqrt(size);

        boolean isRightEdge = (col + 1) % blockSize == 0 && col < size - 1;
        boolean isBottomEdge = (row + 1) % blockSize == 0 && row < size - 1;

        if (isRightEdge && isBottomEdge) {
            cell.getStyleClass().add("section-both");
        } else if (isRightEdge) {
            cell.getStyleClass().add("section-right");
        } else if (isBottomEdge) {
            cell.getStyleClass().add("section-bottom");
        }
    }

    private void handleInputUser(TextField field) {
        int row = GridPane.getRowIndex(field);
        int col = GridPane.getColumnIndex(field);
        Space space = board.getSpaces().get(row).get(col);
        String text = field.getText();

        if ((board.getSize() == 16) && text.matches("[1-9A-G]")) {
            var value = SymbolConverter.converterCharToInteger(text.charAt(0));
            space.setActualNum(value);
        } else if ((board.getSize() != 16) && text.matches("[1-9]")) {
            space.setActualNum(Integer.parseInt(text));    
        } else {
            field.clear();
            space.setActualNum(null);
        }
    }

    @FXML
    private void verifyStatusGame() {
        String message = switch (board.getStatus()) {
            case NON_STARTED -> "O desafio aguarda! Clique em iniciar para começar.";
            case INCOMPLETE, COMPLETE -> (board.hasErrors()) 
                                ? "Atenção: algumas células precisam de correção." 
                                : "Excelente! Seu progresso está impecável.";
        };

        SudokuAlerts.showInformation("Análise do Tabuleiro",
                                     "Status: " + board.getStatus().getLabel(),
                                     message
        );
    }

    @FXML
    private void cleanGame() {
        if (SudokuAlerts.showConfirmation("Reiniciar Partida?",
                                          "Deseja limpar o tabuleiro?",
                                          "Isso removerá todos os números inseridos e voltará ao estado inicial. Esta ação não pode ser desfeita."
            )) {
            board.reset();
            renderBoard();
        }
    }

    @FXML
    private void finalizeGame() {

       if (board.gameIsFinished()) {
            SudokuAlerts.showInformation("Vitória Magnífica!",
                                     "Você é um mestre do Sudoku!",
                                     "Desafio concluído com perfeição. Sua mente está afiada!"
            );
            mainController.showMenu();
        } else if (board.hasErrors()) {
            SudokuAlerts.showError();
        } else {
            SudokuAlerts.showWarning("Quase lá!",
                                    "O trabalho ainda não acabou!", 
                                "O tabuleiro ainda tem segredos a revelar. Preencha todos os espaços vazios para finalizar!"
            );
        }
    }

    @FXML
    private void backToMenu() {
        if (SudokuAlerts.showConfirmation("Voltar ao Menu?",
                                          "O progresso será perdido!",
                                          "Ao retornar ao menu principal, sua partida atual será encerrada. Tem certeza que deseja sair agora?"
            )) {
            mainController.showMenu();
        }
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
