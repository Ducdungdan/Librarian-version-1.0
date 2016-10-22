/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import Models.RentBookModel;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Duc Dung Dan
 */
public class RentBookController {
    
    public static void tableRentBook(JTable jtable) throws SQLException, ParseException {
        if(RentBookModel.getRentBook()) {
            ResultSet rs = Models.RentBookModel.rs;
            DefaultTableModel model = (DefaultTableModel) jtable.getModel();
            int rowCount = model.getRowCount();
            //Remove rows one by one from the end of the table
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            while(rs.next()) {
                String status;
                if(rs.getDate("day_return") != null) {
                    status = "Đã trả";
                } else {
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateNow = new Date();
                    Date dayReturn = ft.parse(MainController.addDays(rs.getDate("day_borrow"), 60));
                    if(dateNow.compareTo(dayReturn) >= 0) {
                        status = "Quá " + MainController.subDays(dateNow, dayReturn) + " ngày";
                    } else {
                        status = "Còn " + MainController.subDays(dayReturn, dateNow) + " ngày";
                    }
                }
                model.addRow(new Object[]{rs.getInt("idRent"), rs.getInt("book.idBook"), rs.getString("Name"), rs.getString("day_borrow"), rs.getString("day_return"), status});
            }
        }
    }
    
    public static void InforRent(Number idRent, Number idBook, JLabel imageBook, JLabel nameBook, JLabel authorBook, JLabel companyBook, JLabel yearBook, JLabel typeBook, JLabel countryBook, JLabel rental) throws SQLException {
        if(RentBookModel.getRentBook(idBook, idRent)) {
            ResultSet rs = RentBookModel.rs;
            if(rs.next()) {
                nameBook.setText(rs.getString("Name"));
                authorBook.setText("Tác giả: " + rs.getString("Author"));
                companyBook.setText(rs.getString("Publishing_company"));
                yearBook.setText("Năm XB: " + rs.getString("Publishing_year"));
                typeBook.setText("Thể loại: " + rs.getString("Type"));
                countryBook.setText("Quốc gia: " + rs.getString("Country"));
                rental.setText("Giá: " + rs.getString("rental"));

                if (rs.getBlob("Image") != null) {
                    Blob blob = rs.getBlob("Image");
                    ImageIcon icon = new ImageIcon(blob.getBytes(1, (int) blob.length()));
                    imageBook.setIcon(icon);
                } else {
                    String imagePath = "src/Image/avartar.png";
                    ImageIcon icon = new ImageIcon(imagePath);
                    imageBook.setIcon(icon);
                }
            }
        }
    }
    
    
    
}
