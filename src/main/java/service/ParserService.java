package service;

public class ParserService {

    /**
     * @param array
     * @return
     */
    public boolean checkSize(String[] array) {
        return array.length < 2;
    }

    /**
     * @param data
     * @return
     */
    public boolean checkString(String data) {
        return data.equals(" ");
    }

    /**
     * @param str argument string
     * @return integer value -1 if string not contains only digits
     */
    public long parseLong(String str) {
        return this.isValidNumber(str) ? Integer.parseInt(str) : -1;
    }

    /**
     * @param str argument string
     * @return boolean true if string contains only digits
     */
    private boolean isValidNumber(String str) {
        return str.matches("^\\d+$");
    }
}
