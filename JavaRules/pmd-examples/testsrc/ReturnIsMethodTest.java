public class ReturnIsMethodTest {
    private String foo;

    private int violation1(){
      return (42);
    }

    public static boolean violation2(){
      return (12<23);
    }

    private String noViolation(){
      return "noViolation";
    }
}
