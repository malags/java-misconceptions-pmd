import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;

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

      //get ArgumentCount (deals with overloading)
      int argCount;
      try{
        List<Node> dec = node.findChildNodesWithXPath​("./MethodDeclarator");
        if(dec.size() == 0)
          return false;
        argCount = ((ASTMethodDeclarator)dec.get(0)).getParameterCount();
        System.out.println(argCount);
        //getMethodName
        String name = node.getName();

        List<Node> uses = node.findChildNodesWithXPath(".//PrimaryExpression[./PrimaryPrefix/Name[@Image = '"+name+"'] and ./PrimarySuffix[@ArgumentCount = "+argCount+"] ]");
        if(uses.size() != 0)
          return true;

        //should also check indirectly recursive
        return false;

      }
      catch(JaxenException e){
        return false;
      }
    }

    private int numberOfReturns(ASTMethodDeclaration node){
      return node.findDescendantsOfType(ASTReturnStatement.class).size();
    }

}
