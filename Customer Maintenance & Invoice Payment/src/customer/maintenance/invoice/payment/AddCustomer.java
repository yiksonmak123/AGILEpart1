package customer.maintenance.invoice.payment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

public class AddCustomer extends JFrame {

    private final String SQL_INSERT = "INSERT INTO CUSTOMER VALUES (?,?,?,?,?,?)";
    

    private JLabel custIc = new JLabel("     Customer IC (Without '-')");
    private JLabel custName = new JLabel("     Customer Name");
    private JLabel custType = new JLabel("     Customer Type");
    private JLabel custEmail = new JLabel("     Customer Email");
    private JLabel custAddress = new JLabel("     Customer Address");
    private JLabel custContact = new JLabel("     Customer Contact Number (Without '-')");
    
    private JTextField txtCustIc = new JTextField();
    private JTextField txtCustName = new JTextField();
    private JTextField txtCustEmail = new JTextField();
    private JTextField txtCustAddress = new JTextField();
    private JTextField txtCustContact = new JTextField();
    
    private ButtonGroup BGCustomerType = new ButtonGroup();
    private String[] customerType = {"Normal Customer", "Corporate Customer"};
    private JRadioButton[] CBCustomerType = new JRadioButton[customerType.length];
    
    private JButton btnConfirm = new JButton("Confirm"); 
    private JButton btnReset = new JButton("Reset");
    

    private Connection con;
    private PreparedStatement pStmt_Insert;
    
    
    ResultSet rs;

    public AddCustomer() {

        setLayout(new BorderLayout());
        
        try {
            initDbConnection();
            initPrepareStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        add(getHeaderPanel(), BorderLayout.NORTH);
        add(getInputPanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);

     
    }
    

    private JPanel getHeaderPanel() {

        JPanel panel = new JPanel(new GridLayout(4, 1));

        panel.add(new JLabel("  Please Follow Instrcutions Below: "));
        panel.add(new JLabel("  1. Enter Customer Details"));
        panel.add(new JLabel("  2. Click Confirm Button To Add"));
       

        return panel;
    }

    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2));
         
        panel.add(custIc);
        panel.add(txtCustIc);     

        panel.add(custName);
        panel.add(txtCustName);

        panel.add(custType);
        panel.add(getCustomerType());
        
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

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();              
                String custType = null;
                if (!txtCustIc.getText().isEmpty() && !txtCustName.getText().isEmpty() && !txtCustEmail.getText().isEmpty() && !txtCustAddress.getText().isEmpty()
                        && !txtCustContact.getText().isEmpty()) {                   

                        sb.append("Customer IC : " + txtCustIc.getText() + "\n");
                        sb.append("Customer Name : " + txtCustName.getText() + "\n");
                        sb.append("Customer Type : " ); 
                        for (int i = 0; i < CBCustomerType.length; i++) {
                                    if (CBCustomerType[i].isSelected()) {
                                        sb.append(CBCustomerType[i].getText());
                                        custType = CBCustomerType[i].getText();
                                        
                                        sb.append("\n");
                                    }
                                }
                        sb.append("Customer Email: " + txtCustEmail.getText() + "\n");
                        sb.append("Customer Address : " + txtCustAddress.getText() + "\n");
                        sb.append("Customer Contact Number : " + txtCustContact.getText() + "\n");
                        
                        
                        sb.append("\n\nClick Confirm To Continue:");

                        int isConfirm = JOptionPane.showConfirmDialog(
                                null,
                                sb.toString(),
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION);

                        if (isConfirm == JOptionPane.YES_OPTION) {

                            String custIc = txtCustIc.getText();
                            String custName = txtCustName.getText();
                            String custEmail = txtCustEmail.getText();
                            String custAddress = txtCustAddress.getText();
                            String custContact = txtCustContact.getText();
                            
                            

                            try {

                                pStmt_Insert.setString(1, custIc);
                                pStmt_Insert.setString(2, custName);
                                pStmt_Insert.setString(3, custType);
                                pStmt_Insert.setString(4, custEmail);
                                pStmt_Insert.setString(5, custAddress);
                                pStmt_Insert.setString(6, custContact);
                                
                                pStmt_Insert.executeUpdate();
                                
                                JOptionPane.showMessageDialog(null, "New Customer Record Had Been Added To The Database!");

                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                            }

                        }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Please Fill Up All The Details", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                txtCustIc.setText("");
                txtCustName.setText("");
            }
        });


        panel.add(btnConfirm);     
        panel.add(btnReset);
        return panel;
    }
    
    private JPanel getCustomerType() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        for (int i = 0; i < customerType.length; i++) {
            CBCustomerType[i] = new JRadioButton(customerType[i], true);
            BGCustomerType.add(CBCustomerType[i]);

            panel.add(CBCustomerType[i]);
        }
        return panel;
    }

    private void initDbConnection() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/FlowerShopDB", "nbuser", "nbuser");
    }

    private void initPrepareStatement() throws SQLException {
        pStmt_Insert = con.prepareStatement(SQL_INSERT);       
        
    }

    public static void main(String[] args) {
        AddCustomer objFrame = new AddCustomer();

        objFrame.setTitle("Add Customer Records");
        objFrame.setSize(600, 300);
        objFrame.setLocationRelativeTo(null);
        objFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        objFrame.setVisible(true);
    }

}
