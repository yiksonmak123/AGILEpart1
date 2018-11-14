package flower.ordering;

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
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FlowerOrdering extends JFrame{
    
    private final String SQL_INSERT = "INSERT INTO FLOWERORDER (FLOWERTYPE ,QUANTITIES )VALUES (?,?)";
    
    
    private JLabel FlowerType = new JLabel("     Flower Type :");
    private JLabel Quantities = new JLabel("     Flower Quantities :");
    
    private JTextField txtFlowerType = new JTextField();
    private JTextField txtQuantities = new JTextField();
    private JButton btnConfirm = new JButton("Confirm");
    private JButton btnReset = new JButton("Reset");


    private Connection con;
    private PreparedStatement pStmt_Insert;
    ResultSet rs;
    
    private int intorderID = 1;
    int generatedKey = 0;
    public FlowerOrdering() {

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
    
    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
         
        panel.add(FlowerType);
        panel.add(txtFlowerType);     

        panel.add(Quantities);
        panel.add(txtQuantities);

       

        return panel;
    }
    
    
    private JPanel getHeaderPanel() {

        JPanel panel = new JPanel(new GridLayout(4, 1));

        panel.add(new JLabel("  -----Please select flower type and the quantites you needed----- "));
       

        return panel;
    }
    
    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();              
                if (!txtFlowerType.getText().isEmpty() && !txtQuantities.getText().isEmpty()) {                   

                        sb.append("Flower Type : " + txtFlowerType.getText() + "\n");
                        sb.append("Quantities : " + txtQuantities.getText() + "\n");                  
                        sb.append("\n\nClick Confirm To Continue:");

                        int isConfirm = JOptionPane.showConfirmDialog(
                                null,
                                sb.toString(),
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION);

                        if (isConfirm == JOptionPane.YES_OPTION) {

                            String FlowerType = txtFlowerType.getText();
                            String Quantities = txtQuantities.getText();
                            
                            

                            try {

                                pStmt_Insert.setString(1, FlowerType);
                                pStmt_Insert.setString(2, Quantities);
                                
//                                pang here ?
//                                
//                                eihh i want to update my primary key 
//                                        what i want is like if the data inside the table is empty , 
//                                                i want to assign the primary = 1 ; if got any record ady take the last primary 
//                                                        key number which is 1 then ++ 
//                                                                my problem is when i using this rs.next then got problem dy
                                pStmt_Insert.executeUpdate();
                                
                                JOptionPane.showMessageDialog(null, "New Order Record Had Been Created!");
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

                txtFlowerType.setText("");
                txtQuantities.setText("");
            }
        });


        panel.add(btnConfirm);     
        panel.add(btnReset);
        return panel;
    }
    
    
        private void initDbConnection() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/FlowerShopDB", "nbuser", "nbuser");
    }

    private void initPrepareStatement() throws SQLException {
        pStmt_Insert = con.prepareStatement(SQL_INSERT,Statement.RETURN_GENERATED_KEYS);       
        
    }
    
    public static void main(String[] args) {
        FlowerOrdering objFrame = new FlowerOrdering();

        objFrame.setTitle("New Order Records");
        objFrame.setSize(600, 300);
        objFrame.setLocationRelativeTo(null);
        objFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        objFrame.setVisible(true);
    }
    
    
}
