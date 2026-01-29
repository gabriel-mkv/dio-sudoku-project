package br.com.gabrielmkv.generator;

import java.util.Random;

import br.com.gabrielmkv.model.Board;
import br.com.gabrielmkv.model.Space;

/**
 * Gerador responsável pela criação e configuração de novos tabuleiros de Sudoku.
 * <p>
 * Esta classe encapsula a lógica de construção de uma grade válida através do 
 * {@link #generateSolved(int) preenchimento inicial}, seguido pelo embaralhamento 
 * de {@link #shuffleRows(int[][]) linhas} e {@link #shuffleColumns(int[][]) colunas}.
 * <p>
 * Por fim, o método {@link #createSudoku(int, int)} realiza a distribuição de pistas 
 * no tabuleiro com base na dificuldade selecionada.
 */
public class SudokuGenerator {
    
    /**
     * Gera uma matriz preenchida com uma solução válida inicial.
     * <p>
     * Utiliza um algoritmo de deslocamento baseado na raiz quadrada do tamanho 
     * da grade (sectionSize) para garantir que não haja números repetidos 
     * em linhas, colunas ou quadrantes.
     * </p>
     * @param size dimensão do tabuleiro.
     * @return matriz bidimensional de inteiros resolvida.
     */
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

    /**
     * Embaralha as linhas do tabuleiro dentro de seus respectivos blocos.
     * <p>
     * Este método mantém a validade do Sudoku pois apenas troca linhas 
     * que pertencem ao mesmo conjunto de quadrantes horizontais.
     * </p>
     * @param grid a matriz a ser embaralhada.
     */
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

    /**
     * Embaralha as colunas do tabuleiro dentro de seus respectivos blocos.
     * <p>
     * Semelhante ao {@link #shuffleRows(int[][])}, este método apenas troca 
     * colunas dentro do mesmo bloco vertical para preservar as regras do jogo.
     * </p>
     * @param grid a matriz a ser embaralhada.
     */
    private static void shuffleColumns(int[][] grid) {
        int size = grid.length;
        int sectionSize = (int) Math.sqrt(size);
        Random random = new Random();

        for (int block = 0; block < sectionSize; block++) {
            int start = block * sectionSize;

            for (int i = 0; i < sectionSize; i++) {
                int c1 = start + random.nextInt(sectionSize);
                int c2 = start + random.nextInt(sectionSize);

                for (int row = 0; row < size; row++) {
                    int temp = grid[row][c1];
                    grid[row][c1] = grid[row][c2];
                    grid[row][c2] = temp;
                }
            }
        }
    }

    /**
     * Cria e configura um novo objeto {@link Board} pronto para o jogo.
     * <p>
     * Este método integra a geração da solução, o embaralhamento e a 
     * definição das células fixas com base na dificuldade escolhida.
     * </p>
     * @param size dimensão do tabuleiro (4, 9 ou 16).
     * @param fixedPercentage probabilidade (0-100) de uma célula ser revelada.
     * @return um {@link Board} populado com os valores esperados e células fixas definidas.
     */
    public static Board createSudoku(int size, int fixedPercentage) {

        int[][] solved = generateSolved(size);
        shuffleRows(solved);
        shuffleColumns(solved);

        Board board = new Board(size);
        Random random = new Random();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                Space space = board.getSpaces().get(row).get(col);

                space.setExpectedNum(solved[row][col]);

                boolean fixed = random.nextInt(100) < fixedPercentage;
                space.setFixed(fixed);
                
            }
        }

        return board;
    }
}
