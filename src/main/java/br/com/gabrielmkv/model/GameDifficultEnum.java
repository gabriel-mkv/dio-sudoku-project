package br.com.gabrielmkv.model;

public enum GameDifficultEnum {
    
    EASY(50),
    MEDIUM(40),
    HARD(30);

    private int percentage;

    private GameDifficultEnum(final int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }
}
