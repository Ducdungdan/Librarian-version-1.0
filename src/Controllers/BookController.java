/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static Controllers.MainController.truncate;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Models.BookModel;
import java.sql.Blob;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Duc Dung Dan
 */
public class BookController {
    public static int start = 0;
    public static int pageBook = 0;
    public static String search = "";
    public static String typeSearch = "idBook";
    
    
    public static Boolean loadTableListBooks(JTable listBook) {
        ResultSet rs = BookModel.getDataBooks(start, search, typeSearch);
        try {
            if(rs.next()) {
                DefaultTableModel model = (DefaultTableModel) listBook.getModel();
                int rowCount = model.getRowCount();
                //Remove rows one by one from the end of the table
                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }

                do {
                    model.addRow(new Object[]{rs.getInt("idBook"), rs.getString("Name"), rs.getString("Author"), rs.getString("Type"), rs.getInt("Numbers"), rs.getInt("Value")});
                    ++start;
                } while (rs.next());
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public static void nextTableListBooks(JTable listBook, JLabel page) {
        if(loadTableListBooks(listBook)) {
            ++pageBook;
            page.setText(String.valueOf(pageBook));
        }
    }
    
    public static void prevTableListBooks(JTable listBook, JLabel page) {
        if (pageBook > 0) {
            int rowCount = listBook.getRowCount();
            //Remove rows one by one from the end of the table
            start = start - 20 - rowCount;
            if (loadTableListBooks(listBook)) {
                --pageBook;
                page.setText(String.valueOf(pageBook));
            }
        }
        
    }
    
    public static void searchTableListBooks(JTable listBook, JLabel page, String inputSearch, String inputTypeSearch) {
        switch (inputTypeSearch) {
            case "id":
                typeSearch = "idBook";
                break;
            case "Tên sách":
                typeSearch = "Name";
                break;
            case "Tác giả":
                typeSearch = "Author";
                break;
            default:
                typeSearch = "Type";
        }
        
        search = inputSearch;
        pageBook = 0;
        start = 0;
        page.setText(String.valueOf(pageBook));
        if(!loadTableListBooks(listBook)) {
            JOptionPane.showMessageDialog(null, "Không tìm được kết quả nào thỏa mãn", "Thông báo", 2);
        }
    }

    public static void viewBook(Number idBook, JLabel name, JLabel author, JTextArea content, JLabel company, JLabel year, JLabel type, JLabel value, JLabel country, JLabel imageBook) throws SQLException {
        ResultSet rs = BookModel.getDataBook(idBook);
        rs.next();
        name.setText(truncate(rs.getString("Name"), 60));
        author.setText(truncate("Tác giả: " + rs.getString("Author"), 30));
        content.setText(rs.getString("Content"));
        company.setText(truncate(rs.getString("Publishing_company"), 30));
        year.setText(truncate("Năm xuất bản: " + rs.getString("Publishing_year"), 30));
        type.setText(truncate("Thể loại: " + rs.getString("Type"), 30));
        value.setText("Tiền mượn: " + rs.getString("Value"));
        country.setText(truncate("Quốc gia: " + rs.getString("Country"), 30));
        if (rs.getBlob("Image") != null) {
            Blob blob = rs.getBlob("Image");
            ImageIcon icon = new ImageIcon(blob.getBytes(1, (int) blob.length()));
            imageBook.setIcon(icon);
        } else {
            String imagePath = "src/Image/book-null.png";
            ImageIcon icon = new ImageIcon(imagePath);
            imageBook.setIcon(icon);
        }
    }
    
    
}
