/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Object.*;
import Models.UserModel;
import Views.*;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Duc Dung Dan
 */
public class UserController {
    
    public static byte[] avartarOld;

    
    public static void login(String email, String password, Frame frame) {
        if ("".equals(email) || "".equals(password)) {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập Email hoặc password", "Thông báo", 1);
        } else {
            if (UserModel.login(email, password)) {
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
    
    public static void viewUser(JTextField name, JTextField adress, JLabel avartar, JTextField email, JDateChooser birth, JRadioButton nam, JRadioButton nu, JComboBox city) {
        User user = Models.UserModel.user;
        String[] listCity = Models.UserModel.listCity;
        for (int i = 0; i < listCity.length; ++i) {
            city.addItem(listCity[i]);
        }
        
        name.setText(user.getName());
        if (user.getSex()) {
            nam.setSelected(true);
        } else {
            nu.setSelected(true);
        }
        adress.setText(user.getAdress());
        email.setText(user.getEmail());
        city.setSelectedItem(user.getCity());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date birthDate = df.parse(user.getBirth());
            birth.setDate(birthDate);
        } catch (ParseException ex) {
            Logger.getLogger(Information.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (user.getImage() != null) {
            ImageIcon icon = new ImageIcon(user.getImage());
            avartar.setIcon(icon);
        } else {
            String imagePath = "src/Image/avartar.png";
            ImageIcon icon = new ImageIcon(imagePath);
            avartar.setIcon(icon);
        }
    }
    
    public static void bowserAvartar(JLabel avartar, JButton bower, JButton save, JButton close, Component infor) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter loc = new FileNameExtensionFilter("Image", "jpg", "png");
        chooser.setFileFilter(loc);
        int i = chooser.showOpenDialog(infor);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                BufferedImage bimage = ImageIO.read(file);
                ImageIcon icon = new ImageIcon(bimage);
                if (icon.getIconHeight() < 121 && icon.getIconHeight() > 99 && icon.getIconWidth() < 121 && icon.getIconWidth() > 99) {
                    avartar.setIcon(icon);
                    BufferedImage bufferedImage = ImageIO.read(file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    String nameFile = file.getName();
                    String[] nameFiles = nameFile.split("\\.");
                    ImageIO.write(bufferedImage, nameFiles[nameFiles.length-1], baos);
                    avartarOld = baos.toByteArray();

                    save.setVisible(true);
                    close.setVisible(true);
                    bower.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ảnh có kích thước \n 100 < Height < 120 và  100 < Width < 120", "Thông báo", 2);
                }
            } catch (IOException | HeadlessException e) {
            }
        }
    }
    
    public static void saveAvartar(JButton bower, JButton save, JButton close) throws SQLException {
        if(UserModel.updateImageUser(avartarOld)) {
            save.setVisible(false);
            close.setVisible(false);
            bower.setVisible(true);
            showNavItem(Main.lastName, Main.icon);
        }
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

    public static void closeInsertAvatar(JButton bower, JButton save, JButton close, JLabel avartar) {
        User user = UserModel.user;
        if (user.getImage() != null) {
            ImageIcon icon = new ImageIcon(user.getImage());
            avartar.setIcon(icon);
        } else {
            String imagePath = "src/Image/avartar.png";
            ImageIcon icon = new ImageIcon(imagePath);
            avartar.setIcon(icon);
        }
        save.setVisible(false);
        close.setVisible(false);
        bower.setVisible(true);
    }

    public static void showNavItem(JLabel lastName, JLabel icon) {
        String fullName = UserModel.user.getName();
        String[] fullNames = fullName.split(" ");
        lastName.setText(fullNames[fullNames.length-1]);
        ImageIcon iconAvartar;
        if (UserModel.user.getImage() != null) {
            iconAvartar = new ImageIcon(UserModel.user.getImage());
        } else {
            String imagePath = "src/Image/avartar.png";
            iconAvartar = new ImageIcon(imagePath);
        }
        Image imageAvartar = iconAvartar.getImage();
        Image imageFixSize = imageAvartar.getScaledInstance( 22, 22,  java.awt.Image.SCALE_SMOOTH ) ;
        icon.setIcon(new ImageIcon(imageFixSize));

    }

}
