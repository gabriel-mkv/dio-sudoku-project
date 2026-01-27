package br.com.gabrielmkv.generator;

import java.util.Random;

import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;

public class SudokuGenerator {
    
    private static int[][] generateSolved(int size) {
        int sectionSize = (int) Math.sqrt(size);
        int[][] grid = new int[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] =
                        (row * sectionSize + row / sectionSize + col) % size + 1;
            }
        }

        return grid;
    }

    private static void shuffleRows(int[][] grid) {
        int size = grid.length;
        int sectionSize = (int) Math.sqrt(size);
        Random random = new Random();

        for (int block = 0; block < sectionSize; block++) {
            int start = block * sectionSize;

            for (int i = 0; i < sectionSize; i++) {
                int r1 = start + random.nextInt(sectionSize);
                int r2 = start + random.nextInt(sectionSize);

                int[] temp = grid[r1];
                grid[r1] = grid[r2];
                grid[r2] = temp;
            }
        }
    }

    public static Board createSudoku(int size, int fixedPercentage) {

        int[][] solved = generateSolved(size);
        shuffleRows(solved);

        Board board = new Board(size);
        Random random = new Random();

        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {

                Space space = board.getSpaces().get(col).get(row);

                space.setExpectedNum(solved[row][col]);

                boolean fixed = random.nextInt(100) < fixedPercentage;
                space.setFixed(fixed);

            }
        }

        return board;
    }
}
