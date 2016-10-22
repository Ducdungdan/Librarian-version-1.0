/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javax.swing.*;
import java.sql.*;

/**
 *
 * @author Duc Dung Dan
 */
public class SQLService {
    
    private static Connection connection;

    public static Connection getConnect() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/datalibrary", "root",  "chungtoilawindow");
            System.out.println("Kết nối databse thành công");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Kết nối dữ liệu thất bại", "Thông báo", 1);
            System.out.println("Kết nối databse không thành công");
            System.err.println(exception.getMessage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Kết nối dữ liệu thất bại", "Thông báo", 1);
        }
            return connection;
    }

//    public boolean getData(String[] columnNames, String table) throws SQLException {
//        String columns = "";
//        int columnLength = columnNames.length;
//        for (int i = 0; i < columnLength; ++i) {
//            if (i == columnLength - 1) {
//                columns += columnNames[i];
//            } else {
//                columns += columnNames[i] + ", ";
//            }
//        }
//
//        String query = "SELECT " + columns + " FROM " + table;
//
//        try {
//            this.result = this.statement.executeQuery(query);
//            return true;
//        } catch (SQLException exception) {
//            System.out.println("Sai tên column hoặc table");
//            System.err.println(exception.getMessage());
//            return false;
//        }
//    }
//
//    public boolean updateDataBook(String commands, String idBook, String Data) throws SQLException {
//        String query ="";
//        switch (commands) {
//            case "DELETE": {
//                query = String.format("DELETE FROM %s.book WHERE idBook=%s", this.database, idBook);
//                return this.statement.execute(query);
//            }
//            case "INSERT": {
//                query = String.format("INSERT INTO %s.book (idBook, Name, Author, Publishing_company, Publishing_year, Type, Country, Numbers) VALUES (%s)", this.database, Data);
//                return this.statement.execute(query);
//            }
//            default: {
//                query = String.format("UPDATE %s.book SET %s WHERE idBook=%s", this.database, Data, idBook);
//                return this.statement.execute(query);
//            }
//        }
//    }
//
//      
//    public boolean updateAuthentication(String commands, String idUser, String Data) throws SQLException {
//        String query ="";
//        switch (commands) {
//            case "INSERT": {
//                query = String.format("INSERT INTO %s.authentication (idUser, Authentication) VALUES (%s)", this.database, Data);
//                return this.statement.execute(query);
//            }
//            default: {
//                query = String.format("UPDATE %s.authentication SET %s WHERE idUser=%s", this.database, Data, idUser);
//                return this.statement.execute(query);
//            }
//        }
//    }
//
//    public boolean updateRent(String commands, String idUser, String idBook, String Data) throws SQLException {
//        String query = "";
//        switch (commands) {
//            case "DELETE": {
//                query = String.format("DELETE FROM %s.user_has_book WHERE idBook=%s, idUser=%s", this.database, idBook, idUser);
//                return this.statement.execute(query);
//            }
//            case "INSERT": {
//                query = String.format("INSERT INTO %s.user_has_book  (User_idUser, Book_idBook, Day_borrow, Day_return, Value) VALUES (%s)", this.database, Data);
//                return this.statement.execute(query);
//            }
//            default: {
//                query = String.format("UPDATE %s.user_has_book SET %s WHERE idBook=%s, idUser=%s", this.database, Data, idBook, idUser);
//                return this.statement.execute(query);
//            }
//        }
//
//    }
//
//    public void stop() throws SQLException {
//        try {
//            this.result.close();
//            this.statement.close();
//            this.connection.close();
//        } catch (SQLException e) {
//        }
//    }
//
//    public boolean findidUser(String text) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
