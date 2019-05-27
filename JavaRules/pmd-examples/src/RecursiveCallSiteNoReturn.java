import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;

public class RecursiveCallSiteNoReturn extends AbstractJavaRule {

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {

        if(node.isVoid())
          return super.visit(node, data);

        if(!isDirectlyRecursive(node))
          return super.visit(node, data);

        if (numberOfReturns(node) < 2) {
            addViolationWithMessage(data, node, "A recursive method that has a non-void type always requires at least two return statements as it must always return a value.");
        }
        return super.visit(node, data);
    }

    private boolean isDirectlyRecursive(ASTMethodDeclaration node){
      //getMethodName
      String name = node.getName();
      List<ASTName> names = node.findDescendantsOfType(ASTName.class);
      for(ASTName n : names){
          Node exp = n.getNthParent​(2);
          if(exp == null || exp.getClass() != ASTPrimaryExpression.class)
            continue;
          if(!exp.hasDescendantOfType​(ASTArguments.class))
            continue;
          if(n.getImage().equals(name)){
              return true;
            }
      }

      //should also check indirectly recursive
      return false;
    }

    private static int numberOfReturns(ASTMethodDeclaration node){

      return node.findDescendantsOfType(ASTReturnStatement.class).size();
    }

}
