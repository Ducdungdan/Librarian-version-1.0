/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static Controllers.MainController.truncate;
import Views.ProductRow;
import Models.*;
import Object.RentBook;
import java.awt.GridLayout;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Duc Dung Dan
 */
public class OrderController {
    public static  JPanel jpanelOrder;
    public static void showOrder(JPanel order) {
        jpanelOrder = order;
        RentBook rentBook = new RentBook();
        Date dateNow = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        ResultSet rs;
        for(int i = 0; i < RentBookModel.rentBook.size(); ++i) {
            rentBook = RentBookModel.rentBook.get(i);
            rs = BookModel.getDataBook(rentBook.getIdBook());
            ProductRow row = new ProductRow();
            //GridLayout girdlayout = new GridLayout(i, i);
            //row.setLayout(girdlayout);
            order.setLayout(new GridLayout(RentBookModel.rentBook.size()+1, 1,5,5));
            order.add(row.productRow);
            order.updateUI();
            try {
                if(rs.next()) {
                    row.name.setText(truncate(rs.getString("Name"), 100));
                    row.author.setText(truncate("Tác giả: " + rs.getString("Author"), 50));
                    row.type.setText(truncate("Thể loại: " + rs.getString("Type"), 50));
                    row.company.setText(truncate(rs.getString("Publishing_company"), 50));
                    row.day_borrow.setText("Ngày mượn: " + df.format(dateNow));
                    try {
                        row.day_return.setText("Hạn trả: " + MainController.addDays(dateNow, 60));
                    } catch (ParseException ex) {
                        Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    row.time_to_user.setText("Thời gian mượn: 60 ngày");
                    row.rental.setText("Giá: " + rentBook.getRental().toString());
                    if (rs.getBlob("Image") != null) {
                        Blob blob = rs.getBlob("Image");
                        ImageIcon icon = new ImageIcon(blob.getBytes(1, (int) blob.length()));
                        row.imageBook.setIcon(icon);
                    } else {
                        String imagePath = "src/Image/book-null.png";
                        ImageIcon icon = new ImageIcon(imagePath);
                        row.imageBook.setIcon(icon);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void closeProductRow(JPanel row) {
        int i = jpanelOrder.getComponentZOrder(row);
        String question = "Bạn muốn bỏ đầu sách có id = " + RentBookModel.rentBook.get(i).getIdBook().toString() + " ra khỏi giỏ hàng";
        if (JOptionPane.showConfirmDialog(null, question, "Thông báo", 2) == 0) {
            RentBookModel.rentBook.remove(i);
            jpanelOrder.remove(i);
            jpanelOrder.updateUI();
        }
    }
}
