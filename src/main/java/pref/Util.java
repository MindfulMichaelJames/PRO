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
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiomVisitor;
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
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
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
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.search.EntitySearcher;
import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;
import java.lang.String;
import java.lang.Boolean;
import java.util.Set;
import java.io.StringWriter;
import java.util.Collection;
import java.util.stream.Collectors;
import java.lang.IllegalArgumentException;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
//import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import org.obolibrary.macro.ManchesterSyntaxTool;
import org.semanticweb.owlapi.model.OWLOntologyID;

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

	public void makeDefeasible(OWLAxiom axiom){
		manager.removeAxiom(ontology, axiom);
		OWLAxiom outax = axiom.getAnnotatedAxiom(DEFAULTANNOTATIONS);
		manager.addAxiom(ontology, outax);
		System.out.println(ontID.getOntologyIRI().get());
		System.out.println(outax.getAnnotations());
		// return axiom.getAnnotatedAxiom(DEFAULTANNOTATIONS);
		try 
        {
            manager.saveOntology(ontology);
        } 
        catch(OWLOntologyStorageException error)
        {
             System.out.println(error);
        };
	}

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
		// return outax.getAnnotatedAxiom(rankAnnotation);
		// manager.removeAxiom(ontology, axiom);
		// manager.addAxiom(ontology, axiom.getAnnotatedAxiom(rankAnnotation));
	}

	// public void assignRank(Set<OWLSubClassOfAxiom> axioms, int rankInt){
	// 	for (OWLSubClassOfAxiom axiom : axioms){
	// 		assignRank(axiom, rankInt);
	// 	}
	// }

	public OWLClassExpression getQueryPart(){
		String input = (String)JOptionPane.showInputDialog(frame, "Enter a query: ", "PRO", JOptionPane.PLAIN_MESSAGE);
		ManchesterSyntaxTool parser = new ManchesterSyntaxTool(ontology);
		return parser.parseManchesterExpression(input);
	}

	public void newObjectProperty(){
		// IRI ontIRI = ontology.getOntologyID().getOntologyIRI().get();
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

	public void newClass(){
		// IRI ontIRI = ontology.getOntologyID().getOntologyIRI().get();
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

	public Set<OWLSubClassOfAxiom> getAllAxioms(){
		// AxiomType.SUBCLASS_OF, 
		return ontology.getAxioms(AxiomType.SUBCLASS_OF, Imports.INCLUDED);
	}

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

	public boolean isDefeasible(OWLAxiom axiom){
  		// System.out.println(axiom.isAnnotated());
  		Stream<OWLAnnotation> defAnnos = axiom.annotations(defeasibleAnnotationProperty);
  		Object[] arrAnnos = defAnnos.toArray();
  		if (Array.getLength(arrAnnos) == 0){
  			return false;
  		}
  		// for (OWLAnnotation annotation : axiom.getAnnotations(defeasibleAnnotationProperty)){
  		// 	System.out.println("got here");
  		// 	OWLLiteral defeasibleLiteral = (OWLLiteral) annotation.getValue();
  		// 	if (defeasibleLiteral.parseBoolean()){
  		// 		System.out.println(defeasibleLiteral.parseBoolean());
  		// 		return true;
  		// 	}
  		// }
      	else {
      		return true;
      	}
	}

	public int getRank(OWLAxiom axiom){
		OWLLiteral rankLiteral = null;
		for (OWLAnnotation annotation : axiom.getAnnotations(rankAnnotationProperty)){
			rankLiteral = (OWLLiteral) annotation.getValue();
		}
		return rankLiteral.parseInteger();
	}

	public OWLClassExpression getMaterialisation(OWLSubClassOfAxiom axiom){
		return df.getOWLObjectUnionOf(df.getOWLObjectComplementOf(axiom.getSubClass()), axiom.getSuperClass());
	}

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

	public OWLSubClassOfAxiom getFirstQuery(OWLClassExpression internalisation, OWLSubClassOfAxiom query){
		return df.getOWLSubClassOfAxiom(internalisation, df.getOWLObjectComplementOf(query.getSubClass()));
	}

	public OWLSubClassOfAxiom getSecondQuery(OWLClassExpression internalisation, OWLSubClassOfAxiom query){
		return df.getOWLSubClassOfAxiom(df.getOWLObjectIntersectionOf(internalisation, query.getSubClass()), query.getSuperClass());
	}

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

	public String printDefeasibleAxioms() {
		StringWriter sw = new StringWriter();
		ManchesterOWLSyntaxPrefixNameShortFormProvider ssfp = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
		ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(sw, ssfp);
		// OWLLogicalAxiomVisitor renderer = new OWLLogicalAxiomVisitor(sw, ssfp);

		for (OWLSubClassOfAxiom axiom : getDefeasibleAxioms()){
			renderer.visit(axiom);
		}

		return sw.toString();
	}
}