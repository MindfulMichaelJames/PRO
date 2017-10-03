package pref;
import javax.swing.*;  
import java.awt.event.*;
import java.awt.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import java.io.File;
public class OntLoader implements ActionListener{
    private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();  
    JTextField tfLHS, tfRHS;  
    JButton bLoad, bCreate;
    JLabel lHeading;
    JFrame f, frame;
    GridBagConstraints gbc;
    JPanel panelC, panelB;
    static OWLOntology ontology;
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

        lHeading = new JLabel("<html><heading>Preferential Reasoner for Ontologies</heading></html>");  
 
    
        bLoad = new JButton("Load Ontology");  
  
        bCreate = new JButton("Create Ontology");  

        bLoad.addActionListener(this);  
        bCreate.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelC.add(lHeading, gbc);

        panelB.add(bLoad);
        panelB.add(bCreate);

        f.add(panelC);
        f.add(panelB);
    }         
    public void actionPerformed(ActionEvent e) {  
        f.dispose();

        if(e.getSource()==bLoad){  
            try 
            {
                ontology = manager.loadOntologyFromOntologyDocument(new File((String)JOptionPane.showInputDialog(frame, "Ontology file name: ", "PRO", JOptionPane.PLAIN_MESSAGE)));
            } 
            catch(OWLOntologyCreationException error)
            {
                 System.out.println(error);
            }
        }else if(e.getSource()==bCreate){  
            try 
            {
                ontology = manager.createOntology(IRI.create((new File((String)JOptionPane.showInputDialog(frame, "Ontology file name: ", "PRO", JOptionPane.PLAIN_MESSAGE)).toURI())));
            } 
            catch(OWLOntologyCreationException error)
            {
                 System.out.println(error);
            }

            try 
            {
                manager.saveOntology(ontology);
            } 
            catch(OWLOntologyStorageException error)
            {
                 System.out.println(error);
            }
        }  
    } 

    public OntLoader(){
        prepGUI();
        showAdder();
    }

    public static OWLOntology getOnt()
    {
        return ontology;
    }

    private void showAdder(){
        f.setVisible(true);
    }
}