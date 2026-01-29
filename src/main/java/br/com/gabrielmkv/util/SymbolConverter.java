package br.com.gabrielmkv.util;

public final class SymbolConverter {
    
    private SymbolConverter() {}

    public static String converterIntToChar(int number) {
        String symbols = "123456789ABCDEFG";
        return String.valueOf(symbols.charAt(number - 1));
    }

    public static Integer converterCharToInteger(char symbol){
        if (symbol >= '1' && symbol <= '9') {
            return symbol - '0';
        } else {
            return symbol - 'A' + 10;
        }
    }

}
