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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Duc Dung Dan
 */
public class MainController {
    private final static String NON_THIN = "[^iIl1\\.,']";
    public static final String FONT = "font/timenewroman.ttf";
    private static final Font smallBold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, 13);
    private static final Font titleFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, 14);
    private static final Font catFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, 14);
    
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
    
    public static void payment(JPanel jpanel) {
        Payment payment = new Payment();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(payment.payment);
        jpanel.updateUI();
        PaymentController.pageRent = 0;
        PaymentController.search = "";
        PaymentController.show = "all";
        PaymentController.start = 0;
        PaymentController.typeSearch = "rent.idRent";
        if(PaymentController.loadTableListRentBook(payment.informationRent)) {
            Payment.page.setText("0");
        }
    }
    
    public static void staticcal(JPanel jpanel) {
        Statistical statical = new Statistical();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(statical.Statistics);
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
        BookController.loadTableListBooks(listBook.tableListBook);
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
    
    public static void users(JPanel jpanel) {
        Users users = new Users();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(users.users);
        jpanel.updateUI();
    }
    
    public static void checkouts(JPanel jpanel) {
        CheckOutBook checkouts = new CheckOutBook();
        jpanel.removeAll();
        GridLayout girdlayout = new GridLayout();
        jpanel.setLayout(girdlayout);
        jpanel.add(checkouts.checkout);
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
    
    public static void printComponent(JPanel jpanelName) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Print Component ");

        pj.setPrintable(new Printable() {
            @Override
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                jpanelName.paint(g2);
                return Printable.PAGE_EXISTS;
            }
        });
        if (pj.printDialog() == false) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            // handle exception
        }
    }
    
    public static void exportPDF(String pathFile, String title, JTable table) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pathFile));
            
            document.open();
            addMetaData(document, "THƯ VIỆN ONLINE CONVENIENT", title);
            addTitlePage(document, title);
            addContent(document, table);
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
        }
    }
    
    private static void addMetaData(Document document, String title, String subject) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addKeywords("Java, PDF, iText");
        document.addAuthor(UserModel.user.getName());
        document.addCreator(UserModel.user.getName());
    }

    private static void addTitlePage(Document document, String title) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "Report generated by: " + UserModel.user.getName() + ", " + new Date(), smallBold));
        addEmptyLine(preface, 2);

        // Lets write a big header
        
        Paragraph name = new Paragraph("THƯ VIỆN ONLINE CONVENIENT", titleFont);
        name.setAlignment(Element.ALIGN_CENTER);
        preface.add(name);
        
        Paragraph titleName = new Paragraph(title, catFont);
        titleName.setAlignment(Element.ALIGN_CENTER);
        preface.add(titleName);

        addEmptyLine(preface, 3);
        

        document.add(preface);
    }
    
    private static void addContent(Document document, JTable tab) throws DocumentException {
        
        Paragraph paragraph = new Paragraph();
        

        // add a table
        createTable(paragraph, tab);
        addEmptyLine(paragraph, 2);
        // now add all this to the document

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd MM yyyy");
        String[] stringDate = ft.format(date).split(" ");
        
        // Sign
        Paragraph signDate = new Paragraph("Hà Nội, ngày " + stringDate[0] + " tháng " + stringDate[1] + " năm " + stringDate[2], smallBold);
        signDate.setAlignment(Element.ALIGN_RIGHT);
        paragraph.add(signDate);
        addEmptyLine(paragraph, 2);
        
        Paragraph sign = new Paragraph(UserModel.user.getName(), smallBold);
        sign.setAlignment(Element.ALIGN_RIGHT);
        paragraph.add(sign);
        
        addEmptyLine(paragraph, 1);

        // now add all this to the document
        document.add(paragraph);

    }

    private static void createTable(Paragraph paragraph, JTable tab) throws DocumentException {
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        
        PdfPTable table = new PdfPTable(model.getColumnCount());
        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        PdfPCell c1;
        for(int i = 0; i < model.getColumnCount(); ++i) {
            c1 = new PdfPCell(new Phrase(model.getColumnName(i), smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        table.setHeaderRows(1);

        for(int i = 0; i < model.getRowCount(); ++i) {
            for(int j = 0; j < model.getColumnCount(); ++j) {
                table.addCell( new PdfPCell(new Phrase(model.getValueAt(i, j).toString(), smallBold)));
            }
        }
        paragraph.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
