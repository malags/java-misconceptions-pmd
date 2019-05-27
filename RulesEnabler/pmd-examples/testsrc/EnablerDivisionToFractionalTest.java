public class EnablerDivisionToFractionalTest {
    private String foo;

    private double violation1(){
      double i = 42/3;              //violation
      something(i);
      return i;
    }


    private float violation2(){
      float i = 42/3;               //violation
      something(i);
      return i;
    }

    private float noViolation1(){
      float f = 12f;
      f = 2.0/3;
      return f;
    }

}
