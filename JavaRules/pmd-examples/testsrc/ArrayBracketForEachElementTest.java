public class ArrayBracketForEachElementTest {
    private String foo;

    private void violation1(){
      int[] a = new int[3][2];  //violation
      a.something();
      a.something();
    }

    private void violation2(){
      Integer[][] a = new Integer[3];  //violation
      a.something();
      a.something();
    }

    private void violation3(){
      Integer[][] a = new Integer[3][2];

      a[2] = new Integer[1][2];  //violation
      a.something();
    }

    private void violation3(){
      Integer[][] a;

      a = new Integer[1];  //violation
      a.something();
    }

    private void violation4(){
      Integer[][] a = new Integer[3][2];

      a = new Integer[1];  //violation
      a.something();
    }


    private void noViolation1(){
      Integer[][] a = new Integer[3][2];
      a[1] = new Integer[2];
      a[1][1] = new Integer(2);
      a = new Integer[3][2];
    }

    private void noViolation2(){
      Integer[][][] a = new Integer[3][2][2];

      a[2][2] = new Integer[1];
      a[2][2][2] = a.something();
    }


}
