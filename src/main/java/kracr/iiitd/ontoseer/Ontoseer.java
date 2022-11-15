package kracr.iiitd.ontoseer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;


public class Ontoseer {
	public static List<String> classlist;
	public static List<String> objectPropertyList;
	public static List<String> dataPropertyList;
	public  String path;
	public HashMap<String, String> reClass;
	public HashMap<String, String> reProperty;

	
	public void commands() {
		 System.out.println("-h  -- Help");
		 System.out.println("-p <Ontology file path> -cr|-pr|-or|-ar|-chr|-vr <arguments for respective recommendation>");
		 System.out.println("\t-cr -ClassRecommendation <class1 class2 class3 .... >");
		 System.out.println("\t-pr -PropertyRecommendation <Property1 Property2 ......>");
		 System.out.println("\t-or -ODPRecommendation <arg1 arg2 arg3>");
		 System.out.println("\t\targ1 -- Description of the ontology");
		 System.out.println("\t\targ2 -- Domain of ontology");
		 System.out.println("\t\targ3 -- Competency questions");
		 System.out.println("\t-ar -AxiomRecommendation <class|property>");
		 System.out.println("\t\tClass or Property for which you want the axiom recommendation");
		 System.out.println("\t-chr -ClassHierarchyValidation <arg1 arg2 arg3 arg4>(all arguments are mandatory)");
		 System.out.println("\t\targ1 -- Do the properties of superclass cease to exit in the future(temporal dependency)? (Y/N)");
		 System.out.println("\t\targ2 -- Do the properties of the sub class cease to exist in the future(temporal dependency)? (Y/N)");
		 System.out.println("\t\targ3 -- Are the properties of super-class and sub-class identical? (Y/N)");
		 System.out.println("\t\targ4 -- Are the properties of sub-class part of the properties whole class? (Y/N)");
		 System.out.println("\t-vr -VocabularyRecommendation <arg1>");
		 System.out.println("\t\targ1 -- Class or Property");
	}
	
