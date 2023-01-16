package com.bookslib.app.service;

import com.bookslib.app.dao.BookDaoPostgresqlImpl;
import com.bookslib.app.exceptions.ParserServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParserService {

    private static final Logger log = LoggerFactory.getLogger(BookDaoPostgresqlImpl.class);

    /**
     * @param str argument string
     * @return integer value -1 if string not contains only digits
     */
    public long parseLong(String str) {
        try {
            if (str.isBlank()) {
                throw new ParserServiceException("Your entered data can not be an empty string!");
            }
            return Long.parseLong(str);
        } catch (NumberFormatException numberFormatException) {
            throw new ParserServiceException("string is not contains only digits!");
        }
    }
}
