package br.com.gabrielmkv.controller;

import br.com.gabrielmkv.generator.SudokuGenerator;
import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;
import br.com.gabrielmkv.util.SudokuAlerts;
import br.com.gabrielmkv.util.SymbolConverter;
import br.com.gabrielmkv.ui.AppFX;
import br.com.gabrielmkv.config.Config;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;

/**
 * Controlador responsável pela lógica da tela principal do jogo (Tabuleiro).
 * <p>
 * Esta classe gerencia a renderização dinâmica da grade de Sudoku, o
 * processamento
 * das jogadas do usuário e as ações de controle da partida (verificar, limpar,
 * finalizar).
 * </p>
 */
public class GameController implements ScreanController {

    @FXML
    private MainController mainController;

    private Board board;

    @FXML
    private GridPane sudokuGrid;

    @FXML
    private ProgressIndicator loadingIcon;

    @FXML
    private Button verifyStatusGame;

    @FXML
    private Button cleanGame;

    @FXML
    private Button endGame;

    /**
     * Inicializa o controlador e prepara o ambiente de jogo de forma assíncrona.
     * <p>
     * Este método é chamado automaticamente pelo JavaFX. Para evitar que a interface
     * gráfica congele durante a geração do tabuleiro (que pode ser demorada),
     * ele delega a criação do Sudoku para uma {@link Task} executada em uma
     * thread separada.
     * </p>
     * <p>
     * Durante a execução da tarefa, um {@link ProgressIndicator} (ícone de carregamento)
     * é exibido.
     * <ul>
     *   <li><b>Em caso de sucesso:</b> O tabuleiro gerado é recebido, o ícone de
     *       carregamento é ocultado, e os métodos {@link #renderBoard()} e
     *       {@link #setStageWidthAndHeight()} são chamados para construir a
     *       interface e ajustar a janela.</li>
     *   <li><b>Em caso de falha:</b> Um alerta de erro é exibido ao usuário através
     *       de {@link SudokuAlerts#showError(String, String, String)}.</li>
     * </ul>
     * A geração utiliza os parâmetros de tamanho e dificuldade definidos em {@link Config}.
     * </p>
     */
    @FXML
    private void initialize() {
        int size = Config.getBoardSize();
        int difficulty = Config.getDifficulty();
        loadingIcon.setVisible(true);

        Task<Board> generateTask = new Task<>() {
            @Override
            protected Board call() {
                SudokuGenerator generator = new SudokuGenerator(size);
                Board board = new Board(size);
                return generator.generateSudoku(difficulty, board);
            }
        };

        generateTask.setOnSucceeded(event -> {
            loadingIcon.setVisible(false);
            board = generateTask.getValue();
            renderBoard();
            setStageWidthAndHeight();
        });

        generateTask.setOnFailed(event -> {
            loadingIcon.setVisible(false);
            generateTask.getException().printStackTrace();
            SudokuAlerts.showError("Ops!",
                    "Algo deu errado",
                    "Não conseguimos gerar o seu Sudoku agora. Tente novamente!");
        });

        Thread thread = new Thread(generateTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Constrói a representação visual do tabuleiro no {@link GridPane}.
     * <p>
     * Limpa qualquer conteúdo existente e itera sobre a matriz de {@link Space} do
     * modelo,
     * criando e adicionando uma célula visual para cada posição.
     * </p>
     */
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

    /**
     * Cria o componente visual para uma célula individual do tabuleiro.
     * <p>
     * Este método instancia um {@link TextField} que representará uma célula na
     * grade.
     * Ele aplica as seguintes configurações:
     * <ul>
     * <li>Define um {@link TextFormatter} para converter automaticamente a entrada
     * para maiúsculas.</li>
     * <li>Aplica estilos CSS base, incluindo um específico para tabuleiros 16x16
     * ("cell-16x16").</li>
     * <li>Se a célula ({@link Space}) já tiver um valor, ele é convertido via
     * {@link SymbolConverter} e exibido.</li>
     * <li>Se a célula for fixa ({@link Space#isFixed()}), o campo é desabilitado
     * para edição e estilizado em negrito.</li>
     * <li>Se for editável, anexa um ouvinte de evento
     * ({@link #handleInputUser(TextField)}) para processar a entrada do
     * usuário.</li>
     * <li>Invoca {@link #applyBlockBoundaries(TextField, int, int)} para adicionar
     * as bordas visuais dos quadrantes.</li>
     * </ul>
     * </p>
     * 
     * @param space o modelo de dados da célula.
     * @param row   o índice da linha da célula na grade.
     * @param col   o índice da coluna da célula na grade.
     * @return o nó {@link TextField} configurado e pronto para ser adicionado ao
     *         {@link GridPane}.
     */
    private Node createCell(Space space, int row, int col) {

        TextField field = new TextField();

        field.setTextFormatter(new TextFormatter<String>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        field.setPrefSize(80, 80);
        field.setAlignment(Pos.CENTER);
        field.getStyleClass().add("sudoku-cell");

        if (board.getSize() == 16)
            field.getStyleClass().add("cell-16x16");

        if (space.getActualNum() != null) {
            field.setText(String.valueOf(SymbolConverter.converterIntToChar(space.getActualNum())));
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

    /**
     * Ajusta as dimensões da janela da aplicação conforme o tamanho do tabuleiro.
     * <p>
     * Garante que a janela tenha espaço suficiente para acomodar grades 4x4, 9x9 ou
     * 16x16.
     * </p>
     */
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

    /**
     * Aplica estilos CSS para desenhar as bordas dos quadrantes (blocos) do Sudoku.
     * 
     * @param cell o componente visual da célula.
     * @param row  índice da linha.
     * @param col  índice da coluna.
     */
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

    /**
     * Processa a entrada de dados do usuário em uma célula do tabuleiro.
     * <p>
     * Este método é acionado por um evento de teclado no {@link TextField} da
     * célula.
     * Ele obtém a posição (linha e coluna) da célula no {@link GridPane} e atualiza
     * o modelo de dados {@link Space} correspondente.
     * </p>
     * <p>
     * A validação da entrada é feita com base no tamanho do tabuleiro:
     * <ul>
     * <li><b>4x4:</b> Aceita apenas números de 1 a 4.</li>
     * <li><b>9x9:</b> Aceita apenas números de 1 a 9.</li>
     * <li><b>16x16:</b> Aceita números de 1 a 9 e letras de A a G (insensível a
     * maiúsculas).</li>
     * </ul>
     * Se a entrada for válida, o valor é convertido e salvo no modelo. Se for
     * inválida
     * ou se o campo for esvaziado, o campo de texto é limpo e o valor no modelo é
     * definido como {@code null}.
     * </p>
     * 
     * @param field o campo de texto (célula) que originou o evento.
     */
    private void handleInputUser(TextField field) {
        int row = GridPane.getRowIndex(field);
        int col = GridPane.getColumnIndex(field);

        Space space = board.getSpaces().get(row).get(col);
        String text = field.getText();

        if (text.isEmpty()) {
            space.setActualNum(null);
            return;
        }

        boolean regex = switch (board.getSize()) {
            case 16 -> text.matches("[1-9A-G]");
            case 9 -> text.matches("[1-9]");
            case 4 -> text.matches("[1-4]");
            default -> false;
        };

        if (regex) {
            int value = switch (board.getSize()) {
                case 16 -> SymbolConverter.converterCharToInteger(text.charAt(0));
                case 9, 4 -> Integer.parseInt(text);
                default -> 0;
            };
            space.setActualNum(value);
        } else {
            field.clear();
            space.setActualNum(null);
        }
    }

    /**
     * Verifica o estado atual do jogo e exibe um feedback ao usuário.
     * <p>
     * Informa se o jogo está incompleto, se há erros ou se está correto até o
     * momento.
     * </p>
     */
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
                message);
    }

    /**
     * Reinicia a partida atual após confirmação do usuário.
     * <p>
     * Remove todas as jogadas do usuário, mantendo apenas os números fixos
     * originais.
     * </p>
     */
    @FXML
    private void cleanGame() {
        if (SudokuAlerts.showConfirmation("Reiniciar Partida?",
                "Deseja limpar o tabuleiro?",
                "Isso removerá todos os números inseridos e voltará ao estado inicial. Esta ação não pode ser desfeita.")) {
            board.reset();
            renderBoard();
        }
    }

    /**
     * Tenta finalizar a partida.
     * <p>
     * Verifica as condições de vitória. Se o tabuleiro estiver completo e correto,
     * exibe mensagem de vitória e retorna ao menu. Caso contrário, alerta sobre
     * erros
     * ou campos vazios.
     * </p>
     */
    @FXML
    private void finalizeGame() {

        if (board.gameIsFinished()) {
            SudokuAlerts.showInformation("Vitória Magnífica!",
                    "Você é um mestre do Sudoku!",
                    "Desafio concluído com perfeição. Sua mente está afiada!");
            mainController.showMenu();
        } else if (board.hasErrors()) {
            SudokuAlerts.showError("Algo não está certo...",
                    "Conflito detectado!",
                    "Alguns números está desafiando as leis do Sudoku. Dê uma revisada nas linhas e colunas!");
        } else {
            SudokuAlerts.showWarning("Quase lá!",
                    "O trabalho ainda não acabou!",
                    "O tabuleiro ainda tem segredos a revelar. Preencha todos os espaços vazios para finalizar!");
        }
    }

    /**
     * Retorna ao menu principal após confirmação.
     */
    @FXML
    private void backToMenu() {
        if (SudokuAlerts.showConfirmation("Voltar ao Menu?",
                "O progresso será perdido!",
                "Ao retornar ao menu principal, sua partida atual será encerrada. Tem certeza que deseja sair agora?")) {
            mainController.showMenu();
        }
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