	// To check next argument is a command or not
	public boolean isPresent(String s, String[] commands_) {
		for(String cmd : commands_) {
			if(cmd.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public void odpRecommendation(String[] args, int i) {
		System.out.println("\n\n\n************** ODP Recommendation *************");
		
		 // List to contains all the entities 
		 List<String>temp = new ArrayList<String>();
		 for(Map.Entry<String, String> entry : reClass.entrySet()) {
			 temp.add(entry.getValue());
		 }
		 for(Map.Entry<String, String> entry : reProperty.entrySet()) {
			 temp.add(entry.getValue());
		 }
		 temp.addAll(dataPropertyList);
		 String[] str = temp.toArray(new String[0]);
		 ODPRecommendation odprcmd = new ODPRecommendation(str);
		 odprcmd.ODP(str, args[i], args[i+1], args[i+2]);
		 System.out.println("-----------------------------------------------------------------------------------------------");
	}
	
	public int axiomRecommendation(String[] args, int argLength, String[] commands_, int i, Ontoseer ontoseer) {
		System.out.println("\n\n\n************** Axiom Recommendation *************");
		 AxiomRecommendation axioms = new AxiomRecommendation();
		 if(i<argLength && !ontoseer.isPresent(args[i], commands_)) {
			 axioms.recommendAxioms(args[i]);
			 i++;
		 }
		 System.out.println("-----------------------------------------------------------------------------------------------");
		 return i;
	}
	
	public int classNameConvention(String []args, int argLength, String[] commands_, int i, Ontoseer ontoseer) {
		System.out.println("\n\n\n************** Class Recommendation *************");
			NameConventionPanel namingPanel = new NameConventionPanel();
			reClass = namingPanel.vocab(classlist);
			for(;i<argLength && !ontoseer.isPresent(args[i],commands_);i++) {
				if(reClass.containsKey(args[i])) {
					System.out.println("Recommended class for "+args[i]+" is "+reClass.get(args[i]));
				}
				else {
					System.out.println(args[i]+ " class is not present in ontology");
				}
			}
			System.out.println("-----------------------------------------------------------------------------------------------");
			return i;
	}
	
	public int propertyNameConvention(String[] args, int argLength, String[] commands_, int i, Ontoseer ontoseer) {
		System.out.println("\n\n\n************** Property Recommendation *************");
		NameConventionPanel proPanel = new NameConventionPanel();
		reProperty = proPanel.vocab1(objectPropertyList);
		for(;i<argLength && !ontoseer.isPresent(args[i],commands_);i++) {
			if(reProperty.containsKey(args[i])) {
				System.out.println("Recommended property for "+args[i]+" is "+reProperty.get(args[i]));
			}
			else {
				System.out.println(args[i]+ " property is not present in ontology");
			}
		}
		System.out.println("-----------------------------------------------------------------------------------------------");
		return i;
	}
	
	public void classHierarchyValidation(String[] args, int i) {
		ClassHierarchyValidation classHierarchyValidation = new ClassHierarchyValidation();
		System.out.println("\n\n\n************** Class Hierarchy Validation *************");
		classHierarchyValidation.classHierarchy(args[i], args[i+1], args[i+2], args[i+3]);
		System.out.println("-----------------------------------------------------------------------------------------------");
	}
	
	public int vocabularyRecommendation(String[] args, int argLength, String[] commands_, int i, Ontoseer ontoseer) {
		 System.out.println("\n\n\n************** Vocabulary Recommendation *************");
		 VocabularyRecommendation vocabRecommendation = new VocabularyRecommendation();
		 if(i<argLength && !ontoseer.isPresent(args[i], commands_)) {
			 vocabRecommendation.findsimilarity(args[i]);
			 vocabRecommendation.URI(args[i]);
			 i++;
		 }
		 return i;
	}
	
	public static void main(String[] args) {
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
		 OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
		 Ontoseer ontoseer = new Ontoseer();
		 
		 if(args[0].equalsIgnoreCase("-p") && args.length>=2) {
			 ontoseer.path = args[1];
		 }
		 
		 else if(args[0].equalsIgnoreCase("-h") && args.length==1){
			 ontoseer.commands();
			 System.exit(0);
		 }
		 else if(args.length==2) {
			 ontoseer.commands();
			 return;
		 }
		 else {
			 System.out.println("Not a valid command....");
			 System.exit(0);
		 }
		 try {
		    // loading the axioms
			final OWLOntology owl=manager.loadOntologyFromOntologyDocument(new File(ontoseer.path));
			 //System.out.println(owl.getAxiomCount());
			 Set<OWLEntity> ont = owl.getSignature();
			 Set<OWLClass>classes = owl.getClassesInSignature();
			 Set<OWLObjectProperty> prop;
	         Set<OWLDataProperty> dataProp;
	         Set<OWLNamedIndividual> individuals;
			 
	         prop = owl.getObjectPropertiesInSignature();
	         dataProp = owl.getDataPropertiesInSignature();
	         individuals = owl.getIndividualsInSignature();
	         
			 
	         classlist = new ArrayList<String>();
	         objectPropertyList = new ArrayList<String>();
	         dataPropertyList = new ArrayList<String>();
	         
	       //System.out.println("\n**********#### Classes ######*************\n");
			 for(OWLClass cls : classes) {
				 //System.out.println("+: " + cls.getIRI().getShortForm());
				 classlist.add(cls.getIRI().getShortForm());
				 //System.out.println("Class "+cls);
				 
				 //System.out.println(" \tObject Property Domain");
	                for (OWLObjectPropertyDomainAxiom op : owl.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {                        
	                        if (op.getDomain().equals(cls)) {   
	                            for(OWLObjectProperty oop : op.getObjectPropertiesInSignature()){
	                                 //System.out.println("\t\t +: " + oop.getIRI().getShortForm());
	                                 objectPropertyList.add(oop.getIRI().getShortForm());
	                            }
	                            //System.out.println("\t\t +: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
	                        }
	                    }

	                    //System.out.println(" \tData Property Domain");
	                    for (OWLDataPropertyDomainAxiom dp : owl.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
	                        if (dp.getDomain().equals(cls)) {   
	                            for(OWLDataProperty odp : dp.getDataPropertiesInSignature()){
	                                 //System.out.println("\t\t +: " + odp.getIRI().getShortForm());
	                                 dataPropertyList.add(odp.getIRI().getShortForm());
	                            }
	                            //System.out.println("\t\t +:" + dp.getProperty());
	                        }
	                    }
			 }

			 ontoseer.reClass = new HashMap<>();
			 ontoseer.reProperty = new HashMap<>();
			 
			 int argLength = args.length;
			 String[] commands_ = {"-cr","-pr","-or","-ar","-vr","-chr","-ClassRecommendation","-PropertyRecommendation","-ODPRecommendation","-AxiomRecommendation","-VocabularyRecommendation","-ClassHierarchyValidation"};
			 if(args.length>2) {
				 int i = 2;
				 while(i<argLength) {
					 String cmd = args[i];
					 i++;
					 // Recommend Class Name
					 if(cmd.equals("-cr") || cmd.equals("-ClassRecommendation")) {
						 if(i<argLength && ontoseer.isPresent(args[i], commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 i = ontoseer.classNameConvention(args, argLength, commands_, i, ontoseer);
			 			}
						 				
			 		 // Property Name Recommendation
					 else if(cmd.equals("-pr") || cmd.equals("-PropertyRecommendation")) {
							 if(i<argLength && ontoseer.isPresent(args[i], commands_)) {
								 ontoseer.commands();
								 break;
							 }
						 i = ontoseer.propertyNameConvention(args, argLength, commands_, i, ontoseer);
			 			}
						 				
					 // ODP Recommendation
					 else if(cmd.equals("-or") || cmd.equals("-ODPRecommendation")) {
						 
						 if(i>=argLength || (i+1)>=argLength || (i+2)>=argLength) {
							 ontoseer.commands();
							 break;
						 }
						 
						 else if(ontoseer.isPresent(args[i],commands_) || ontoseer.isPresent(args[i+1],commands_) || ontoseer.isPresent(args[i+2],commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
						 ontoseer.odpRecommendation(args, i);
						 i = i+3;
						 }
						}
			 
			 
			 //parsing the axioms === 
//			 System.out.println("\n=========================== Axioms =====================\n");
//			 owl.getClassesInSignature().forEach(i->{
//			        final PrintParse p=new PrintParse(i);
//			        owl.getReferencingAxioms(i).forEach(axiom->axiom.accept(p));
//			    });
//			 for (OWLEntity entity : ont) {
//			    System.out.println(entity);//this print only the entities
//			}
				 
				 
							 
					// Axiom Recommendation
					 else if(cmd.equals("-ar") || cmd.equals("-AxiomRecommendation")){
						 if(i<argLength && ontoseer.isPresent(args[i], commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 i = ontoseer.axiomRecommendation(args, argLength, commands_, i, ontoseer);
						 }
						}
					
					// Vocabulary Recommendation
					 else if(cmd.equals("-vr") || cmd.equals("-VocabularyRecommendation")) {
						 if(i<argLength && ontoseer.isPresent(args[i], commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 i = ontoseer.vocabularyRecommendation(args, argLength, commands_, i, ontoseer);
						 }
					}
						
					// Class Hierarchy validation
					 else if(cmd.equals("-chr") || cmd.equals("-ClassHierarchyValidation")) {
						 if(i>=argLength || (i+1)>=argLength || (i+2)>=argLength || (i+3)>=argLength) {
							 ontoseer.commands();
							 break;
						 }
						
						 else if(ontoseer.isPresent(args[i],commands_) || ontoseer.isPresent(args[i+1],commands_) || ontoseer.isPresent(args[i+2],commands_) || ontoseer.isPresent(args[i+3],commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 ontoseer.classHierarchyValidation(args, i);
							 i = i+4;
						 }
					}
							 
					else {
						 System.out.println("Invalid command....");
						 ontoseer.commands();
						 System.exit(0);
						}
				}
			 }
		else {
				ontoseer.commands();
				System.exit(0);
			}
	}catch (OWLOntologyCreationException e) {
		e.printStackTrace();
	}
	System.out.println();
}
}
