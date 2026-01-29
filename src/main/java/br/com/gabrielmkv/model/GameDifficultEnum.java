package br.com.gabrielmkv.model;

public enum GameDifficultEnum {
    
    EASY(new int []{38, 42, 45}),
    MEDIUM(new int []{29, 37, 40}),
    HARD(new int []{24, 31, 35});

    private int[] percentages;

    private GameDifficultEnum(int[] percentages) {
        this.percentages = percentages;
    }

    public int getPercentage(int size) {
        return switch (size) {
            case 4 -> percentages[0];
            case 9 -> percentages[1];
            case 16 -> percentages[2];
            default -> percentages[1];
        };
    }
}
