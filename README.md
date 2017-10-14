# PRO (Preferential Reasoner for Ontologies)

A preferential reasoner and ontology editor for defeasible OWL ontologies.

#### Author

Michael Harrison
*for completion of a Computer Science Honours course at the University of Cape Town.*


#### Building the tab plugin from source

Requirements:

+ [Gradle](https://gradle.org).
+ A tool for checking out a [Git](http://git-scm.com/) repository.

Steps:

1. Get a copy of the code: 
	git clone https://github.com/MindfulMichaelJames/PRO
    
2. Change into the PRO directory.

3. Type ./gradlew build.  On build completion, the "distributions" directory will contain .zip and .tar compressed files. Unzipping any of these compressed files will give you a directory that contains bin and lib directories. There will be a PRO excecutable in the bin directory that can be used to run the tool.


#### Running the tool

Requirements:

+ [Gradle](https://gradle.org).
+ A tool for checking out a [Git](http://git-scm.com/) repository.

Steps:

1. Get a copy of the code: 
	git clone https://github.com/MindfulMichaelJames/PRO
    
2. Change into the PRO directory.

3. Type ./gradlew run.  On build completion, the tool will start.
 
#### Using the tool

1. Put the OWL file to be edited or queried in the PRO directory.

2. Run the tool as described above.

3. When the tool starts, enter the name of the OWL file (including the .OWL extension).

- To add an axiom to the ontology, any classes or object properties used in the axiom must be added to the ontology first.

- Only defeasible axioms will appear in the ranking. In order to rank an axiom, it must first be specified as defeasible.

- When entering text, Manchester Syntax must be used. 


#### License

[GNU General Public License](https://www.gnu.org/licenses/gpl-3.0.en.html)