package br.com.gabrielmkv.model;

/**
 * Enumerado que representa a dificuldade do jogo do Sudoku.
 * <p>
 * Cada instância do enum define o número de células que serão removidas de um
 * tabuleiro completo para gerar o desafio, com base no tamanho da grade.
 * </p>
 * <p>
 * O valor para cada tamanho é armazenado em um array de inteiros (`cellsRemoved`),
 * onde o índice corresponde ao tamanho do tabuleiro:
 * <ul>
 *   <li>Índice 0: Tabuleiro 4x4</li>
 *   <li>Índice 1: Tabuleiro 9x9</li>
 *   <li>Índice 2: Tabuleiro 16x16</li>
 * </ul>
 * </p>
 * <p>
 * O número de células a serem removidas para uma partida pode ser obtido através
 * do método {@link #getCellsRemoved(int)}.
 */
public enum GameDifficultEnum {
    
    EASY(new int[]{5, 32, 90}, "Fácil"),
    MEDIUM(new int[]{8, 42, 120}, "Normal"),
    HARD(new int[]{10, 52, 140}, "Difícil");

    private final int[] cellsRemoved;
    private final String label;

    private GameDifficultEnum(int[] cellsRemoved, final String label) {
        this.cellsRemoved = cellsRemoved;
        this.label = label;
    }

    /**
     * Retorna o número de células a serem removidas do tabuleiro para a dificuldade e tamanho especificados.
     * <p>
     *
     * @param size o tamanho do tabuleiro (4, 9 ou 16).
     * @return o número de células a serem removidas.
     */
    public int getCellsRemoved(int size) {
        return switch (size) {
            case 4 -> cellsRemoved[0];
            case 9 -> cellsRemoved[1];
            case 16 -> cellsRemoved[2];
            default -> cellsRemoved[1];
        };
    }

    @Override
    public String toString() {
        return label;
    }
}
