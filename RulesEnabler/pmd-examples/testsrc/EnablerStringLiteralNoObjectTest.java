public class StringLiteralNoObjectTest {
    private String foo;

    private void violation1(String s){
      s = "s";              //violation
    }

    public void violation2(){
      String s = "a";       //violation
      s.reverse();
      s.reverse();
    }

    private String noViolation(){
      return "noViolation";
    }
}
