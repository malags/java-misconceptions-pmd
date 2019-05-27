import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.ast.Node;
import org.jaxen.JaxenException;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import java.util.List;

public class ObjectsMustBeNamed extends AbstractJavaRule {

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {

      int count = 0;
      List<ASTName> names = node.findDescendantsOfType(ASTName.class);

      try{
        List<Node> variables = node.findChildNodesWithXPath(".//BlockStatement/LocalVariableDeclaration//VariableDeclaratorId");
        for(Node variable : variables){
          if(variable == null)
            continue;
          if(((AbstractJavaTypeNode)variable).getType() == null)
            continue;
          if(((AbstractJavaTypeNode)variable).getType().isPrimitive())
            continue;
          count = 0;
          for(ASTName name : names){
            if(variable.getImage().equals(substringBefore(name.getImage())))
              count++;
          }
          if(count < 2)
            addViolation(data,variable);
        }
      }
      catch(JaxenException e){
        e.printStackTrace();
      }

      return super.visit(node, data);
    }


    private String substringBefore(String s){
      if(s == null)
        return s;
      int index = s.indexOf('.');
      if(index == -1)
        return s;
      return s.substring(0,index);
    }
}
