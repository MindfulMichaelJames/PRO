package pref;

import javax.swing.*;  
import java.awt.event.*;
import java.awt.*;
import java.io.StringWriter;
import java.util.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;

public class DefeasibleRanker {
    private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    Util u;
    JFrame frame;
    JPanel mainPanel;
    // Map<Integer, JPanel> rankLevels;
    TreeMap<Integer, Set<OWLSubClassOfAxiom>> rankAxioms;
    ActionListener incAction;
    ActionListener decAction;
    Set<OWLSubClassOfAxiom> axioms;
    OWLOntology ontology;
    JButton bCancel;

    public DefeasibleRanker(OWLOntology inOntology, Set<OWLSubClassOfAxiom> inAxioms) {
    	ontology = inOntology;
    	u = new Util(ontology);
        frame =  new JFrame();
        // rankLevels = new HashMap<>();
        rankAxioms = new TreeMap<>();
        axioms = inAxioms;
        // incAction = new ActionListener() {
        // 	public void actionPerformed(ActionEvent e) {

        // 	}
        // }

     //        public void actionPerformed(ActionEvent e) {
	    // decAction = new ActionListener() {
     //        public void actionPerformed(ActionEvent e) {
     //            int level = u.getRank(axiom);
     //            int downLevel = level - 1;
     //            if (downLevel == 0){
     //            	decEx.setEnabled(false);
     //            }
     //            u.assignRank(axiom, downLevel);
     //            rankLevels.get(key).remove(rowItem);
     //            rankLevels.get(downLevel).add(rowItem);
     //            mainPanel.revalidate();
     //        }
     //    };

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
				        OWLSubClassOfAxiom newAx = u.assignRank(axiom, upLevel);
						rankAxioms.get(level).remove(axiom);

						System.out.println(u.getRank(newAx));

						manager.removeAxiom(ontology, axiom);
						manager.addAxiom(ontology, newAx);

						Set<OWLSubClassOfAxiom> newRank = rankAxioms.get(upLevel);
			 			if (newRank == null){
			 				newRank = new HashSet<OWLSubClassOfAxiom>();
			 				rankAxioms.put(upLevel, newRank);
			 			}
			 			newRank.add(newAx);


						// rankAxioms.get(upLevel).add(axiom);
						mainPanel.removeAll();
						frame.remove(mainPanel);
				        populateGUI();
				    }
				});

				decEx.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
				        int level = u.getRank(axiom);
				        int downLevel = level - 1;
				        OWLSubClassOfAxiom newAx = u.assignRank(axiom, downLevel);
				        manager.removeAxiom(ontology, axiom);
						manager.addAxiom(ontology, newAx);
						rankAxioms.get(level).remove(axiom);
						rankAxioms.get(downLevel).add(axiom);
						mainPanel.removeAll();
						frame.remove(mainPanel);
				        populateGUI();
				    }
				});


	      //       incEx.addActionListener(new ActionListener() {
			    // 	public void actionPerformed(ActionEvent e) {
				   //      int level = u.getRank(axiom);
				   //      int upLevel = level + 1;
				   //      u.assignRank(axiom, upLevel);
				   //      rankLevels.get(level).remove(rowItem);
				        
				   //      if (level == 0){
				   //      	decEx.setEnabled(true);
				   //      }

				   //      if (rankLevels.get(upLevel) == null){
				   //      	JPanel rowSection = new JPanel();
				   //      	rowSection.setLayout(new BoxLayout(rowSection, BoxLayout.Y_AXIS));
				   //      	rowSection.setBorder(BorderFactory.createLineBorder(Color.black));

				   //      	JLabel heading = new JLabel("<html><h1>Level " + upLevel + "</h1></html>");
							// rowSection.add(heading);

							// rankLevels.put(upLevel, rowSection);
							// mainPanel.add(rowSection);
				   //      }

				   //      rankLevels.get(upLevel).add(rowItem);
				   //      mainPanel.revalidate();
				   //  }   
			    // });
        	}
        	// rankLevels.put(key, rowSection);
        	mainPanel.add(rowSection);
		}

		frame.add(mainPanel);
		frame.pack();
		frame.revalidate();
		frame.setVisible(true);

	}

	

    // public void listenerRefresh(AbstractButton button){
    	// for (ActionListener listener : button.getActionListeners(){
    	// 	button.removeActionListener(listener);
    	// }
    	// button.addActionListener(new ActionListener() {
	    // 	public void actionPerformed(ActionEvent e) {
		   //      int level = u.getRank(axiom);
		   //      int upLevel = level + 1;
		   //      u.assignRank(axiom, upLevel);
		   //      rankLevels.get(level).remove(rowItem);
		        
		   //      if (level == 0){
		   //      	decEx.setEnabled(true);
		   //      }

		   //      if (rankLevels.get(upLevel) == null){
		   //      	JPanel rowSection = new JPanel();
		   //      	rowSection.setLayout(new BoxLayout(rowSection, BoxLayout.Y_AXIS));
		   //      	rowSection.setBorder(BorderFactory.createLineBorder(Color.black));

		   //      	JLabel heading = new JLabel("<html><h1>Level " + upLevel + "</h1></html>");
					// rowSection.add(heading);

					// rankLevels.put(upLevel, rowSection);
					// mainPanel.add(rowSection);
		   //      }

		   //      rankLevels.get(upLevel).add(rowItem);
		   //      mainPanel.revalidate();

		   //      ((AbstractButton) e.getSource()).removeActionListener(this);
		   //      ((AbstractButton) e.getSource()).addActionListener(this);
		   //  }   
	    // });

    // }

}