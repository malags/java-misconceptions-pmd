public class LocalVariablesAutoInitializedTest {

  int count = 0;

  private void violation1(){
    int i;        //violation
    i++;
  }

  private void violation2(){
    String s;     //violation
    s.reverse();
    s = "";
  }

  private void violation3(){
    String s;     //violation
    s.reverse();
    s.reverse();
  }

  private void violation4(){
    int s;
    int b;        //violation
    s = b;
  }


  private void violation5(){
    int i;        //violation
    --i;
  }

  private void violation6(){
    int i;        //violation
    ++i;
  }

  private void noViolation1(){
    int i = 2;
    ++i;
    i++;
  }

  private void noViolation2(){
    String s = "file";
    s.reverse();
    s.reverse();
  }

  private void noViolation3(int[] ints){
    int sum = 0;
    for(int i : ints)
      sum += i;
    sum++;
  }

  private void noViolation4(int v){
    int s = v;
    s++;
    s++;
  }

}
