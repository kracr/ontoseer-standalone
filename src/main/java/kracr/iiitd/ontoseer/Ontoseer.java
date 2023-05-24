package kracr.iiitd.ontoseer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	public int argIndx,argLength; 
	public String[] arguments;
	public HashMap<String, String> reClass;
	public HashMap<String, String> reProperty;
	public String[] commands_ = {"-cr","-pr","-or","-ar","-vr","-chr",
			"-ClassRecommendation","-PropertyRecommendation",
			"-ODPRecommendation","-AxiomRecommendation",
			"-VocabularyRecommendation","-ClassHierarchyValidation"};

	
	public void commands() {
		 System.out.println("-h  -- Help");
		 System.out.println("-p <Ontology file path> -cr|-pr|-or|-ar|-chr|-vr <arguments for respective recommendation>");
		 System.out.println("\t-cr -ClassRecommendation <class1 class2 class3 .... >");
		 System.out.println("\t-pr -PropertyRecommendation <Property1 Property2 ......>");
		 System.out.println("\t-or -ODPRecommendation <arg1 arg2 arg3(optional)>");
		 System.out.println("\t\targ1 -- Description of the ontology");
		 System.out.println("\t\targ2 -- Domain of ontology");
		 System.out.println("\t\targ3 -- Competency questions, "+ "It is optional");
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
	public boolean isPresent(String s) {
		for(String cmd : commands_) {
			if(cmd.equals(s)) {
				return true;
			}
		}
		return false;
	}
		
	public HashMap<String,List<String>> classNameRecommendation() {
		HashMap<String,List<String>>recommendedClass = new HashMap<>();
		System.out.println("\n************** Class Recommendation *************");
		ClassRecommendation className = new ClassRecommendation();
		for(;argIndx<argLength && !isPresent(arguments[argIndx]);argIndx++) {
			recommendedClass.put(arguments[argIndx], className.classRecommendation(arguments[argIndx]));
		}
		System.out.println("-----------------------------------------------------------------------------------------------");
		return recommendedClass;
	}
	
	public HashMap<String,List<String>> propertyNameRecommendation() {
		
		HashMap<String,List<String>>recommendedProperty = new HashMap<>();
		System.out.println("\n************** Property Recommendation *************");
		PropertyRecommendation propertyName = new PropertyRecommendation();
		for(;argIndx<argLength && !isPresent(arguments[argIndx]);argIndx++) {
			recommendedProperty.put(arguments[argIndx], propertyName.propertiesRecommendation(arguments[argIndx]));
		}
		System.out.println("-----------------------------------------------------------------------------------------------");
		return recommendedProperty;
	}
	
	
	public HashMap<String,HashMap<String,String>> axiomRecommendation() {
		System.out.println("\n************** Axiom Recommendation *************");
		HashMap<String,HashMap<String, String>>reAxiom = new HashMap<>();
		 AxiomRecommendation axioms = new AxiomRecommendation();
		 if(argIndx<argLength && !isPresent(arguments[argIndx])) {
			 HashMap<String,String>result =  axioms.axiomRecommendation(arguments[argIndx]);
			 reAxiom.put(arguments[argIndx], result);
			 argIndx++;
		 }
		 System.out.println("-----------------------------------------------------------------------------------------------");
		 return reAxiom;
	}
	
	public List<List<String>> odpRecommendation() {
		System.out.println("\n************** ODP Recommendation *************");
		List<List<String>> reODP = new ArrayList<>();
		 // List to contains all the entities 
		 List<String>temp = new ArrayList<String>();
		 for(Map.Entry<String, String> entry : reClass.entrySet()) {
			 temp.add(entry.getValue());
		 }
		 for(Map.Entry<String, String> entry : reProperty.entrySet()) {
			 temp.add(entry.getValue());
		 }
		 temp.addAll(dataPropertyList);
		 String[] str = temp.toArray(new String[0]);   // Array of all class and data properties 
		 ODPRecommendation odprcmd = new ODPRecommendation(str);
		 if((argIndx+2)<argLength && !isPresent(arguments[argIndx+2])) {
			 reODP = odprcmd.ODP(str, arguments[argIndx], arguments[argIndx+1], arguments[argIndx+2]);
		}
		 else if((argIndx+1)<argLength && !isPresent(arguments[argIndx+1])){
			 reODP = odprcmd.ODP(str, arguments[argIndx], arguments[argIndx+1]);
		 }
		 else {
			 commands();
		 }
		 System.out.println("-----------------------------------------------------------------------------------------------");
		 return reODP;
	}

	
	public String classHierarchyValidation() {
		String result = "";
		ClassHierarchyValidation classHierarchyValidation = new ClassHierarchyValidation();
		System.out.println("\n************** Class Hierarchy Validation *************");
		result = classHierarchyValidation.classHierarchy(arguments[argIndx], arguments[argIndx+1], 
				arguments[argIndx+2], arguments[argIndx+3]);
		System.out.println("-----------------------------------------------------------------------------------------------");
		return result;
	}
	
	public List<List<String>> vocabularyRecommendation() {
		 System.out.println("\n************** Vocabulary Recommendation *************");
		 List<List<String>> recVocab = new ArrayList<>();
		 VocabularyRecommendation vocabRecommendation = new VocabularyRecommendation();
		 if(argIndx<argLength && !isPresent(arguments[argIndx])) {
			 recVocab.add(vocabRecommendation.findsimilarity(arguments[argIndx]));
			 recVocab.add(vocabRecommendation.URI(arguments[argIndx]));
			 argIndx++;
		 }
		 System.out.println("-----------------------------------------------------------------------------------------------");
		 return recVocab;
	}
	
	// To fetch the details of an ontology
	public void parseOntology() {
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
		try {
		    // loading the axioms
			final OWLOntology owl=manager.loadOntologyFromOntologyDocument(new File(path));
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

			 for(OWLClass cls : classes) {
				 classlist.add(cls.getIRI().getShortForm());
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
		 Ontoseer ontoseer = new Ontoseer();
		 ontoseer.arguments = args;
		 ontoseer.argLength = args.length;
		 System.out.println();
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
			 if(args.length>2) {
				 ontoseer.argIndx = 2;
				 while(ontoseer.argIndx<ontoseer.argLength) {
					 String cmd = args[ontoseer.argIndx];
					 ontoseer.argIndx++;
					 
					 // Recommend Class Name
					 if(cmd.equals("-cr") || cmd.equals("-ClassRecommendation")) {
						 if(ontoseer.argIndx<ontoseer.argLength && ontoseer.isPresent(args[ontoseer.argIndx])) {
							 ontoseer.commands();
							 break;
						 }
						 HashMap<String,List<String>> result= ontoseer.classNameRecommendation(); //, i
						 for(Map.Entry<String, List<String>> mp : result.entrySet()) {
							 System.out.println(mp.getKey());
							 System.out.println("\t+"+mp.getValue());
						 }
			 			}
						 				
			 		 // Property Name Recommendation
					 else if(cmd.equals("-pr") || cmd.equals("-PropertyRecommendation")) {
							 if(ontoseer.argIndx<ontoseer.argLength && ontoseer.isPresent(args[ontoseer.argIndx])) {
								 ontoseer.commands();
								 break;
							 }
							 HashMap<String,List<String>>result = ontoseer.propertyNameRecommendation();
							 for(Map.Entry<String, List<String>> mp : result.entrySet()) {
								 System.out.println(mp.getKey());
								 System.out.println("\t+"+mp.getValue());
							 }
			 			}
					 
					// Axiom Recommendation
					 else if(cmd.equals("-ar") || cmd.equals("-AxiomRecommendation")){
						 if(ontoseer.argIndx<ontoseer.argLength && ontoseer.isPresent(args[ontoseer.argIndx])) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 HashMap<String,HashMap<String,String>>result = ontoseer.axiomRecommendation();
							 for(Map.Entry<String, HashMap<String,String>>mp : result.entrySet()) {
								 System.out.println(mp.getKey());
								 for(Map.Entry<String, String>submp : mp.getValue().entrySet()) {
									 System.out.println("\t+"+submp.getKey()+"\t"+submp.getValue());
								 }
							 }
						  }
						}
						 				
					 // ODP Recommendation
					 else if(cmd.equals("-or") || cmd.equals("-ODPRecommendation")) {
						 List<List<String>> recommendedODP = new ArrayList<>();
						 if(ontoseer.argIndx>=ontoseer.argLength || (ontoseer.argIndx+1)>=ontoseer.argLength ) {
							 ontoseer.commands();
							 break;
						 }
						 
						 else if(ontoseer.isPresent(args[ontoseer.argIndx]) || ontoseer.isPresent(args[ontoseer.argIndx+1])){
							 ontoseer.commands();
							 break;
						 }
						 else {
							 if((ontoseer.argIndx+2)<ontoseer.argLength && !ontoseer.isPresent(args[ontoseer.argIndx+2])) {
								 recommendedODP = ontoseer.odpRecommendation();
								 ontoseer.argIndx = ontoseer.argIndx+3;
							}
							 else {
								 recommendedODP = ontoseer.odpRecommendation();
								 ontoseer.argIndx +=2;
							 }
							 
							 if(recommendedODP.size()>0) {
								List<String>ls1 = recommendedODP.get(0);
							 	List<String>ls2 = recommendedODP.get(1);
							 	System.out.println("\t1. "+ls1.get(0).toString()+"\n"+"\tIRI: "+ls2.get(0)+"\n"+"\n"+"\t2. "+ls1.get(1).toString()+"\n"+"\tIRI: "+
								    	ls2.get(1)+"\n"+"\n"+"\t3. "+ls1.get(2).toString()+"\n"+"\tIRI: "+ls2.get(2)+"\n"+"\n"+"\t4. "+ls1.get(3).toString()+"\n"+"\tIRI: "+
								    	ls2.get(3));
							 }
						 }
						}		 
							 
					
					
					// Vocabulary Recommendation
					 else if(cmd.equals("-vr") || cmd.equals("-VocabularyRecommendation")) {
						 if(ontoseer.argIndx<ontoseer.argLength && ontoseer.isPresent(args[ontoseer.argIndx])) {
							 ontoseer.commands();
							 break;
						 }
						 else {
							 ontoseer.vocabularyRecommendation();
						 }
					}
						
					// Class Hierarchy validation
					 else if(cmd.equals("-chr") || cmd.equals("-ClassHierarchyValidation")) {
						 if(ontoseer.argIndx>=ontoseer.argLength || (ontoseer.argIndx+1)>=ontoseer.argLength 
								 || (ontoseer.argIndx+2)>=ontoseer.argLength ||(ontoseer.argIndx+3)>=ontoseer.argLength) {
							 System.out.println("Insert all four input for class hierarchy validagtion");
							 ontoseer.commands();
							 break;
						 }
						
						 else if(ontoseer.isPresent(args[ontoseer.argIndx]) || ontoseer.isPresent(args[ontoseer.argIndx+1]) 
								 || ontoseer.isPresent(args[ontoseer.argIndx+2]) || ontoseer.isPresent(args[ontoseer.argIndx+3])) {
							 System.out.println("Any input out of four for class hierarchy validation is missing");
							 ontoseer.commands();
							 break;
						 }
						 else {
							 
							 System.out.println(ontoseer.classHierarchyValidation());
							 ontoseer.argIndx = ontoseer.argIndx+4;
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
