package Core;

import java.sql.*;

public class Fuctions {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";

    public static class Book {

        public static String getAllBooks() throws SQLException {
            var con = connect();
            var state = con.prepareStatement("Select * from book");
            var rs = state.executeQuery();
            var str = new StringBuilder("All data in table Book:\n|");

            var meta = rs.getMetaData();
            for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                str.append(meta.getColumnName(i) + "\t");
            }

            str.append("|\n");
            while (rs.next()) {
                str.append("|");
                for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                    str.append(rs.getString(i) + "\t");
                }
                str.append("|\n");
            }
            con.close();
            return str.toString();
        }

        private static ResultSet getBookById(int id) throws SQLException {
            var con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            var sql = "Select * from book where id = ?";
            var state = con.prepareStatement(sql);
            state.setInt(1, id);
            return state.executeQuery();
        }

        public static String getBookByAuthor(String name) throws SQLException {
            var con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            var sql = "Select * from book where name = ?";
            var state = con.prepareStatement(sql);
            state.setString(1, name);
            var rs = state.executeQuery();
            var str = new StringBuilder("All data about Book where name = ?:\n|".formatted(name));
            var meta = rs.getMetaData();
            for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                str.append(meta.getColumnName(i) + "\t");
            }
            str.append("|\n");
            while (rs.next()) {
                str.append("|");
                for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                    str.append(rs.getString(i) + "\t");
                }
                str.append("|\n");
            }
            con.close();
            return str.toString();
        }

        public static void CreateBook(String data) throws SQLException {
            var con = connect();
            var _data = data.replace(" / ", " ").split(" ");
            var sql = "INSERT INTO book( name, author) VALUES(?, ?)";
            if (_data.length < 2 || _data.length > 2) {
                System.out.println("Error enter a valid data!");
                return;
            }
            var state = con.prepareStatement(sql);
            state.setString(1, _data[0]);
            state.setString(2, _data[1]);
            var rs = state.executeUpdate();
            con.close();
            if (rs == 1) {
                System.out.println("Success added new Book");
            } else System.out.println("Error enter a valid data!");
        }
    }

    public static class Reader {

        public static String getAllReaders() throws SQLException {
            var con = connect();
            var state = con.prepareStatement("Select * from reader");
            var rs = state.executeQuery();
            var str = new StringBuilder("All data in table:\n|");

            var meta = rs.getMetaData();
            for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                str.append(meta.getColumnName(i) + "\t");
            }

            str.append("|\n");
            while (rs.next()) {
                str.append("|");
                for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                    str.append(rs.getString(i) + "\t");
                }
                str.append("|\n");
            }
            con.close();
            return str.toString();
        }

        private static ResultSet getAllFromReaderById(int id) throws SQLException {
            var con = connect();
            var sql = "Select * from reader where id = ?";
            var state = con.prepareStatement(sql);
            ResultSet rs;
            state.setInt(1, id);
            try {
                rs = state.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            con.close();
            return rs;
        }

        public static void CreateReader(String name) throws SQLException {
            var con = connect();
            var data = name.replaceAll("([\\.,@#$!/}{\\]\\[()\";:'><|`*&^%])", "");
            var sql = "INSERT INTO reader(name) Values(?)";
            var state = con.prepareStatement(sql);
            state.setString(1, data);
            var rs = state.executeUpdate();
            if (rs == 1) {
                System.out.println("Successful create reader name : %s".formatted(data));
            } else System.out.println("Error please enter valid data");
            con.close();
        }
    }

    private static int[] parseId(String data) {
        var _data = data.replace(" / ", " ").split(" ");
        return new int[]{Integer.parseInt(_data[0]), Integer.parseInt(_data[1])};
    }

    public static class Borrow {
        public static void borrowBook(String data) throws SQLException {
            var ids = parseId(data);
            if (ids == null) return;
            var reader = Reader.getAllFromReaderById(ids[0]);
            reader.next();
            var book = Book.getBookById(ids[1]);
            book.next();
            var con = connect();
            var sql = "INSERT INTO borrow(reader_id, book_id, returned) VALUES (?, ?, false)";
            var state = con.prepareStatement(sql);
            state.setInt(1, ids[0]);
            state.setInt(2, ids[1]);
            if (reader == null || book == null) return;
            state.executeUpdate();
            System.out.println("Successful, %s borrow the book %s"
                    .formatted(reader.getString("name"),
                            book.getString("name")));
            con.close();
        }

        public static void returnBook(String data) throws SQLException {
            var ids = parseId(data);
            if (ids == null) return;
            var con = connect();
            var sql = "UPDATE borrow set returned = true where book_id = ? AND reader_id = ?";
            var state = con.prepareStatement(sql);
            state.setInt(1, ids[0]);
            state.setInt(2, ids[1]);
            if (state.executeUpdate() == 1){
                System.out.println("Successful returned book");
            }else System.out.println("Error enter a valid data!");
            con.close();
        }

        public static void showAllBorrowedBooksById(int id) throws SQLException {
            var con = connect();
            var sql = "SELECT b.name, b. author FROM book b\n" +
                    "LEFT OUTER JOIN borrow bor ON bor.book_id = b.id\n" +
                    " WHERE bor.reader_id = ? AND bor.returned = False";
            var state = con.prepareStatement(sql);
            state.setInt(1, id);
            var rs = state.executeQuery();
            var str = new StringBuilder("|");
            var meta = rs.getMetaData();
            for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                str.append(meta.getColumnName(i) + "\t");
            }

            str.append("|\n");
            while (rs.next()) {
                str.append("|");
                for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                    str.append(rs.getString(i) + "\t");
                }
                str.append("|\n");
            }
            con.close();
            System.out.println(str.toString());
        }

        public static void showCurrentReaderByBookId(int id) throws SQLException {
            var con = connect();
            var sql = "SELECT r.id, r.name FROM reader r\n" +
                    "LEFT OUTER JOIN borrow bor ON bor.reader_id = r.id\n" +
                    "WHERE bor.book_id = ? AND bor.returned = False";
            var state = con.prepareStatement(sql);
            state.setInt(1, id);
            var rs = state.executeQuery();
            var str = new StringBuilder("|");
            var meta = rs.getMetaData();
            for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                str.append(meta.getColumnName(i) + "\t");
            }
            str.append("|\n");
            while (rs.next()) {
                str.append("|");
                for (var i = 1; i < meta.getColumnCount() + 1; i++) {
                    str.append(rs.getString(i) + "\t");
                }
                str.append("|\n");
            }
            con.close();
            System.out.println(str.toString());

        }
    }

    private static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }

        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
