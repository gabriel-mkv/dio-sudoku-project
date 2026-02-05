package br.com.gabrielmkv;

import java.util.Arrays;

import br.com.gabrielmkv.ui.AppConsole;
import br.com.gabrielmkv.ui.AppFX;

/**
 * Ponto de entrada unificado da aplicação.
 * <p>
 * Esta classe decide qual interface iniciar (Console ou GUI) baseada nos argumentos
 * de linha de comando fornecidos.
 * </p>
 */
public class App {

    public static void main(String[] args) {
        boolean isConsole = Arrays.asList(args).contains("--console") || 
                            Arrays.asList(args).contains("-c") ||
                            Arrays.asList(args).contains("console");

        if (isConsole) {
            AppConsole.main(args);
        } else {
            AppFX.main(args);
        }
    }
}