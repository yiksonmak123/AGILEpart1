
package cataloguemaintenance;

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

public class AddFlowers extends JFrame {

    private final String SQL_INSERT = "INSERT INTO FLOWER VALUES (?,?,?,?,?)";
    

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
    
    private JButton btnConfirm = new JButton("Confirm"); 
    private JButton btnReset = new JButton("Reset");
    

    private Connection con;
    private PreparedStatement pStmt_Insert;
    
    
    ResultSet rs;

    public AddFlowers() {

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
        panel.add(new JLabel("  1. Enter Flower Details"));
        panel.add(new JLabel("  2. Click Confirm Button To Add"));
       

        return panel;
    }

    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2));
         
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

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();              
                String custType = null;
                if (!txtFID.getText().isEmpty() && !txtFName.getText().isEmpty() && !txtFPrice.getText().isEmpty() && !txtForigin.getText().isEmpty()
                        && !txtFunit.getText().isEmpty()) {                   

                        sb.append("Customer IC : " + txtFID.getText() + "\n");
                        sb.append("Customer Name : " + txtFName.getText() + "\n");
                        sb.append("Customer Email: " + txtFPrice.getText() + "\n");
                        sb.append("Customer Address : " + txtForigin.getText() + "\n");
                        sb.append("Customer Contact Number : " + txtFunit.getText() + "\n");
                        
                        
                        sb.append("\n\nClick Confirm To Continue:");

                        int isConfirm = JOptionPane.showConfirmDialog(
                                null,
                                sb.toString(),
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION);

                        if (isConfirm == JOptionPane.YES_OPTION) {

                            String FlowID = txtFID.getText();
                            String FlowName = txtFName.getText();
                            String FlowPrice = txtFPrice.getText();
                            String FlowOrigin = txtForigin.getText();
                            String FlowUnit = txtFunit.getText();
                            
                            

                            try {

                                pStmt_Insert.setString(1, FlowID);
                                pStmt_Insert.setString(2, FlowName);
                                pStmt_Insert.setString(3, FlowPrice);
                                pStmt_Insert.setString(4, FlowOrigin);
                                pStmt_Insert.setString(5, FlowUnit);

                                pStmt_Insert.executeUpdate();
                                
                                JOptionPane.showMessageDialog(null, "New Flower Record Had Been Added To The Database!");

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

                txtFID.setText("");
                txtFName.setText("");
            }
        });


        panel.add(btnConfirm);     
        panel.add(btnReset);
        return panel;
    }


    private void initDbConnection() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/FlowerDB", "nbuser", "nbuser");
    }

    private void initPrepareStatement() throws SQLException {
        pStmt_Insert = con.prepareStatement(SQL_INSERT);       
        
    }

    public static void main(String[] args) {
        AddFlowers objFrame = new AddFlowers();

        objFrame.setTitle("Add Customer Records");
        objFrame.setSize(600, 300);
        objFrame.setLocationRelativeTo(null);
        objFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        objFrame.setVisible(true);
    }

}
