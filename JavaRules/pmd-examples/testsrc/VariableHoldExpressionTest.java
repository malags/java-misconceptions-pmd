public class VariableHoldExpressionTest {
    private String foo;

    private Integer violation1(){
      int i = 42;          //violation
      return i;
    }

    private int violation2(){
      int i = 12 + 1234;   //violation
      return i;
    }

    private int violation2(){
      int i;               //violation
      i = 12 + 321;
      return i;
    }

    private int noViolation1(){
      return 42;
    }


    private boolean noViolation2(){
      boolean b = true;
      b = b || !b;
      return b;
    }



}
