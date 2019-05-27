import java.util.ArrayList;
public class Foo {
    public void violation1() {
       ArrayList<Integer> asd = new Integer[3];             //violation
       int[][] a = new ArrayList<ArrayList<Integer>>();     //violation

       ArrayList<String> arL = new ArrayList<String>();
       arL[0] = "Hi";                                       //violation

       this.arL[3] = 12;
    }


    public void violation2(){
      int[][] a = new int[2][2];
      a[3] = new int[3];
      a[2][3] = 12;
      a.get(2);                                             //violation
    }

    public void noViolation(){
      int[][] a = new int[2][2];
      a[3] = new int[3];
      a[2][3] = 12;
    }
}
