import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTArrayDimsAndInits;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;


public class ArrayBracketForEachElement extends AbstractJavaRule{
    @Override
    public Object visit(ASTBlockStatement node, Object data) {

        try{
          //check case where size of initialiser is different from declaration
          List<Node> violation = node.findChildNodesWithXPath(".//LocalVariableDeclaration[.//VariableInitializer][@Array = 'true'][ .//ReferenceType/@ArrayDepth != count(.//ArrayDimsAndInits/Expression) ]");
          if(violation.size() > 0){
            addViolation(data,node);
          }

          //get all cases in which we assign an array to something
          //contains both ./PrimaryPrefix  [./PrimarySuffix]+
          //and
          // ./PrimaryPrefix/Name
          List<Node> possibleViolations = node.findChildNodesWithXPath("./Statement/StatementExpression[./AssignmentOperator]/PrimaryExpression");
          if( possibleViolations.size() == 0 ){
            return super.visit(node, data);
          }

          Node target = possibleViolations.get(0);

          Node child = target.getFirstChildOfType(ASTPrimaryPrefix.class);

          // ./PrimaryPrefix/Name must remove suffixNr
          if(child != null){
            ASTName name = child.getFirstChildOfType(ASTName.class);
            int derefTimes = target.findChildrenOfType(ASTPrimarySuffix.class).size();
            if(name == null)
              return super.visit(node, data);
            Class typeOfName = ((AbstractJavaTypeNode) name ).getType();

            //iterate to compare type of element with value that is assigned to it
            while(derefTimes > 0){
              derefTimes--;
              if(typeOfName == null)
                return super.visit(node, data);
              typeOfName = typeOfName.getComponentType();
            }

            if(typeOfName == null)
              return super.visit(node, data);

              // prevent anything with . see: https://sourceforge.net/p/pmd/bugs/803/
              /*
              when fixed
              use
              if(! typeOfName.getName().equals(((ASTExpression)node.findChildNodesWithXPath("./Statement/StatementExpression/Expression").get(0)).getType().getName()) ){
                addViolation(data, node);
              }

              instead
              */
            List<Node> right = node.findChildNodesWithXPath("./Statement/StatementExpression/Expression[not(.//Name[contains(@Image,'.')])]");
            if(right.size() == 0)
              return super.visit(node, data);

            if(typeOfName == null || right.get(0) == null || ((ASTExpression)right.get(0)).getType() == null)
              return super.visit(node, data);
            //right side must be array
            if(!((ASTExpression)right.get(0)).getType().isArray())
              return super.visit(node, data);

            if(! typeOfName.getName().equals( ((ASTExpression)right.get(0)).getType().getName() ) ){
              addViolation(data, node);
            }
            //end workaround


            return super.visit(node, data);
          }


        }
        catch(JaxenException e){
          e.printStackTrace();
          return super.visit(node, data);
        }
        return super.visit(node, data);
    }

}
