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
import br.com.gabrielmkv.model.GameStatusEnum;
import br.com.gabrielmkv.model.Space;
import br.com.gabrielmkv.util.BoardTemplate;
import br.com.gabrielmkv.util.SymbolConverter;

public class AppConsole {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board = null;

    public static void main( String[] args ) {

        configureGame();

        var option = 0;

        while (true) {
            limparTela();

            System.out.print(
                "\n====================================\n" +
                "|         KONO SUDOKU DA!          |\n" +
                "====================================\n" +
                "| [1] Iniciar jogo                 |\n" +
                "|----------------------------------|\n" +
                "| [2] Colocar número               |\n" +
                "| [3] Remover número               |\n" +
                "| [4] Visualizar o jogo atual      |\n" +
                "|----------------------------------|\n" +
                "| [5] Verificar status do jogo     |\n" +
                "| [6] Limpar o jogo                |\n" +
                "| [7] Finalizar o jogo             |\n" +
                "|----------------------------------|\n" +
                "| [8] Sair                         |\n" +
                "====================================\n" +
                "\n > Escolha uma opção: "
            );

            option = runUntilGetValidNumber(1, 8);

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
            }

            aguardarEnter();
        }
    }

    /**
     * Orquestra a interface de configuração inicial antes do início da partida.
     * <p>
     * Este método é responsável por exibir os menus de seleção no console e capturar 
     * as escolhas do usuário, mapeando os números digitados para as constantes 
     * {@link GameBoardSizeEnum} e {@link GameDifficultEnum}.
     * </p>
     * <p>
     * Ao final da execução, as configurações são persistidas globalmente através 
     * de {@link Config#setup(GameBoardSizeEnum, GameDifficultEnum)}.
     * </p>
     */
    private static void configureGame() {
        System.out.print(
            "\n====================================\n" +
            "|       SELECIONE A DIMENSÃO       |\n" +
            "====================================\n" +
            "| [1] Grade 4x4   (Pequeno)        |\n" +
            "| [2] Grade 9x9   (Médio)          |\n" +
            "| [3] Grade 16x16 (Grande)         |\n" +
            "====================================\n" +
            "\n > Escolha o tamanho do desafio: "
        );
        var userOptionSize = runUntilGetValidNumber(1, 3);

        System.out.print(
            "\n====================================\n" +
            "|      NÍVEL DE COMPLEXIDADE       |\n" +
            "====================================\n" +
            "| [1] Aprendiz (Fácil)             |\n" +
            "| [2] Estrategista (Médio)         |\n" +
            "| [3] Mestre (Difícil)             |\n" +
            "====================================\n" +
            "\n > Defina o nível da partida: "
        );
        var userOptionDifficulty = runUntilGetValidNumber(1, 3);

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

    /**
     * Inicia uma nova partida de Sudoku com base nas configurações previamente definidas.
     * <p>
     * O método verifica se já existe uma instância de {@link Board} ativa para evitar 
     * a sobreposição de jogos. Caso o tabuleiro esteja livre, ele utiliza o 
     * {@link SudokuGenerator#createSudoku(int, GameDifficultEnum)} para gerar um novo 
     * desafio técnico usando os parâmetros de {@link Config}.
     * </p>
     * <p>
     * Exibe uma mensagem de confirmação ao usuário após a criação bem-sucedida 
     * do tabuleiro.
     * </p>
     */
    private static void startGame() {
        if (nonNull(board)) {
            System.out.println("\n  [!] O jogo já foi iniciado!");
            return;
        }

        board = SudokuGenerator.createSudoku(Config.getBoardSize(), Config.getDifficulty());
        System.out.println("\n  [✓] O jogo foi criado com sucesso! Boa sorte!");
    }

    /**
     * Captura e processa a tentativa de jogada do usuário.
     * <p>
     * O método solicita as coordenadas (coluna e linha) e o valor desejado. 
     * Para tabuleiros de tamanho 16, utiliza o {@link SymbolConverter} para tratar entradas 
     * hexadecimais ou símbolos, enquanto para outros tamanhos utiliza entrada numérica direta.
     * </p>
     * <p>
     * A jogada é validada pelo {@link Board#changeValue(int, int, int)}, que impede a 
     * alteração de células {@link Space#isFixed() fixas}. Caso o jogo não tenha sido 
     * inicializado, a operação é abortada com um aviso.
     * </p>
     */
    private static void inputNumber(){
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("\n > Escolha a coluna da jogada: ");
        var col = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.print("\n > Escolha a linha da jogada: ");
        var row = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        Integer value;

        if (Config.getBoardSize() == 16) {
            System.out.printf("\n > Qual valor deseja colocar na posição [%s, %s]? ", col, row);
            value = SymbolConverter.converterCharToInteger(runUntilGetValidChar());
        } else {
            System.out.printf("\n > Digite o número para a posição [%s, %s]: ", col, row);
            value = runUntilGetValidNumber(1, Config.getBoardSize());
        }

        if (!board.changeValue(row, col, value)) {
            System.out.printf("\n  [!] A posição [%s, %s] possui um valor fixo e não pode ser alterada.\n", col, row);
        }     
    }

    /**
     * Realiza a remoção de um valor inserido pelo usuário em uma coordenada específica.
     * <p>
     * O método solicita a coluna e a linha desejadas e tenta executar a limpeza 
     * através do {@link Board#clearValue(int, int)}. Assim como na inserção, 
     * a operação é bloqueada caso a célula seja {@link Space#isFixed() fixa}.
     * </p>
     * <p>
     * Caso o tabuleiro não tenha sido inicializado, uma mensagem de aviso é exibida 
     * e a operação é encerrada.
     * </p>
     */
    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("\n  [!] O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("\n > Escolha a coluna do número a ser removido: ");
        var col = runUntilGetValidNumber(0, Config.getBoardSize() - 1);
        System.out.print("\n > Escolha a linha do número a ser removido: ");
        var row = runUntilGetValidNumber(0, Config.getBoardSize() - 1);

        if (!board.clearValue(row, col)) {
            System.out.printf("\n  [!] A posição [%s, %s] possui um valor fixo e não pode ser alterada.\n", col, row);
        }
    }

    /**
     * Renderiza e exibe o estado atual do tabuleiro no console.
     * <p>
     * O método processa a matriz de {@link Space}, convertendo cada valor numérico 
     * em sua representação visual (caractere ou espaço vazio) através do 
     * {@link SymbolConverter#converterIntToChar(Integer)}.
     * </p>
     * <p>
     * A exibição utiliza um modelo visual dinâmico obtido por 
     * {@link BoardTemplate#getTemplateForSize(int)}, garantindo que a moldura do 
     * tabuleiro se ajuste ao tamanho configurado (4x4, 9x9 ou 16x16).
     * </p>
     */
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
            "\n==================================\n" +
            "       SITUAÇÃO DO TABULEIRO       \n" +
            "==================================\n"
        );
        System.out.println(String.format(BoardTemplate.getTemplateForSize(Config.getBoardSize()), (Object[]) boardToPrint));
    }

    /**
     * Exibe o progresso e a integridade da partida atual.
     * <p>
     * O método apresenta o rótulo descritivo do {@link GameStatusEnum status} do jogo 
     * e realiza uma varredura através do {@link Board#hasErrors()} para informar 
     * se há conflitos entre os valores inseridos e a solução esperada.
     * </p>
     * <p>
     * Se o tabuleiro não tiver sido inicializado, uma mensagem de aviso é exibida.
     * </p>
     */
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

    /**
     * Realiza a limpeza completa das jogadas do usuário no tabuleiro atual.
     * <p>
     * O método solicita uma confirmação do usuário (S/N) para evitar a perda acidental 
     * de progresso. Caso confirmado, utiliza o método {@link Board#reset()} para 
     * remover todos os valores inseridos, preservando apenas as pistas {@link Space#isFixed() fixas}.
     * </p>
     * <p>
     * Inclui uma estrutura de repetição para validar a entrada do usuário, garantindo 
     * que apenas respostas válidas sejam processadas.
     * </p>
     */
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

    /**
     * Avalia as condições de finalização e encerra a partida caso o desafio tenha sido vencido.
     * <p>
     * O método utiliza o {@link Board#gameIsFinished()} para validar se o tabuleiro está totalmente 
     * preenchido e sem {@link Board#hasErrors() erros}. Caso positivo, exibe a mensagem de vitória, 
     * renderiza o tabuleiro final através do {@link #showCurrentGame()} e libera a memória 
     * definindo o {@code board} como {@code null}.
     * </p>
     * <p>
     * Se o jogo ainda contiver erros ou estiver incompleto, fornece o feedback específico 
     * ao jogador sem encerrar a sessão.
     * </p>
     */
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

    /**
     * Solicita e valida uma entrada numérica via console, garantindo que o valor 
     * esteja dentro de um intervalo específico.
     * <p>
     * O método permanece em um laço de repetição (loop infinito) até que o usuário 
     * forneça uma entrada que satisfaça duas condições:
     * <ol>
     * <li>Seja um dígito numérico válido (validado via Expressão Regular).</li>
     * <li>Esteja contido entre os limites {@code min} e {@code max} (inclusive).</li>
     * </ol>
     * </p>
     * * @param min o valor mínimo aceitável.
     * @param max o valor máximo aceitável.
     * @return o número inteiro validado e convertido.
     */
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

    /**
     * Solicita e valida uma entrada de caractere único via console para tabuleiros grandes.
     * <p>
     * O método trata a entrada de forma insensível a maiúsculas/minúsculas (convertendo 
     * internamente para {@code uppercase}) e valida se o caractere pertence ao conjunto 
     * aceito para o Sudoku de dimensão 16:
     * </p>
     * <ul>
     * <li>Dígitos numéricos de '1' a '9'.</li>
     * <li>Letras de 'A' a 'G' (representando os valores de 10 a 16).</li>
     * </ul>
     * <p>
     * O laço de repetição garante que a execução só retorne quando um caractere 
     * dentro desses intervalos for fornecido.
     * </p>
     * @return o caractere validado em formato maiúsculo.
     */
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

    /*
     * Limpa o terminal e move o cursor para o topo.
     */
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /*
     * Pausa o jogo até o usuário apertar Enter.
     * O primeiro nextLine() limpa o lixo do buffer e o segundo espera a tecla.
     */
    private static void aguardarEnter() {
        System.out.println("\n  [↵] Pressione ENTER para voltar ao menu");
        scanner.nextLine();
        scanner.nextLine();
    }
}
