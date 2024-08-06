package org.tba.paralleltranslator.models;

/**
 * Represents a language with a code and a name.
 * Used to describe supported languages for translation.
 */
public class Language
{
    private String code;
    private String name;

    /**
     * Constructs a new Language with the specified code and name.
     *
     * @param code the language code (e.g., "en" for English)
     * @param name the language name (e.g., "English")
     */
    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the language code.
     *
     * @return the language code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the language name.
     *
     * @return the language name
     */
    public String getName() {
        return name;
    }
}
