import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private Account account;
    private String today;
    private String header = "Date        Amount  Balance";

    @BeforeEach
    void setUp() {
        account = new Account();
        today = LocalDate.now().format(DateTimeFormatter.ofPattern("d.M.yyyy"));
    }

    // Crea una línea del extracto con la fecha de hoy
    private String line(String amount, int balance) {
        return String.format("%-10s %6s %8s", today, amount, balance);
    }

    @Test
    void cuentaVaciaSoloMuestraLaCabecera() {
        assertEquals(header, account.printStatement());
    }

    @Test
    void ingresoSeMuestraConSignoMas() {
        account.deposit(500);

        String expected = header + "\n" + line("+500", 500);
        assertEquals(expected, account.printStatement());
    }

    @Test
    void retiradaSeMuestraConSignoMenos() {
        account.deposit(500);
        account.withdraw(100);

        String expected = header + "\n"
                + line("+500", 500) + "\n"
                + line("-100", 400);
        assertEquals(expected, account.printStatement());
    }

    @Test
    void elSaldoSeAcumulaConVariosMovimientos() {
        account.deposit(1000);
        account.withdraw(300);
        account.deposit(50);

        String expected = header + "\n"
                + line("+1000", 1000) + "\n"
                + line("-300", 700) + "\n"
                + line("+50", 750);
        assertEquals(expected, account.printStatement());
    }

    @Test
    void ingresoConImporteCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
    }

    @Test
    void ingresoNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-10));
    }

    @Test
    void retiradaConImporteCeroOMenorLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-5));
    }

    @Test
    void unaOperacionRechazadaNoApareceEnElExtracto() {
        try {
            account.deposit(-10);
        } catch (IllegalArgumentException e) {
            // esperado
        }

        assertEquals(header, account.printStatement());
    }
}