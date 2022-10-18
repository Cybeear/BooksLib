package service;

import exceptions.ParserServiceException;

public class ParserService {

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
