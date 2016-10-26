/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.UserModel;
import Object.User;
import Views.*;
import static Views.ListBook.pageBook;
import static Views.ListBook.tableListBook;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Duc Dung Dan
 */
public class MainController {
    private final static String NON_THIN = "[^iIl1\\.,']";
    
    public void startApplication() {
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
        new MainController().startApplication();
    }
    
    public static void information(JPanel jpanel) {
        Information infor = new Information();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(infor.getPanelInformation());
        jpanel.updateUI();
    }
    
    public static void listBook(JPanel jpanel) {
        ListBook listBook = new ListBook();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        if(!UserModel.user.getAdmin()) {
            listBook.delete.setVisible(false);
            listBook.addBook.setVisible(false);
            listBook.edit.setVisible(false);
        }
        jpanel.add(listBook.getPanelListBook());
        jpanel.updateUI();
        BookController.pageBook = 0;
        BookController.start = 0;
        BookController.search = "";
        BookController.typeSearch = "idBook";
        BookController.loadTableListBooks(tableListBook);
        pageBook.setText("0");
    }
    
    public static void order(JPanel jpanel) {
        Order order = new Order();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(order.getPanelOrder());
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
    private static int textWidth(String str) {
        return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }
    
    public static String truncate(String text, int max) {
        if (textWidth(text) <= max) {
            return text;
        }
        
        int end = text.lastIndexOf(' ', max - 3);
        
        if (end == -1) {
            return text.substring(0, max - 3) + "...";
        }
        
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);

            // No more spaces.
            if (newEnd == -1) {
                newEnd = text.length();
            }

        } while (textWidth(text.substring(0, newEnd) + "...") < max);

        return text.substring(0, end) + "...";
    }
    
    public static Image readImageFromURL(String path, Number height, Number weight) {
        try {
            URL url = new URL(path);
            Image image = ImageIO.read(url).getScaledInstance((int)weight, (int)height, java.awt.Image.SCALE_SMOOTH );
            return image;
        } catch (Exception e) {
            if("Can't get input stream from URL!".equals(e.getMessage())) {
                JOptionPane.showMessageDialog(null, "Không lấy được hình ảnh có url: " + path, "Thông báo", 2);
            }
            return null;
        }
    }
    
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    
}
