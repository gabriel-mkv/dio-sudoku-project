package br.com.gabrielmkv.model;

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
