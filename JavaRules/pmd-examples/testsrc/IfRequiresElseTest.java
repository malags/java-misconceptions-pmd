public class IfRequiresElse {

  int count = 0;

  private void violation1(int v1){
    if (v1 > 0) {
      count = count + 1;
    }
    else {
      // else branch is not necessary
      count = count;
    }
  }

  private void violation2(int v2){
    if (v2==value) {
      count = count + 1;
    }
    else {
      // else branch is not necessary
    }
  }

  private void noViolation1(int nv1){
    if (nv1==value) {
      count = count + 1;
    }
    else {
      count++;
    }
  }


  private void noViolation2(int nv2){
    if (nv2==value) {
      count = count + 1;
    }
  }

  private void noViolation3(int nv2){
    if (contains (newX, newY)) {
      return null;
    }
    else{
      return new Tail(newX, newY, this);
    }
  }
}
