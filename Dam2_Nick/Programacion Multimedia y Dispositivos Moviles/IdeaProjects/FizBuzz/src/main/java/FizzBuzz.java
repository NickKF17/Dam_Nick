public class FizzBuzz {

  public String print(int numero){
    if(multiploCinco(numero)&&multiploTres(numero)){
      return "FizzBuzz";
    }
    if(multiploCinco(numero)){
      return "Buzz";
    }
    if(multiploTres(numero)) {
      return "Fizz";
    }
    return numero+"";
  }

  private boolean multiploTres(int numero){
    return numero % 3==0;
  }

  private boolean multiploCinco(int numero){
    return numero % 5==0;
  }
}
