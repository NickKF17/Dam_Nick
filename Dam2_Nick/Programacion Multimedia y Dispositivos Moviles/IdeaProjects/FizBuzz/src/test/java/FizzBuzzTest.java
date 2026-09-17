import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FizzBuzzTest {
    FizzBuzz fb;
    @BeforeEach
    public void setup() {
      fb = new FizzBuzz();

    }

    @Test
    public void pintoUnNumero(){
        Assertions.assertEquals("1",fb.print(1));
        Assertions.assertEquals("2",fb.print(2));
        Assertions.assertEquals("4",fb.print(4));
        Assertions.assertEquals("38",fb.print(38));

    }
    @Test
    public void hacenFizz(){

        Assertions.assertEquals("Fizz",fb.print(3));
        Assertions.assertEquals("Fizz",fb.print(6));
        Assertions.assertEquals("Fizz",fb.print(9));

    }
    @Test
    public void hacenBuzz(){

        Assertions.assertEquals("Buzz",fb.print(5));
        Assertions.assertEquals("Buzz",fb.print(10));
    }
    @Test
    public void hacenFizzBuzz(){
        Assertions.assertEquals("FizzBuzz",fb.print(15));
    }
}
