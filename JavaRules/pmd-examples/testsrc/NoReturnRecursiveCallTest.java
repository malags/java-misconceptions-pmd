public class NoReturnRecursiveCallTest {
    private String foo;
    private boolean noViolation2 = false;

    public int violation1(int n) {
          if(n <= 1){
              return 1;
          }
          violation1(n-1);
        }

    public int violation2(int n) {
          if(n <= 1){
              n--;
          }
          return violation2(n-1);
        }

    public static boolean noViolation(boolean b){
      if(!b)
        return noViolation(!b);
      return b;
    }

    public static boolean noViolation2(boolean b){
      if(!b)
        return noViolation2;
      return b;
    }

}
