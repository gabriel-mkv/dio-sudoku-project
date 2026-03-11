package br.com.gabrielmkv.model;

/**
 * Enumerado que representa a dificuldade do jogo do Sudoku.
 * <p>
 * Cada instância do enum contém a porcentagem de valores preenchidos no tabuleiro para cada dificuldade.
 * <p>
 * A porcentagem de valores preenchidos pode ser obtida através do método {@link #getPercentage(int)}.
 * <p>
 * O array de inteiros em cada constante segue o mapeamento: [0]=4x4, [1]=9x9 e [2]=16x16
 * <p>
 * A porcentagem é calculada com base no tamanho do tabuleiro, que pode ser obtido através do método {@link GameBoardSizeEnum#getSize()}.
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
     * Retorna a porcentagem de valores preenchidos no tabuleiro para a dificuldade especificada.
     *
     * @param size o tamanho do tabuleiro
     * @return a porcentagem de valores preenchidos no tabuleiro
     */
    public int getPercentage(int size) {
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
