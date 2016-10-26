/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.UserController;
import Controllers.MainController;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;


/**
 *
 * @author Duc Dung Dan
 */
public final class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
    }
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Login = new JPanel();
        jPanel8 = new JPanel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        SignUp4 = new JButton();
        jLabel11 = new JLabel();
        jLabel12 = new JLabel();
        Password = new JPasswordField();
        Email = new JFormattedTextField();
        SignIn = new JButton();
        showPassword = new JCheckBox();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new CardLayout());

        Login.setBackground(new Color(252, 252, 252));
        Login.setBorder(BorderFactory.createEtchedBorder(new Color(0, 0, 0), null));
        Login.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        jPanel8.setBackground(new Color(244, 249, 253));
        jPanel8.setBorder(BorderFactory.createEtchedBorder());

        jLabel9.setBackground(new Color(255, 255, 255));
        jLabel9.setFont(new Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new Color(101, 111, 121));
        jLabel9.setText("User login");

        jLabel10.setForeground(new Color(101, 111, 121));
        jLabel10.setText("Need an acount?");

        SignUp4.setBackground(new Color(237, 250, 255));
        SignUp4.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        SignUp4.setForeground(new Color(33, 113, 200));
        SignUp4.setText("Sign Up");
        SignUp4.setCursor(new Cursor(Cursor.HAND_CURSOR));
        SignUp4.setRolloverEnabled(false);
        SignUp4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SignUp4ActionPerformed(evt);
            }
        });

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(SignUp4)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addComponent(SignUp4))
        );

        jLabel11.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new Color(101, 111, 121));
        jLabel11.setText("Email");

        jLabel12.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new Color(101, 111, 121));
        jLabel12.setText("Password");

        Password.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                PasswordActionPerformed(evt);
            }
        });
        Password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                PasswordKeyPressed(evt);
            }
        });

        Email.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                EmailActionPerformed(evt);
            }
        });

        SignIn.setBackground(new Color(48, 124, 210));
        SignIn.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        SignIn.setForeground(new Color(255, 255, 255));
        SignIn.setText("Sign In");
        SignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        SignIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SignInActionPerformed(evt);
            }
        });

        showPassword.setForeground(new Color(101, 111, 121));
        showPassword.setText("Show password");
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showPasswordActionPerformed(evt);
            }
        });

        GroupLayout LoginLayout = new GroupLayout(Login);
        Login.setLayout(LoginLayout);
        LoginLayout.setHorizontalGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(LoginLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(Password, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(Email))
                        .addContainerGap(44, Short.MAX_VALUE))
                    .addGroup(LoginLayout.createSequentialGroup()
                        .addComponent(showPassword)
                        .addGap(44, 44, 44)
                        .addComponent(SignIn, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LoginLayout.setVerticalGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(LoginLayout.createSequentialGroup()
                .addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                    .addComponent(Email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                    .addComponent(Password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(LoginLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(SignIn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                    .addComponent(showPassword))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        getContentPane().add(Login, "card2");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SignUp4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_SignUp4ActionPerformed
        MainController.signUp();
    }//GEN-LAST:event_SignUp4ActionPerformed

    private void EmailActionPerformed(ActionEvent evt) {//GEN-FIRST:event_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailActionPerformed

    private void SignInActionPerformed(ActionEvent evt) {//GEN-FIRST:event_SignInActionPerformed
        UserController.login(this.Email.getText().trim(), String.valueOf(this.Password.getPassword()), this);
    }//GEN-LAST:event_SignInActionPerformed

    private void PasswordActionPerformed(ActionEvent evt) {//GEN-FIRST:event_PasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordActionPerformed

    private void PasswordKeyPressed(KeyEvent evt) {//GEN-FIRST:event_PasswordKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            UserController.login(this.Email.getText().trim(), String.valueOf(this.Password.getPassword()), this);
        }
    }//GEN-LAST:event_PasswordKeyPressed

    private void showPasswordActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showPasswordActionPerformed
        if (showPassword.isSelected()) {
            Password.setEchoChar((char) 0);
        } else {
            Password.setEchoChar('*');
        }
    }//GEN-LAST:event_showPasswordActionPerformed
  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFormattedTextField Email;
    private JPanel Login;
    private JPasswordField Password;
    private JButton SignIn;
    private JButton SignUp4;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel9;
    private JPanel jPanel8;
    private JCheckBox showPassword;
    // End of variables declaration//GEN-END:variables
}
