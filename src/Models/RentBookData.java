/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Controllers.MainController.addDays;
import java.util.Date;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Duc Dung Dan
 */
public class RentBookData implements DataInterface{
    public static PreparedStatement ps;
    public static ResultSet rs;
    
    @Override
    public ResultSet getStatisticalData(String statisticalName) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT " + statisticalName + ", COUNT(*) FROM datalibrary.rentbook GROUP BY " + statisticalName);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi", "Lỗi", 2);
            return null;
        }
    }
    
    
    public static Boolean getRentBookData(Number start, String search, String typeSearch) throws ParseException, SQLException {
        try {
            if ("Day_return".equals(typeSearch) && !"".equals(search)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                search = addDays(format.parse(search), -160);
                typeSearch = "Day_borrow";
            }
            ps = SQLService.getConnect().prepareStatement("SELECT rentbook.idUser, rentbook.idBook, Day_borrow, Day_return, rentbook.Value, Name, Author FROM rentbook, book WHERE " + (UserData.user.getAdmin()?" ":"(rentbook.idUser =" + UserData.user.getIdUser().toString() + ") AND ") + "(" + typeSearch + " LIKE \"%" + search + "%\") AND (Day_return = \"1970-01-01\") AND (book.idBook = rentbook.idBook) LIMIT " + start + ",18");
            rs = ps.executeQuery();
            return true;
        } catch (ParseException | SQLException e) {
            return false;
        }
    }
    
    public static Boolean setRentBookData(Number idUser, Number idBook, Number Fine) {
        try {
            ps = SQLService.getConnect().prepareStatement("INSERT INTO datalibrary.rentbook (idUser, idBook, Day_borrow, Day_return, Value) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, (int) idUser);
            ps.setInt(2, (int) idBook);
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            ps.setString(3, ft.format(date));
            ps.setString(4, "1970-01-01");
            ps.setInt(5, (int) Fine);
            if(!ps.execute()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Bạn đang mượn đầu sách này", "Lỗi", 2);
                return false;
            }
        } catch (SQLException | HeadlessException e) {
            if("Numbers = 0".equals(e.getMessage())) {
                JOptionPane.showMessageDialog(null, "Không mượn được đầu sách này \n Số lượng đã hết", "Thông báo", 2);
            } else {
                if("Duplicate entry \'1-2\' for key \'PRIMARY\'".equals(e.getMessage())){
                    JOptionPane.showMessageDialog(null, "Bạn đã mượn đầu sách này rồi", "Thông báo", 2);
                } else {
                    JOptionPane.showMessageDialog(null, "Bạn đang mượn đầu sách này", "Lỗi", 2);
                }   
            }
            return false;
        }
    }
    
    public static Boolean getRentedBookData(Number start, String search, String typeSearch) {

        try {
            ps = SQLService.getConnect().prepareStatement("SELECT rentbook.idUser, rentbook.idBook, Day_borrow, Day_return, rentbook.Value, Name, Author FROM rentbook, book WHERE " + (UserData.user.getAdmin()?"":"(rentbook.idUser =" + UserData.user.getIdUser().toString() + ") AND ") + "(" + typeSearch + " LIKE \"%" + search + "%\") AND (Day_return != \"1970-01-01\") AND (book.idBook = rentbook.idBook) LIMIT " + start + ",18");
            rs = ps.executeQuery();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Boolean returnBook(Number idBook, Number idUser) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT Day_return FROM datalibrary.rentbook WHERE idUser=? AND idBook=?");
            ps.setString(1, idUser.toString());
            ps.setString(2, idBook.toString());
            rs = ps.executeQuery();
            if (rs.next() && UserData.user.getAdmin()) {
                if(!"1970-01-01".equals(rs.getString("Day_return"))) {
                    JOptionPane.showMessageDialog(null, "idUser = " + idUser + " đã trả sách có idBook = " + idBook + " ngày " + rs.getString("Day_return"), "Lỗi", 2);
                    return false;
                } else {
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                    ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.rentbook SET Day_return=? WHERE idUser=? and`idBook`=?");
                    ps.setString(1, ft.format(date));
                    ps.setString(2, idUser.toString());
                    ps.setString(3, idBook.toString());
                    
                    if(!ps.execute()) {
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Trả sách không thành công", "Lỗi", 2);
                        return false;   
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "idUser = " + idUser + " không mượn sách có idBook = " + idBook, "Lỗi", 2);
                return false;
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Trả sách không thành công", "Lỗi", 2);
            return false;
        }
        
    }
    
    public static Boolean getOutOfDate(Number start, String search, String typeSearch) {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ("Day_return".equals(typeSearch) && !"".equals(search)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                search = addDays(format.parse(search), -160);
                typeSearch = "Day_borrow";
            }
            ps = SQLService.getConnect().prepareStatement("SELECT rentbook.idUser, rentbook.idBook, Day_borrow, Day_return, rentbook.Value, Name, Author FROM rentbook, book WHERE " + (UserData.user.getAdmin() ? "" : "(rentbook.idUser =" + UserData.user.getIdUser().toString() + ") AND ") + "(" + typeSearch + " LIKE \"%" + search + "%\") AND (Day_return = \"1970-01-01\") AND (book.idBook = rentbook.idBook) AND (Day_borrow + 15 > " + ft.format(date) + ") LIMIT " + start + ",18");
            System.out.println(ps);
            rs = ps.executeQuery();
            return true;
        } catch (ParseException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Không lấy được dữ liệu \n Vui lòng thử lại sau 5 phút", "Lỗi", 2);
            return false;
        }
    }
    
//    public static Boolean fineBook(Number idBook, Number idUser, Number fine) {
//        try {
//            ps = SQLService.getConnect().prepareStatement("SELECT Day_return FROM datalibrary.rentbook WHERE idUser=? AND idBook=?");
//            ps.setString(1, idUser.toString());
//            ps.setString(2, idBook.toString());
//            rs = ps.executeQuery();
//            if (rs.next() && UserData.user.getAdmin()) {
//                if (!"1970-01-01".equals(rs.getString("Day_return"))) {
//                    JOptionPane.showMessageDialog(null, "idUser = " + idUser + " đã trả sách có idBook = " + idBook + " ngày " + rs.getString("Day_return"), "Lỗi", 2);
//                    return false;
//                } else {
//                    Date date = new Date();
//                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
//                    ps = SQLService.getConnect().prepareStatement("INSERT INTO datalibrary.outofdate (idUser, idBook, Day_return, Fine) VALUES (?, ?, ?, ?)");
//                    ps.setInt(1, (int) idUser);
//                    ps.setInt(2, (int) idBook);
//                    ps.setString(3, ft.format(date));
//                    ps.setInt(4, (int) fine);
//
//                    if (!ps.execute()) {
//                        ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.rentbook SET Day_return=? WHERE idUser=? and idBook=?");
//                        ps.setString(1, ft.format(date));
//                        ps.setInt(2, (int) idUser);
//                        ps.setInt(3, (int) idBook);
//                        if (!ps.execute()) {
//                            return true;
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Nộp phạt không thành công", "Lỗi", 2);
//                            return false;
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Nộp phạt không thành công", "Lỗi", 2);
//                        return false;
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "idUser = " + idUser + " không mượn sách có idBook = " + idBook, "Lỗi", 2);
//                return false;
//            }
//        } catch (SQLException | HeadlessException e) {
//            JOptionPane.showMessageDialog(null, "Nộp phạt không thành công", "Lỗi", 2);
//            return false;
//        }
//
//    }
}
