package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserServiceTest {

    ParserService parserService = new ParserService();

    @Test
    void parseIntWithInValidDataInString() {
        assertEquals(-1, parserService.parseLong("test string"));
    }

    @Test
    void parseIntWithValidDataInString() {
        assertEquals(5, parserService.parseLong("5"));
    }

    @Test
    void parseIntWithFloatNumberInString() {
        assertEquals(-1, parserService.parseLong("423.42"));
    }

    @Test
    void checkStringIfStringIsNotEmpty() {
        assertFalse(parserService.checkString("test string"));
    }

    @Test
    void checkStringIfStringIsEmpty() {
        assertTrue(parserService.checkString(" "));
    }
}