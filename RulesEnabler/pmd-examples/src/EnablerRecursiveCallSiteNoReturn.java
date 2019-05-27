import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;

public class EnablerRecursiveCallSiteNoReturn extends AbstractJavaRule {

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {

        if(node.isVoid())
          return super.visit(node, data);

        if(!isDirectlyRecursive(node))
          return super.visit(node, data);

        if (numberOfReturns(node) < 2) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    private boolean isDirectlyRecursive(ASTMethodDeclaration node){
      //getMethodName
      String name = node.getName();
      List<ASTName> names = node.findDescendantsOfType(ASTName.class);
      for(ASTName n : names)
          if(n.getImage().equals(name))
              return true;

      //should also check indirectly recursive
      return false;
    }

    private int numberOfReturns(ASTMethodDeclaration node){
      return node.findDescendantsOfType(ASTReturnStatement.class).size();
    }

}
