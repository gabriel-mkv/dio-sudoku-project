package br.com.gabrielmkv.model;

/**
 * Enumerado que representa as dimensões do tabuleiro do jogo do Sudoku.
 * <p>
 * Cada instância do enum contém o tamanho do tabuleiro.
 * <p>
 * O tamanho do tabuleiro pode ser definido através dos valores:
 * <ul>
 *   <li>{@link #SMALL} - Tabuleiro de tamanho 4x4;</li>
 *   <li>{@link #MEDIUM} - Tabuleiro de tamanho 9x9;</li>
 *   <li>{@link #LARGE} - Tabuleiro de tamanho 16x16.</li>
 * </ul>
 * <p>
 * O tamanho do tabuleiro é obtido através do método {@link #getSize()}.
 */
public enum GameBoardSizeEnum {
    
    SMALL(4),
    MEDIUM(9),
    LARGE(16);

    private int size;

    private GameBoardSizeEnum(final int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
