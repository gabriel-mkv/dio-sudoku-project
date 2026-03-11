package br.com.gabrielmkv.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;

public class SudokuGenerator {

    private int size;
    private int sectionSize;
    private int[][] grid;

    private int[] rowMask;
    private int[] colMask;
    private int[] sectionMask;

    private int fullMask;

    public SudokuGenerator(int size) {
        this.size = size;
        this.sectionSize = (int) Math.sqrt(size);
        this.grid = new int[size][size];

        this.rowMask = new int[size];
        this.colMask = new int[size];
        this.sectionMask = new int[size];

        this.fullMask = (1 << size) - 1;
    }

    public Board generateSudoku(int targetEmptyCells, Board board) {
        generateBaseGrid();
        shuffleGrid();

        int[][] solvedGrid = new int[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                solvedGrid[r][c] = grid[r][c];
            }
        }

        createPuzzle(targetEmptyCells);
        return transformIntoBoard(solvedGrid, board);
    }

    private Board transformIntoBoard(int[][] solution, Board board) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Space space = board.getSpaces().get(r).get(c);

                int currentVal = grid[r][c];
                space.setActualNum(currentVal);

                space.setExpectedNum(solution[r][c]);

                space.setFixed(currentVal != 0);
            }
        }
        return board;
    }

    private void generateBaseGrid() {
        Arrays.fill(rowMask, 0);
        Arrays.fill(colMask, 0);
        Arrays.fill(sectionMask, 0);

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int value = (sectionSize * (r % sectionSize) + (r / sectionSize) + c) % size;
                grid[r][c] = value + 1;

                updateMasks(r, c, grid[r][c], true);
            }
        }
    }

    private void updateMasks(int row, int col, int value, boolean isAdding) {
        if (value == 0)
            return;

        int bit = 1 << (value - 1);

        int sectionIdx = (row / sectionSize) * sectionSize + (col / sectionSize);

        if (isAdding) {
            rowMask[row] |= bit;
            colMask[col] |= bit;
            sectionMask[sectionIdx] |= bit;
        } else {
            rowMask[row] &= ~bit;
            colMask[col] &= ~bit;
            sectionMask[sectionIdx] &= ~bit;
        }
    }

    private void shuffleGrid() {
        shuffleNumbers();
        shuffleRows();
        shuffleColumns();
    }

    private void shuffleNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int[] map = new int[size + 1];

        for (int i = 1; i <= size; i++) {
            map[i] = numbers.get(i - 1);
        }

        Arrays.fill(rowMask, 0);
        Arrays.fill(colMask, 0);
        Arrays.fill(sectionMask, 0);

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = map[grid[r][c]];

                updateMasks(r, c, grid[r][c], true);
            }
        }
    }

    private void shuffleRows() {
        Random rand = new Random();

        for (int block = 0; block < size; block += sectionSize) {

            int r1 = block + rand.nextInt(sectionSize);
            int r2 = block + rand.nextInt(sectionSize);

            if (r1 != r2) {
                int[] tempRow = grid[r1];
                grid[r1] = grid[r2];
                grid[r2] = tempRow;

                int tempMask = rowMask[r1];
                rowMask[r1] = rowMask[r2];
                rowMask[r2] = tempMask;
            }
        }
    }

    private void shuffleColumns() {
        Random rand = new Random();

        for (int block = 0; block < size; block += sectionSize) {

            int c1 = block + rand.nextInt(sectionSize);
            int c2 = block + rand.nextInt(sectionSize);

            if (c1 != c2) {
                for (int r = 0; r < size; r++) {
                    int temp = grid[r][c1];
                    grid[r][c1] = grid[r][c2];
                    grid[r][c2] = temp;
                }

                int tempMask = colMask[c1];
                colMask[c1] = colMask[c2];
                colMask[c2] = tempMask;
            }
        }
    }

    private int getPossibleValues(int row, int col) {
        int sectionIdx = (row / sectionSize) * sectionSize + (col / sectionSize);

        int used = rowMask[row] | colMask[col] | sectionMask[sectionIdx];

        return fullMask & ~used;
    }

    private int countSolutions() {
        int bestRow = -1;
        int bestCol = -1;
        int minValues = size + 1;
        int bestMask = 0;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c] == 0) {
                    int mask = getPossibleValues(r, c);
                    int count = Integer.bitCount(mask);
                    if (count == 0)
                        return 0;
                    if (count < minValues) {
                        minValues = count;
                        bestRow = r;
                        bestCol = c;
                        bestMask = mask;
                    }
                    if (minValues == 1)
                        break;
                }
            }
            if (minValues == 1)
                break;
        }

        if (bestRow == -1)
            return 1; 

        int total = 0;
        for (int num = 1; num <= size; num++) {
            if ((bestMask & (1 << (num - 1))) != 0) {
                grid[bestRow][bestCol] = num;
                updateMasks(bestRow, bestCol, num, true);

                total += countSolutions();

                updateMasks(bestRow, bestCol, num, false);
                grid[bestRow][bestCol] = 0;

                if (total >= 2)
                    return total;
            }
        }
        return total;
    }

    private void createPuzzle(int targetEmptyCells) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions);

        int removedCount = 0;

        for (int pos : positions) {
            if (removedCount >= targetEmptyCells)
                break;

            int r = pos / size;
            int c = pos % size;

            if (grid[r][c] != 0) {
                int temp = grid[r][c];

                grid[r][c] = 0;
                updateMasks(r, c, temp, false);

                if (countSolutions() != 1) {
                    grid[r][c] = temp;
                    updateMasks(r, c, temp, true);
                } else {
                    removedCount++;
                }
            }
        }
    }
}
