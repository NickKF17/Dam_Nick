import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FizzBuzzTest {
    @Test
    public void puedoHacerFizzBuzz() {
        FizzBuzz fb = new FizzBuzz();
    }
    @Test
    public void pintoUnNumero() {
        FizzBuzz fb = new FizzBuzz();
        Assertions.assertEquals("1", fb.print(1));
        Assertions.assertEquals("2", fb.print(2));
        Assertions.assertEquals("4", fb.print(4));

    }
    @Test
    public void hacenFizz() {
        FizzBuzz fb = new FizzBuzz();
        Assertions.assertEquals("Fizz", fb.print(3));
        Assertions.assertEquals("Fizz", fb.print(6));
    }
    @Test
    public void hacenBuzz() {
        FizzBuzz fb = new FizzBuzz();
        Assertions.assertEquals("Buzz", fb.print(5));
        Assertions.assertEquals("Buzz", fb.print(10));
    }
}
