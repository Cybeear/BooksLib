package service;

import exceptions.ParserServiceException;

public class ParserService {

    /**
     * @param str argument string
     * @return integer value -1 if string not contains only digits
     */
    public long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException numberFormatException) {
            throw new ParserServiceException("string is not contains only digits!");
        }
    }
}
