package br.com.gabrielmkv.controller;

/**
 * Interface que define o contrato para controladores de tela (Views) gerenciados pelo {@link MainController}.
 * <p>
 * Classes que implementam esta interface indicam que precisam manter uma referência ao
 * controlador principal da aplicação, geralmente para solicitar a troca de cenas (navegação).
 * </p>
 */
public interface ScreanController {

    /**
     * Injeta a dependência do controlador principal na classe implementadora.
     * 
     * @param mainController a instância do controlador principal que gerencia a navegação.
     */
    void setMainController(MainController mainController);
}
