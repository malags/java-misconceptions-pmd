public class EnablerNoReturnRecursiveCallTest {
    private String foo;

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
        return something(!b);
      return b;
    }

}
