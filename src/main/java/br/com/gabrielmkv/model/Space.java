package br.com.gabrielmkv.model;

/**
 * Representação de uma célula individual no tabuleiro do Sudoku.
 * <p>
 * Esta classe gerencia o estado de um espaço, controlando o {@link #getActualNum() valor atual} 
 * inserido pelo jogador e comparando-o com o {@link #getExpectedNum() valor esperado} da solução.
 * <p>
 * Células marcadas como {@link #isFixed() fixas} representam as pistas iniciais do desafio 
 * e não permitem alteração de valor, garantindo a integridade do puzzle.
 * <p>
 * Para remover o valor inserido em uma célula não fixa, utilize o método {@link #clearSpace()}.
 */
public class Space {
    
    private Integer actualNum;
    private int expectedNum;
    private boolean fixed;

    /**
     * Construtor da célula do tabuleiro.
     * * @param expectedNum o valor correto da solução para esta célula.
     * @param fixed define se a célula começará preenchida e bloqueada para edição.
     */
    public Space(int expectedNum, boolean fixed) {
        this.expectedNum = expectedNum;
        this.fixed = fixed;
        if (fixed) {
            actualNum = expectedNum;
        }
    }

    public Integer getActualNum() {
        return actualNum;
    }

    /**
     * Define o valor atual da célula, desde que ela não seja {@link #fixed}.
     * @param actualNum o número a ser inserido.
     */
    public void setActualNum(final Integer actualNum){
        if (fixed) return;
        this.actualNum = actualNum;
    }

    public int getExpectedNum() {
        return expectedNum;
    }

    public void setExpectedNum(int expectedNum){
        this.expectedNum = expectedNum;
    }

    public boolean isFixed() {
        return fixed;
    }

    /**
     * Define se a célula é fixa. Se for definida como fixa, seu valor atual 
     * é automaticamente ajustado para o valor esperado.
     * @param fixed {@code true} para bloquear a célula com o valor correto.
     */
    public void setFixed(boolean fixed){
        this.fixed = fixed;
        this.actualNum = fixed ? expectedNum : null;
    }

    /**
     * Limpa o valor atual do espaço, definindo-o como {@code null}.
     * <p>
     * Nota: Este método respeita a trava de células {@link #fixed} através 
     * do método {@link #setActualNum(Integer)}.
     * </p>
     */
    public void clearSpace(){
        setActualNum(null);
    }
}
