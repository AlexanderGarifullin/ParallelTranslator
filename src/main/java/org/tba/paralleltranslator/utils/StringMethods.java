package org.tba.paralleltranslator.utils;

public class StringMethods {

    public static String getCleanWorld(String text) {
        int firstIndex = findFirstLetterIndex(text);
        int lastIndex = findLastLetterIndex(text);

        if (firstIndex == -1 || lastIndex == -1) {
            return "";
        }

        return text.substring(firstIndex, lastIndex + 1);
    }

    public static String replaceWorld(String text, String translatedText) {
        int firstIndex = findFirstLetterIndex(text);
        int lastIndex = findLastLetterIndex(text);

        if (firstIndex == -1 || lastIndex == -1) {
            return text;
        }

        String before = text.substring(0, firstIndex);
        String after = text.substring(lastIndex + 1);
        return before + translatedText + after;
    }


    public static int findFirstLetterIndex(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public static int findLastLetterIndex(String text) {
        for (int i = text.length() - 1; i >= 0; i--) {
            if (Character.isLetter(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
