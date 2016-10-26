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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Object.*;

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
                    try {
                        rentb = rentBook.get(i);
                        ps = SQLService.getConnect().prepareStatement("INSERT INTO datalibrary.infor_rent (idRent, idBook, day_borrow, rental) VALUES (?, ?, NOW(), ?)");
                        ps.setInt(1, rs.getInt("LAST_INSERT_ID()"));
                        ps.setInt(2, (int) rentb.getIdBook());
                        ps.setInt(3, (int) rentb.getRental());
                        ps.execute();
                        
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
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
            ps = SQLService.getConnect().prepareStatement("SELECT " + statisticalName + ", COUNT(*) FROM datalibrary.rentbook GROUP BY " + statisticalName);
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
}
