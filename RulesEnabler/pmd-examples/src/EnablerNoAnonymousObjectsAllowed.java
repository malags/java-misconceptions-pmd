import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;

public class EnablerNoAnonymousObjectsAllowed extends AbstractJavaRule {

    @Override
    public Object visit(ASTVariableDeclarator node, Object data) {

        ASTMethodDeclaration method = getMethodParent(node);

        //skip fields
        if(method == null)
          return super.visit(node, data);

        int occurrences = numberOfOverlap(method, node.getName());

        if (occurrences < 2) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    private ASTMethodDeclaration getMethodParent(ASTVariableDeclarator node){
      return node.getFirstParentOfAnyType(ASTMethodDeclaration.class);
    }


    private int numberOfOverlap(ASTMethodDeclaration node, String name){
      int count = 0;
      List<ASTName> occurrences = node.findDescendantsOfType(ASTName.class);
      for(ASTName n : occurrences)
        if(name.equals(substringBefore(n.getImage())))
          count++;
      return count;
    }

    private String substringBefore(String s){
      if(s == null)
        return s;
      int index = s.indexOf('.');
      if(index == -1)
        return s;
      return s.substring(0,index);
    }
}
