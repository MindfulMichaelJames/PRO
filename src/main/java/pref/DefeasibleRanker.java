package pref;

import javax.swing.*;  
import java.awt.event.*;
import java.awt.*;
import java.io.StringWriter;
import java.util.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;

/* 
* This class allows users specify the positions of defeasible axioms in the ranking
*/
public class DefeasibleRanker {
    private OWLOntologyManager manager;
    Util u;
    JFrame frame;
    JPanel mainPanel;
    TreeMap<Integer, Set<OWLSubClassOfAxiom>> rankAxioms;
    ActionListener incAction;
    ActionListener decAction;
    Set<OWLSubClassOfAxiom> axioms;
    OWLOntology ontology;
    JButton bCancel;

    public DefeasibleRanker(OWLOntologyManager m, OWLOntologyID ontID, Util ontUtil) {
    	manager = m;
    	ontology = m.getOntology(ontID);
    	u = ontUtil;
        frame =  new JFrame();
        rankAxioms = new TreeMap<>();
        axioms = u.getDefeasibleAxioms();
        for (OWLSubClassOfAxiom axiom : axioms){
 			int rank = u.getRank(axiom);
 			Set<OWLSubClassOfAxiom> newRank = rankAxioms.get(rank);
 			if (newRank == null){
 				newRank = new HashSet<OWLSubClassOfAxiom>();
 				rankAxioms.put(rank, newRank);
 			}
 			newRank.add(axiom);
 		}
 		initialiseGUI();
 		populateGUI();
	}

	private void initialiseGUI() {

		mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    }

    private void populateGUI(){
 		for(Map.Entry<Integer, Set<OWLSubClassOfAxiom>> ranking : rankAxioms.entrySet()) {
			Integer key = ranking.getKey();
			Set<OWLSubClassOfAxiom> rankingAxioms = ranking.getValue();
			JPanel rowSection = new JPanel();
        	rowSection.setLayout(new BoxLayout(rowSection, BoxLayout.Y_AXIS));
        	rowSection.setBorder(BorderFactory.createLineBorder(Color.black));
        	JLabel heading = new JLabel("<html><h1>Level " + key + "</h1></html>");
        	heading.setHorizontalAlignment(JLabel.LEFT);
			rowSection.add(heading);
        	for (OWLSubClassOfAxiom axiom : rankingAxioms){
        		StringWriter sw = new StringWriter();
	            ManchesterOWLSyntaxPrefixNameShortFormProvider ssfp = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
	            ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(sw, ssfp);
	            renderer.visit(axiom);
	            JLabel axiomLabel = new JLabel(sw.toString());
	            JButton incEx = new JButton("Increase exceptionality");
	            JButton decEx = new JButton("Decrease exceptionality");
	            if (key == 0){
	            	decEx.setEnabled(false);
	            }
        		JPanel rowItem = new JPanel();
            	rowItem.setLayout(new BoxLayout(rowItem, BoxLayout.X_AXIS));
            	rowItem.add(axiomLabel);
            	rowItem.add(incEx);
            	rowItem.add(decEx);
            	rowSection.add(rowItem);
            	bCancel = new JButton("Cancel");
            	
            	bCancel.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
			    		frame.dispose();
			    	}
			    });
            	
            	incEx.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
				        int level = u.getRank(axiom);
				        int upLevel = level + 1;
				        u.assignRank(axiom, upLevel);
						rankAxioms.get(level).remove(axiom);
						try 
				        {
				            manager.saveOntology(ontology);
				        } 
				        catch(OWLOntologyStorageException error)
				        {
				             System.out.println(error);
				        }
						Set<OWLSubClassOfAxiom> newRank = rankAxioms.get(upLevel);
			 			if (newRank == null){
			 				newRank = new HashSet<OWLSubClassOfAxiom>();
			 				rankAxioms.put(upLevel, newRank);
			 			}
			 			newRank.add(axiom);
						mainPanel.removeAll();
						frame.remove(mainPanel);
				        populateGUI();
				    }
				});

				decEx.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
				        int level = u.getRank(axiom);
				        int downLevel = level - 1;

				        u.assignRank(axiom, downLevel);

						rankAxioms.get(level).remove(axiom);
						rankAxioms.get(downLevel).add(axiom);
						mainPanel.removeAll();
						frame.remove(mainPanel);
				        populateGUI();
				    }
				});
        	}
        	mainPanel.add(rowSection);
		}
		frame.add(mainPanel);
		frame.pack();
		frame.revalidate();
		frame.setVisible(true);
	}
}