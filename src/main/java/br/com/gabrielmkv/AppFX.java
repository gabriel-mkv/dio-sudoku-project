package br.com.gabrielmkv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppFX extends Application {

        private static Stage primaryStage;

        @Override
        public void start(Stage stage) throws Exception {
                primaryStage = stage;

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/br/com/gabrielmkv/view/main.fxml"
                        )
                );

                Scene scene = new Scene(loader.load());

                // CSS global
                scene.getStylesheets().add(
                        getClass().getResource(
                                "/br/com/gabrielmkv/css/global.css"
                        ).toExternalForm()
                );

                stage.setTitle("Sudoku");
                stage.setScene(scene);
                stage.show();

        }

        public static Stage getStage() {
                return primaryStage;
        }

        public static void main(String[] args) {
                launch(args);
        }
}
