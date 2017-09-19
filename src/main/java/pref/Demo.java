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

/**
 * This example demonstrates how HermiT can be used to check the consistency of the Pizza ontology
 */
public class Demo {
  private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
  private static OWLOntologyManager m1 = OWLManager.createOWLOntologyManager();
  private static OWLOntologyManager m2 = OWLManager.createOWLOntologyManager();
  private static OWLOntologyManager m3 = OWLManager.createOWLOntologyManager();
  private static OWLDataFactory df = manager.getOWLDataFactory();
  private static OWLDataFactory df3 = m3.getOWLDataFactory();
  private static OWLAnnotationProperty userRank = df.getOWLAnnotationProperty(IRI.create("#userRank"));

	public static void main(String[] args) throws Exception {
	// First, we create an OWLOntologyManager object. The manager will load and save ontologies.
    
    OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("/Users/Michael/ProtSupp/Student.owl"));
    
    // Now, we instantiate HermiT by creating an instance of the Reasoner class in the package org.semanticweb.HermiT.
    //Reasoner hermit=new Reasoner(ontology)

    OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);

    //OWLAxiom query = null;

    OWLAxiom[] ontArr = new OWLAxiom[3];

    //ManchesterOWLSyntaxOWLObjectRendererImpl rendering = new ManchesterOWLSyntaxOWLObjectRendererImpl();
    int rankInt = 0;
    //NEED TO APPEND GENERAL AXIOMS
    //get defeasible axioms
    for (OWLClass cls : ontology.getClassesInSignature()) {
      Set<OWLClassAxiom> curAxioms = ontology.getAxioms(cls);
      //System.out.println(cls.toStringID());
      for (OWLAxiom curAx : curAxioms){
        for (OWLAnnotation annotation : curAx.getAnnotations(df.getOWLAnnotationProperty(IRI.create("http://cair.za.net/defeasible")))){
          Set<OWLAnnotation> rankAnnos = new HashSet<OWLAnnotation>();
          rankAnnos.add(df.getOWLAnnotation(userRank, df.getOWLLiteral(rankInt)));
          OWLAxiom toAdd = curAx.getAnnotatedAxiom(rankAnnos);
          manager.addAxiom(ontology, toAdd);
          manager.removeAxiom(ontology, curAx);
          manager.saveOntology(ontology);
          //if (cls.toStringID().equals("http://www.semanticweb.org/michaejamesharrisonl/ontologies/2017/7/untitled-ontology-214#Student")){
            //query = curAx.getAxiomWithoutAnnotations();
          //}
          ontArr[rankInt] = toAdd;
          rankInt++;
        }
      }
    }

    Set<OWLClassAxiom> gcAxioms = ontology.getGeneralClassAxioms();
    for (OWLAxiom gcAx : gcAxioms){
      for (OWLAnnotation annotation : gcAx.getAnnotations(df.getOWLAnnotationProperty(IRI.create("http://cair.za.net/defeasible")))){
        Set<OWLAnnotation> rankAnnos = new HashSet<OWLAnnotation>();
        rankAnnos.add(df.getOWLAnnotation(userRank, df.getOWLLiteral(rankInt)));
        OWLAxiom toAdd = gcAx.getAnnotatedAxiom(rankAnnos);
        manager.addAxiom(ontology, toAdd);
        manager.removeAxiom(ontology, gcAx);
        manager.saveOntology(ontology);
        ontArr[rankInt] = gcAx;
        //query = gcAx.getAxiomWithoutAnnotations();
        rankInt++;
      }
    }

    OWLOntology o1 = m1.copyOntology(ontology, OntologyCopy.SHALLOW);
    
    OWLOntology o2 = m2.copyOntology(ontology, OntologyCopy.SHALLOW);

    OWLOntology o3 = m3.copyOntology(ontology, OntologyCopy.SHALLOW);

    //System.out.println(o1.getAxiomCount());

    m1.removeAxiom(o1, ontArr[0]);
    m2.removeAxiom(o2, ontArr[1]);
    m2.removeAxiom(o2, ontArr[0]);
    m3.removeAxiom(o3, ontArr[1]);
    m3.removeAxiom(o3, ontArr[0]);
    m3.removeAxiom(o3, ontArr[2]);

    // System.out.println(ontology.getLogicalAxioms(Imports.INCLUDED));

    //System.out.println(o1.getAxiomCount());


    OWLAxiom[] takeOut = new OWLAxiom[2];

    int jCount = 0;
    for (OWLAxiom inAx : o3.getLogicalAxioms()){
      // System.out.println(jCount + " : " + inAx);
      takeOut[jCount] = inAx;
      jCount++;
    }

    m3.removeAxiom(o3, takeOut[0]);


    OWLReasoner r1 = new Reasoner.ReasonerFactory().createReasoner(o1);
    OWLReasoner r2 = new Reasoner.ReasonerFactory().createReasoner(o2);
    OWLReasoner r3 = new Reasoner.ReasonerFactory().createReasoner(o3);

    // System.out.println(o3.getLogicalAxioms());

