package kracr.iiitd.ontoseer;


/*code for class hierarchy validation*/
public class ClassHierarchyValidation{
     
    public ClassHierarchyValidation() {
//        System.out.println("Class Hierarchy Validation");
    }
     
    public String classHierarchy(String supClassRigidity, String subClassRigidity, String identity, String unity) {
    	String result = "";
    	if(supClassRigidity.equalsIgnoreCase("")||subClassRigidity.equalsIgnoreCase("")||identity.equalsIgnoreCase("")||unity.equalsIgnoreCase("")) {
    		result = "All mandatory fields are not filled";
//    		System.out.println("All mandatory fields are not filled");
    	}
    	else {
    	//The cases for Y and N are ignored. Y is yes and N is No
    	/*Rigidity if superclass is Y then sub class can be Y||N. If superclass is N then subclass has to be N*/
    	if(supClassRigidity.equalsIgnoreCase("y")) {
    		if(subClassRigidity.equalsIgnoreCase("y")||subClassRigidity.equalsIgnoreCase("N")) {
    			result = "Rigidity is correctly maintained.";
//    			System.out.println("Rigidity is correctly maintained.");
    		}
    		
    	}
    	else if(supClassRigidity.equalsIgnoreCase("N")) {
    		if(subClassRigidity.equalsIgnoreCase("N")) {
    			result = "Rigidity is correctly maintained.";
//    			System.out.println("Rigidity is correctly maintained.");
    		}
    		else if(subClassRigidity.equalsIgnoreCase("Y")) {
    			result = "Rigidity is not correctly maintained. Subclass hierarchy is not correct.";
//    			System.out.println("Rigidity is not correctly maintained. Subclass hierarchy is not correct.");
    		}
    	}
    	
    	/*If identity is Y then they are identical else not*/
    	if(identity.equalsIgnoreCase("y")) {
    			result += "\n" + "Identity is correctly maintained.";
//    			System.out.println("Identity is correctly maintained.");
    		
    		
    	}
    	else if(identity.equalsIgnoreCase("N")) {
    		result += "\n" + "Identity is not correctly maintained. Subclass hierarchy is not correct.";
//    		System.out.println("Identity is not correctly maintained. Subclass hierarchy is not correct.");
    	}
    	
    	if(unity.equalsIgnoreCase("y")) {
    			result += "\n"+ "Unity is correctly maintained.\n";
//    			System.out.println("Unity is correctly maintained.\n");
    		
    		
    	}
    	
    	/*If Unity is Y then they are identical else not*/
    	else if(unity.equalsIgnoreCase("N")) {
    		
    			result += "\n"+ "Unity is not correctly maintained. Subclass hierarchy is not correct.\n";
//    			System.out.println("Unity is not correctly maintained. Subclass hierarchy is not correct.\n");
    	}
    
    	}
    	return result;
    }
    
    public static void main(String[] args) {
     
//    		ClassHierarchyValidation j=new ClassHierarchyValidation();
//    		j.classHierarchy("y", "y", "n", "n");
//    		j.classHierarchy("Y", "y", "N", "N");
    }
}

