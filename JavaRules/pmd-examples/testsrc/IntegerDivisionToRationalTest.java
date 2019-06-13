public class IntegerDivisionToRationalTest {
    private String foo;

    private double violation1(){
      double d1 = 42/3;
      d1 /= 2;;
      return d1;
    }

    private double violation2(){
      double d2 = 12.0;
      d2 = 1/2;
      return d2;
    }

    private float violation3(){
      float f1 = 42/3;
      f1 += 1;
      return f1;
    }

    private float violation4(){
      float f2 = 1f;
      f2 = 1/2;
      return f2;
    }

    private float noViolation1(){
      float f3 = 12f;
      f3 = 2.0/3;
      return f3;
    }

}
