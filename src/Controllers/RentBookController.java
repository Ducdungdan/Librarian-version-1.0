/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import static Controllers.MainController.truncate;
import Models.RentBookModel;
import Object.RentBook;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Duc Dung Dan
 */
public class RentBookController {
    
    public static void tableRentBook(JTable jtable) throws SQLException, ParseException {
        if(RentBookModel.getRentBooks()) {
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
                model.addRow(new Object[]{rs.getInt("idRent"), rs.getInt("idBook"), rs.getString("Name"), rs.getString("day_borrow"), rs.getString("day_return"), status});
            }
        }
    }
    
    public static void InforRent(Number idRent, Number idBook, JLabel imageBook, JLabel nameBook, JLabel authorBook, JLabel companyBook, JLabel yearBook, JLabel typeBook, JLabel countryBook, JLabel rental) throws SQLException {
        if(RentBookModel.getRentBook(idBook, idRent)) {
            ResultSet rs = RentBookModel.rs;
            if(rs.next()) {
                nameBook.setText(truncate(rs.getString("Name"),60));
                authorBook.setText(truncate("Tác giả: " + rs.getString("Author"), 30));
                companyBook.setText(truncate(rs.getString("Publishing_company"), 30));
                yearBook.setText(truncate("Năm XB: " + rs.getString("Publishing_year"), 30));
                typeBook.setText(truncate("Thể loại: " + rs.getString("Type"), 30));
                countryBook.setText(truncate("Quốc gia: " + rs.getString("Country"), 30));
                rental.setText("Giá: " + rs.getString("rental"));

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
    }
    
    public static void rentBook(Number idBook, JTable tableListBook, Number selectRow) {
        DefaultTableModel model = (DefaultTableModel) tableListBook.getModel();
        int rental = (int) tableListBook.getValueAt((int) selectRow, 5);
        RentBook newRentBook = new RentBook();
        
        newRentBook.setIdBook(idBook);
        newRentBook.setRental((Number) model.getValueAt((int) selectRow, 5));
        if(checkOrdered(idBook)) {
            RentBookModel.rentBook.add(newRentBook);
            JOptionPane.showMessageDialog(null, "Thêm thành công đầu sách có idBook = " + idBook + " \nBạn vui lòng vào giỏ hàng để thanh toán", "Thông báo", 2);
        } else {
            JOptionPane.showMessageDialog(null, "Đầu sách có idBook = " + idBook + " đã có trong giỏ hàng", "Thông báo", 2);
        }
    }
    
    public static Boolean checkOrdered(Number idBook) {
        int orderedLeng = RentBookModel.rentBook.size();
        for(int i = 0; i < orderedLeng; ++i) {
            if(RentBookModel.rentBook.get(i).getIdBook() == idBook) {
                return false;
            }
        }
        return true;
    }
    
    public static void rentBook() {
        String question = "Bạn muốn thanh toán hóa đơn trong giỏ hàng!";
        if (JOptionPane.showConfirmDialog(null, question, "Thông báo", 2) == 0) {
            RentBookModel.setRentBook();
        }
    }
}
