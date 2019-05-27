public class ObjectsMustBeNamedTest {
    private String foo;

    private Integer violation1(){
      Integer i = 42;
      return i;
    }

    public static String violation2(){
      String s = "violation1";
      return s;
    }

    private String noViolation1(){
      return "noViolation";
    }

    private String noViolation2(){
      String s = "violation1";
      s.reverse();
      return s;
    }

    private String noViolation3(){
      int i = 12;
      i++;
      return i;
    }
}
