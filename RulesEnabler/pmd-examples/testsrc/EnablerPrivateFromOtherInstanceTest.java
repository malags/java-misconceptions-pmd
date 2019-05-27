public class EnablerPrivateFromOtherInstanceTest {

  private int violation1;
  private boolean violation2;
  private boolean noViolation1;
  private boolean noViolation2;


  public int getViolation1(){
    return violation1;
  }

  public boolean getViolation2(){
    return this.violation2;
  }

  public boolean getNoViolation1(){
    return !this.noViolation1;
  }

  public boolean getNoViolation2(){
    return !noViolation2;
  }


}
