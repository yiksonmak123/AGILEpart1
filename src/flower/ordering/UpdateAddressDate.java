package flower.ordering;

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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author ACER
 */
public class UpdateAddressDate extends JFrame{
    
    static UpdateAddressDate editDonorDetailsObjFrame;
    
    private final String SQL_SELECT = "SELECT * FROM dateaddress WHERE ORDERID = ?";
    private final String SQL_UPDATE = "UPDATE DATEADDRESS SET DATE = ?," + " TIME = ? ," +  " ADDRESS = ? "+ "WHERE ORDERID = ?";

    private JLabel OID = new JLabel("ORDER ID :");
    private JLabel DATE = new JLabel("DATE (DD/MM/YYYY) :");
    private JLabel TIME = new JLabel("TIME (am/pm):");
    private JLabel ADDRESS = new JLabel("ADDRESS :");

    private JTextField txtORDERID = new JTextField();
    private JTextField txtDate = new JTextField();
    private JTextField txtTime = new JTextField();
    private JTextField txtAddress = new JTextField();


    private JButton btnReset = new JButton("Reset");
    private JButton btnRetrieve = new JButton("Retrieve");
    private JButton btnUpdate = new JButton("Update");
    
    private Connection con;
    private PreparedStatement pStmt_Select, pStmt_Update;
    
    public UpdateAddressDate(){
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

        panel.add(new JLabel("Please Enter ORDERID And Click Retrieve Button To Edit ORDER Details"));
       

        return panel;
    }

    private JPanel getDisplayPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2));

        panel.add(OID);     
        panel.add(txtORDERID);
//        txtORDERID.setEditable(false);
        
        panel.add(DATE);     
        panel.add(txtDate);

        
        panel.add(TIME);
        panel.add(txtTime);

        
        panel.add(ADDRESS);        
        panel.add(txtAddress);       

 
        return panel;
    }

    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        btnRetrieve.addActionListener(new RetrieveListener());
        btnUpdate.addActionListener(new UpdateListener());
        
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtORDERID.setText("");
                txtORDERID.setEditable(true);
                txtDate.setText("");
                txtTime.setText("");
                txtAddress.setText("");

                
                
                txtORDERID.requestFocus();
            }

        });
       

        panel.add(btnRetrieve);
        panel.add(btnUpdate);
        panel.add(btnReset);
        
        return panel;

    }

    private class RetrieveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            txtORDERID.setEditable(false);
            ResultSet rs = selectRecord(txtORDERID.getText());

            try {
                if (rs.next()) {
                    txtDate.setText(rs.getString("DATE"));
                    txtTime.setText(rs.getString("TIME"));
                    txtAddress.setText(rs.getString("ADDRESS"));

                } else {
                    JOptionPane.showMessageDialog(null, "Order ID Entered Not Found");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        private ResultSet selectRecord(String ORDERID) {
            ResultSet rs = null;

            try {
                pStmt_Select.setString(1, ORDERID);

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
            String date = txtDate.getText();
            String time = txtTime.getText();
            String address = txtAddress.getText();
            String OrderID = txtORDERID.getText();
            StringBuilder sb = new StringBuilder();

            try {
                pStmt_Update.setString(1, date);
                pStmt_Update.setString(2, time);
                pStmt_Update.setString(3, address);
                pStmt_Update.setString(4, OrderID);

                pStmt_Update.executeUpdate();
                
                sb.append("ORDER ID      : " + txtORDERID.getText() + "\n");
                sb.append("DATE          : " + txtDate.getText() + "\n");
                sb.append("TIME          : " + txtTime.getText() + "\n");
                sb.append("ADDRESS       : " + txtAddress.getText() + "\n");


                sb.append("\n\nConfirm Changes? ");

                int isConfirm = JOptionPane.showConfirmDialog(
                        null,
                        sb.toString(),
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (isConfirm == JOptionPane.YES_OPTION) {

                    JOptionPane.showMessageDialog(null, "Changes For " + OrderID + " Had Been Saved!");
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

        editDonorDetailsObjFrame = new UpdateAddressDate();

        editDonorDetailsObjFrame.setTitle("Update Date/Time and Address");
        editDonorDetailsObjFrame.setSize(700, 350);
        editDonorDetailsObjFrame.setLocationRelativeTo(null);
        editDonorDetailsObjFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editDonorDetailsObjFrame.setVisible(true);

    }
}
