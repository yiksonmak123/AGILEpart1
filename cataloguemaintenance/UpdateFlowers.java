/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataloguemaintenance;

import cataloguemaintenance.AddFlowers;
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
public class UpdateFlowers extends JFrame{
    
    static UpdateFlowers UpdateFlowers;
    
    private final String SQL_SELECT = "SELECT * FROM FLOWER WHERE FLOWERNAME = ?";
    private final String SQL_UPDATE = "UPDATE FLOWER " + "SET FLOWERUNIT = ?," + " FLOWERORIGIN = ?" +  "WHERE FLOWERNAME = ?";
    
    private JLabel FID = new JLabel("     Flower ID ");
    private JLabel FName = new JLabel("     Flower Name");
    private JLabel FPrice = new JLabel("     Flower Price");
    private JLabel Forigin = new JLabel("     Flower Origin");
    private JLabel Funit = new JLabel("     Flower Unit");
    
    private JTextField txtFID = new JTextField();
    private JTextField txtFName = new JTextField();
    private JTextField txtFPrice = new JTextField();
    private JTextField txtForigin = new JTextField();
    private JTextField txtFunit = new JTextField();
    
    
    private JButton btnReset = new JButton("Reset");
    private JButton btnRetrieve = new JButton("Retrieve");
    private JButton btnUpdate = new JButton("Update");
    
    private Connection con;
    private PreparedStatement pStmt_Select, pStmt_Update;
    
    public UpdateFlowers(){
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

        panel.add(new JLabel("Please Enter Flower Name and Click Retrieve Button To Edit Personal Details"));
       

        return panel;
    }

    private JPanel getDisplayPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2));

        panel.add(FID);
        panel.add(txtFID);     

        panel.add(FName);
        panel.add(txtFName);

        panel.add(FPrice);
        panel.add(txtFPrice);
        
        panel.add(Forigin);
        panel.add(txtForigin);     

        panel.add(Funit);
        panel.add(txtFunit);
        
        return panel;
    }

    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        btnRetrieve.addActionListener(new RetrieveListener());
        btnUpdate.addActionListener(new UpdateListener());
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtFID.setText("");
                txtFName.setEditable(true);
                txtFPrice.setText("");
                txtForigin.setText("");
                txtFunit.setText("");
                
                txtFID.setEditable(true);
                txtFName.setEditable(true);
             
                 
                txtFID.requestFocus();
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
            
            ResultSet rs = selectRecord(txtFID.getText());

            try {
                if (rs.next()) {
                    txtFID.setText(rs.getString("FLOWERID"));
                    txtFPrice.setText(rs.getString("FLOWERPRICE"));                  
                    txtForigin.setText(rs.getString("FLOWERORIGIN"));
                    txtFunit.setText(rs.getString("FLOWERUNIT"));
                    
                    txtFID.setEditable(false);
                           
                    
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Flower Name Entered Not Found");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        private ResultSet selectRecord(String FName) {
            ResultSet rs = null;

            try {
                pStmt_Select.setString(1, FName);

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
           
            String FName  = txtFName.getText();
            String FPrice = txtFPrice.getText();
            String FUnit = txtFunit.getText();
            String FOrigin = txtForigin.getText();
            
            StringBuilder sb = new StringBuilder();

            try {
                pStmt_Update.setString(1, FName);
                pStmt_Update.setString(2, FPrice);
                pStmt_Update.setString(3, FUnit);
                pStmt_Update.setString(4, FOrigin);

                pStmt_Update.executeUpdate();

                sb.append("Flower ID : " + txtFID.getText() + "\n");
                sb.append("FLower Name : " + txtFName.getText() + "\n");
                sb.append("Flower Price: " + txtFPrice.getText() + "\n");
                sb.append("Flower Origin : " + txtForigin.getText() + "\n");
                sb.append("Flower Unit  : " + txtFunit.getText() + "\n");
                

                sb.append("\n\nConfirm Changes? ");

                int isConfirm = JOptionPane.showConfirmDialog(
                        null,
                        sb.toString(),
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (isConfirm == JOptionPane.YES_OPTION) {

                    JOptionPane.showMessageDialog(null, "Changes For Flower record had been saved");

                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    

    private void initDbConnection() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/FlowerDB", "nbuser", "nbuser");
    }

    private void initPrepareStatement() throws SQLException {
        pStmt_Select = con.prepareStatement(SQL_SELECT);
        pStmt_Update = con.prepareStatement(SQL_UPDATE);       
        
    }

    public static void main(String[] args) {
        UpdateFlowers objFrame = new UpdateFlowers();

        objFrame.setTitle("Update Customer Records");
        objFrame.setSize(700, 400);
        objFrame.setLocationRelativeTo(null);
        objFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        objFrame.setVisible(true);
    }
}
