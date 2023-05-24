package kracr.iiitd.ontoseer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
	/*
	 * public List<String> retrieveFilePaths(String path) { File filePath = new
	 * File(path); File filesList[] = filePath.listFiles(); //
	 * System.out.println(filesList); List<String> paths = new ArrayList<String>();
	 * for (File file : filesList) { if (file.isFile() &&
	 * file.getName().endsWith(".owl")) {
	 * paths.add(file.getAbsolutePath().replace("\\", "/")); } } // for (String s :
	 * paths) { // System.out.println(s); // } return paths; }
	 *
	 * // function to add value in a set public void addToSet(HashMap<String,
	 * Set<String>> map, String setName, String value) { if
	 * (!map.containsKey(setName)) { map.put(setName, new HashSet<String>()); }
	 * Set<String> values = map.get(setName); values.add(value); }
	 *
	 * public void getOntologyClassDetails() throws FileNotFoundException { String
	 * line = ""; String splitBy = ","; ClassRecommendation cls = new
	 * ClassRecommendation(); try { //parsing a CSV file into BufferedReader class
	 * constructor BufferedReader br = new BufferedReader(new
	 * FileReader("C:/Users/ankit/Desktop/ontologyDetails_CSV")); while ((line =
	 * br.readLine()) != null) //returns a Boolean value { String[] employee =
	 * line.split(splitBy); // use comma as separator try {
	 * System.out.println(employee[0] + ", " + employee[1]); try { FileWriter
	 * myWriter = new FileWriter(
	 * "C:/Users/ankit/Desktop/ontoseer-standalone/src/main/resources/raw_sentences1.txt"
	 * ,true); FileWriter classIRI = new FileWriter(
	 * "C:/Users/ankit/Desktop/ontoseer-standalone/src/main/resources/classIRI.txt",
	 * true); List<String> cl = new ArrayList<>(); cl.add(employee[1]);
	 * HashMap<String,String>reClass = cls.classRecommendation(cl);
	 *
	 * // if recommended class and given class are same then add any one of them in
	 * file if(!reClass.get(employee[1]).equals(employee[1])) {
	 * myWriter.write(employee[1]+"\n"+reClass.get(employee[1])+"\n");
	 * classIRI.write(employee[0]+","+employee[1]+"\n"+employee[0]+","+reClass.get(
	 * employee[1])+"\n"); } else { myWriter.write(employee[1]+"\n");
	 * classIRI.write(employee[0]+","+employee[1]+"\n"); } Thread.sleep(70);
	 * myWriter.close(); classIRI.close();
	 * System.out.println("*****  class IRI **** "); } catch (IOException e1) { //
	 * TODO Auto-generated catch block e1.printStackTrace(); } }catch(Exception e) {
	 * e.printStackTrace(); } } } catch (IOException e) { e.printStackTrace(); }
	 * System.out.println(" Class Write operation complete"); }
	 *
	 * // Read data from CSV file..... public void getOntologyPropertyDetails()
	 * throws FileNotFoundException { String line = ""; String splitBy = ",";
	 * PropertyRecommendation property = new PropertyRecommendation(); try {
	 * //parsing a CSV file into BufferedReader class constructor BufferedReader br
	 * = new BufferedReader(new
	 * FileReader("C:/Users/ankit/Desktop/ontologyPropertyDetails_CSV")); while
	 * ((line = br.readLine()) != null) //returns a Boolean value { String[]
	 * employee = line.split(splitBy); // use comma as separator try { //
	 * System.out.println(employee[0] + ", " + employee[1]); try { FileWriter
	 * myWriter = new FileWriter(
	 * "C:/Users/ankit/Desktop/ontoseer-standalone/src/main/resources/raw_sentences.txt"
	 * ,true); FileWriter propertyIRI = new FileWriter(
	 * "C:/Users/ankit/Desktop/ontoseer-standalone/src/main/resources/propertyIRI.txt"
	 * ,true); List<String> pr = new ArrayList<>(); pr.add(employee[1]);
	 * HashMap<String,String>reProperty = property.propertiesRecommendation(pr);
	 *
	 * // if recommended Property and given property are same then add any one of
	 * them in file if(!reProperty.get(employee[1]).equals(employee[1])) {
	 * myWriter.write(employee[1]+"\n"+reProperty.get(employee[1])+"\n");
	 * propertyIRI.write(employee[0]+","+employee[1]+"\n"+employee[0]+","+reProperty
	 * .get(employee[1])+"\n"); } else { myWriter.write(employee[1]+"\n");
	 * propertyIRI.write(employee[0]+","+employee[1]+"\n"); } Thread.sleep(50);
	 * myWriter.close(); propertyIRI.close();
	 * System.out.println("*** Property IRI ***"); } catch (IOException e1) { //
	 * TODO Auto-generated catch block e1.printStackTrace(); } }catch(Exception e) {
	 * e.printStackTrace(); } } } catch (IOException e) { e.printStackTrace(); }
	 * System.out.println("Write operation complete"); }
	 */


	public HashMap<String, String> axiomRecommendation(String className) {
		JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
		HashMap<String, String> result = new HashMap<>();
		String line = "";
		String splitBy = ",";
		try {

			// parsing a CSV file into BufferedReader class constructor
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("classIRI.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
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
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("propertyIRI.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
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
