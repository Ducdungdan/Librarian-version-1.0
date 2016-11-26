/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Object.*;
import java.awt.HeadlessException;

/**
 *
 * @author Duc Dung Dan
 */
public class RentBookModel implements DataInterface{
    public static PreparedStatement ps;
    public static ResultSet rs;
    public static final ArrayList<RentBook> rentBook = new ArrayList<>();

    public static boolean setRentBook() {
        try {
            ps = SQLService.getConnect().prepareStatement("call datalibrary.insert_rent(?)");
            ps.setInt(1, (int) UserModel.user.getIdUser());
            rs = ps.executeQuery();
            RentBook rentb;
            if(rs.next()) {
                for(int i = 0; i < rentBook.size(); ++i) {
                    rentb = rentBook.get(i);

                    try {
                        ps = SQLService.getConnect().prepareStatement("INSERT INTO datalibrary.infor_rent (idRent, idBook, day_borrow, rental) VALUES (?, ?, NOW(), ?)");
                        ps.setInt(1, rs.getInt("LAST_INSERT_ID()"));
                        ps.setInt(2, (int) rentb.getIdBook());
                        ps.setInt(3, (int) rentb.getRental());
                        if(!ps.execute()) {
                            JOptionPane.showMessageDialog(null, "Mượn sách có id = " + rentb.getIdBook() + " thành công \n Vào trang cá nhân để kiểm tra", "Thông báo", 2);
                        }
                        
                    } catch (SQLException | HeadlessException e) {
                        if("EROOR number = 0".equals(e.getMessage())) {
                            JOptionPane.showMessageDialog(null, "Mượn sách có id = " + rentb.getIdBook() + " thất bại \n Số lượng sách đã hết", "Thông báo", 2);
                        }
                    }
                }
                rentBook.clear();
                return true;
                        
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    @Override
    public ResultSet getStatisticalData(String statisticalName) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT " + statisticalName + ", COUNT(*) FROM datalibrary.infor_rent GROUP BY " + statisticalName + ("Day_return".equals(statisticalName)?" HAVING NOT(Day_return IS NULL)":""));
            System.out.println(ps);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi", "Lỗi", 2);
            return null;
        }
    }
    
    
    public static Boolean getRentBooks() {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT rent.idRent, book.idBook, Name, day_borrow, day_return FROM datalibrary.rent, datalibrary.book, datalibrary.infor_rent WHERE infor_rent.idBook = book.idBook AND rent.idRent = infor_rent.idRent AND idUser = ?");
            ps.setInt(1, (int) UserModel.user.getIdUser());
            rs = ps.executeQuery();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi \n không lấy dữ liệu thuê sách được", "Lỗi", 2);
            return false;
        }
    }
    
    public static ResultSet getCheckOuts(String search, String typeSearch) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT DISTINCT rent.idRent, rent.idUser, Name, Email, day_borrow FROM rent, user, authentication, infor_rent WHERE rent.idUser = user.idUser AND rent.idUser = authentication.idUser AND rent.idRent = infor_rent.idRent AND (" + typeSearch + " LIKE \"%" + search + "%\")");
            return ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi \n không lấy dữ liệu thuê sách được", "Lỗi", 2);
            return null;
        }
    }
    
    public static ResultSet getInforCheckOuts(Number idRent) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT infor_rent.idBook, Name, day_borrow, day_return, rental FROM infor_rent, book WHERE infor_rent.idBook = book.idBook AND infor_rent.idRent = ?");
            ps.setInt(1, (int) idRent);
            return ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi \n không lấy dữ liệu thuê sách được", "Lỗi", 2);
            return null;
        }
    }
    
    public static ResultSet getRentBooks(Number start, String search, String typeSearch, String show) {
        String filter;
        if("all".equals(show)) {
            filter = "";
        } else {
            if("rented".equals(show)) {
                filter = "AND NOT (infor_rent.day_return IS NULL) ";
            } else {
                if("renting".equals(show)) {
                    filter = "AND date_add(infor_rent.day_borrow, INTERVAL 60 DAY) > now() AND infor_rent.day_return IS NULL ";
                } else {
                    filter = "AND date_add(infor_rent.day_borrow, INTERVAL 60 DAY) < now() AND infor_rent.day_return IS NULL ";
                }
            }
        }
        
        
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT rent.idRent, infor_rent.idBook, book.Name, user.idUser, authentication.Email, user.Name, infor_rent.day_borrow, infor_rent.day_return, infor_rent.rental FROM datalibrary.infor_rent, datalibrary.rent, datalibrary.authentication, datalibrary.user, datalibrary.book WHERE rent.idRent = infor_rent.idRent AND rent.idUser = user.idUser AND user.idUser = authentication.idUser AND infor_rent.idBook = book.idBook " + filter + "AND " + typeSearch + " LIKE \"%"+ search +"%\" LIMIT " + start + ",28 ");
            return ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi \n không lấy dữ liệu thuê sách được", "Lỗi", 2);
            return null;
        }
    }
    
    public static Boolean getRentBook(Number idBook, Number idRent) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT Image, Name, Author, Publishing_company, Publishing_year, Type, Country, rental  FROM datalibrary.infor_rent, datalibrary.book WHERE infor_rent.idBook = book.idBook AND book.idBook = ? AND idRent = ?");
            ps.setInt(1, (int) idBook);
            ps.setInt(2, (int) idRent);
            rs = ps.executeQuery();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi \n không lấy dữ liệu thuê sách được", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean setReturnBook(Number idRent, Number idBook) {
        try {
            ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.infor_rent SET day_return=NOW() WHERE idRent=? AND idBook=?");
            ps.setInt(1, (int) idRent);
            ps.setInt(2, (int) idBook);
            return !ps.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi \n tạm thời không trả sách được", "Lỗi", 2);
            return false;
        }
    }
}
