/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.maintenance.invoice.payment;

import customer.maintenance.invoice.payment.AddCustomer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author ACER
 */
public class UpdateCustomer extends JFrame{
    
    static UpdateCustomer UpdateCustomer;
    
    private final String SQL_SELECT = "SELECT * FROM CUSTOMER WHERE CUSTIC = ?";
    private final String SQL_UPDATE = "UPDATE CUSTOMER " + "SET CUSTEMAIL = ?," + " CUSTADDRESS = ?," + " CUSTCONTACT = ?" + "WHERE CUSTIC = ?";

    private JLabel custIc = new JLabel("     Customer IC (Without '-')");
    private JLabel custName = new JLabel("     Customer Name");
    private JLabel custType = new JLabel("     Customer Type");
    private JLabel custEmail = new JLabel("     Customer Email");
    private JLabel custAddress = new JLabel("     Customer Address");
    private JLabel custContact = new JLabel("     Customer Contact Number (Without '-')");

    private JTextField txtCustIc = new JTextField();
    private JTextField txtCustName = new JTextField();
    private JTextField txtCustType = new JTextField();
    private JTextField txtCustEmail = new JTextField();
    private JTextField txtCustAddress = new JTextField();
    private JTextField txtCustContact = new JTextField();
    
    private JButton btnReset = new JButton("Reset");
    private JButton btnRetrieve = new JButton("Retrieve");
    private JButton btnUpdate = new JButton("Update");
    
    private Connection con;
    private PreparedStatement pStmt_Select, pStmt_Update;
    
    public UpdateCustomer(){
    setLayout(new BorderLayout());

        add(getHeaderPanel(), BorderLayout.NORTH);
        add(getDisplayPanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);

        try {
            initDbConnection();
            initPrepareStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    private JPanel getHeaderPanel() {

        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JLabel("Please Enter Customer IC And Click Retrieve Button To Edit Personal Details"));
       

        return panel;
    }

    private JPanel getDisplayPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2));

        panel.add(custIc);     
        panel.add(txtCustIc);
        
        panel.add(custName);     
        panel.add(txtCustName);
        
        panel.add(custType);
        panel.add(txtCustType);
        
        panel.add(custEmail);        
        panel.add(txtCustEmail);       
               
        panel.add(custAddress);    
        panel.add(txtCustAddress);
        
        panel.add(custContact);      
        panel.add(txtCustContact);
        
        return panel;
    }

    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        btnRetrieve.addActionListener(new RetrieveListener());
        btnUpdate.addActionListener(new UpdateListener());
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCustIc.setText("");
                txtCustName.setEditable(true);
                txtCustType.setText("");
                txtCustEmail.setText("");
                txtCustAddress.setText("");
                txtCustContact.setText("");
                
                txtCustIc.setEditable(true);
                txtCustName.setEditable(true);
                txtCustType.setEditable(true);  
                 
                txtCustIc.requestFocus();
            }

        });
       
        
        panel.add(btnUpdate);
        panel.add(btnRetrieve);
        panel.add(btnReset);
        
        return panel;

    }

    private class RetrieveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            ResultSet rs = selectRecord(txtCustIc.getText());

            try {
                if (rs.next()) {
                    txtCustIc.setText(rs.getString("CUSTIC"));
                    txtCustName.setText(rs.getString("CUSTNAME"));
                    txtCustType.setText(rs.getString("CUSTTYPE"));
                    txtCustEmail.setText(rs.getString("CUSTEMAIL"));                  
                    txtCustAddress.setText(rs.getString("CUSTADDRESS"));
                    txtCustContact.setText(rs.getString("CUSTCONTACT"));
                    
                    txtCustIc.setEditable(false);
                    txtCustName.setEditable(false);
                    txtCustType.setEditable(false);                    
                    
                    
                } else {
                    JOptionPane.showMessageDialog(null, "IC Entered Not Found");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        private ResultSet selectRecord(String custIC) {
            ResultSet rs = null;

            try {
                pStmt_Select.setString(1, custIC);

                rs = pStmt_Select.executeQuery();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            return rs;
        }
    }

    private class UpdateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String contactNum = txtCustContact.getText();
            String email = txtCustEmail.getText();
            String address = txtCustAddress.getText();
            String ic = txtCustIc.getText();
            StringBuilder sb = new StringBuilder();

            try {
                pStmt_Update.setString(1, email);
                pStmt_Update.setString(2, address);
                pStmt_Update.setString(3, contactNum);
                pStmt_Update.setString(4, ic);

                pStmt_Update.executeUpdate();

                sb.append("IC : " + txtCustIc.getText() + "\n");
                sb.append("Name : " + txtCustName.getText() + "\n");
                sb.append("Customer Type : " + txtCustType.getText() + "\n");
                sb.append("Email : " + txtCustEmail.getText() + "\n");
                sb.append("Address : " + txtCustAddress.getText() + "\n");
                sb.append("Contact Number : " + txtCustContact.getText() + "\n");
                

                sb.append("\n\nConfirm Changes? ");

                int isConfirm = JOptionPane.showConfirmDialog(
                        null,
                        sb.toString(),
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (isConfirm == JOptionPane.YES_OPTION) {

                    JOptionPane.showMessageDialog(null, "Changes For " + ic + " Had Been Saved!");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    

    private void initDbConnection() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/FlowerShopDB", "nbuser", "nbuser");
    }

    private void initPrepareStatement() throws SQLException {
        pStmt_Select = con.prepareStatement(SQL_SELECT);
        pStmt_Update = con.prepareStatement(SQL_UPDATE);       
        
    }

    public static void main(String[] args) {
        UpdateCustomer objFrame = new UpdateCustomer();

        objFrame.setTitle("Update Customer Records");
        objFrame.setSize(700, 400);
        objFrame.setLocationRelativeTo(null);
        objFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        objFrame.setVisible(true);
    }
}
