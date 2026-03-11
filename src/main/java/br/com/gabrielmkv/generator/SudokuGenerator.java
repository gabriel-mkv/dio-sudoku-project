package br.com.gabrielmkv.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;

/**
 * Classe responsável por gerar tabuleiros de Sudoku válidos e únicos.
 * <p>
 * O processo de geração envolve:
 * <ol>
 *   <li>Criar um tabuleiro base completamente resolvido.</li>
 *   <li>Embaralhar os números, linhas e colunas para criar uma nova solução válida.</li>
 *   <li>Remover células (mascarar) do tabuleiro embaralhado, garantindo que o quebra-cabeça resultante ainda tenha uma única solução.</li>
 * </ol>
 * Utiliza bitmasks para otimizar a verificação de validade e a contagem de soluções.
 * </p>
 */
public class SudokuGenerator {

    private int size;
    private int sectionSize;
    private int[][] grid;

    private int[] rowMask;
    private int[] colMask;
    private int[] sectionMask;

    private int fullMask;

    /**
     * Constrói um gerador de Sudoku para um tamanho de tabuleiro específico.
     *
     * @param size A dimensão do tabuleiro (ex: 9 para 9x9, 16 para 16x16).
     */
    public SudokuGenerator(int size) {
        this.size = size;
        this.sectionSize = (int) Math.sqrt(size);
        this.grid = new int[size][size];

        this.rowMask = new int[size];
        this.colMask = new int[size];
        this.sectionMask = new int[size];

        this.fullMask = (1 << size) - 1;
    }

    /**
     * Orquestra a geração completa de um novo quebra-cabeça de Sudoku.
     * <p>
     * Este é o método principal da classe. Ele gera uma solução, embaralha-a,
     * cria um quebra-cabeça removendo células e, finalmente, popula o objeto
     * {@link Board} fornecido com o resultado.
     * </p>
     *
     * @param targetEmptyCells O número de células que devem estar vazias no quebra-cabeça final.
     *                         Este valor define a dificuldade do jogo.
     * @param board            O objeto {@link Board} que será preenchido com o quebra-cabeça gerado
     *                         e sua solução.
     * @return O mesmo objeto {@link Board} passado como parâmetro, agora populado.
     */
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

    /**
     * Transforma a matriz interna `grid` (o quebra-cabeça) e a matriz de solução
     * em um objeto {@link Board} estruturado.
     *
     * @param solution A matriz 2D contendo a solução completa do tabuleiro.
     * @param board    O objeto {@link Board} a ser populado.
     * @return O objeto {@link Board} populado com os valores do quebra-cabeça,
     *         a solução e o status de cada célula (fixa ou não).
     */
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

    /**
     * Gera uma grade de Sudoku inicial válida e completamente preenchida.
     * <p>
     * Utiliza um padrão matemático para preencher a grade de forma que ela já
     * satisfaça as regras do Sudoku. Esta grade servirá como base para o
     * embaralhamento.
     * </p>
     */
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

    /**
     * Atualiza as máscaras de bits de linha, coluna e seção ao adicionar ou remover
     * um valor de uma célula.
     *
     * @param row      A linha da célula.
     * @param col      A coluna da célula.
     * @param value    O valor (1 a 'size') a ser adicionado/removido.
     * @param isAdding {@code true} se o valor está sendo adicionado à máscara,
     *                 {@code false} se está sendo removido.
     */
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

    /**
     * Orquestra as operações de embaralhamento para criar uma nova solução
     * a partir da grade base.
     */
    private void shuffleGrid() {
        shuffleNumbers();
        shuffleRows();
        shuffleColumns();
    }

    /**
     * Embaralha os números na grade.
     * <p>
     * Cria um mapeamento aleatório (ex: todos os 1s se tornam 5s, todos os 2s se tornam 8s, etc.)
     * e aplica essa transformação a toda a grade.
     * </p>
     */
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

    /**
     * Embaralha as linhas da grade, mas apenas dentro de seus respectivos blocos.
     * <p>
     * Por exemplo, em um tabuleiro 9x9, as linhas 0, 1 e 2 podem ser trocadas entre si,
     * mas nunca com as linhas 3, 4 ou 5.
     * </p>
     */
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

    /**
     * Embaralha as colunas da grade, mas apenas dentro de seus respectivos blocos.
     * <p>
     * Similar ao embaralhamento de linhas, as colunas só podem ser trocadas com outras
     * colunas que pertencem ao mesmo bloco vertical.
     * </p>
     */
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

    /**
     * Retorna uma máscara de bits representando os valores possíveis para uma célula.
     *
     * @param row A linha da célula.
     * @param col A coluna da célula.
     * @return Uma máscara de bits onde o i-ésimo bit está ativo se o número (i+1)
     *         for uma jogada válida para a célula.
     */
    private int getPossibleValues(int row, int col) {
        int sectionIdx = (row / sectionSize) * sectionSize + (col / sectionSize);

        int used = rowMask[row] | colMask[col] | sectionMask[sectionIdx];

        return fullMask & ~used;
    }

    /**
     * Conta o número de soluções possíveis para o estado atual da grade usando
     * um algoritmo de backtracking recursivo.
     * <p>
     * O método para e retorna assim que encontra 2 ou mais soluções, pois para o
     * propósito de gerar um quebra-cabeça, só precisamos saber se a solução é única (1) ou não (>1).
     * </p>
     * @return O número de soluções encontradas (0, 1 ou 2+).
     */
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

    /**
     * Remove células da grade resolvida para criar o quebra-cabeça final.
     * <p>
     * Itera sobre as posições da grade em ordem aleatória. Para cada célula,
     * tenta remover seu valor e verifica se o tabuleiro resultante ainda tem
     * uma solução única usando {@link #countSolutions()}. Se a solução não for mais
     * única, o valor é restaurado.
     * </p>
     * @param targetEmptyCells O número de células a serem removidas.
     */
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
