package kracr.iiitd.ontoseer;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

public class JaroWinklerSimilarity {
	
	public double similarityCheck(String str1, String str2) {
		SimilarityStrategy strategy = new JaroWinklerStrategy();
		StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
		double score = service.score(str1, str2); // Score is 0.90
		return score;
	}
	
   public static void main(String[] args) {
	   
   }
		
	
}
