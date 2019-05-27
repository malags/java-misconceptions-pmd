public class PrivateFromOtherInstanceTest {

  private int key;
  private boolean bool;
  private boolean scam;

  public int getKey(){
    return key;
  }

  public boolean getBool(){
    return this.bool;
  }

  public boolean getScam(){
    return !this.bool;
  }

  public boolean violation1(PrivateFromOtherInstanceTest other) {
    return this.key == other.getKey();
  }

  public boolean violation2(PrivateFromOtherInstanceTest other) {
    return this.bool == other.getBool();
  }

  public boolean noViolation(PrivateFromOtherInstanceTest other) {
    return this.key == other.key;
  }

  public boolean noViolation1(PrivateFromOtherInstanceTest other) {
    return this.bool == other.getScam();
  }

}
