package pref;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import static org.semanticweb.owlapi.model.parameters.OntologyCopy.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import static org.semanticweb.owlapi.model.parameters.Imports.*;
import static org.semanticweb.owlapi.model.AxiomType.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import java.io.File;
import java.util.*;
import java.util.stream.Stream;
import java.lang.reflect.Array;
import java.lang.String;
import java.lang.Boolean;
import java.io.StringWriter;
import java.lang.IllegalArgumentException;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import org.obolibrary.macro.ManchesterSyntaxTool;
import org.semanticweb.owlapi.model.OWLOntologyID;

/*
* This class contains utiliy methods for interacting with the ontology.
*/
public class Util {
	private static OWLOntologyManager manager;
	private static OWLDataFactory df;
	private static final IRI defeasibleIRI = IRI.create("http://cair.cs.uct.ac.za/defeasible");
	private static final IRI rankIRI = IRI.create("http://cair.cs.uct.ac.za/rank");
	public static OWLAnnotationProperty defeasibleAnnotationProperty;
	public static OWLAnnotationProperty rankAnnotationProperty;
	public static OWLAnnotationValue defeasibleAnnotationValue;
	public static OWLAnnotationValue strictAnnotationValue;
	public static OWLAnnotation defeasibleAnnotation;
	public static OWLAnnotation strictAnnotation;
	public static OWLAnnotation defaultRankAnnotation;
	public static OWLAnnotation[] ANNOTATIONS;
	public static Set<OWLAnnotation> DEFAULTANNOTATIONS;
	public OWLOntologyID ontID;

	private JFrame frame;
	private OWLOntology ontology;

	public Util(OWLOntologyID oID, OWLOntologyManager m){
		manager = m;
		ontID = oID;
		ontology = m.getOntology(oID);
		df = manager.getOWLDataFactory();

		defeasibleAnnotationProperty = df.getOWLAnnotationProperty(defeasibleIRI);
		rankAnnotationProperty = df.getOWLAnnotationProperty(rankIRI);
		defeasibleAnnotationValue = df.getOWLLiteral(true);
		strictAnnotationValue = df.getOWLLiteral(false);
		defeasibleAnnotation = df.getOWLAnnotation(defeasibleAnnotationProperty, defeasibleAnnotationValue);
		strictAnnotation = df.getOWLAnnotation(defeasibleAnnotationProperty, strictAnnotationValue);
		defaultRankAnnotation = df.getOWLAnnotation(rankAnnotationProperty, df.getOWLLiteral(0));
		ANNOTATIONS = new OWLAnnotation[] { defeasibleAnnotation, defaultRankAnnotation };
		DEFAULTANNOTATIONS = new HashSet<>(Arrays.asList(ANNOTATIONS));
	}

	/*
	* This method adds a true defeasible annotation and a ranking annotation of 0 to the inputted axiom
	* This is done by removing the original axiom and adding a new one with the annotations
	*/
	public void makeDefeasible(OWLAxiom axiom){
		manager.removeAxiom(ontology, axiom);
		OWLAxiom outax = axiom.getAnnotatedAxiom(DEFAULTANNOTATIONS);
		manager.addAxiom(ontology, outax);
		try 
        {
            manager.saveOntology(ontology);
        } 
        catch(OWLOntologyStorageException error)
        {
             System.out.println(error);
        };
	}

	/*
	* This method adds changes the defeasible annotation from true to false, thereby denoting a strict axiom
	* This is done by removing the original axiom and adding a new one with the annotations
	*/
	public void makeStrict(OWLAxiom axiom){
		Set<OWLAnnotation> strictAnnotations = new HashSet<>();
		strictAnnotations.add(strictAnnotation);
		manager.removeAxiom(ontology, axiom);
		OWLAxiom outax = axiom.getAxiomWithoutAnnotations();
		manager.addAxiom(ontology, outax.getAnnotatedAxiom(strictAnnotations));
		try 
        {
            manager.saveOntology(ontology);
        } 
        catch(OWLOntologyStorageException error)
        {
             System.out.println(error);
        };
	}

	/*
	* This method assigns a new specified rank annotation value to the inputted axiom
	*/
	public void assignRank(OWLAxiom axiom, int rankInt){
		Set<OWLAnnotation> rankAnnotation = new HashSet<>();
		rankAnnotation.add(defeasibleAnnotation);
		rankAnnotation.add(df.getOWLAnnotation(rankAnnotationProperty, df.getOWLLiteral(rankInt)));
		OWLAxiom outax = axiom.getAxiomWithoutAnnotations();
		System.out.println(outax);
		manager.removeAxiom(ontology, axiom);
		manager.addAxiom(ontology, outax.getAnnotatedAxiom(rankAnnotation));
		try 
        {
            manager.saveOntology(ontology);
        } 
        catch(OWLOntologyStorageException error)
        {
             System.out.println(error);
        };
	}

	/*
	* This method adds a new object property to the ontology.
	* This method produces a pop-up that allows the user to enter a new object property name.
	*/
	public void newObjectProperty(){
		String input = "#" + (String)JOptionPane.showInputDialog(frame, "Enter a new object property: ", "PRO", JOptionPane.PLAIN_MESSAGE);
		OWLObjectProperty opToAdd =  df.getOWLObjectProperty(IRI.create(input));
		OWLDeclarationAxiom daxiomop = df.getOWLDeclarationAxiom(opToAdd);
		manager.addAxiom(ontology, daxiomop);
		try 
        {
            manager.saveOntology(ontology);
        } 
        catch(OWLOntologyStorageException error)
        {
             System.out.println(error);
        };
	}

