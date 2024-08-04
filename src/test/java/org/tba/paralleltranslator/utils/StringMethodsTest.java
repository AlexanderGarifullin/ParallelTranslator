package org.tba.paralleltranslator.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringMethodsTest {

    @Test
    public void testFindFirstLetterIndex() {
        assertEquals(0, StringMethods.findFirstLetterIndex("Hello"));
        assertEquals(2, StringMethods.findFirstLetterIndex("  Hello"));
        assertEquals(3, StringMethods.findFirstLetterIndex("123a456"));
        assertEquals(-1, StringMethods.findFirstLetterIndex("123456"));
        assertEquals(1, StringMethods.findFirstLetterIndex("!a"));
    }

    @Test
    public void testFindLastLetterIndex() {
        assertEquals(4, StringMethods.findLastLetterIndex("Hello"));
        assertEquals(6, StringMethods.findLastLetterIndex("  Hello"));
        assertEquals(3, StringMethods.findLastLetterIndex("123a"));
        assertEquals(-1, StringMethods.findLastLetterIndex("123456"));
        assertEquals(0, StringMethods.findLastLetterIndex("a!"));
    }

    @Test
    public void testGetCleanWorld() {
        assertEquals("Hello", StringMethods.getCleanWorld("Hello"));
        assertEquals("Hello", StringMethods.getCleanWorld("  Hello  "));
        assertEquals("a", StringMethods.getCleanWorld("123a456"));
        assertEquals("", StringMethods.getCleanWorld("123456"));
        assertEquals("a", StringMethods.getCleanWorld("!a!"));
    }

    @Test
    public void testReplaceWorld() {
        assertEquals("Hola", StringMethods.replaceWorld("Hello", "Hola"));
        assertEquals("  Hola  ", StringMethods.replaceWorld("  Hello  ", "Hola"));
        assertEquals("123Hola456", StringMethods.replaceWorld("123Hello456", "Hola"));
        assertEquals("123456", StringMethods.replaceWorld("123456", "Hola"));
        assertEquals("!231Hola", StringMethods.replaceWorld("!231Hello", "Hola"));
        assertEquals("Hola!123", StringMethods.replaceWorld("Hello!123", "Hola"));
    }
}