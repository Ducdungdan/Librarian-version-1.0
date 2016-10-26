/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Object.Book;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Duc Dung Dan
 */
public class BookModel implements DataInterface {
    
    public static PreparedStatement ps;
    public static ResultSet rs;
    public static Book book = new Book();

    public static boolean update(Book newBook) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT idBook FROM datalibrary.book WHERE Name = ? and Author = ? and Publishing_year = ?");
            ps.setString(1, newBook.getName());
            ps.setString(2, newBook.getAuthor());
            ps.setString(3, newBook.getYear().toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("idBook") != (int) book.getIdBook()) {
                    JOptionPane.showMessageDialog(null, "Đầu sách này đã tồn tại \n Có idBook = " + rs.getInt("idBook"), "Lỗi", 2);
                    return false;
                }
            }
            ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.book SET Name=?, Author=?, Content=?, Publishing_company=?, Publishing_year=?, Type=?, Country=?, Numbers=?, Value=?, Editor=?, Image=? WHERE idBook=?");

            ps.setString(1, newBook.getName());
            ps.setString(2, newBook.getAuthor());
            ps.setString(3, newBook.getContent());
            ps.setString(4, newBook.getCompany());
            ps.setString(5, newBook.getYear().toString());
            ps.setString(6, newBook.getType());
            ps.setString(7, newBook.getCountry());
            ps.setInt(8, (int) newBook.getNumber());
            ps.setInt(9, (int) newBook.getValue());
            ps.setString(10, UserModel.user.getIdUser().toString());
            ps.setBytes(11, book.getImage());
            ps.setInt(12, (int) book.getIdBook());
            if (!ps.execute()) {
                JOptionPane.showMessageDialog(null, "Sửa đầu sách  thành công", "Thông báo", 2);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Sửa đầu sách không thành công", "Lỗi", 2);
                return false;
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Sửa đầu sách không thành công", "Lỗi", 2);
            return false;
        }
    }

    public static boolean insert(Book newBook) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT idBook FROM datalibrary.book WHERE Name = ? and Author = ? and Publishing_year = ?");
            ps.setString(1, newBook.getName());
            ps.setString(2, newBook.getAuthor());
            ps.setString(3, newBook.getYear().toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Đầu sách này đã tồn tại \n Có idBook = " + rs.getInt("idBook"), "Lỗi", 2);
                return false;
            } else {
                ps = SQLService.getConnect().prepareStatement("INSERT INTO datalibrary.book (Name, Author, Content, Publishing_company, Publishing_year, Type, Country, Numbers, Value, Image, Editor) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                ps.setString(1, newBook.getName());
                ps.setString(2, newBook.getAuthor());
                ps.setString(3, newBook.getContent());
                ps.setString(4, newBook.getCompany());
                ps.setString(5, newBook.getYear().toString());
                ps.setString(6, newBook.getType());
                ps.setString(7, newBook.getCountry());
                ps.setInt(8, (int) newBook.getNumber());
                ps.setInt(9, (int) newBook.getValue());
                ps.setBytes(10, newBook.getImage());
                ps.setString(11, UserModel.user.getIdUser().toString());
                if (!ps.execute()) {
                    JOptionPane.showMessageDialog(null, "Thêm đầu sách  thành công", "Thông báo", 2);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm đầu sách thất bại", "Lỗi", 2);
                    return false;
                }
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Thêm đầu sách thất bại", "Lỗi", 2);
            return false;
        }
    }
    
    @Override
    public ResultSet getStatisticalData(String statisticalName) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT " + statisticalName + ", COUNT(*) FROM datalibrary.book GROUP BY " + statisticalName);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi", "Lỗi", 2);
            return null;
        }
    }
    
    public static ResultSet getDataBooks(Number start, String search, String typeSearch) {
        
        
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT idBook, Name, Author, Type, Value, Numbers FROM datalibrary.book WHERE " + typeSearch + " LIKE \"%"+ search +"%\" LIMIT " + start + ",20");
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không lấy được dữ liệu \n Vui lòng thử lại sau 5 phút", "Lỗi", 2);
            return null;
        }
    }
    
    public static ResultSet getDataBook(Number idBook) {

        try {
            ps = SQLService.getConnect().prepareStatement("SELECT * FROM datalibrary.book WHERE idBook = ?");
            ps.setInt(1, (int)idBook);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không lấy được dữ liệu \n Vui lòng thử lại sau 5 phút", "Lỗi", 2);
            return null;
        }
    }
    
    public static Boolean delete(Number idBook) {
        try {
            ps = SQLService.getConnect().prepareStatement("DELETE FROM datalibrary.book WHERE idBook=?");
            ps.setString(1, idBook.toString());
            if(ps.execute()){
                JOptionPane.showMessageDialog(null, "Đầu sách này không tồn tại", "Lỗi", 2);
                return false;   
            } else {
                return true;
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Không thể xóa \nĐầu sách này đang được mượn", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean updateImageBook(byte[] image) throws SQLException, FileNotFoundException {
        ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.user SET Image = ? WHERE idUser = ?");
        if (image == null) {
            ps.setBytes(1, null);
        } else {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(image);
            ps.setBytes(1, image);
        }
        ps.setInt(2, (int) book.getIdBook());
        if (!ps.execute()) {
            JOptionPane.showMessageDialog(null, "Thay đổi avartar thành công", "Thông báo", 2);
            book.setImage(image);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Hiện tại không thể cập nhật avartar", "Lỗi", 2);
            return false;
        }
    }
    
    public static void getTop4Remaining() throws SQLException {
        ps = SQLService.getConnect().prepareStatement("SELECT Name, Image, Value, Remaining FROM datalibrary.book WHERE Remaining > 0 ORDER BY Remaining DESC LIMIT 4");
        rs = ps.executeQuery();
    }
}
