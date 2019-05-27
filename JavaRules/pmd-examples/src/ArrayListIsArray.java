import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;


public class ArrayListIsArray extends AbstractJavaRule{
    @Override
    public Object visit(ASTBlock node, Object data) {

        try{

          //use expression for simple cases
          List<Node> violations = node.findChildNodesWithXPath(".//LocalVariableDeclaration[ (./Type[@TypeImage = 'ArrayList'] and ./VariableDeclarator//ArrayDimsAndInits) or (./Type[@ArrayType = 'true'][ not(@TypeImage = 'ArrayList')] and ./VariableDeclarator//ClassOrInterfaceType[@Image = 'ArrayList']) ]");
          //there are violations
          if(violations.size() != 0){
            //report each line
            for (Node n : violations){
              addViolation(data, n);
            }
          }



          // of type a[n]
          List<Node> arrayUses = node.findChildNodesWithXPath(".//PrimaryExpression[ ./PrimaryPrefix and ./PrimarySuffix/Expression]/PrimaryPrefix/Name");
          for(Node name : arrayUses){
            if(isViolation(name)){
              addViolation(data, name);
            }
          }

          //of type this.a[n]
          List<Node> arrayUses2 = node.findChildNodesWithXPath(".//PrimaryExpression[ ./PrimaryPrefix[@ThisModifier = 'true'] ]/PrimarySuffix[1][ (not(./Expression)) and (@Arguments = 'false')]");
          //  node.findChildNodesWithXPath(".//PrimaryExpression[ ./PrimarySuffix[@ArrayDereference = true()] ][./PrimarySuffix[@ArrayDereference = false() and not(@image = 'null')] or ./PrimaryPrefix/Name]");
          for(Node name : arrayUses2){
            if(isViolation(name)){
              addViolation(data, name);
            }
          }

          //
          List<Node> arrayGet = node.findChildNodesWithXPath(".//Name[contains(@Image, '.get')]");
          for(Node name : arrayGet){
            if((name != null) &&  ((AbstractJavaTypeNode)name).getType().isArray())
              addViolation(data, name);
          }
        }
        catch(JaxenException | NullPointerException e ){
          return super.visit(node, data);
        }
        return super.visit(node, data);
    }

    private boolean isViolation(Node name){
      return (name != null) && (! ((AbstractJavaTypeNode)name).getType().isArray()) && ! name.getImage().contains(".");
    }

}
