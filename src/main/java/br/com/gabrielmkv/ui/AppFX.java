package br.com.gabrielmkv.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppFX extends Application {

        private static Stage primaryStage;

        /**
         * Método de entrada do ciclo de vida da aplicação JavaFX.
         * <p>
         * Configura a janela principal (Stage), carrega o layout raiz (main.fxml)
         * e exibe a interface ao usuário.
         * </p>
         * 
         * @param stage o palco principal fornecido pelo runtime JavaFX.
         */
        @Override
        public void start(Stage stage) {
                primaryStage = stage;

                try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource(
                                        "/br/com/gabrielmkv/view/main.fxml"
                                )
                        );

                        Scene scene = new Scene(loader.load());

                        stage.setTitle("Sudoku");
                        stage.setScene(scene);
                        stage.show();
                } catch (Exception e) {
                        System.err.println("Falha ao iniciar a aplicação: " + e.getMessage());
                }

        }

        /**
         * Retorna a referência estática para o palco principal da aplicação.
         * <p>
         * Útil para que controladores e classes utilitárias (como alertas) possam definir
         * a janela proprietária (owner) de diálogos modais.
         * </p>
         * 
         * @return a instância do {@link Stage} principal.
         */
        public static Stage getStage() {
                return primaryStage;
        }

        /**
         * Ponto de entrada padrão da aplicação Java.
         * <p>
         * Invoca o método {@link #launch(String...)} para iniciar o ciclo de vida do JavaFX.
         * </p>
         * 
         * @param args argumentos de linha de comando.
         */
        public static void main(String[] args) {
                launch(args);
        }
}
