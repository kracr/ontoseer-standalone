package kracr.iiitd.ontoseer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
		// Iterating through words
		// Converting StringBuilder to String. trim() is needed to trim last space appended. 
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

	
	public static void main(String args[]) {
		ClassRecommendation s=new ClassRecommendation();
		List<String>ls=new ArrayList<String>();
		List<String>ls1=new ArrayList<String>();
		
		List<String>classes = new ArrayList<>();
		classes.add("Person#!%$&_under");
		classes.add("1234");
		System.out.println(s.classRecommendation(classes));
	}

	// Recommend a class name in CamelCase
	public HashMap<String, String> classRecommendation(List<String> classnames) {
		List<String>ls=new ArrayList<String>();
		HashMap<String, String>reClass = new HashMap<>();
		 String EMPTY = "";
		String splChrs = "-/@#$%^&_+=()";
		String chrs = "-/@#$%^&_+=()0123456789" ;
		
		for(int i=0;i<classnames.size(); i++) {
			boolean found = classnames.get(i).matches("[" + splChrs + "]+");
	
			if(found) {
				ls.add("Classname only contains Special characters Please change.");
				reClass.put(classnames.get(i), "Classname only contains Special characters Please change.");
				//return ls;
			}
			else if(classnames.get(i).matches("[0-9]+") && classnames.get(i).length() > 2) {
		          System.out.println("");
		          ls.add("Classname only contains digits Please change.");
		          reClass.put(classnames.get(i), "Classname only contains digits Please change.");
		          //return ls;
		         // classname="";
		      }
		    else if(classnames.get(i).matches("[" + chrs + "]+"))
	    	{
	    		System.out.println("");
		        ls.add("Classname only contains digits and special characters Please change.");
		        reClass.put(classnames.get(i), "Classname only contains digits and special characters Please change.");
		        //return ls;
	    	}
			else{ 
			ClassRecommendation s=new ClassRecommendation();
			String x=s.convertToCamelCase(classnames.get(i));
			String x1=s.remove_spaceproperties(x);
			String y=s.isStringOnlyAlphabet(x1);
			ls.add(y);
			reClass.put(classnames.get(i), y);
			}
		//return ls;
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
}
