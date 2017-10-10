package pref;
import javax.swing.*;  
import java.awt.event.*;
import java.awt.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.obolibrary.macro.ManchesterSyntaxTool;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;


public class QueryGetter implements ActionListener{
    Util u;  
    JTextField tfLHS, tfRHS;  
    JButton bAdd, bCancel;
    JLabel lLHS, lSCO, lRHS;
    JFrame f;
    GridBagConstraints gbc;
    BoxLayout layoutF, layoutB;
    JPanel panelC, panelB;
    OWLOntology o;
    ManchesterSyntaxTool parser;
    private static final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private static final OWLDataFactory df = manager.getOWLDataFactory();

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
    
        bAdd = new JButton("Query");  
  
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

        OWLSubClassOfAxiom inputQuery = df.getOWLSubClassOfAxiom(parser.parseManchesterExpression(lhs), parser.parseManchesterExpression(rhs));
        // String newAx = lhs + " SubClassOf " + rhs;
        System.out.println(inputQuery);
        if(e.getSource()==bAdd){  
            Boolean entailed = u.performRationalClosure(inputQuery);
            if (entailed){
                System.out.println("In rational closure");
                JOptionPane.showMessageDialog(null, "In rational closure", "Result", JOptionPane.INFORMATION_MESSAGE);
            } 
            else {
                System.out.println("Not in rational closure");
                JOptionPane.showMessageDialog(null, "Not in rational closure", "Result", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getSource()==bCancel){  
            f.dispose();  
        }  
    } 

    public QueryGetter(OWLOntology ontology, Util ontUtil){
        parser = new ManchesterSyntaxTool(ontology);
        o = ontology;
        u = ontUtil;
        prepGUI();
        showAdder();
    }

    private void showAdder(){
        f.setVisible(true);
    }
}