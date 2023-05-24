package kracr.iiitd.ontoseer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class PropertyRecommendation {

	/*convert to inverse camel case*/
	public  String convertToReverseCamelCase(String sentence) {

		// Extract all words
		String words[] = sentence.split("[  _]+");

		// Creating an empty string of type StringBuilder so that modification of string is possible.
		StringBuilder sb = new StringBuilder();

		// Iterating through words
		boolean flag = true;
		String splChrs = "-/@#$%^&_+=()";
		for (int i=0;i<words.length;i++) {

			//Extracting first char
			String word=words[i];
			char firstChar = word.charAt(0);
			String first = ""+firstChar;

			if(flag && (first.matches("[" + splChrs + "]+") || first.matches("[0-9]+")))
				continue;
			if(flag) {
				if (Character.isUpperCase(firstChar)) {

					// Convert first char into upper case and then append remaining characters of words.
					sb.append(Character.toLowerCase(firstChar)).append(word.substring(1));
				} else
					sb.append(word.substring(0));
				flag = false;
			}
			else{

				// Checking if firstchar is not in upper case already
				if (!Character.isUpperCase(firstChar)) {

					// Convert first char into upper case and then append remaining characters of words.
					sb.append(Character.toUpperCase(firstChar)).append(word.substring(1));
				}
				else
					sb.append(word.substring(0));
			}
		}
		String result = sb.toString().trim();
		return result;
	}

	/*checking whether string is alphabet*/
	public  String isStringOnlyAlphabet(String str)
	{
		String d=str;
	    if(!str.equals("")&& (str != null)&& (str.matches(".*\\d.*"))){
	    	 d=str.replaceAll("\\d","");
	    }
	    //System.out.println(d);
	    return d;
	}



	// Recommand the function name in reverse camelCase
	public List<String> propertiesRecommendation(String property) {

		List<String>recommendedProperty=new ArrayList<String>();
		JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
		String EMPTY = "";
		String splChrs = "-/@#$%^&_+=()";
		String chrs = "-/@#$%^&_+=()0123456789";
		boolean found = property.matches("[" + splChrs + "]+");
		if(found) {
			recommendedProperty.add("Property name only contains Special characters Please change.");
		}
		else if(property.matches("[0-9]+") && property.length() > 2) {
	          System.out.println("");
	          recommendedProperty.add("Property name only contains digits Please change.");
	      }
	    else if(property.matches("[" + chrs + "]+"))
    	{
    		System.out.println("");
	        recommendedProperty.add("Property name only contains digits and special characters Please change.");
    	}
		else {
		PropertyRecommendation s=new PropertyRecommendation();
		String x=s.convertToReverseCamelCase(property);
		x=s.remove_spaceproperties(x);
		x=s.isStringOnlyAlphabet(x);
		recommendedProperty.add(x);
		String line = "";
		String splitBy = ",";
		try {

			// parsing a CSV file into BufferedReader class constructor
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("propertyIRI.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				String[] pr = line.split(splitBy); // use comma as separator
				double score = similarity.similarityCheck(x, pr[1]);
				if (score > 0.8 && recommendedProperty.size() <= 4) {
					pr[1] = s.convertToReverseCamelCase(pr[1]);
					pr[1] = s.remove_spaceproperties(pr[1]);
					pr[1] = s.isStringOnlyAlphabet(pr[1]);
					if(!recommendedProperty.contains(pr[1]))
						recommendedProperty.add(pr[1]);
				} else if (recommendedProperty.size() >= 5)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		return recommendedProperty;
	}

	public String remove_spaceproperties(String entity) {

		entity=entity.replaceAll("\\p{Punct}","");
	       char[] charArray = entity.toCharArray();

	       String stringWithoutSpaces = "";
	       for (int i = 0; i < charArray.length; i++)
	       {
	           if ( (charArray[i] != ' ') && (charArray[i] != '\t') )
	               stringWithoutSpaces = stringWithoutSpaces + charArray[i];
	       }
	       return stringWithoutSpaces;
	}

	public static void main(String args[]) {
		PropertyRecommendation s=new PropertyRecommendation();
		List<String>property = new ArrayList<>();
		property.add("has_under");
		property.add("Person news");
		property.add("@#%");
		System.out.println(s.propertiesRecommendation(property.get(0)));
		System.out.println(s.propertiesRecommendation(property.get(1)));
		System.out.println(s.propertiesRecommendation(property.get(2)));
	}

}
