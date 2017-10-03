package pref;

import org.semanticweb.HermiT.Reasoner;
//import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.HasIndex;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import static org.semanticweb.owlapi.model.parameters.OntologyCopy.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import static org.semanticweb.owlapi.model.parameters.Imports.*;
import org.semanticweb.owlapi.model.AxiomType;
import static org.semanticweb.owlapi.model.AxiomType.*;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiomShortCut;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;
import java.lang.String;
import java.lang.Boolean;
import java.util.Set;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
//import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.junit.Assert;

public class PRO {
	private static JFrame frame;
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private static OWLDataFactory df = manager.getOWLDataFactory();
	private static OWLAnnotationProperty userRank = df.getOWLAnnotationProperty(IRI.create("#userRank"));
	static OWLOntology ontology;
	static Util u;

	public static void main(String[] args) throws Exception {

		ontology = manager.loadOntologyFromOntologyDocument(new File((String)JOptionPane.showInputDialog(frame, "Ontology file name: ", "PRO", JOptionPane.PLAIN_MESSAGE)));
		//menu load or create ontology

		//add classes and objectproperties and axioms
		// OntLoader ontLoader = new OntLoader();

		// while (ontology == null){
		// 	ontology = ontLoader.getOnt();
		// }

		if (!(ontology == null)){
			u = new Util(ontology);
			functionMenu();
		}
		
		


		System.out.println(ontology);



	}

	// public static OWLOntology loadOntology(){
	// 	OntLoader ontLoader = new OntLoader();
	// 	return ontLoader.getOnt();
	// }

	public static void functionMenu(){
		frame =  new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JButton classButton = new JButton("Create new class");
        JButton opButton = new JButton("Create new object property");
        JButton newAxiom = new JButton("Create new axiom");
        JButton denoteDefeasible = new JButton("Change defeasibility");
        JButton editRanking = new JButton("Change ranking");
        JButton queryButton = new JButton("Query");

        classButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        manager.addAxiom(ontology, u.newClass());
		        try 
		        {
		            manager.saveOntology(ontology);
		        } 
		        catch(OWLOntologyStorageException error)
		        {
		             System.out.println(error);
		        };
		    }
		});
		opButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        u.newObjectProperty();
		        try 
		        {
		            manager.saveOntology(ontology);
		        } 
		        catch(OWLOntologyStorageException error)
		        {
		             System.out.println(error);
		        };
		    }
		});
		newAxiom.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        AxiomAdder axAd = new AxiomAdder();
		        try 
		        {
		            manager.saveOntology(ontology);
		        } 
		        catch(OWLOntologyStorageException error)
		        {
		             System.out.println(error);
		        };
		    }
		});
		denoteDefeasible.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        u = new Util(ontology);
		        DefeasibleMaker dM = new DefeasibleMaker(ontology, u.getDefeasibleAxioms(u.getAllAxioms()));
		        System.out.println(ontology);
		        try 
		        {
		            manager.saveOntology(ontology);
		        } 
		        catch(OWLOntologyStorageException error)
		        {
		             System.out.println(error);
		        };
		    }
		});
		editRanking.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        DefeasibleRanker dR = new DefeasibleRanker(ontology, u.getDefeasibleAxioms(u.getAllAxioms()));
		        System.out.println(ontology);
		    	try 
		        {
		            manager.saveOntology(ontology);
		        } 
		        catch(OWLOntologyStorageException error)
		        {
		             System.out.println(error);
		        };
		    }
		});
		queryButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
		        DefeasibleRanker dR = new DefeasibleRanker(ontology, u.getDefeasibleAxioms(u.getAllAxioms()));
		    	try 
		        {
		            manager.saveOntology(ontology);
		        } 
		        catch(OWLOntologyStorageException error)
		        {
		             System.out.println(error);
		        };
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
		// frame.revalidate();
        frame.setVisible(true);
	}


}
