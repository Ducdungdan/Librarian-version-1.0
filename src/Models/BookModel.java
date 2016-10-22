/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Object.Book;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
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
    
    public static Boolean getBookData(Number start, String search, String typeSearch) {
        
        
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT * FROM datalibrary.book WHERE " + typeSearch + " LIKE \"%"+ search +"%\" LIMIT " + start + ",20");
            rs = ps.executeQuery();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không lấy được dữ liệu \n Vui lòng thử lại sau 5 phút", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean insertBook(Book bookNew) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT idBook FROM datalibrary.book WHERE Name = ? and Author = ? and Publishing_year = ?");
            ps.setString(1, bookNew.getName());
            ps.setString(2, bookNew.getAuthor());
            ps.setString(3, bookNew.getYear().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Sách này đã tồn tại \n Có id = " + rs.getString("idBook"));
                return false;
            } else {
                try {
                    ps = SQLService.getConnect().prepareStatement("SELECT MAX(idBook) FROM datalibrary.book");
                    rs = ps.executeQuery();

                    if (rs.next()) {

                        book.setIdBook(rs.getInt("MAX(idBook)") + 1);
                        try {
                            ps = SQLService.getConnect().prepareStatement("INSERT INTO datalibrary.book (idBook, Name, Author, Publishing_company, Publishing_year, Type, "
                                    + "Country, Numbers, Value, Remaining, Editor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            ps.setInt(1, (int) book.getIdBook());
                            ps.setString(2, bookNew.getName());
                            ps.setString(3, bookNew.getAuthor());
                            ps.setString(4, bookNew.getCompany());
                            ps.setString(5, bookNew.getYear().toString());
                            ps.setString(6, bookNew.getType());
                            ps.setString(7, bookNew.getCountry());
                            ps.setString(8, bookNew.getNumber().toString());
                            ps.setString(9, bookNew.getValue().toString());
                            ps.setString(10, bookNew.getRemaining().toString());
                            ps.setString(11, UserData.user.getIdUser().toString());
                            if (!(ps.execute())) {
                                return true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Thêm đầu sách không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
                                return false;
                            }

                        } catch (SQLException | HeadlessException e) {
                            JOptionPane.showMessageDialog(null, "Thêm đầu sách không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
                            return false;
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm đầu sách không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
                        return false;
                    }
                } catch (SQLException | NumberFormatException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, "Thêm đầu sách không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
                    return false;
                }
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Thêm đầu sách không thành công \n Bạn vui lòng đăng nhập lại", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean editBook(Book editbook) throws SQLException {
        
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT idBook FROM datalibrary.book WHERE Name = ? and Author = ? and Publishing_year = ?");
            ps.setString(1, editbook.getName());
            ps.setString(2, editbook.getAuthor());
            ps.setString(3, editbook.getYear().toString());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                if(rs.getInt("idBook") != (int)editbook.getIdBook()) {
                    JOptionPane.showMessageDialog(null, "Đầu sách này đã tồn tại \n Có idBook = " + rs.getInt("idBook"), "Lỗi", 2);
                    return false;
                } else {
                    ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.book SET Name=?, Author=?, Publishing_company=?, Publishing_year=?, Type=?, Country=?, Numbers=?, Value=?, Remaining=?, Editor=? WHERE idBook=?");

                    ps.setString(1, editbook.getName());
                    ps.setString(2, editbook.getAuthor());
                    ps.setString(3, editbook.getCompany());
                    ps.setString(4, editbook.getYear().toString());
                    ps.setString(5, editbook.getType());
                    ps.setString(6, editbook.getCountry());
                    ps.setInt(7, (int) editbook.getNumber());
                    ps.setInt(8, (int) editbook.getValue());
                    ps.setInt(9, (int) editbook.getRemaining());
                    ps.setInt(10, (int) UserData.user.getIdUser());
                    ps.setInt(11, (int) editbook.getIdBook());
                    if (!ps.execute()) {
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa đầu sách không thành công", "Lỗi", 2);
                        return false;
                    }
                }
            } else {
                ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.book SET Name=?, Author=?, Publishing_company=?, Publishing_year=?, Type=?, Country=?, Numbers=?, Value=?, Remaining=?, Editor=? WHERE idBook=?");

                ps.setString(1, editbook.getName());
                ps.setString(2, editbook.getAuthor());
                ps.setString(3, editbook.getCompany());
                ps.setString(4, editbook.getYear().toString());
                ps.setString(5, editbook.getType());
                ps.setString(6, editbook.getCountry());
                ps.setInt(7, (int) editbook.getNumber());
                ps.setInt(8, (int) editbook.getValue());
                ps.setInt(9, (int) editbook.getRemaining());
                ps.setString(10, editbook.getEditor().toString());
                ps.setInt(11, (int) editbook.getIdBook());
                if (!ps.execute()) {
                    JOptionPane.showMessageDialog(null, "Sửa đầu sách  thành công", "Thông báo", 2);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Sửa đầu sách không thành công", "Lỗi", 2);
                    return false;
                }
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Sửa đầu sách không thành công", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean deleteBook(Number idBook) {
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
            JOptionPane.showMessageDialog(null, "Đầu sách này không tồn tại", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean updateImageBook(File file) throws SQLException, FileNotFoundException {
        ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.book SET Image = ? WHERE idBook = ?");
        FileInputStream fis = new FileInputStream(file);
        ps.setBinaryStream(1, fis, (int) file.length());
        ps.setInt(2, (int) book.getIdBook());
        if (!ps.execute()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Hiện tại không thể cập nhật image", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean getImageBook() throws SQLException {
        ps = SQLService.getConnect().prepareStatement("SELECT Image FROM datalibrary.book WHERE idBook = ?");
        ps.setInt(1, (int) book.getIdBook());
        rs = ps.executeQuery();
        rs.next();
        Blob blob = rs.getBlob("Image");
        if(blob == null) {
            return false;
        } else {
            book.setImage(blob.getBytes(1, (int) blob.length()));
            return true;
        }
    }
    
    public static void getTop4Remaining() throws SQLException {
        ps = SQLService.getConnect().prepareStatement("SELECT Name, Image, Value, Remaining FROM datalibrary.book WHERE Remaining > 0 ORDER BY Remaining DESC LIMIT 4");
        rs = ps.executeQuery();
    }
}
