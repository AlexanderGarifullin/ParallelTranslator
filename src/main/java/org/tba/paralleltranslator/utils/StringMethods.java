package org.tba.paralleltranslator.utils;

/**
 * Utility class for various string manipulation methods.
 */
public class StringMethods {

    /**
     * Returns a cleaned version of the input text,
     * which contains only the part of the text between the first and last letter.
     *
     * @param text the input text to clean
     * @return a substring of the input text that contains only the part between the first and last letter
     */
    public static String getCleanWorld(String text) {
        int firstIndex = findFirstLetterIndex(text);
        int lastIndex = findLastLetterIndex(text);

        if (firstIndex == -1 || lastIndex == -1) {
            return "";
        }

        return text.substring(firstIndex, lastIndex + 1);
    }

    /**
     * Replaces the part of the text between the first and last letter with a new word.
     *
     * @param text the input text where the replacement will be made
     * @param newWorld the new word to insert
     * @return the text with the specified replacement
     */
    public static String replaceWorld(String text, String newWorld) {
        int firstIndex = findFirstLetterIndex(text);
        int lastIndex = findLastLetterIndex(text);

        if (firstIndex == -1 || lastIndex == -1) {
            return text;
        }

        String before = text.substring(0, firstIndex);
        String after = text.substring(lastIndex + 1);
        return before + newWorld + after;
    }

    /**
     * Finds the index of the first letter in the text.
     *
     * @param text the input text
     * @return the index of the first letter, or -1 if no letter is found
     */
    public static int findFirstLetterIndex(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the index of the last letter in the text.
     *
     * @param text the input text
     * @return the index of the last letter, or -1 if no letter is found
     */
    public static int findLastLetterIndex(String text) {
        for (int i = text.length() - 1; i >= 0; i--) {
            if (Character.isLetter(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
