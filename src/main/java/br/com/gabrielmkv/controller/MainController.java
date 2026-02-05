package br.com.gabrielmkv.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 * Controlador principal responsável pelo gerenciamento da navegação e troca de cenas (Views).
 * <p>
 * Esta classe atua como o orquestrador da interface gráfica, carregando dinamicamente
 * os arquivos FXML (Menu, Jogo) dentro do container principal definido em {@code main.fxml}.
 * </p>
 */
public class MainController {
    
    @FXML
    private BorderPane main;

    /**
     * Método de inicialização do JavaFX.
     * <p>
     * É chamado automaticamente após o carregamento do arquivo FXML raiz.
     * Define a tela de Menu como a visualização inicial da aplicação.
     * </p>
     */
    @FXML
    private void initialize() {
        showMenu();
    }

    /**
     * Carrega e exibe a tela de Menu Principal.
     */
    public void showMenu() {
        setContent("/br/com/gabrielmkv/view/menu-view.fxml");
    }

    /**
     * Carrega e exibe a tela do Tabuleiro de Jogo.
     */
    public void showGame() {
        setContent("/br/com/gabrielmkv/view/game-view.fxml");
    }

    /**
     * Método auxiliar para carregar arquivos FXML e injetá-los na área central da janela.
     * <p>
     * Além de carregar a view, este método verifica se o controlador da nova tela
     * implementa {@link ScreanController}. Se sim, injeta a referência deste
     * {@code MainController} para permitir a navegação de volta.
     * </p>
     * 
     * @param fxmlPath o caminho absoluto do recurso FXML a ser carregado.
     */
    private void setContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof ScreanController sc) {
                sc.setMainController(this);
            }
            
            main.setCenter(view);
            main.setBottom(null);

        } catch (Exception e) {
            System.err.println("Erro ao carregar o fxml: " + fxmlPath + " - " + e.getMessage());
        }
    }
}
