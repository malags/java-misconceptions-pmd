public class ArrayElementsUntypedTest {
    private String foo;

    private void violation1(){
      int[] a = new int[3];
      a[0] = "Hi"; // type error
      a[1] = 12;
      a[2] = 13.5; // type error
    }

    private void violation2(){
      Integer[] a = new Integer[3];
      a[1] = 12; // type error
      ints[1] = 0;
    }

    private void violation3(){
      int[] a = new int[3];
      a[1] = 12.0/4; // type error
      a[1] = 0;
    }

    private void violation3(){
      int[][] ints = new ints[1][1];
      ints[1][0] = 0.5; // type error
      ints[1][0] = 0;
    }


    private void noViolation1(){
      float f3 = 12f;
      f3 = 2.0/3;
      return f3;
    }

    private void noViolation2(){
      int[][] a = new int[1][1];
      a[0][0] = 12;
      a[0][0] = 1;
    }

    private void noViolation3(){
      int[][] no_violation3 = new int[1][1];
      no_violation3[1][0] = no_violation3.length;
      no_violation3[1][0] = "abc".length();
    }

}
