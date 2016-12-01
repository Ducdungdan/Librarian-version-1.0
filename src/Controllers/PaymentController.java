/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import javax.swing.JLabel;
import javax.swing.JTable;
import Models.RentBookModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Duc Dung Dan
 */
public class PaymentController {
    public static int start = 0;
    public static int pageRent = 0;
    public static String search = "";
    public static String typeSearch = "rent.idRent";
    public static String show = "all";

    public static Boolean loadTableListRentBook(JTable listBook) {
        ResultSet rs = RentBookModel.getRentBooks(start, search, typeSearch, show);
        try {
            if (rs.next()) {
                DefaultTableModel model = (DefaultTableModel) listBook.getModel();
                int rowCount = model.getRowCount();
                //Remove rows one by one from the end of the table
                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }

                do {
                    int fine = 0;
                    if(rs.getInt("fine") == 0) {
                        DateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateNow = new Date();

                        if(dateNow.compareTo(rs.getDate("expected_day_return")) > 0) {
                            int d = MainController.subDays(dateNow, rs.getDate("expected_day_return"));
                            fine = d*rs.getInt("rental")/10;
                        }
                    } else {
                        fine = rs.getInt("fine");
                    }
                    
                    
                    model.addRow(new Object[]{rs.getInt("idRent"), rs.getInt("idBook"), rs.getString("book.Name"), rs.getInt("idUser"), rs.getString("Email"), rs.getString("user.Name"), rs.getString("day_borrow"), rs.getString("expected_day_return"), rs.getString("day_return"), rs.getString("rental"), fine});
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

    public static void nextTablePayment(JTable informationRent, JLabel page) {
        if(loadTableListRentBook(informationRent)) {
            ++pageRent;
            page.setText(String.valueOf(pageRent));
        } else {
            JOptionPane.showMessageDialog(null, "Đây là trang cuối", "Thông báo", 2);
        }
    }

    public static void prevTablePayment(JTable informationRent, JLabel page) {
        if (pageRent > 0) {
            int rowCount = informationRent.getRowCount();
            //Remove rows one by one from the end of the table
            start = start - 28 - rowCount;
            if (loadTableListRentBook(informationRent)) {
                --pageRent;
                page.setText(String.valueOf(pageRent));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Đây là trang đầu", "Thông báo", 2);
        }
    }

    public static void searchTablePayment(JTable informationRent, JLabel page, String inputSearch, String Type) {
        switch (Type) {
            case "idRent":
                typeSearch = "rent.idRent";
                break;
            case "idBook":
                typeSearch = "infor_rent.idBook";
                break;
            case "Tên sách":
                typeSearch = "book.Name";
                break;
            case "idUser":
                typeSearch = "user.idUser";
                break;
            case "Email":
                typeSearch = "Email";
                break;
            case "Tên người dùng":
                typeSearch = "user.Name";
                break;
            case "Ngày mượn":
                typeSearch = "day_borrow";
                break;
            default:
                typeSearch = "day_return";
        }

        search = inputSearch;
        pageRent = 0;
        start = 0;
        page.setText(String.valueOf(pageRent));
        if (!loadTableListRentBook(informationRent)) {
            JOptionPane.showMessageDialog(null, "Không tìm được kết quả nào thỏa mãn", "Thông báo", 2);
        }
    }
    
    public static void showTablePayment(JTable informationRent, JLabel page, String typeShow) {
        show = typeShow;

        pageRent = 0;
        start = 0;
        page.setText(String.valueOf(pageRent));
        if (!loadTableListRentBook(informationRent)) {
            JOptionPane.showMessageDialog(null, "Không tìm được kết quả nào thỏa mãn", "Thông báo", 2);
        }
    }
    
    public static void returnBook(Number idRent, Number idBook, Number fine) {
        if(RentBookModel.setReturnBook(idRent, idBook, fine)) {
            JOptionPane.showMessageDialog(null, "Trả sách thành công", "Thông báo", 2);
        } else {
            JOptionPane.showMessageDialog(null, "Trả sách thất bại", "Thông báo", 2);
        }
    }
    
}
