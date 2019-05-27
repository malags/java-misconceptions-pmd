public class EnablerNoAnonymousObjectsAllowedTest {
    private String foo;

    private int violation1(){
      int i = 42;
      return i;
    }

    public static String violation2(){
      String s = "violation1";
      return s;
    }

    private String noViolation1(){
      return "noViolation";
    }

}
