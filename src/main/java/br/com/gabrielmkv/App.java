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
            System.out.print("\n > Escolha uma opção: ");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame();
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> {
                    System.out.println("\n  [✕] Finalizando o jogo... Até a próxima!");
                    System.exit(0);
                }
                default -> System.out.print("\n  [!] Opção inválida! Por favor, escolha uma das opções do menu!\n");
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
            " > Digite um número de 1 a 3 (qualquer outro valor será 9x9 por padrão): "
        );
        var userOptionSize = scanner.nextInt();

        System.out.print(
            "\n=================================\n" +
            "       Dificuldade do Jogo       \n" +
            "=================================\n" +
            "1 - Fácil\n" +
            "2 - Médio (padrão)\n" +
            "3 - Difícil\n" +
            "---------------------------------\n" +
            " > Digite um número de 1 a 3 (qualquer outro valor será Médio por padrão): "
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
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        board = SudokuGenerator.createSudoku(Config.getBoardSize(), Config.getDifficulty());
        System.out.println("\n  [✓] O jogo foi criado com sucesso! Boa sorte!");
    }

    private static void inputNumber(){
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print(" > Escolha a coluna da jogada: ");
        var col = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.print(" > Escolha a linha da jogada: ");
        var row = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        Integer value;

        if (Config.getBoardSize() == 16) {
            System.out.printf(" > Qual valor deseja colocar na posição [%s, %s]? ", col, row);
            value = SymbolConverter.converterCharToInteger(runUntilGetValidChar());
        } else {
            System.out.printf(" > Digite o número para a posição [%s, %s]: ", col, row);
            value = runUntilGetValidNumber(1, Config.getBoardSize());
        }

        if (!board.changeValue(row, col, value)) {
            System.out.printf("\n  [!] A posição [%s, %s] possui um valor fixo e não pode ser alterada.", col, row);
        }     
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print(" > Escolha a coluna do número a ser removido: ");
        var col = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.print(" > Escolha a linha do número a ser removido: ");
        var row = runUntilGetValidNumber(0, Config.getBoardSize() - 1);

        if (!board.clearValue(row, col)) {
            System.out.printf("\n  [!] A posição [%s, %s] possui um valor fixo e não pode ser alterada.", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
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
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        System.out.printf("\n  [i] Status atual: %s", board.getStatus().getLabel());
    
        if(board.hasErrors()){
            System.out.println("\n  [X] O jogo atual contém erros!");
        } else {
            System.out.println("\n  [✓] O jogo não contém erros!");
        }
    }

    private static void clearGame(){
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("\n  (?) Tem certeza que deseja limpar o jogo e perder seu progresso? (S/N) > ");
        var confirm = scanner.next().trim().toLowerCase();

        while (!confirm.equals("s") && !confirm.equals("n")) {
            System.out.print("  [!] Por favor, responda apenas com 'S' ou 'N' > ");
            confirm = scanner.next().trim().toLowerCase();;
        }

        if (confirm.equals("s")) {
            board.reset();
            System.out.println("\n  [✓] O tabuleiro foi reiniciado com sucesso!");
        }
    }

    private  static void finishGame() {
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("\n  [✓] Parabéns! Você concluiu o desafio com sucesso!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("\n  [X] Ops! Existem números incorretos no seu tabuleiro.");
        } else {
            System.out.println("\n  [!] Tabuleiro incompleto. Preencha todos os espaços vazios!");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        while (true){
            var input = scanner.next().trim();

            if (input.matches("^\\d$")) {
                int current = Integer.parseInt(input);

                if (current >= min && current <= max) {
                    return current;
                } 

                System.out.printf("\n  [!] Fora do intervalo [%d-%d] > ", min, max);
            } else {
                System.out.printf("\n  [!] '%s' fora do limite. Digite de %d a %d: ", input, min, max);
            }
        }
    }

    private static char runUntilGetValidChar() {
        String input;
        char current;

        while (true) {
            input = scanner.next().trim().toUpperCase();
            
            if (input.isEmpty() || input.length() > 1) {
                System.out.print("\n  [!] Entrada inválida. Por favor, digite um valor aceito > ");
                continue;
            }

            current = input.charAt(0);

            if (current >= '1' && current <= '9') {
                return current;
            }

            if (current >= 'A' && current <= 'G') {
                return current;
            }

            System.out.printf("\n  [!] Entrada inválida! Escolha um número [1-9] ou uma letra [A-G] > ");
        }
    }

}
