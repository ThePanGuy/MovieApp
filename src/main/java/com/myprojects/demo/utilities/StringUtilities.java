package com.myprojects.demo.utilities;

public class StringUtilities {

    private StringUtilities() {
    }

    /**
     * Handles stings with only letters, not digits
     * Also has problem with words like ThisIsAWord, will wrongly convert them to this_is_aword
     * @param str
     * @return
     */
    public static String camelCaseToSnakeCase(String str) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return str.replaceAll(regex, replacement)
                .toLowerCase();
    }
}
