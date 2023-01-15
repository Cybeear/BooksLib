package com.bookslib.app.service;

import com.bookslib.app.exceptions.ParserServiceException;
import com.bookslib.app.service.ParserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserServiceTest {

    ParserService parserService = new ParserService();

    @Test
    void parseIntWithInValidDataInString() {
        assertThrows(ParserServiceException.class, () -> parserService.parseLong("test string"));
    }

    @Test
    void parseIntWithValidDataInString() {
        assertAll(() -> assertEquals(5, parserService.parseLong("5")),
                () -> assertDoesNotThrow(() -> parserService.parseLong("5")));

    }

    @Test
    void parseIntWithFloatNumberInString() {
        assertThrows(ParserServiceException.class, () -> parserService.parseLong("423.42"));
    }
}