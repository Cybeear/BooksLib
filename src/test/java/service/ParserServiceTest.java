package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserServiceTest {

    ParserService parserService = new ParserService();

    @Test
    void parseIntWithInValidDataInString() {
        assertEquals(-1, parserService.parseInt("test string"));
    }

    @Test
    void parseIntWithValidDataInString() {
        assertEquals(5, parserService.parseInt("5"));
    }

    @Test
    void parseIntWithFloatNumberInString() {
        assertEquals(-1, parserService.parseInt("423.42"));
    }
}