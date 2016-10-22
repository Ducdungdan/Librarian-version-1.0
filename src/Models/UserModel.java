/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Controllers.MainController.HMAC_SHA256;
import Object.User;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Duc Dung Dan
 */
public class UserModel implements DataInterface {
    
    @Override
    public ResultSet getStatisticalData(String statisticalName) {
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT " + statisticalName +", COUNT(*) FROM datalibrary.user GROUP BY " + statisticalName);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hệ thống đang bị lỗi", "Lỗi", 2);
            return null;
        }
    }
    
    public static PreparedStatement ps;
    public static ResultSet rs;
    public static User user = new User();
    public static String[] listCity = {"An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh", "Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau", "Cao Bằng", "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Đồng Nai", "Đồng Tháp", "Điện Biên", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hòa Bình", "Hậu Giang", "Hưng Yên", "TP Hồ Chí Minh", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lào Cai", "Lạng Sơn", "Lâm Đồng", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên - Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"};


    public static Boolean login(String email, String password){
        String auth = HMAC_SHA256(email, password);
        System.out.println(email + " " + password);
        System.out.println(email + "  " + auth);
        try {
            ps = SQLService.getConnect().prepareStatement("SELECT * FROM datalibrary.authentication, datalibrary.user WHERE Email = ? and Authentication = ? and authentication.idUser = user.idUser");
            ps.setString(1, email);
            ps.setString(2, auth);
            rs = ps.executeQuery();
            if(rs.next()) {
                if ("1".equals(rs.getString("Block"))) {
                    JOptionPane.showMessageDialog(null, "Tài khoản của bạn đang bị khóa \n Bạn vui lòng liên hệ admin để mở tài khoản", "Lỗi", 2);
                    return false;
                } else {
                    user.setIdUser(rs.getInt("user.idUser"));
                    user.setName(rs.getString("Name"));
                    user.setSex(rs.getBoolean("Sex"));
                    user.setEmail(email);
                    user.setAdress(rs.getString("Adress"));
                    user.setBirth(rs.getString("Date_of_Birth"));
                    user.setAdmin(rs.getBoolean("Admin"));
                    user.setCity(rs.getString("City"));
                    Blob blob=rs.getBlob("image");
                    if(blob != null) {
                        user.setImage(blob.getBytes(1, (int) blob.length()));
                    } else {
                        user.setImage(null);
                    }
                    
                    JFrame frame = new JFrame("Xin chào");
                    JOptionPane.showMessageDialog(frame, "Xin Chào " + user.getName());
                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không chính xác", "Lỗi", 2);
                return false;
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không chính xác", "Lỗi", 2);
            return false;
        }
    }

    public static Boolean signUp(User newUser, String password){

        try {
            ps = SQLService.getConnect().prepareStatement("SELECT idUser FROM datalibrary.authentication WHERE Email = ?");
            ps.setString(1, newUser.getEmail());
            rs = ps.executeQuery();
            if(rs.next()) { 
                JOptionPane.showMessageDialog(null, "Tài khoản đã tồn tại \n Vui lòng liên hệ admin để lấy lại tài khoản", "Thông Báo", 2);
                return false;
            } else {
                try {
                    ps = SQLService.getConnect().prepareStatement("call datalibrary.insert_user(?, ?, ?, ?, ?, ?, ?)");
                        ps.setString(1, newUser.getName());
                        ps.setString(2, newUser.getEmail());
                        ps.setBoolean(3, newUser.getSex());
                        ps.setString(4, newUser.getBirth());
                        ps.setString(5, newUser.getAdress());
                        ps.setString(6, newUser.getCity());
                        ps.setString(7, HMAC_SHA256(newUser.getEmail(), password));
                        
                        rs = ps.executeQuery();
                        if(rs.next()) {
                            JOptionPane.showMessageDialog(null, "Tạo tài khoản thành công", "Thông báo", 2);
                            newUser.setIdUser(rs.getInt("LAST_INSERT_ID()"));
                            user = newUser;
                            return true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Tạo tài khoản không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
                            return false;
                        }
                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, "Tạo tài khoản không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
                        return false;
                }
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Tạo tài khoản không thành công \n Bạn vui lòng thử lại", "Thông báo", 2);
            return false;
        }          
    }
    
    public static Boolean update(User newUser) throws SQLException {
        ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.user SET Name=?, Sex=?, Date_of_Birth=?, Adress=?, City=? WHERE idUser=?");
        ps.setString(1, newUser.getName());
        ps.setInt(2, newUser.getSex()?1:0);
        ps.setString(3, newUser.getBirth());
        ps.setString(4, newUser.getAdress());
        ps.setString(5, newUser.getCity());
        ps.setInt(6, (int) user.getIdUser());
        if (!ps.execute()) {
            return true;
        } else {
            return false;
        }
    }
        
    public static Boolean getUserData(Number start, String search, String typeSearch) {

        try {
            ps = SQLService.getConnect().prepareStatement("SELECT user.idUser, Name, Email, Sex, Admin, Block, Adress FROM authentication, user WHERE (user.idUser = authentication.idUser) AND (" + typeSearch + " LIKE \"%" + search + "%\") LIMIT " + start +",7");
            rs = ps.executeQuery();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không lấy được dữ liệu \n Vui lòng thử lại sau 5 phút", "Lỗi", 2);
            return false;
        }
    }
        
    public static Boolean blockUser(Number idUser, String status) {
        try {
            ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.user SET Block=? WHERE idUser=?");
            ps.setInt(1, ("Bị khóa".equals(status)? 0 : 1));
            ps.setString(2, idUser.toString());
            if (ps.execute()) {
                JOptionPane.showMessageDialog(null, "User này không tồn tại", "Lỗi", 2);
                return false;
            } else {
                return true;
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "User này không tồn tại", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean changePassword(String currentPassword, String newPassword) throws SQLException {

        ps = SQLService.getConnect().prepareStatement("SELECT Email FROM datalibrary.authentication where idUser = ? and Authentication = ?");
        ps.setInt(1, (int) user.getIdUser());
        ps.setString(2, HMAC_SHA256(user.getEmail(), currentPassword));
        rs = ps.executeQuery();
        if(rs.next()) {
            ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.authentication SET Authentication=? WHERE idUser=?");
            ps.setString(1, HMAC_SHA256(user.getEmail(), newPassword));
            ps.setInt(2, (int) user.getIdUser());
            if(!ps.execute()) {
                JOptionPane.showMessageDialog(null, "Đổi password thành công", "Thông báo", 2);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Đổi password thất bại", "Thông báo", 2);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Password hiện tại nhập không đúng", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean updateInforUser(Number idUser, String Data) throws SQLException {
        ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.user SET " + Data + " WHERE idUser=" + user.getIdUser().toString());
        if(!ps.execute()) {
            JOptionPane.showMessageDialog(null, "Cập nhật thông tin user thành công", "Thông báo", 2);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Hiện tại không thể cập nhật thông tin user", "Lỗi", 2);
            return false;
        }
    }
    
    public static Boolean updateImageUser(File file) throws SQLException, FileNotFoundException {
        ps = SQLService.getConnect().prepareStatement("UPDATE datalibrary.user SET Image = ? WHERE idUser = ?");
        FileInputStream fis = new FileInputStream(file);
        ps.setBinaryStream(1,fis,(int)file.length());
        ps.setInt(2, (int) user.getIdUser());
        if (!ps.execute()) {
            JOptionPane.showMessageDialog(null, "Thay đổi avartar thành công", "Thông báo", 2);
            ps = SQLService.getConnect().prepareStatement("SELECT Image FROM datalibrary.user WHERE idUser = ?");
            ps.setInt(1, (int) user.getIdUser());
            rs = ps.executeQuery();
            rs.next();
            Blob blob = rs.getBlob("Image");
            if(blob != null) {
                user.setImage(blob.getBytes(1, (int) blob.length()));
            } else {
                user.setImage(null);
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Hiện tại không thể cập nhật avartar", "Lỗi", 2);
            return false;
        }
    }
    
}
