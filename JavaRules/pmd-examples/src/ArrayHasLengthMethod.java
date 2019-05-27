import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.symboltable.ScopedNode;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;
import net.sourceforge.pmd.lang.java.symboltable.ClassScope;
import net.sourceforge.pmd.lang.java.symboltable.LocalScope;
import net.sourceforge.pmd.lang.java.symboltable.MethodScope;
import net.sourceforge.pmd.lang.symboltable.Scope;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.jaxen.JaxenException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class ArrayHasLengthMethod extends AbstractJavaRule{
    @Override
    public Object visit(ASTBlockStatement node, Object data) {

        try{

          //get all uses of .size() and .length()
          List<Node> uses = node.findChildNodesWithXPath(".//PrimaryExpression[ ./PrimaryPrefix/Name[fn:ends-with(@Image,'.length') or fn:ends-with(@Image,'.size')] and ./PrimarySuffix[@ArgumentCount = 0] ]/PrimaryPrefix/Name");

          verify(uses,data);

          //uses =  this.something.size() or this.something.length()
          uses = node.findChildNodesWithXPath(".//PrimaryExpression[ ./PrimaryPrefix[@ThisModifier = 'true'] and ./PrimarySuffix[2][ @Image = 'length' or @Image = 'size'] and ./PrimarySuffix[3][@ArgumentCount = 0] ]/PrimarySuffix[1]");

          verify(uses,data);

        }
        catch(JaxenException | NullPointerException e ){
          return super.visit(node, data);
        }
        return super.visit(node, data);
    }

    private static boolean isViolation(NameDeclaration d, Node use){
      String name = substringBefore(use.getImage());
      boolean result = name.equals(d.getNode().getImage());
      return result && ((AbstractJavaTypeNode)d.getNode()).getType().isArray();
    }

    private static String substringBefore(String s){
      if(s == null)
        return s;
      int index = s.indexOf('.');
      if(index == -1)
        return s;
      return s.substring(0,index);
    }

    private void verify(List<Node> uses, Object data){
      for(Node use : uses){
        if(use == null)
          continue;

        List<Scope> all_scopes = new ArrayList<>();
        all_scopes.add(((ScopedNode)use).getScope());
        int index = 0;
        while(all_scopes.get(index).getParent() != null){
          all_scopes.add(all_scopes.get(index).getParent());
          index++;
        }
        out_to_next:
        for(Scope scope : all_scopes){
          Map<NameDeclaration,List<NameOccurrence>> delar = scope.getDeclarations();
          for(NameDeclaration d : delar.keySet()){
            if(isViolation(d,use)){
              addViolation(data, use);
              break out_to_next;
            }
          }
        }

      }
    }

}
