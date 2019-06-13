import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTMultiplicativeExpression;
import net.sourceforge.pmd.lang.java.ast.ASTVariableInitializer;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;


public class IntegerDivisionToRational extends AbstractJavaRule{
    @Override
    public Object visit(ASTBlockStatement node, Object data) {
        boolean keep = node.hasDescendantOfType​(ASTAssignmentOperator.class) ||
                       node.hasDescendantOfType​(ASTVariableDeclarator.class);
        if(! keep){
          return super.visit(node, data);
        }

        if(node.hasDescendantOfType​(ASTAssignmentOperator.class)){
          return assignmentOperator(node,data);
        }
        else{
          return variableDeclarator(node,data);
        }
    }




    //contains an ASTAssignmentOperator
    private Object assignmentOperator(ASTBlockStatement node, Object data){

      //cannot be null
      ASTStatementExpression statementExpression = node.getFirstDescendantOfType(ASTStatementExpression.class);

      //only consider in case the value is assigned to a double or float
      // MUST call .getName() on the value as getType returns a java.lang.Class
      if( statementExpression == null || statementExpression.getType() == null ||
            !(
              statementExpression.getType().getName().equals("double")
              ^ statementExpression.getType().getName().equals("float")
            )
        ){
        return super.visit(node, data);
      }

      ASTMultiplicativeExpression multExpression =
        statementExpression.getFirstDescendantOfType(ASTMultiplicativeExpression.class);

      if(multExpression == null || ! multExpression.getOperator().equals("/")){
        return super.visit(node, data);
      }

      if (multExpression.getType().getName().equals("int") || multExpression.getType().getName().equals("long")) {
          addViolation(data, node);
      }
      return super.visit(node, data);

    }

    private Object variableDeclarator(ASTBlockStatement node, Object data){

      //cannot be null
      ASTVariableDeclarator variableDeclarator = node.getFirstDescendantOfType(ASTVariableDeclarator.class);

      //only consider in case the value is assigned to a double or float
      if(    variableDeclarator == null || variableDeclarator.getType() == null ||
              !(
                variableDeclarator.getType().getName().equals("double")
                ^ variableDeclarator.getType().getName().equals("float")
              )
      ){
        return super.visit(node, data);
      }

      if( ! variableDeclarator.hasDescendantMatchingXPath("//MultiplicativeExpression[@Image = '/']")){
        return super.visit(node, data);
      }

      ASTMultiplicativeExpression multExpression =
        variableDeclarator.getFirstDescendantOfType(ASTMultiplicativeExpression.class);

      if(multExpression == null)
        return super.visit(node, data);

      if (multExpression.getType().getName().equals("int") || multExpression.getType().getName().equals("long")) {
          addViolation(data, node);
      }
      return super.visit(node, data);
    }

}
