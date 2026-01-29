package br.com.gabrielmkv.model;

import java.util.Collection;
import java.util.List;

import static br.com.gabrielmkv.model.GameStatusEnum.COMPLETE;
import static br.com.gabrielmkv.model.GameStatusEnum.INCOMPLETE;
import static br.com.gabrielmkv.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;

/**
 * Gerencia a matriz de jogo e as operações lógicas do tabuleiro de Sudoku.
 * <p>
 * Esta classe é responsável por controlar o fluxo da partida, permitindo a 
 * {@link #changeValue(int, int, int) alteração de valores}, verificação de 
 * {@link #hasErrors() erros} e monitoramento do {@link #getStatus() status atual}.
 * </p>
 */
public class Board {
    
    private final int size;
    private final List<List<Space>> spaces = new ArrayList<>();

    public Board(int size) {
        this.size = size;
        initializeEmptyGrid(size);
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public int getSize() {
        return size;
    }

    /**
     * Determina o estado atual do jogo.
     * @return {@link GameStatusEnum#NON_STARTED} se não houver jogadas,
     * {@link GameStatusEnum#INCOMPLETE} se houver espaços vazios,
     * ou {@link GameStatusEnum#COMPLETE} se todas as células estiverem preenchidas.
     */
    public GameStatusEnum getStatus(){
        if (spaces.stream()
                    .flatMap(Collection::stream)
                    .noneMatch(s -> !s.isFixed() && nonNull(s.getActualNum()))){
            return NON_STARTED;
        }

        return spaces.stream()
                    .flatMap(Collection::stream)
                    .anyMatch(s -> isNull(s.getActualNum())) ? INCOMPLETE : COMPLETE;
    }

    /**
     * Verifica se existe alguma divergência entre o valor inserido e o gabarito.
     * @return {@code true} se houver ao menos um número incorreto (não fixo) preenchido.
     */
    public boolean hasErrors(){
        if (getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream()
                        .flatMap(Collection::stream)
                        .anyMatch(s -> nonNull(s.getActualNum()) && !s.getActualNum().equals(s.getExpectedNum()));
    }

    /**
     * Insere um valor em uma coordenada específica, respeitando as travas de segurança.
     * @param row índice da linha.
     * @param col índice da coluna.
     * @param value valor a ser inserido.
     * @return {@code true} se a operação foi bem-sucedida; {@code false} se a célula for {@link Space#isFixed() fixa}.
     */
    public boolean changeValue(final int row, final int col, final int value){
        var space = spaces.get(row).get(col);

        if (space.isFixed()){
            return false;
        }

        space.setActualNum(value);
        return true;
    }

    /**
     * Remove o valor de uma célula específica.
     * @param row índice da linha.
     * @param col índice da coluna.
     * @return {@code true} se a célula foi limpa; {@code false} se for uma célula {@link Space#isFixed() fixa}.
     */
    public boolean clearValue(final int row, final int col){
        var space = spaces.get(row).get(col);

        if (space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    /**
     * Redefine o tabuleiro, removendo todas as jogadas do usuário e mantendo apenas as pistas.
     */
    public void reset(){
        spaces.forEach(row -> row.forEach(col -> col.clearSpace()));
    }

    /**
     * Valida se o tabuleiro foi preenchido totalmente e sem erros.
     * @return {@code true} se o jogo foi vencido.
     */
    public boolean gameIsFinished(){
        return !hasErrors() && getStatus() == COMPLETE;
    }

    /**
     * Cria a estrutura inicial da grade preenchida com espaços vazios.
     * @param size dimensão do tabuleiro.
     */
    private void initializeEmptyGrid(int size){
        for (int row = 0; row < size; row++){
            List<Space> currentRow = new ArrayList<>();

            for (int col = 0; col < size; col++) {
                currentRow.add(new Space(0, false));
            }

            this.spaces.add(currentRow);
        }
    }
}