/*
   *System.out.println(ontArr[0]);

    if (reasoner.isEntailed(ontArr[1])){
      System.out.println("KNIFE");
    }

    if (r1.isEntailed(ontArr[1])){
      System.out.println("FORK");
    }
*/

    Set<OWLAxiom> query = new HashSet<OWLAxiom>();

    query.add(ontArr[1]);



    /*
    for(Map.Entry m : ontMap.entrySet()){  
      if (m.getKey().equals(0)){
        o1.removeAxiom(m.getValue());
        o2.removeAxiom(m.getValue());
      }
      if (m.getKey() = 1){
        o2.removeAxiom(m.getValue());
      }   
    }
    */

    //Iterator iter = query.getClassesInSignature().iterator();

    //iter.next();

    //OWLAxiom q1 = iter.next();

    //System.out.println(query.getObjectPropertiesInSignature());

    OWLClass q1 = null;

    OWLClassExpression conceptC = null;

    OWLClassExpression conceptD = null;

    for (OWLClassExpression cEx : ontArr[1].getNestedClassExpressions()){
      if (cEx.getClassExpressionType().toString().equals("Class")){
        conceptC = cEx;
      }
      if (cEx.getClassExpressionType().toString().equals("ObjectSomeValuesFrom")){
        conceptD = cEx;
      }
      //System.out.println("NEW: " + cEx);
    }

    OWLAxiom subQuery = df.getOWLSubClassOfAxiom(conceptC, df.getOWLObjectComplementOf(conceptD));

    query.add(subQuery);


     for (OWLAxiom lAx : o1.getLogicalAxioms()){

    }   


    // OWLSubClassOfAxiom entCheck = df.getOWLSubClassOfAxiom(df.getOWLObjectIntersectionOf(ontArr[1], ontArr[2]), df.getOWLObjectComplementOf(conceptC));

    // if (r3.isEntailed(entCheck)){
    //   System.out.println("ENTCHECK");
    // }

    //System.out.println("QUERY:" + ontArr[1]);

    //System.out.println("NEG - QUERY: " + subQuery);

    // System.out.println("UNSATISFIABLE:  " + reasoner.getUnsatisfiableClasses());

    // System.out.println("EQUIVALENT CLASSES:  " + r1.getEquivalentClasses(conceptC));

    // for (OWLObjectPropertyExpression opEx : ontArr[1].getObjectPropertiesInSignature()){
    //   System.out.println("OBJECT PROPERTY EXPRESSION:  " + opEx);
    //   //reasoner.getObjectPropertyValues(conceptC, opEx);
    // }

    // if (reasoner.isEntailed(subQuery)){
    //   System.out.println("ENTAILED");
    // }
    /*
    ?*
    for (OWLClass qAx : query.getClassesInSignature()){
      System.out.println(q1);
      q1 = qAx;
    }
    */



    OWLClassExpression intersection = conceptC;

    OWLClassExpression inverse = df.getOWLObjectComplementOf(conceptD);

    //System.out.println(query);

    //System.out.println(conceptC);

    // if (reasoner.isSatisfiable(conceptC)){
    //   System.out.println("YAS");
    // }
    // else{
    //   System.out.println("NAH");
    // }

    for (OWLClassExpression allAx : ontology.getNestedClassExpressions()){
      intersection = df.getOWLObjectIntersectionOf(intersection, allAx);
    }

    int countAx = 1;

    OWLSubClassOfAxiom[] dIntersect = new OWLSubClassOfAxiom[3];

    int iCount = 0;

    for (OWLSubClassOfAxiom lAx : o1.getAxioms(AxiomType.SUBCLASS_OF)){
      dIntersect[iCount] = lAx;
      iCount++;
      // System.out.println(lAx.getSubClass() + " SUBCLASS OF " + lAx.getSuperClass());
    }

    OWLSubClassOfAxiom mainQuery = df3.getOWLSubClassOfAxiom(df3.getOWLObjectIntersectionOf(df3.getOWLObjectUnionOf(df3.getOWLObjectComplementOf(dIntersect[0].getSubClass()), dIntersect[0].getSuperClass()), df3.getOWLObjectUnionOf(df3.getOWLObjectComplementOf(dIntersect[2].getSubClass()), dIntersect[2].getSuperClass())), df3.getOWLObjectComplementOf(dIntersect[2].getSubClass()));


    if (r3.isEntailed(mainQuery)){
      System.out.println("IN IT");
    }

    System.out.println(mainQuery);

    // 	for (OWLAnnotation annotation : lAx.getAnnotations(df.getOWLAnnotationProperty(IRI.create("http://cair.za.net/defeasible")))){
    // 		for (OWLClassExpression cEx : lAx.getNestedClassExpressions()){
    // 			System.out.println("RANK 2:	" + countAx + " : " + cEx + " : " + cEx.getClassExpressionType() + " : " + r1.getSubClasses(cEx, true));
    // 		}
    // 		countAx++;
    // 	}
    // }
    
    //System.out.println(conceptD);

    OWLClassExpression intersect = df.getOWLObjectIntersectionOf(conceptC, df.getOWLObjectComplementOf(conceptD));

    // if (reasoner.isSatisfiable(intersect)){
    //   System.out.println("YES");
    // }
    // else{
    //   System.out.println("NOH");
    // }

    // if (r1.isEntailed(subQuery)){
    //   System.out.println("FORK");
    // }
    
    OWLAxiom norm = df.getOWLSubClassOfAxiom(conceptC, conceptD);
    OWLAxiom unnorm = df.getOWLSubClassOfAxiom(conceptC, inverse);

    // if (r1.isEntailed(norm)) {
    //   System.out.println("NORM");
    // }

    // if (r1.isEntailed(unnorm)) {
    //   System.out.println("UNNORM");
    // }

    OWLClassExpression wholeQuery = df.getOWLObjectIntersectionOf(conceptC, inverse);

    // if (r3.isSatisfiable(intersect)) {
    //   System.out.println("QuErY");
    // }

    // InferredSubClassAxiomGenerator gen = new InferredSubClassAxiomGenerator();
    // Set<OWLSubClassOfAxiom> subClass = gen.createAxioms(df, r1);
    // for (OWLSubClassOfAxiom subAx : subClass) {
    //   System.out.println("INFERRED: " + subAx);
    // }

    if (reasoner.isSatisfiable(conceptC)){
      if (!(reasoner.isEntailed(subQuery) && reasoner.isEntailed(ontArr[1]))) {
        System.out.println("Not in Rational Closure: 0, 0");
      }
      else if (!(r1.isEntailed(subQuery) && r1.isEntailed(ontArr[1]))) {
        System.out.println("In Rational Closure: 0, 1");
      }
      else if (!(r2.isEntailed(subQuery) && r2.isEntailed(ontArr[1]))) {
        System.out.println("In Rational Closure: 0, 2");
      }
      else if (!(r3.isEntailed(subQuery) && r3.isEntailed(ontArr[1]))) {
        System.out.println("In Rational Closure: 0, inf");
      }
    }
  
    else if (r1.isSatisfiable(conceptC)){
      if (!(r1.isEntailed(subQuery) && r1.isEntailed(ontArr[1]))) {
        System.out.println("Not in Rational Closure: 1, 1");
      }
      else if (!(r2.isEntailed(subQuery) && r2.isEntailed(ontArr[1]))) {
        System.out.println("In Rational Closure: 1, 2");
      }
      else if (!(r3.isEntailed(subQuery) && r3.isEntailed(ontArr[1]))) {
        System.out.println("In Rational Closure: 1, inf");
      }
    }

    else if (r2.isSatisfiable(conceptC)){
      if (!(r2.isEntailed(subQuery) && r2.isEntailed(ontArr[1]))) {
        System.out.println("Not in Rational Closure: 2, 2");
      }
      else if (!(r3.isEntailed(subQuery) && r3.isEntailed(ontArr[1]))) {
        System.out.println("In Rational Closure: 2, inf");
      }
    }
    else if (r3.isSatisfiable(conceptC)){
      System.out.println("In Rational Closure: inf");
    }
    else {
      System.out.println("Blah");
    }

  

  //INPUT RANKINGS FOR ALL DEFEASIBLE AXIOMS

  //PERFORM RATIONAL CLOSURE

  //CHECK FROM RANK 0 IF C IS ENTAILED
  //CHECK IF RANK OF C AND NOT D IS ENTAILED IN GREATER RANK

  //IF RANK OF C IS INF OR LESS THANK RANK OF (C AND NOT D) THEN (C USUSALLY SUBSUMED BY D) IN RATIONAL CLOSURE




    /**if (annotation.getValue() instanceof OWLLiteral) {
      OWLLiteral val = (OWLLiteral) annotation.getValue();
      // look for portuguese labels
      if (val.hasLang("pt"))
        System.out.println(cls +" labelled " + val.getLiteral());
    }*/

    //Start from lowest ranking
    //Check if LSH or RHS of query is entailed in lowest ranking
    //Take the ranking that entails LSH and 
    //for(int i = 0; i <= ranks; i++){
      //ont0 is ontology
      //ont1 is ontology with statements of rank 0 removed
      //ontInf is ontology with all defeasible statements removed
    //}
	}

 
}