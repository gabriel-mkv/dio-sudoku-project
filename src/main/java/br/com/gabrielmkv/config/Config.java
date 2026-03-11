package br.com.gabrielmkv.config;

import br.com.gabrielmkv.model.GameBoardSizeEnum;
import br.com.gabrielmkv.model.GameDifficultEnum;

/**
 * Configuração do jogo do Sudoku.
 * <p>
 * Essa classe contém as configurações do tamanho do tabuleiro e da dificuldade do jogo.
 * <p>
 * A configuração é feita através do método {@link #setup(GameBoardSizeEnum, GameDifficultEnum)}.
 * <p>
 * O tamanho do tabuleiro pode ser definido através do enum {@link GameBoardSizeEnum}.
 * <p>
 * A dificuldade do jogo pode ser definida através do enum {@link GameDifficultEnum}.
 * <p>
 * O tamanho do tabuleiro e a dificuldade do jogo são obtidos através dos métodos {@link #getBoardSize()} e {@link #getDifficulty()}, respectivamente.
 */
public final class Config {
    
    private static int BOARD_SIZE;
    private static int DIFFICULTY;

    public Config() {}

    public static void setup(GameBoardSizeEnum size, GameDifficultEnum difficulty) {
        BOARD_SIZE = size.getSize();
        DIFFICULTY = difficulty.getCellsRemoved(size.getSize());
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }

    public static int getDifficulty() {
        return DIFFICULTY;
    }
}
