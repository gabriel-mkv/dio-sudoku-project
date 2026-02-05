package br.com.gabrielmkv.util;

import java.util.Optional;

import br.com.gabrielmkv.ui.AppFX;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * Classe utilitária responsável pela exibição de alertas e diálogos na interface gráfica JavaFX.
 * <p>
 * Esta classe centraliza a criação e estilização de pop-ups ({@link Alert}), garantindo
 * consistência visual e comportamental em toda a aplicação. Todos os alertas são
 * modais em relação ao estágio principal ({@link AppFX#getStage()}).
 * </p>
 */
public final class SudokuAlerts {
    
    /**
     * Aplica a folha de estilos CSS personalizada ao diálogo de alerta.
     * 
     * @param alert o alerta a ser estilizado.
     * @param styleClassName a classe CSS específica a ser aplicada (ex: "alert-error").
     */
    private static void applyStyle(Alert alert, String styleClassName){
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(SudokuAlerts.class.getResource("/br/com/gabrielmkv/css/alert.css")
                                                          .toExternalForm());
        dialogPane.getStyleClass().addAll("sudoku-alert", styleClassName);
    }

    /**
     * Exibe um alerta informativo ao usuário.
     *
     * @param title o título da janela do alerta.
     * @param header o cabeçalho da mensagem.
     * @param content o corpo da mensagem com os detalhes.
     */
    public static void showInformation(String title, String header, String content){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyStyle(alert, "alert-information");
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de erro padronizado para conflitos no jogo.
     * <p>
     * Este método é especializado para notificar o usuário quando existem números
     * incorretos ou inválidos no tabuleiro.
     * </p>
     */
    public static void showError(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle("Algo não está certo...");
        alert.setHeaderText("Conflito detectado!");
        alert.setContentText("Alguns números está desafiando as leis do Sudoku. Dê uma revisada nas linhas e colunas!");

        applyStyle(alert, "alert-error");
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de aviso (Warning).
     *
     * @param title o título da janela do alerta.
     * @param header o cabeçalho da mensagem.
     * @param content o corpo da mensagem de aviso.
     */
    public static void showWarning(String title, String header, String content){
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyStyle(alert, "alert-warning");
        alert.showAndWait();
    }

    /**
     * Exibe um diálogo de confirmação e aguarda a resposta do usuário.
     *
     * @param title o título da janela do alerta.
     * @param header o cabeçalho da pergunta.
     * @param content o corpo da mensagem explicando a ação.
     * @return {@code true} se o usuário clicou em OK, {@code false} caso contrário.
     */
    public static boolean showConfirmation(String title, String header, String content){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(AppFX.getStage());
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyStyle(alert, "alert-confirm");        
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
