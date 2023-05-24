package kracr.iiitd.ontoseer;

import java.util.ArrayList;
import java.util.List;

public class ODPRecommendation {
    public List<String>ct1;
    public String ct2[]=new String[50000];
    
    
    public ODPRecommendation(String ct[]) {
        ct1=new ArrayList<>();
    	ct2=ct;
    }
     
    public List<List<String>> ODP(String ct2[], String S1, String S2, String S4) {
    	List<List<String>> result = new ArrayList<>();
    	List<String>ls1=new ArrayList<String>();
    	List<String>ls2=new ArrayList<String>();
    	
    	ODPDescription o=new ODPDescription();
    	
    	String s;
    	s = S1+" "+S2+" "+" "+S4;

    	ls1.addAll(o.findsimilarity(s, ct2));
    	ls2.addAll(o.findsimilarity1(s, ct2));
    	result.add(ls1);
    	result.add(ls2);
    	return result;	
    }
    
    public List<List<String>> ODP(String ct2[], String S1, String S2) {
    	
    	List<List<String>> result = new ArrayList<>();
    	List<String>ls1=new ArrayList<String>();
    	List<String>ls2=new ArrayList<String>();
    	
    	ODPDescription o=new ODPDescription();
    	
    	String s;
    	s = S1+" "+S2;

    	ls1.addAll(o.findsimilarity(s, ct2));
    	ls2.addAll(o.findsimilarity1(s, ct2));
    	result.add(ls1);
    	result.add(ls2);
    	return result;	
    }
    
//    public static void main(String[] args) {
//    	System.out.println("ODP Recommendation");
//    }
}


