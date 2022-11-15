package kracr.iiitd.ontoseer;
import org.semanticweb.owlapi.model.*;

public class ParseOntology implements OWLAxiomVisitor {
    private OWLClassExpression exp;
    
    
    public ParseOntology(OWLClassExpression exp) {
        this.exp = exp;
    }
    
    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
      if (exp.equals(axiom.getSubClass())) {
        System.out.println(axiom.getSubClass() + " subClassOf" + axiom.getSuperClass()); // prints sub class
      } else {
        System.out.println(axiom.getSubClass() + " superClassOf" + axiom.getSuperClass()); // prints super class
      }
    }
    
    @Override
    public void visit(OWLEquivalentClassesAxiom axiom) {
      System.out.println(exp + " equivalent to " + axiom.getClassExpressionsMinus(exp)); // prints equivalent class
    }
}