	/*
	* This method adds a new class to the ontology.
	* This method produces a pop-up that allows the user to enter a new class name.
	*/
	public void newClass(){
		String input = "#" + (String)JOptionPane.showInputDialog(frame, "Enter a new class name: ", "PRO", JOptionPane.PLAIN_MESSAGE);
		OWLClass classToAdd = df.getOWLClass(IRI.create(input));
		manager.addAxiom(ontology, df.getOWLDeclarationAxiom(classToAdd));
		try 
        {
            manager.saveOntology(ontology);
        } 
        catch(OWLOntologyStorageException error)
        {
             System.out.println(error);
        };
	}

	/*
	* This method returns all subsumption axioms in the ontology
	*/
	public Set<OWLSubClassOfAxiom> getAllAxioms(){
		return ontology.getAxioms(AxiomType.SUBCLASS_OF, Imports.INCLUDED);
	}

	/*
	* This method returns all the defeasible subsumption axioms in the ontology
	*/
	public Set<OWLSubClassOfAxiom> getDefeasibleAxioms(){
		Set<OWLAxiom> inAxioms = ontology.getAxioms();
		Set<OWLSubClassOfAxiom> outAxioms =  new HashSet<>();
		for (OWLAxiom axiom : inAxioms){
      		if (isDefeasible(axiom)){
      			outAxioms.add((OWLSubClassOfAxiom) axiom);
      		}
      	}
      	return outAxioms;
	}

	/*
	* This method checks if an axiom is defeasible
	*/
	public boolean isDefeasible(OWLAxiom axiom){
  		Stream<OWLAnnotation> defAnnos = axiom.annotations(defeasibleAnnotationProperty);
  		Object[] arrAnnos = defAnnos.toArray();
  		if (Array.getLength(arrAnnos) == 0){
  			return false;
  		}
      	else {
      		return true;
      	}
	}

	/*
	* This method returns the rank of an axiom
	*/
	public int getRank(OWLAxiom axiom){
		OWLLiteral rankLiteral = null;
		for (OWLAnnotation annotation : axiom.getAnnotations(rankAnnotationProperty)){
			rankLiteral = (OWLLiteral) annotation.getValue();
		}
		return rankLiteral.parseInteger();
	}

	/*
	* This method returns the materialisation of an axiom
	*/
	public OWLClassExpression getMaterialisation(OWLSubClassOfAxiom axiom){
		return df.getOWLObjectUnionOf(df.getOWLObjectComplementOf(axiom.getSubClass()), axiom.getSuperClass());
	}

	/*
	* This method returns a map that contains the internalisations for every ranking level in the ontology
	*/
	public TreeMap<Integer, OWLClassExpression> getInternalisations(){
		Set<OWLSubClassOfAxiom> inAxioms = getDefeasibleAxioms();
 		TreeMap<Integer, Set<OWLClassExpression>> rankAxioms = new TreeMap<>();
 		for (OWLAxiom axiom : inAxioms){
 			int rank = getRank(axiom);
 			Set<OWLClassExpression> newRank = rankAxioms.get(rank);
 			if (newRank == null){
 				newRank = new HashSet<OWLClassExpression>();
 				rankAxioms.put(rank, newRank);
 			}
 			newRank.add(getMaterialisation((OWLSubClassOfAxiom) axiom));
 		}
 		TreeMap<Integer, OWLClassExpression> internalisations = new TreeMap<>();
 		rankAxioms.forEach((k,v)->{
			internalisations.put(k, df.getOWLObjectIntersectionOf(v));
		});
        return internalisations;
	}

	/*
	* This method returns the first axiom that needs to be checked for TBox entailment in the generalised rational closure algorithm.
	* This axiom is used to check when the antecedent of a query is no longer exceptional.
	*/
	public OWLSubClassOfAxiom getFirstQuery(OWLClassExpression internalisation, OWLSubClassOfAxiom query){
		return df.getOWLSubClassOfAxiom(internalisation, df.getOWLObjectComplementOf(query.getSubClass()));
	}

	/*
	* This method returns the second axiom that needs to be checked for entailment in the generalised rational closure algorithm.
	* If this axiom is entailed by the TBox, then the original query is in the rational closure of the knowledge base.
	*/
	public OWLSubClassOfAxiom getSecondQuery(OWLClassExpression internalisation, OWLSubClassOfAxiom query){
		return df.getOWLSubClassOfAxiom(df.getOWLObjectIntersectionOf(internalisation, query.getSubClass()), query.getSuperClass());
	}

	/*
	* This method returns the TBox (in the form of an ontology) that contains all the strict axioms.
	*/
	public OWLOntology getTBox(){
		Set<OWLSubClassOfAxiom> axioms = getAllAxioms();
		try {
			OWLOntology TBox = manager.createOntology();
			for (OWLSubClassOfAxiom axiom : axioms){
				Set<OWLAnnotation> annotations = axiom.getAnnotations(defeasibleAnnotationProperty);
				if (!annotations.contains(defeasibleAnnotation)){
					manager.addAxiom(TBox, axiom);
				}
			}
			return TBox;
		}
		catch (OWLOntologyCreationException e){
			System.out.println("TBox ontology not created");
			return null;
		}
	}

	/*
	* This method performs the generalised rational closure algorithm on the original query.
	*/
	public boolean performRationalClosure(OWLSubClassOfAxiom query){
		OWLOntology TBox = getTBox();
		Map<Integer, OWLClassExpression> internalisations = getInternalisations();
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(TBox);
		int ranks = internalisations.size();
		for (int rank = 0; rank < ranks; rank++){
			if (!reasoner.isEntailed(getFirstQuery(internalisations.get(rank), query))){
				if (reasoner.isEntailed(getSecondQuery(internalisations.get(rank), query))){
					return true;
				}
				return false;
			}
		}
		if (reasoner.isEntailed(query)) {
			return true;
		}
		return false;
	}
}