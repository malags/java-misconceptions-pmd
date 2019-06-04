import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;


public class LocalVariablesAutoInitialized extends AbstractJavaRule{
    @Override
    public Object visit(ASTBlock node, Object data) {


        try{
          //take all declarations without initialisation and not for local variables
          List<Node> declarations = node.findChildNodesWithXPath(".//VariableDeclarator[not(./VariableDeclaratorId and ./VariableInitializer)][not( ./parent::LocalVariableDeclaration/parent::ForStatement )]/VariableDeclaratorId");

          //no uninitialised variables at declaration
          if (declarations.size() == 0)
            return super.visit(node, data);


          for(Node declaration : declarations){
            //prevent multiple violations for one declaration
            boolean not_signaled = true;
            String name = declaration.getImage();

            List<Node> assignments = node.findChildNodesWithXPath(".//StatementExpression[./PrimaryExpression/PrimaryPrefix/Name[@Image = \'"+name+"\'] and ./AssignmentOperator ]");

            int pos_initialise = Integer.MAX_VALUE;
            for(Node assignment : assignments)
              if(assignment.getBeginLine() < pos_initialise)
                pos_initialise = assignment.getBeginLine();

            //expression -> right value, also ++ and -- casesxs
            List<Node> usesName = node.findChildNodesWithXPath(".//Expression//Name[@Image ='" + name +"'] | .//PreDecrementExpression//Name | .//PreIncrementExpression//Name | .//PostfixExpression//Name");
            if(assignments.size() == 0 && usesName.size() > 0){
              addViolation(data, declaration);
              continue;
            }

            //use before declaration
            for(Node use : usesName){
              if(not_signaled && use.getBeginLine() < pos_initialise){
                addViolation(data, declaration);
                not_signaled = false;
                break;
              }
            }

            //check for . cases
            usesName = node.findChildNodesWithXPath(".//PrimaryPrefix/Name[fn:substring-before(@Image,'.') = '"+name+"']");
            if(usesName.size() == 0)
              continue;

            for(Node use : usesName){
              if(not_signaled && use.getBeginLine() < pos_initialise){
                addViolation(data, declaration);
                not_signaled = false;
                break;
              }
            }


          }

        }
        catch(Exception e){
          e.printStackTrace();
          return super.visit(node, data);
        }
        return super.visit(node, data);
    }

}
