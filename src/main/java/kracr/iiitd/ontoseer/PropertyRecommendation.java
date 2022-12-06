package kracr.iiitd.ontoseer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PropertyRecommendation {

	/*convert to inverse camel case*/
	public  String convertToReverseCamelCase(String sentence) {
		String sentence1=sentence; 
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
		PropertyRecommendation s=new PropertyRecommendation();
		List<String>ls=new ArrayList<String>();
		List<String>ls1=new ArrayList<String>();
		
		List<String>property = new ArrayList<>();
		property.add("has_under");
		property.add("Person news");
		property.add("@#%");
		System.out.println(s.propertiesRecommendation(property));
		System.out.println(ls.size());
}

	// Recommend a class name in CamelCase
	
	// Recommand the function name in reverse camelCase
	public HashMap<String, String> propertiesRecommendation(List<String> propertylist) {
		List<String>ls=new ArrayList<String>();
		HashMap<String, String>reProperty = new HashMap<>();
		 String EMPTY = "";
		String splChrs = "-/@#$%^&_+=()";
		String chrs = "-/@#$%^&_+=()0123456789";
		for(int i=0;i<propertylist.size();i++) {
			boolean found = propertylist.get(i).matches("[" + splChrs + "]+");
			if(found) {
				ls.add("Property name only contains Special characters Please change.");
				reProperty.put(propertylist.get(i), "Property name only contains Special characters Please change.");
				//return ls;
			}
			else if(propertylist.get(i).matches("[0-9]+") && propertylist.get(i).length() > 2) {
		          System.out.println("");
		          ls.add("Property name only contains digits Please change.");
		          reProperty.put(propertylist.get(i), "Property name only contains digits Please change.");
		          //return ls;
		         // classname="";
		      }
		    else if(propertylist.get(i).matches("[" + chrs + "]+"))
	    	{
	    		System.out.println("");
		        ls.add("Property name only contains digits and special characters Please change.");
		        reProperty.put(propertylist.get(i), "Property name only contains digits and special characters Please change.");
		        //return ls;
	    	}
			else { 
			PropertyRecommendation s=new PropertyRecommendation();
			String x=s.convertToReverseCamelCase(propertylist.get(i));
			String x1=s.remove_spaceproperties(x);
			String y=s.isStringOnlyAlphabet(x1);
			ls.add(y);
			reProperty.put(propertylist.get(i), y);
			}
		}
		return reProperty;
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
}
