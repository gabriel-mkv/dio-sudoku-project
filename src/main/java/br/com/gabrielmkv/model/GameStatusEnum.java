package br.com.gabrielmkv.model;

/**
 * Enumerado que representa o status do jogo do Sudoku.
 * <p>
 * Cada instância do enum contém uma descrição do status do jogo.
 * <p>
 * O status do jogo pode ser obtido através do método {@link #getLabel()}.
 * <p>
 * Os status possíveis são:
 * <ul>
 *   <li>{@link #NON_STARTED} - O jogo ainda não foi iniciado;</li>
 *   <li>{@link #INCOMPLETE} - O jogo ainda está incompleto;</li>
 *   <li>{@link #COMPLETE} - O jogo foi completado com sucesso.</li>
 * </ul>
 */
public enum GameStatusEnum {
    
    NON_STARTED("não iniciado"),
    INCOMPLETE("incompleto"),
    COMPLETE("completo");

    private String label;

    private GameStatusEnum(final String label) {
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}
