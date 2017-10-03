package pref;

import javax.swing.*;  
import java.awt.event.*;
import java.awt.*;
import java.io.StringWriter;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;

public class DefeasibleMaker {
    private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    public DefeasibleMaker(OWLOntology ontology, Set<OWLSubClassOfAxiom> axioms) {
        Util u = new Util(ontology);
        JFrame frame =  new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        for (OWLSubClassOfAxiom axiom : axioms) {

            StringWriter sw = new StringWriter();
            ManchesterOWLSyntaxPrefixNameShortFormProvider ssfp = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
            ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(sw, ssfp);
            renderer.visit(axiom);

            JPanel rowItem = new JPanel();
            rowItem.setLayout(new BoxLayout(rowItem, BoxLayout.X_AXIS));

            JLabel axiomLabel = new JLabel(sw.toString());
            
            JRadioButton strict = new JRadioButton("Strict");
            JRadioButton defeasible = new JRadioButton("Defeasible");
            ButtonGroup group = new ButtonGroup();
            group.add(strict);
            group.add(defeasible);

            if (u.isDefeasible(axiom)){
                defeasible.setSelected(true);
            }
            else{
                strict.setSelected(true);
            }

            strict.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    u.makeStrict(axiom);
                }
            });

            defeasible.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    u.makeDefeasible(axiom);
                }
            });

            rowItem.add(axiomLabel);
            rowItem.add(strict);
            rowItem.add(defeasible);

            mainPanel.add(rowItem);
            mainPanel.revalidate();
        }
        JButton doneButton = new JButton("Done");

        mainPanel.add(doneButton);

        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}