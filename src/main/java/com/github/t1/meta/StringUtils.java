package com.github.t1.meta;

public class StringUtils {
    public static String initLower(String string) {
        return Character.toLowerCase(string.charAt(0)) + string.substring(1);
    }

    public static String initUpper(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String camelToSpace(String string) {
        StringBuilder out = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (Character.isUpperCase(c))
                out.append(' ').append(c);
            else
                out.append(c);
        }
        return out.toString();
    }
}
