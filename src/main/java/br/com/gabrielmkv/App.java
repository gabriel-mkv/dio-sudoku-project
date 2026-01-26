package br.com.gabrielmkv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;
import br.com.gabrielmkv.util.BoardTemplate;

public class App  {

    private final static Scanner scanner = new Scanner(System.in);
        
    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main( String[] args ) {
        
        final var positions = Stream.of(args[0].split(" "))
                                    .collect(Collectors.toMap(
                                                    k -> k.split(";")[0],
                                                    v -> v.split(";")[1]
                                    ));

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
                case 1 -> startGame(positions);
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

    private static void startGame(final Map<String, String> positions) {
        if (nonNull (board)) {
            System.out.println("O jogo já foi iniciado!");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());

            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo foi criado!");
    }

    private static void inputNumber(){
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Informe a coluna que o número será inserido: ");
        var col = runUntilGetValidNumber(0, 8);
        System.out.print("Informe a linha que o número será inserido: ");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que entrará na posição [%s, %s]: ", col, row);
        var value = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", col, row);
        }     
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Informe a coluna que o número será removido: ");
        var col = runUntilGetValidNumber(0, 8);
        System.out.print("Informe a linha que o número será removido: ");
        var row = runUntilGetValidNumber(0, 8);

        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo!\n", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col : board.getSpaces()) {
                args[argPos ++] = " " + ((isNull(col.get(i).getActualNum())) ? " " : col.get(i).getActualNum());
            }
        }

        System.out.println("Situação atual");
        System.out.println(BoardTemplate.BOARD_TEMPLATE.formatted(args));
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado!");
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
            System.out.println("O jogo ainda não foi iniciado!");
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
            System.out.println("O jogo ainda não foi iniciado!");
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
