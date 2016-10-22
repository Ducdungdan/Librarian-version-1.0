/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Object.User;
import Views.*;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Duc Dung Dan
 */
public class MainController {
    
    public static void startApplication() {
        // View the application's GUI
        Login login = new Login();
        login.setVisible(true);
    }
    
    public static void signUp() {
        SignUp signUp = new SignUp();
        signUp.setVisible(true);
    }
    
    public static void logOut(JFrame jframe) {
        Models.UserModel.user = new User();
        jframe.dispose();
        startApplication();
    }
    
    public static void information(JPanel jpanel) {
        Information infor = new Information();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(infor.getPanelInformation());
        jpanel.updateUI();
                
    }
    
    public static String addDays(Date d, int days) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt.format(calendar.getTime());
    }
    
    public static int subDays(Date minus, Date subtrahend) {
        return (int)((minus.getTime() - subtrahend.getTime())/86400000);
    }
    
    public static String HMAC_SHA256(String email, String password) {
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(password.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String auth = Base64.encodeBase64String(sha256_HMAC.doFinal(email.getBytes()));
            return auth;
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            System.out.println("Error HS-256");
            return "";
        }
    }
    
    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
