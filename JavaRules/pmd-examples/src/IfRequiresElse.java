import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTStatement;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;

public class IfRequiresElse extends AbstractJavaRule {

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        //need an else statement
        if(nrChildren(node) != 3)
          return super.visit(node, data);

        try{
          //take second statement
          List<Node> statements = node.findChildNodesWithXPath("./Statement[2][not(./IfStatement)]");

          //should have exactly 1 result
          if(statements.size() != 1)
            return super.visit(node, data);

          ASTStatement statement = (ASTStatement) statements.get(0);
          //no content in block
          if(!(statement.hasDescendantOfType(ASTBlockStatement.class) || statement.hasDescendantOfType(ASTReturnStatement.class))){
            addViolation(data, node);
            return super.visit(node, data);
          }

          //take all block statements inside else
          List<Node> blockStatements = statement.findChildNodesWithXPath("./Block/BlockStatement");

          //shouldn't occur when more than 1 block statement in else
          if(blockStatements.size() != 1 )
            return super.visit(node, data);


          //check assignment to self and 1 element
          //has assignment?
          if(!blockStatements.get(0).hasDescendantOfType(ASTAssignmentOperator.class))
            return super.visit(node, data);

          //can't happen if returns
          if(blockStatements.get(0).hasDescendantOfType(ASTReturnStatement.class))
            return super.visit(node, data);

          //check 10 children (faster this way)
          if(blockStatements.get(0).findDescendantsOfType​(Node.class).size() != 10)
            return super.visit(node, data);

          //check has two descendants of type ASTName
          List<ASTName> names = blockStatements.get(0).findDescendantsOfType​(ASTName.class);
          if(names.size() != 2)
            return super.visit(node, data);

          //check same name
          if(names.get(0).getImage().equals(names.get(1).getImage()))
            addViolation(data, node);

          //---------------//

        }
        catch(JaxenException e){
          e.printStackTrace();
          return super.visit(node, data);
        }

        return super.visit(node, data);
    }

    private int nrChildren(Node n){
      return n.findChildrenOfType​(Node.class).size();
    }

}
