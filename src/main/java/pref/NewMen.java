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
import java.util.stream.Stream;
import java.lang.String;
import java.lang.Boolean;
import java.util.Set;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
//import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class NewMen {
  private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
  private static OWLDataFactory df = manager.getOWLDataFactory();
  private static OWLAnnotationProperty userRank = df.getOWLAnnotationProperty(IRI.create("#userRank"));

	public static void main(String[] args) throws Exception {

		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("/Users/Michael/ProtSupp/Men1.owl"));

		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);

		OWLSubClassOfAxiom[] subAx = new OWLSubClassOfAxiom[4];

		int axCounter = 0;

		for (OWLSubClassOfAxiom lAx : ontology.getAxioms(AxiomType.SUBCLASS_OF)){
			subAx[axCounter] = lAx;
			axCounter++;
		}

		OWLOntology o1 = manager.createOntology(IRI
                .create("http://www.semanticweb.org/owlapi/ontologies/ontology"));

		manager.addAxiom(o1, subAx[0]);
		manager.addAxiom(o1, subAx[1]);

		OWLObjectComplementOf notC = df.getOWLObjectComplementOf(subAx[0].getSubClass());
		OWLObjectComplementOf not_CandnotD = df.getOWLObjectComplementOf(df.getOWLObjectIntersectionOf(subAx[2].getSubClass(), df.getOWLObjectComplementOf(subAx[2].getSuperClass())));

		OWLObjectUnionOf d0 = df.getOWLObjectUnionOf(df.getOWLObjectComplementOf(subAx[2].getSubClass()), subAx[2].getSuperClass());
		OWLObjectUnionOf d1 = df.getOWLObjectUnionOf(df.getOWLObjectComplementOf(subAx[3].getSubClass()), subAx[3].getSuperClass());

		OWLClassExpression rank0 = df.getOWLObjectIntersectionOf(d1, d0);

		OWLClassExpression lhs = df.getOWLObjectIntersectionOf(d1, subAx[0].getSubClass());
		OWLClassExpression rhs = subAx[2].getSuperClass();

		OWLSubClassOfAxiom firstQuery = df.getOWLSubClassOfAxiom(d1, notC);

		OWLSubClassOfAxiom secondQuery = df.getOWLSubClassOfAxiom(lhs, rhs);

		OWLReasoner r1 = new Reasoner.ReasonerFactory().createReasoner(o1);

		System.out.println(notC);


		if (r1.isEntailed(firstQuery)){
			System.out.println("First Entailed");
		}
		else{
			System.out.println("First not entailed");
		}

		if (r1.isEntailed(secondQuery)){
			System.out.println("Second Entailed");
		}
		else{
			System.out.println("Second not entailed");
		}

	}
}

