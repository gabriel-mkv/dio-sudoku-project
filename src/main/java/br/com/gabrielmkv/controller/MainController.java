package br.com.gabrielmkv.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainController {
    
    @FXML
    private BorderPane main;

    @FXML
    private void initialize() {
        showMenu();
    }

    public void showMenu() {
        setContent("/br/com/gabrielmkv/view/menu-view.fxml");
    }

    public void showGame() {
        setContent("/br/com/gabrielmkv/view/game-view.fxml");
    }

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
