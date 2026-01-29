package br.com.gabrielmkv.config;

import br.com.gabrielmkv.model.GameBoardSizeEnum;
import br.com.gabrielmkv.model.GameDifficultEnum;
import br.com.gabrielmkv.util.BoardTemplate;

public final class Config {
    
    private static int BOARD_SIZE;
    private static int DIFFICULTY;

    public Config() {}

    public static void setup(GameBoardSizeEnum size, GameDifficultEnum difficulty) {
        BOARD_SIZE = size.getSize();
        DIFFICULTY = difficulty.getPercentage(size.getSize());
    }

    public static String getTemplateForSize(int boardSize) {
        return switch(boardSize) {
            case 4 -> BoardTemplate.BOARD_4x4_TEMPLATE;
            case 9 -> BoardTemplate.BOARD_9X9_TEMPLATE;
            case 16 -> BoardTemplate.BOARD_16X16_TEMPLATE;
            default -> BoardTemplate.BOARD_9X9_TEMPLATE;
        };
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }

    public static int getDifficulty() {
        return DIFFICULTY;
    }
}
