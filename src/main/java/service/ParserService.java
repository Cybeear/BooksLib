package service;

public class ParserService {

    /**
     * @param str argument string
     * @return integer value -1 if string not contains only digits
     */
    public static int parseInt(String str) {
        return isValidNumber(str) ? Integer.parseInt(str) : -1;
    }

    /**
     * @param str argument string
     * @return boolean true if string contains only digits
     */
    private static boolean isValidNumber(String str) {
        return str.matches("^\\d+$");
    }
}
