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
	public int argInd; 
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
	
	public void odpRecommendation(String[] args) {  //int argInd
		
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
		 odprcmd.ODP(str, args[argInd], args[argInd+1], args[argInd+2]);
		 System.out.println("-----------------------------------------------------------------------------------------------");
	}
	
	public int axiomRecommendation(String[] args, int argLength, String[] commands_, Ontoseer ontoseer) {
		System.out.println("\n\n\n************** Axiom Recommendation *************");
		 AxiomRecommendation axioms = new AxiomRecommendation();
		 if(argInd<argLength && !ontoseer.isPresent(args[argInd], commands_)) {
			 HashMap<String, String>reAxiom =  axioms.axiomRecommendation(args[argInd]);
			 argInd++;
		 }
		 System.out.println("-----------------------------------------------------------------------------------------------");
		 return argInd;
	}
	
	public int classNameConvention(String []args, int argLength, String[] commands_, Ontoseer ontoseer) {
//		System.out.println("argIndex = "+argInd);
		System.out.println("\n\n\n************** Class Recommendation *************");
//			NameConventionPanel namingPanel = new NameConventionPanel();
		ClassRecommendation className = new ClassRecommendation();
		reClass = className.classRecommendation(classlist);
		for(;argInd<argLength && !ontoseer.isPresent(args[argInd],commands_);argInd++) {
			if(reClass.containsKey(args[argInd])) {
				System.out.println("Recommended class for "+args[argInd]+" is "+reClass.get(args[argInd]));
			}
			else {
				System.out.println(args[argInd]+ " class is not present in ontology");
			}
		}
		System.out.println("-----------------------------------------------------------------------------------------------");
		return argInd;
	}
	
	public int propertyNameConvention(String[] args, int argLength, String[] commands_,  Ontoseer ontoseer) { //int argInd,
		System.out.println("\n\n\n************** Property Recommendation *************");
		PropertyRecommendation propertyName = new PropertyRecommendation();
		reProperty = propertyName.propertiesRecommendation(objectPropertyList);
		for(;argInd<argLength && !ontoseer.isPresent(args[argInd],commands_);argInd++) {
			if(reProperty.containsKey(args[argInd])) {
				System.out.println("Recommended property for "+args[argInd]+" is "+reProperty.get(args[argInd]));
			}
			else {
				System.out.println(args[argInd]+ " property is not present in ontology");
			}
		}
		System.out.println("-----------------------------------------------------------------------------------------------");
		return argInd;
	}
	
	public void classHierarchyValidation(String[] args) { //int argInd,
		ClassHierarchyValidation classHierarchyValidation = new ClassHierarchyValidation();
		System.out.println("\n\n\n************** Class Hierarchy Validation *************");
		classHierarchyValidation.classHierarchy(args[argInd], args[argInd+1], args[argInd+2], args[argInd+3]);
		System.out.println("-----------------------------------------------------------------------------------------------");
	}
	
	public int vocabularyRecommendation(String[] args, int argLength, String[] commands_, Ontoseer ontoseer) { //, int argInd
		 System.out.println("\n\n\n************** Vocabulary Recommendation *************");
		 VocabularyRecommendation vocabRecommendation = new VocabularyRecommendation();
		 if(argInd<argLength && !ontoseer.isPresent(args[argInd], commands_)) {
			 vocabRecommendation.findsimilarity(args[argInd]);
			 vocabRecommendation.URI(args[argInd]);
			 argInd++;
		 }
		 return argInd;
	}
	
	// To fetch the details of an ontology
	public void parseOntology() {
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
		try {
		    // loading the axioms
			final OWLOntology owl=manager.loadOntologyFromOntologyDocument(new File(path));
			 //System.out.println(owl.getAxiomCount());
			 Set<OWLEntity> ont = owl.getSignature();
//			 System.out.println("ont "+ont);
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
//	         System.out.println("classes = "+ classes);
	         
	       //System.out.println("\n**********#### Classes ######*************\n");
			 for(OWLClass cls : classes) {
//				 System.out.println("+: " + cls.getIRI().getShortForm());
				 classlist.add(cls.getIRI().getShortForm());
				 //System.out.println("Class "+cls);
				 
				 //System.out.println(" \tObject Property Domain");
	                for (OWLObjectPropertyDomainAxiom op : owl.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {                        
	                        if (op.getDomain().equals(cls)) {   
	                            for(OWLObjectProperty oop : op.getObjectPropertiesInSignature()){
//	                                 System.out.println("\t\t +: " + oop.getIRI().getShortForm());
	                                 objectPropertyList.add(oop.getIRI().getShortForm());
	                            }
	                            //System.out.println("\t\t +: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
	                        }
	                    }

	                    //System.out.println(" \tData Property Domain");
	                    for (OWLDataPropertyDomainAxiom dp : owl.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
	                        if (dp.getDomain().equals(cls)) {   
	                            for(OWLDataProperty odp : dp.getDataPropertiesInSignature()){
//	                                 System.out.println("\t\t +: " + odp.getIRI().getShortForm());
	                                 dataPropertyList.add(odp.getIRI().getShortForm());
	                            }
	                            //System.out.println("\t\t +:" + dp.getProperty());
	                        }
	                    }
			 }
	}catch (OWLOntologyCreationException e) {
		e.printStackTrace();
	}
}
	
	public static void main(String[] args) {
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
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
		 ontoseer.parseOntology();
			 ontoseer.reClass = new HashMap<>();
			 ontoseer.reProperty = new HashMap<>();
			 
			 int argLength = args.length;
			 String[] commands_ = {"-cr","-pr","-or","-ar","-vr","-chr","-ClassRecommendation","-PropertyRecommendation","-ODPRecommendation","-AxiomRecommendation","-VocabularyRecommendation","-ClassHierarchyValidation"};
			 if(args.length>2) {
				 ontoseer.argInd = 2;
				 while(ontoseer.argInd<argLength) {
					 String cmd = args[ontoseer.argInd];
					 ontoseer.argInd++;
					 // Recommend Class Name
					 if(cmd.equals("-cr") || cmd.equals("-ClassRecommendation")) {
						 if(ontoseer.argInd<argLength && ontoseer.isPresent(args[ontoseer.argInd], commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 ontoseer.argInd= ontoseer.classNameConvention(args, argLength, commands_, ontoseer); //, i
			 			}
						 				
			 		 // Property Name Recommendation
					 else if(cmd.equals("-pr") || cmd.equals("-PropertyRecommendation")) {
							 if(ontoseer.argInd<argLength && ontoseer.isPresent(args[ontoseer.argInd], commands_)) {
								 ontoseer.commands();
								 break;
							 }
							 ontoseer.argInd = ontoseer.propertyNameConvention(args, argLength, commands_, ontoseer); //, i
			 			}
						 				
					 // ODP Recommendation
					 else if(cmd.equals("-or") || cmd.equals("-ODPRecommendation")) {
						 
						 if(ontoseer.argInd>=argLength || (ontoseer.argInd+1)>=argLength || (ontoseer.argInd+2)>=argLength) {
							 ontoseer.commands();
							 break;
						 }
						 
						 else if(ontoseer.isPresent(args[ontoseer.argInd],commands_) || ontoseer.isPresent(args[ontoseer.argInd+1],commands_) || ontoseer.isPresent(args[ontoseer.argInd+2],commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
						 ontoseer.odpRecommendation(args); //, i
						 ontoseer.argInd = ontoseer.argInd+3;
						 }
						}		 
							 
					// Axiom Recommendation
					 else if(cmd.equals("-ar") || cmd.equals("-AxiomRecommendation")){
						 if(ontoseer.argInd<argLength && ontoseer.isPresent(args[ontoseer.argInd], commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 ontoseer.argInd= ontoseer.axiomRecommendation(args, argLength, commands_, ontoseer); //, i
						 }
						}
					
					// Vocabulary Recommendation
					 else if(cmd.equals("-vr") || cmd.equals("-VocabularyRecommendation")) {
						 if(ontoseer.argInd<argLength && ontoseer.isPresent(args[ontoseer.argInd], commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 ontoseer.argInd = ontoseer.vocabularyRecommendation(args, argLength, commands_, ontoseer); //, i
						 }
					}
						
					// Class Hierarchy validation
					 else if(cmd.equals("-chr") || cmd.equals("-ClassHierarchyValidation")) {
						 if(ontoseer.argInd>=argLength || (ontoseer.argInd+1)>=argLength || (ontoseer.argInd+2)>=argLength || (ontoseer.argInd+3)>=argLength) {
							 ontoseer.commands();
							 break;
						 }
						
						 else if(ontoseer.isPresent(args[ontoseer.argInd],commands_) || ontoseer.isPresent(args[ontoseer.argInd+1],commands_) || ontoseer.isPresent(args[ontoseer.argInd+2],commands_) || ontoseer.isPresent(args[ontoseer.argInd+3],commands_)) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 ontoseer.classHierarchyValidation(args); //, i
							 ontoseer.argInd = ontoseer.argInd+4;
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
	System.out.println();
}
}
