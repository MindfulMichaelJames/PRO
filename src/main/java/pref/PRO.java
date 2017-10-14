package pref;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import static org.semanticweb.owlapi.model.parameters.OntologyCopy.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import static org.semanticweb.owlapi.model.parameters.Imports.*;
import org.semanticweb.owlapi.model.AxiomType;
import static org.semanticweb.owlapi.model.AxiomType.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import java.io.File;
import java.util.*;
import java.lang.String;
import java.lang.Boolean;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.junit.Assert;

/*
* This class is the main class that the tool starts in.
* This class provides the interface for the user to interact with the tool.
*/
public class PRO {
	private static JFrame frame;
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private static OWLDataFactory df = manager.getOWLDataFactory();
	OWLOntology ontology;
	OWLOntologyID ontID;
	Util u;

	public static void main(String[] args) throws Exception {		
		PRO pro = new PRO();
        pro.runTool();
	}

	/*
	* Allows the user to enter the name of an ontology to be loaded.
	* The method then calls the method to show the menu.
	*/
	public void runTool(){
		try 
            {
                ontology = manager.loadOntologyFromOntologyDocument(new File((String)JOptionPane.showInputDialog(frame, "Ontology file name: ", "PRO", JOptionPane.PLAIN_MESSAGE)));
            } 
            catch(OWLOntologyCreationException error)
            {
                 System.out.println(error);
            }
        ontID = ontology.getOntologyID();
		u = new Util(ontID, manager);
		functionMenu();
	}

	/*
	* This method creates a menu with options to interact with the ontology.
	*/
	public void functionMenu(){
		frame =  new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JButton classButton = new JButton("Create new class");
        JButton opButton = new JButton("Create new object property");
        JButton newAxiom = new JButton("Create new axiom");
        JButton denoteDefeasible = new JButton("Change defeasibility");
        JButton editRanking = new JButton("Change ranking");
        JButton queryButton = new JButton("Query");
        //This button action opens an interface for adding new classes to the ontology
        classButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        u.newClass();
		    }
		});
		//This button action opens an interface for adding object properties to the ontology
		opButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        u.newObjectProperty();
		    }
		});
		//This button action opens an interface for adding new axioms to the ontology
		newAxiom.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        AxiomAdder axAd = new AxiomAdder(ontology);
		    }
		});
		//This button action opens an interface for specifying whether axioms are strict or defeasible
		denoteDefeasible.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        DefeasibleMaker dM = new DefeasibleMaker(manager, ontID, u);
		    }
		});
		//This button action opens an interface for changing the order of defeasible statements in the ranking
		editRanking.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        DefeasibleRanker dR = new DefeasibleRanker(manager, ontID, u);
		    }
		});
		//This button action opens an interface for querying the ontology
		queryButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        QueryGetter qG = new QueryGetter(ontology, u);
		    }
		});
        mainPanel.add(classButton);
        mainPanel.add(opButton);
        mainPanel.add(newAxiom);
        mainPanel.add(denoteDefeasible);
        mainPanel.add(editRanking);
        mainPanel.add(queryButton);

        frame.add(mainPanel);

        frame.pack();
        frame.setVisible(true);
	}


}

