import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTStatement;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.symboltable.ScopedNode;
import java.util.List;

public class EnablerPrivateFromOtherInstance extends AbstractJavaRule {

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {

        try{

          if(isGetter(node, node.getMethodName())){
            addViolation(data,node);
          }

        }
        catch(JaxenException e){
          e.printStackTrace();
          return super.visit(node, data);
        }
        return super.visit(node, data);
    }


    private boolean isGetter(Node n, String methodName) throws JaxenException{
      //get class node
      ASTTypeDeclaration cl = n.getFirstParentOfType​(ASTTypeDeclaration.class);
      if(!methodName.contains("get"))
        return false;

      String varName;
      //no name for getter
      if(methodName.length() < 4)
        return false;
      char c[] = methodName.substring(3).toCharArray();
  	  c[0] += 32;
  	  varName = new String(c);
      List<Node> methods = cl.findChildNodesWithXPath("//MethodDeclaration[ ./MethodDeclarator[@Image = '" + methodName + "' and @ParameterCount = 0] and ./Block/BlockStatement/Statement/ReturnStatement/Expression/PrimaryExpression/PrimaryPrefix/Name[contains(@Image,'" + varName + "')]]");

      //found at least one method with given name that returns what appears to be a field (without this.)
      if(methods.size() > 0)
        return true;

      //check if one method with given name that returns a field with this.varName
      return cl.findChildNodesWithXPath( "//MethodDeclaration[ ./MethodDeclarator[@Image = '" + methodName + "' and @ParameterCount = 0] and ./Block/BlockStatement/Statement/ReturnStatement/Expression/PrimaryExpression[ ./PrimarySuffix[contains(@Image,'"+ varName + "')] and ./PrimaryPrefix[@ThisModifier = true()]] ]")
      .size() > 0;
    }
}
