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
import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;
import java.lang.String;
import java.lang.Boolean;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collectors;
import java.lang.IllegalArgumentException;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
//import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxInlineAxiomParser;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import javax.swing.JOptionPane;
import javax.swing.JFrame;


public class Util {
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private OWLDataFactory df = manager.getOWLDataFactory();
	private static final IRI defeasibleIRI = IRI.create("defeasible");
	private static final IRI rankIRI = IRI.create("rank");
	public static final OWLAnnotationProperty defeasibleAnnotationProperty = df.getOWLAnnotationProperty(defeasibleIRI);
	public static final OWLAnnotationProperty rankAnnotationProperty = df.getOWLAnnotationProperty(rankIRI);
	public static final OWLAnnotationValue defeasibleAnnotationValue = df.getOWLLiteral(true);
	public static final OWLAnnotationValue strictAnnotationValue = df.getOWLLiteral(false);
	public static final OWLAnnotation defeasibleAnnotation = df.getOWLAnnotaion(defeasibleAnnotationProperty, defeasibleAnnotationValue);
	public static final OWLAnnotation defaultRankAnnotation = df.getOWLAnnotaion(rankAnnotationProperty, df.getOWLLiteral(0));
	public static final OWLAnnotation[] ANNOTATIONS = new OWLAnnotation[] { defeasibleAnnotation, defaultRankAnnotation };
	public static final Set<OWLAnnotation> DEFAULTANNOTATIONS = new HashSet<>(Arrays.asList(ANNOTATIONS));

	private OWLOntology ontology;
	private ManchesterOWLSyntaxInlineAxiomParser parser = new ManchesterOWLSyntaxInlineAxiomParser(OWLDataFactory dataFactory, OWLEntityChecker checker);
	private OWLEntityChecker entityChecker = new ShortFormEntityChecker(new SimpleShortFormProvider());


	public Util(OWLOntology ontology){
		this.ontology = ontology;
	}

	public void makeDefeasible(OWLAxiom axiom){
		return axiom.getAnnotatedAxiom(DEFAULTANNOTATIONS);
	}

	public void makeStrict(OWLAxiom axiom){
		return axiom.getAnnotatedAxiom(defeasibleAnnotationProperty, strictAnnotationValue);
	}

	public OWLAxiom assignRank(OWLAxiom axiom, int rankInt){
		OWLAnnotationValue rankAnnotationValue = df.getOWLLiteral(rankInt);
		return axiom.getAnnotatedAxiom(rankAnnotationProperty, rankAnnotationValue);
	}

	public Set<OWLAxiom> assignRank(Set<OWLAxiom> axioms, int rankInt){
		for (OWLAxiom axiom : axioms){
			axioms.remove(axiom);
			axioms.add(assignRank(axiom));
		}
		return axioms;
	}

	public OWLAxiom getQuery(){
		String input = (String)JOptionPane.showInputDialog(frame, "Enter a query: ", "PRO", JOptionPane.PLAIN_MESSAGE);
		return parser.parse(input);
	}

	public OWLClassExpression getAntecedent(OWLAxiom axiom){
		switch (axiom.getAxiomType()) {
            case AxiomType.SUBCLASS_OF:  
            	return axiom.getSubClass();
                break;
		throw new IllegalArgumentException("Different kind of axiom");
        }
	}

	public Set<OWLAxiom> getAllAxioms(){
		return ontology.axioms(AxiomType.SUBCLASS_OF, Imports.INCLUDED)
	}

	public Set<OWLAxiom> getDefeasibleAxioms(Set<OWLAxiom> inAxioms){
		Set<OWLAxiom> outAxioms = new HashSet<OWLAxiom>;
		for (OWLAxiom axiom : inAxioms){
      		for (OWLAnnotation annotation : axiom.annotations(defeasibleAnnotationProperty)){
      			OWLLiteral defeasibleLiteral = annotation.getValue();
      			if (defeasibleLiteral.parseBoolean()){
      				outAxioms.add(axiom);
      			}
      		}
      	}
	}

	public int getRank(OWLAxiom axiom){
		for (OWLAnnotation annotation : axiom.annotations(rankAnnotationValue)){
			OWLLiteral rankLiteral = annotation.getValue();
			return rankLiteral.parseInteger();
		}
	}

	public OWLObjectUnionOf getMaterialisation(OWLAxiom axiom){
		return df.getOWLObjectUnionOf(df.getOWLObjectComplementOf(axiom.getSubClass()), axiom.getSuperClass());
	}

	public Map<Integer, OWLClassExpression> getInternalisations(Set<OWLAxiom> inAxioms){
 		Map<Integer, Set<OWLAxiom>> rankAxioms = 
 			inAxioms.stream().collect(Collectors.groupingBy(OWLAxiom::getRank));


 		Map<Integer, Set<OWLAxiom>> orderedRankAxioms =
                rankAxioms.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e2, LinkedHashMap::new));

        Set<OWLAxiom> exceptionalAxioms = new HashSet<OWLAxiom>;

        Map<Integer, Set<OWLAxiom>> materialisationGrouping = new HashMap<Integer, Set<OWLAxiom>>();
        
        orderedRankAxioms.forEach((k, v) -> {
        	exceptionalAxioms.add(orderedRankAxioms.get(k));
        	materialisationGrouping.put(k, exceptionalAxioms);
        });

        Map<Integer, OWLClassExpression> output = new HashMap<Integer, Set<OWLClassExpression>>();

        materialisationGrouping.forEach((k, v) -> {
        	output.put(k, df.getOWLObjectIntersectionOf(v));
        });

        return output;
	}

	public OWLClassExpression getFirstQuery(OWLClassExpression internalisation, OWLAxiom query){
		return df.getOWLSubClassOfAxiom(internalisation, df.getOWLComplementOf(query.getSubClass()));
	}

	public OWLClassExpression getSecondQuery(OWLClassExpression internalisation, OWLAxiom query){
		return df.getOWLSubClassOfAxiom(df.getOWLObjectIntersectionOf(internalisation, query.getSubClass()), query.getSuperClass());
	}

	public OWLOntology getTBox(Set<OWLAxiom> axioms){
		OWLOntology TBox = manager.createOntology();
		for (OWLAxiom axiom : axioms){
			Set<OWLAnnotation> annotations = axiom.annotations(defeasibleAnnotationProperty);
			if (!annotations.contains(defeasibleAnnotation)){
				manager.addAxiom(TBox, axiom);
			}
		}
		return TBox;
	}

	public boolean performRationalClosure(OWLOntology TBox, Map<Integer, OWLClassExpression> internalisations, OWLAxiom query){
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(TBox);
		int ranks = internalisations.size();
 		int rank = 0;
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