import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;


public class ArrayElementsUntyped extends AbstractJavaRule{
    @Override
    public Object visit(ASTStatementExpression node, Object data) {

        if(! node.hasDescendantOfType(ASTAssignmentOperator.class)){
          return super.visit(node, data);
        }

        try{
          List<Node> var = node.findChildNodesWithXPath("./PrimaryExpression/PrimaryPrefix/Name");

          if(var.size() == 0)
            return super.visit(node, data);
          Class typeOfVar = ((AbstractJavaTypeNode)var.get(0)).getType();

          if(!typeOfVar.isArray()){
            return super.visit(node, data);
          }
          int derefCount = node.findChildNodesWithXPath("./PrimaryExpression/PrimarySuffix").size();

          while(derefCount > 0){
            derefCount--;
            typeOfVar = typeOfVar.getComponentType();
          }

          // prevent anything with . see: https://sourceforge.net/p/pmd/bugs/803/
          /*
          when fixed
          use
          Class typeOfRValue = node.getFirstChildOfType​(ASTExpression.class).getType();
          instead and remove workaround
          */
          List<Node> r = node.findChildNodesWithXPath("./Expression[not(contains(.//Name/@Image,'.'))]");
          if(r.size() == 0)
            return super.visit(node, data);
          Class typeOfRValue = ((ASTExpression)r.get(0)).getType();
          //remove leftover problem of https://sourceforge.net/p/pmd/bugs/803/
          if(typeOfRValue==null)
            return super.visit(node, data);
          //end workaround

          if( typeOfVar != typeOfRValue){
            addViolation(data, node);
          }
        }
        catch(JaxenException | NullPointerException e ){
          return super.visit(node, data);
        }
        return super.visit(node, data);
    }

}
