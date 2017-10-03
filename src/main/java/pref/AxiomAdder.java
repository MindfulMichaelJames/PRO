package pref;
import javax.swing.*;  
import java.awt.event.*;
import java.awt.*;
public class AxiomAdder implements ActionListener{  
    JTextField tfLHS, tfRHS;  
    JButton bAdd, bCancel;
    JLabel lLHS, lSCO, lRHS;
    JFrame f;
    GridBagConstraints gbc;
    BoxLayout layoutF, layoutB;
    JPanel panelLHS, panelSCO, panelOP, panelRHS, panelC, panelB;
    private void prepGUI(){
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;

        panelB = new JPanel();
        panelC = new JPanel();

        panelC.setLayout(new GridBagLayout());

        panelB.setLayout(new BoxLayout(panelB, BoxLayout.X_AXIS));

        f = new JFrame();
        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        f.setSize(400,100);

        tfLHS = new JTextField();  
 
        tfRHS = new JTextField();  

        lLHS = new JLabel("Class Expression");  

        lSCO = new JLabel("SubClassOf");  

        lRHS = new JLabel("Class Expression");  
    
        bAdd = new JButton("Add Axiom");  
  
        bCancel = new JButton("Cancel");  

        bAdd.addActionListener(this);  
        bCancel.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelC.add(lLHS, gbc);

        gbc.gridy = 0;
        gbc.gridx = 2;
        panelC.add(lRHS, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panelC.add(tfLHS, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panelC.add(lSCO, gbc);

        gbc.gridy = 1;
        gbc.gridx = 2;
        panelC.add(tfRHS, gbc);

        panelB.add(bCancel);
        panelB.add(bAdd);

        f.add(panelC);
        f.add(panelB);
    }         
    public void actionPerformed(ActionEvent e) {  
        String lhs=tfLHS.getText();  
        String rhs=tfRHS.getText();
        String newAx = lhs + " SubClassOf " + rhs;
        if(e.getSource()==bAdd){  
            System.out.println(newAx); 
        }else if(e.getSource()==bCancel){  
            System.out.println("Cancel");  
        }  
    } 

    public AxiomAdder(){
        prepGUI();
        showAdder();
    }

    private void showAdder(){
        f.setVisible(true);
    }

    // public static void main(String[] args) {
    //     AxiomAdder testAdd = new AxiomAdder();
    //     testAdd.showAdder();s
    // }
} 