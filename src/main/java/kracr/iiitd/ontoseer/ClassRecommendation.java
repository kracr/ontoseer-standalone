package kracr.iiitd.ontoseer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ClassRecommendation {

	/*converting to camel case*/
	public  String convertToCamelCase(String sentence) {
		String sentence1=sentence;
		// Extract all words
		String words[] = sentence.split("[  _]+");

		// Creating an empty string of type StringBuilder so that modification of string is possible.
		StringBuilder sb = new StringBuilder();

		// Iterating through words
		for(String word : words){

			//Extracting first char
			char firstChar = word.charAt(0);
			// Checking if firstchar is not in upper case already
			if (!Character.isUpperCase(firstChar)) {
				// Convert first char into upper case and then append remaining characters of words.
				sb.append(Character.toUpperCase(firstChar)).append(word.substring(1));
			} else
				sb.append(word.substring(0));

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
	    return d;
	}

	// Recommend a class name in CamelCase
	public List<String> classRecommendation(String classnames) {
		JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
		 String EMPTY = "";
		String splChrs = "-/@#$%^&_+=()";
		String chrs = "-/@#$%^&_+=()0123456789" ;

//		for(int i=0;i<classnames.size(); i++) {
		List<String>reClass=new ArrayList<String>();
		boolean found = classnames.matches("[" + splChrs + "]+");

		if(found) {
			reClass.add("Classname only contains Special characters Please change.");
		}
		else if(classnames.matches("[0-9]+") && classnames.length() > 2) {
	          System.out.println("");
	          reClass.add("Classname only contains digits Please change.");
	      }
	    else if(classnames.matches("[" + chrs + "]+"))
    	{
    		System.out.println("");
	        reClass.add("Classname only contains digits and special characters Please change.");
    	}
		else{
		ClassRecommendation s=new ClassRecommendation();
		String x=s.convertToCamelCase(classnames);
		x=s.remove_spaceproperties(x);
		x=s.isStringOnlyAlphabet(x);
		reClass.add(x);

		// seperator for text file
		String line = "";
		String splitBy = ",";
		try {

			// parsing a CSV file into BufferedReader class constructor
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("classIRI.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				String[] cls = line.split(splitBy); // use comma as separator
				double score = similarity.similarityCheck(x, cls[1]);
				if (score > 0.8 && reClass.size() <= 4) {
					cls[1] = s.convertToCamelCase(cls[1]);
					cls[1] = s.remove_spaceproperties(cls[1]);
					cls[1] = s.isStringOnlyAlphabet(cls[1]);
					reClass.add(cls[1]);
				} else if (reClass.size() >= 5)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		return reClass;
	}

	public String remove_spaceproperties(String classname) {

		classname=classname.replaceAll("\\p{Punct}","");
	       char[] charArray = classname.toCharArray();

	       String stringWithoutSpaces = "";
	       for (int i = 0; i < charArray.length; i++)
	       {
	           if ( (charArray[i] != ' ') && (charArray[i] != '\t') )
	               stringWithoutSpaces = stringWithoutSpaces + charArray[i];
	       }
	       return stringWithoutSpaces;
		}

	public static void main(String args[]) {
		ClassRecommendation obj=new ClassRecommendation();
		List<String>classes = new ArrayList<>();
		classes.add("Person#!%$&_under");
		classes.add("1234");
		classes.add("Dish");
		List<String> results = obj.classRecommendation(classes.get(0));
		System.out.println(results);
	}

}
