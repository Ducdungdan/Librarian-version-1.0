/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static Controllers.MainController.truncate;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Models.BookModel;
import Object.Book;
import Views.InformationBook;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Duc Dung Dan
 */
public class BookController {
    public static int start = 0;
    public static int pageBook = 0;
    public static String search = "";
    public static String typeSearch = "idBook";
    public static byte[] imageBookOld;
    
    
    public static Boolean loadTableListBooks(JTable listBook) {
        ResultSet rs = BookModel.getDataBooks(start, search, typeSearch);
        try {
            if(rs.next()) {
                DefaultTableModel model = (DefaultTableModel) listBook.getModel();
                int rowCount = model.getRowCount();
                //Remove rows one by one from the end of the table
                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }

                do {
                    model.addRow(new Object[]{rs.getInt("idBook"), rs.getString("Name"), rs.getString("Author"), rs.getString("Type"), rs.getInt("Numbers"), rs.getInt("Value")});
                    ++start;
                } while (rs.next());
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public static void nextTableListBooks(JTable listBook, JLabel page) {
        if(loadTableListBooks(listBook)) {
            ++pageBook;
            page.setText(String.valueOf(pageBook));
        } else {
            JOptionPane.showMessageDialog(null, "Đây là trang cuối", "Thông báo", 2);
        }
    }
    
    public static void prevTableListBooks(JTable listBook, JLabel page) {
        if (pageBook > 0) {
            int rowCount = listBook.getRowCount();
            //Remove rows one by one from the end of the table
            start = start - 28 - rowCount;
            if (loadTableListBooks(listBook)) {
                --pageBook;
                page.setText(String.valueOf(pageBook));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Đây là trang đầu", "Thông báo", 2);
        }
        
    }
    
    public static void searchTableListBooks(JTable listBook, JLabel page, String inputSearch, String inputTypeSearch) {
        switch (inputTypeSearch) {
            case "id":
                typeSearch = "idBook";
                break;
            case "Tên sách":
                typeSearch = "Name";
                break;
            case "Tác giả":
                typeSearch = "Author";
                break;
            default:
                typeSearch = "Type";
        }
        
        search = inputSearch;
        pageBook = 0;
        start = 0;
        page.setText(String.valueOf(pageBook));
        if(!loadTableListBooks(listBook)) {
            JOptionPane.showMessageDialog(null, "Không tìm được kết quả nào thỏa mãn", "Thông báo", 2);
        }
    }

    public static void viewBook(Number idBook, JLabel name, JLabel author, JTextArea content, JLabel company, JLabel year, JLabel type, JLabel value, JLabel country, JLabel imageBook) throws SQLException {
        ResultSet rs = BookModel.getDataBook(idBook);
        rs.next();
        name.setText(truncate(rs.getString("Name"), 60));
        author.setText(truncate("Tác giả: " + rs.getString("Author"), 30));
        content.setText(rs.getString("Content"));
        company.setText(truncate(rs.getString("Publishing_company"), 30));
        year.setText(truncate("Năm xuất bản: " + rs.getString("Publishing_year"), 30));
        type.setText(truncate("Thể loại: " + rs.getString("Type"), 30));
        value.setText("Tiền mượn: " + rs.getString("Value"));
        country.setText(truncate("Quốc gia: " + rs.getString("Country"), 30));
        if (rs.getBlob("Image") != null) {
            Blob blob = rs.getBlob("Image");
            ImageIcon icon = new ImageIcon(blob.getBytes(1, (int) blob.length()));
            imageBook.setIcon(icon);
        } else {
            String imagePath = "src/Image/book-null.png";
            ImageIcon icon = new ImageIcon(imagePath);
            imageBook.setIcon(icon);
        }
    }

    public static void viewNewBook() {
        BookModel.book = new Book();
        InformationBook newDialogNewBook = new InformationBook(null, true);
        InformationBook.updateBook.setVisible(false);
        newDialogNewBook.setVisible(true);
    }
    
    public static void viewEditBook(Number idBook) {
        ResultSet rs = BookModel.getDataBook(idBook);
        try {
            if(rs.next()) {
                BookModel.book.setIdBook(idBook);
                BookModel.book.setName(rs.getString("Name"));
                BookModel.book.setAuthor(rs.getString("Author"));
                BookModel.book.setContent(rs.getString("Content"));
                BookModel.book.setCompany(rs.getString("Publishing_company"));
                BookModel.book.setYear(rs.getInt("Publishing_year"));
                BookModel.book.setType(rs.getString("Type"));
                BookModel.book.setCountry(rs.getString("Country"));
                BookModel.book.setNumber(rs.getInt("Numbers"));
                BookModel.book.setValue(rs.getInt("Value"));
                Blob blob = rs.getBlob("Image");
                if (blob != null) {
                     BookModel.book.setImage(blob.getBytes(1, (int) blob.length()));
                } else {
                    BookModel.book.setImage(null);
                }
               
                InformationBook newDialogNewBook = new InformationBook(null, true);
                InformationBook.addBook.setVisible(false);
                InformationBook.importFromFileJSON.setVisible(false);
                newDialogNewBook.setVisible(true);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loadDataDialogBook(JTextField name, JTextField author, JTextArea content, JTextField company, JTextField year, JTextField type, JTextField country, JTextField number, JTextField value, JLabel imageBook) {
        name.setText(BookModel.book.getName());
        author.setText(BookModel.book.getAuthor());
        content.setText(BookModel.book.getContent());
        company.setText(BookModel.book.getCompany());
        year.setText("null".equals(String.valueOf(BookModel.book.getYear()))?"":String.valueOf(BookModel.book.getYear()));
        type.setText(BookModel.book.getType());
        country.setText(BookModel.book.getCountry());
        number.setText("null".equals(String.valueOf(BookModel.book.getNumber()))?"":String.valueOf(BookModel.book.getNumber()));
        value.setText("null".equals(String.valueOf(BookModel.book.getValue()))?"":String.valueOf(BookModel.book.getValue()));
        imageBookOld = null;
        if (BookModel.book.getImage() != null) {
            ImageIcon icon = new ImageIcon(BookModel.book.getImage());
            imageBook.setIcon(icon);
        } else {
            String imagePath = "src/Image/book-null.png";
            ImageIcon icon = new ImageIcon(imagePath);
            imageBook.setIcon(icon);
        }
    }
    
    public static void bowserImgBook(JLabel imageBook, JButton bower, JButton close, Component infor) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter loc = new FileNameExtensionFilter("Image", "jpg", "png");
        chooser.setFileFilter(loc);
        int i = chooser.showOpenDialog(infor);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                BufferedImage bimage = ImageIO.read(file);
                ImageIcon icon = new ImageIcon(bimage);
                if (icon.getIconHeight() < 181 && icon.getIconHeight() > 169 && icon.getIconWidth() < 131 && icon.getIconWidth() > 119) {
                    imageBook.setIcon(icon);
                    BufferedImage bufferedImage = ImageIO.read(file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    String nameFile = file.getName();
                    String[] nameFiles = nameFile.split("\\.");
                    ImageIO.write(bufferedImage, nameFiles[nameFiles.length - 1], baos);
                    imageBookOld = baos.toByteArray();

                    close.setVisible(true);
                    bower.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ảnh có kích thước \n 170 < Height < 180 và  120 < Width < 130", "Thông báo", 2);
                }
            } catch (IOException | HeadlessException e) {
            }
        }
    }
    
    public static void closeImgBook(JButton bower, JButton close, JLabel imageBook) {
        Book book = BookModel.book;
        if (book.getImage() != null) {
            ImageIcon icon = new ImageIcon(book.getImage());
            imageBookOld = null;
            imageBook.setIcon(icon);
        } else {
            String imagePath = "src/Image/book-null.png";
            ImageIcon icon = new ImageIcon(imagePath);
            imageBook.setIcon(icon);
        }
        close.setVisible(false);
        bower.setVisible(true);
    }

    public static void update(Book newBook, JDialog infor) {
        if (newBook.getName().length() == 0 || newBook.getAuthor().length() == 0 || newBook.getCompany().length() == 0 || (int)newBook.getYear() < 1970 || newBook.getType().length() == 0 ||newBook.getCountry().length() == 0) {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập đầy đủ thông tin", "Thông báo", 1);
        } else {
            if(imageBookOld != null) {
                BookModel.book.setImage(imageBookOld);
            }
            if (BookModel.update(newBook)) {
                infor.dispose();
            }
        }
    }
    
    public static void insert(Book newBook, JDialog infor) {
        if (newBook.getName().length() == 0 || newBook.getAuthor().length() == 0 || newBook.getCompany().length() == 0 || (int) newBook.getYear() < 1970 || newBook.getType().length() == 0 || newBook.getCountry().length() == 0) {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập đầy đủ thông tin", "Thông báo", 1);
        } else {
            if (imageBookOld != null) {
                newBook.setImage(imageBookOld);
            }
            if (BookModel.insert(newBook)) {
                infor.dispose();
            }
        }
    }
    
    public static void delete(Number idBook, JTable tableListBook, Number selectRow) {
        if(BookModel.delete(idBook)){
            DefaultTableModel model = (DefaultTableModel) tableListBook.getModel();
            model.removeRow((int) selectRow);
        }
    }
    
    public static void importData(JPanel jpanel) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter loc = new FileNameExtensionFilter("File", "json");
        chooser.setFileFilter(loc);
        int i = chooser.showOpenDialog(jpanel);

        if (i == JFileChooser.APPROVE_OPTION) {
            String question = "Bạn muốn import file " + chooser.getSelectedFile().getPath();
            if (JOptionPane.showConfirmDialog(null, question, "Thông báo", 2) == 0) {
                try {
                    InputStream inputStream = new FileInputStream(chooser.getSelectedFile().getPath());
                    InputStreamReader fileI = new InputStreamReader(inputStream, "Unicode");
                    try (BufferedReader br = new BufferedReader(fileI)) {
                        StringBuilder str = new StringBuilder("");
                        String line;
                        while ((line = br.readLine()) != null) {
                            str.append(line);
                        }
                        JSONArray fileJSONList = new JSONArray();
                        try {
                            fileJSONList = new JSONArray(str.toString());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        JSONObject object;
                        String path;
                        Image image;
                        for (int k = 0; k < fileJSONList.length(); ++k) {
                            Book bookNew = new Book();
                            object = (JSONObject) fileJSONList.get(k);
                            bookNew.setName((String) object.get("Name"));
                            bookNew.setAuthor((String) object.get("Author"));
                            bookNew.setContent((String) object.get("Content"));
                            bookNew.setYear((Number) object.get("Year"));
                            bookNew.setCompany((String) object.get("Company"));
                            bookNew.setCountry((String) object.get("Country"));
                            bookNew.setType((String) object.get("Type"));
                            bookNew.setNumber((Number) object.get("Number"));
                            bookNew.setValue((Number) object.get("Value"));
                            path = (String) object.get("Image");
                            if("".equals(path.trim())) {
                                bookNew.setImage(null);
                            } else {
                                image = MainController.readImageFromURL(path, 180, 130);
                                if(image != null) {
                                    BufferedImage bufferedImage = MainController.toBufferedImage(image);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    String[] nameFiles = path.split("\\.");
                                    ImageIO.write(bufferedImage, nameFiles[nameFiles.length - 1], baos);
                                    bookNew.setImage(baos.toByteArray());
                                } else {
                                    bookNew.setImage(null);
                                }
                            }
                            BookModel.insert(bookNew);
                        }
                        br.close();
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException | JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }
    }

    public static void viewBook(Number idBook) {
        ResultSet rs = BookModel.getDataBook(idBook);
        try {
            if (rs.next()) {
                BookModel.book.setIdBook(idBook);
                BookModel.book.setName(rs.getString("Name"));
                BookModel.book.setAuthor(rs.getString("Author"));
                BookModel.book.setContent(rs.getString("Content"));
                BookModel.book.setCompany(rs.getString("Publishing_company"));
                BookModel.book.setYear(rs.getInt("Publishing_year"));
                BookModel.book.setType(rs.getString("Type"));
                BookModel.book.setCountry(rs.getString("Country"));
                BookModel.book.setNumber(rs.getInt("Numbers"));
                BookModel.book.setValue(rs.getInt("Value"));
                Blob blob = rs.getBlob("Image");
                if (blob != null) {
                    BookModel.book.setImage(blob.getBytes(1, (int) blob.length()));
                } else {
                    BookModel.book.setImage(null);
                }

                InformationBook newDialogNewBook = new InformationBook(null, true);
                InformationBook.addBook.setVisible(false);
                InformationBook.importFromFileJSON.setVisible(false);
                InformationBook.updateBook.setVisible(false);
                
                newDialogNewBook.bowerImg.setEnabled(false);
                newDialogNewBook.author.setEnabled(false);
                newDialogNewBook.content.setEnabled(false);
                newDialogNewBook.country.setEnabled(false);
                newDialogNewBook.name.setEnabled(false);
                newDialogNewBook.number.setEnabled(false);
                newDialogNewBook.type.setEnabled(false);
                newDialogNewBook.value.setEnabled(false);
                newDialogNewBook.year.setEnabled(false);
                newDialogNewBook.company.setEnabled(false);

                newDialogNewBook.setVisible(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    
}
