package kracr.iiitd.ontoseer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

class Ontology {
	List<String> classes;
	List<String> objectProperties;
	List<String> dataProperties;
	String iri;

	Ontology() {
		classes = new ArrayList<>();
		objectProperties = new ArrayList<>();
		dataProperties = new ArrayList<>();
	}
}

/*
 * running over all the indexed tuples and matching each of the three term and
 * storing most relevant terms in hash-set
 */
public class AxiomRecommendation {
	public List<HashMap<String, String>> clsIRI = new ArrayList<>();
	public List<HashMap<String, String>> prIRI = new ArrayList<>();
	public OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	public List<Ontology> ontologyDetails = new ArrayList<>();

	public HashMap<String, String> axiomRecommendation(String className) {
		JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
		HashMap<String, String> result = new HashMap<>();
		String line = "";
		String splitBy = ",";
		try {
			
			// parsing a CSV file into BufferedReader class constructor
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/classIRI.txt"));
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				String[] axiomList = line.split(splitBy); // use comma as separator
				double score = similarity.similarityCheck(className, axiomList[1]);
				if (score > 0.8 && result.size() <= 4) {
					result.put(axiomList[1], axiomList[0]);
				} else if (result.size() >= 5)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result.size() < 5) {
			try {
				// parsing a CSV file into BufferedReader class constructor
				BufferedReader br = new BufferedReader(new FileReader("src/main/resources/propertyIRI.txt"));
				while ((line = br.readLine()) != null) // returns a Boolean value
				{
					String[] employee = line.split(splitBy); // use comma as separator
					double score = similarity.similarityCheck(className, employee[1]);
					if (score > 0.8 && result.size() <= 4) {
						result.put(employee[1], employee[0]);
//						System.out.println(employee[1]);
					} else if (result.size() >= 5)
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public static void main(String args[]) throws FileNotFoundException {
		AxiomRecommendation obj = new AxiomRecommendation();
		System.out.println("****  ---- getOntologyDetails-----  ******");
		HashMap<String, String>result = obj.axiomRecommendation("CusineType");
		for(Map.Entry<String, String>mp : result.entrySet()) {
			System.out.println(mp.getKey()+"\t"+mp.getValue());
		}
		
	}
}
