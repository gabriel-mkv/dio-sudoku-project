package br.com.gabrielmkv;

import java.util.Scanner;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import br.com.gabrielmkv.config.Config;
import br.com.gabrielmkv.generator.SudokuGenerator;
import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.GameBoardSizeEnum;
import br.com.gabrielmkv.model.GameDifficultEnum;
import br.com.gabrielmkv.util.SymbolConverter;

public class App  {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board = null;

    public static void main( String[] args ) {

        configureGame();

        var option = 0;

        while (true) {
            System.out.println("\n============================");
            System.out.println("       MENU PRINCIPAL       ");
            System.out.println("============================");
            System.out.println("1. Iniciar jogo");
            System.out.println("2. Colocar número");
            System.out.println("3. Remover número");
            System.out.println("4. Visualizar o jogo atual");
            System.out.println("5. Verificar status do jogo");
            System.out.println("6. Limpar o jogo");
            System.out.println("7. Finalizar o jogo");
            System.out.println("8. Sair");
            System.out.print("\nEscolha uma opção: ");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame();
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida! Informe uma opção válida do menu!");
            }
        }
    }

    private static void configureGame() {
        System.out.print(
            "==================================\n" +
            "       Tamanho do Tabuleiro       \n" +
            "==================================\n" +
            "1 - 4x4\n" +
            "2 - 9x9 (padrão)\n" +
            "3 - 16x16\n" +
            "----------------------------------\n" +
            "Digite um número de 1 a 3 (qualquer outro valor será 9x9 por padrão): "
        );
        var userOptionSize = scanner.nextInt();

        System.out.print(
            "=================================\n" +
            "       Dificuldade do Jogo       \n" +
            "=================================\n" +
            "1 - Fácil\n" +
            "2 - Médio (padrão)\n" +
            "3 - Difícil\n" +
            "---------------------------------\n" +
            "Digite um número de 1 a 3 (qualquer outro valor será Médio por padrão): "
        );
        var userOptionDifficulty = scanner.nextInt();

        GameBoardSizeEnum size = switch(userOptionSize) {
            case 1 -> GameBoardSizeEnum.SMALL;
            case 2 -> GameBoardSizeEnum.MEDIUM;
            case 3 -> GameBoardSizeEnum.LARGE;
            default -> GameBoardSizeEnum.MEDIUM;
        };

        GameDifficultEnum difficulty = switch(userOptionDifficulty) {
            case 1 -> GameDifficultEnum.EASY;
            case 2 -> GameDifficultEnum.MEDIUM;
            case 3 -> GameDifficultEnum.HARD;
            default -> GameDifficultEnum.MEDIUM;
        };

        Config.setup(size, difficulty);
    }

    private static void startGame() {
        if (nonNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        board = SudokuGenerator.createSudoku(Config.getBoardSize(), Config.getDifficulty());
        System.out .println("\nO jogo foi criado!");
    }

    private static void inputNumber(){
        if (isNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Informe a coluna que o número será inserido: ");
        var col = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.print("Informe a linha que o número será inserido: ");
        var row = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.printf("Informe o número que entrará na posição [%s, %s]: ", col, row);
        var value = runUntilGetValidNumber(1, Config.getBoardSize());

        if (!board.changeValue(row, col, value)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", col, row);
        }     
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Informe a coluna que o número será removido: ");
        var col = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.print("Informe a linha que o número será removido: ");
        var row = runUntilGetValidNumber(0, Config.getBoardSize() - 1);

        if (!board.clearValue(row, col)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo!\n", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        var boardToPrint = board.getSpaces()
                                .stream()
                                .flatMap(List::stream)
                                .map(space -> space.getActualNum() == null ? " " : SymbolConverter.converterIntToChar(space.getActualNum()))
                                .toArray(String[]::new);

        System.out.println(
            "\n===================================\n" +
            "       Situação do Tabuleiro       \n" +
            "===================================\n"
        );
        System.out.println(String.format(Config.getTemplateForSize(Config.getBoardSize()), (Object[]) boardToPrint));
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s", board.getStatus().getLabel());
    
        if(board.hasErrors()){
            System.out.println("O jogo atual contém erros!");
        } else {
            System.out.println("O jogo não contém erros!");
        }
    }

    private static void clearGame(){
        if (isNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Tem certeza que deseja limpar o jogo e perder todo o seu progresso? ");
        var confirm = scanner.next();

        while (!confirm.equalsIgnoreCase("S") || !confirm.equals("N")) {
            System.out.println("Informe 'S' ou 'N'!");
            confirm = scanner.next();
        }

        if (confirm.equalsIgnoreCase("S")) {
            board.reset();
        }
    }

    private  static void finishGame() {
        if (isNull(board)) {
            System.out.println("\nO jogo ainda não foi iniciado!");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns! Você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros!");
        } else {
            System.out.println("Preencha todos os espaços!");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();
        while (current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s", min, max);
            current = scanner.nextInt();
        }

        return current;
    }

}
