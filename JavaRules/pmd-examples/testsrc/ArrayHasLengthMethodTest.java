public class ArrayHasLengthMethodTest {
    private String foo;
    private int[] noscam;
    private int[] noscam2;
    private List<Integer> scam;
    private List<Integer> scam2;
    private int[] a1 = new int[2];

    private void violation1(int[] a1){
      a1[0] = 1;
      a1[1] = this.a1.length();                       //violation
    }

    private void violation2(){
      int a2[] = new int[2];
      a2[0] = 1;
      a2[1] = a2.size();                              //violation
      a2[1] = a2.length();                            //violation
      for(int i = 0 ; i < a2.length(); ++i){          //violation
        a2[i] = i;
      }
    }


    private void violation3(){
      for(int i = 0 ; i < this.noscam.length();++i){    //violation
        scam[i] = i;
      }

      for(int i = 0 ; i < noscam.length();++i){         //violation
        scam[i] = i;
      }

      for(int i = 0 ; i < this.noscam2.size();++i){     //violation
        scam[i] = i;
      }

      for(int i = 0 ; i < noscam2.size();++i){          //violation
        scam[i] = i;
      }

    }


    private void noViolation1(){
      int a3[] = new int[2];
      a3[0] = 1;
      a3[1] = a3.length;

      for(int i = 0 ; i < this.scam.size(); ++i){
        scam.set(i,i);
      }

      for(int i = 0 ; i < scam2.size(); ++i){
        scam.set(i,i);
      }

    }



}
