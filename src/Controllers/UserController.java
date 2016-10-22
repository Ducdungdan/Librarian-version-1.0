/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Object.*;
import Models.UserModel;
import Views.*;
import java.awt.Frame;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Duc Dung Dan
 */
public class UserController {
    
    public static void login(String email, String password, Frame frame) {
        if ("".equals(email) || "".equals(password)) {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập Email hoặc password", "Thông báo", 1);
        } else {
            if (UserModel.login(email, password)) {
                System.out.println(email + password);
                frame.dispose();
                new Main().setVisible(true);
            }
        }
    }
    
    public static void signUp(User user, String password, Frame frame) {
        if (user.getEmail().length() == 0 || user.getName().length() == 0
                || user.getAdress().length() == 0 || user.getBirth() == null) {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập đầy đủ thông tin", "Thông báo", 1);
        } else {
            if (UserModel.signUp(user, password)) {
                frame.dispose();
            }
        }
    }
    
    public static void update(User newUser) throws SQLException {
        if(newUser.getName().length() == 0 || newUser.getAdress().length() == 0) {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập đầy đủ thông tin", "Thông báo", 1);
        } else {
            if(UserModel.update(newUser)) {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin user thành công", "Thông báo", 2);
                UserModel.user.setAdress(newUser.getAdress());
                UserModel.user.setBirth(newUser.getBirth());
                UserModel.user.setCity(newUser.getCity());
                UserModel.user.setName(newUser.getName());
                UserModel.user.setSex(newUser.getSex());
            } else {
                JOptionPane.showMessageDialog(null, "Hiện tại không thể cập nhật thông tin user", "Lỗi", 2);
            }
        }
    }
    
    public static User getDataUser() {
        return Models.UserModel.user;
    }
    
    public static void viewChangePassword() {
        ChangePassword cPassword = new ChangePassword();
        cPassword.setVisible(true);
    }
    
    public static void changePassword(String currentPassword, String newPassword, JFrame jframe) {
        try {
            if (UserModel.changePassword(currentPassword, newPassword)) {
                jframe.dispose();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
