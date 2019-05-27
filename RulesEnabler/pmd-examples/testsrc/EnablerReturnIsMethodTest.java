public class EnablerReturnIsMethodTest {
    private String foo;

    private int violation1(){
      return (42);
    }

    public static boolean violation2(){
      return (12<23);
    }

    private String violation3(){
      return "noViolation";
    }
}
