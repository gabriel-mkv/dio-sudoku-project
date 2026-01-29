package br.com.gabrielmkv.util;

/**
 * Conversor de símbolos para o jogo do Sudoku.
 * <p>
 * Esta classe contém métodos estáticos para converter números inteiros em caracteres e vice-versa.
 * <p>
 * O método {@link #converterIntToChar(int)} converte um número inteiro em um caractere correspondente.
 * <p>
 * O método {@link #converterCharToInteger(char)} converte um caractere em um número inteiro correspondente.
 */
public final class SymbolConverter {
    
    private SymbolConverter() {}

    /**
     * Converte um número inteiro em um caractere correspondente.
     *
     * @param number o número inteiro a ser convertido
     * @return o caractere correspondente ao número inteiro
     */
    public static String converterIntToChar(int number) {
        String symbols = "123456789ABCDEFG";
        return String.valueOf(symbols.charAt(number - 1));
    }

    /**
     * Converte um caractere em um número inteiro correspondente.
     *
     * @param symbol o caractere a ser convertido
     * @return o número inteiro correspondente ao caractere
     */
    public static Integer converterCharToInteger(char symbol){
        if (symbol >= '1' && symbol <= '9') {
            return symbol - '0';
        } else {
            return symbol - 'A' + 10;
        }
    }

}
