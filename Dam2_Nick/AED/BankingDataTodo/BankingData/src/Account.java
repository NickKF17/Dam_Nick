import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Account {

    private int balance;
    private ArrayList<String> lines;

    public Account() {
        balance = 0;
        lines = new ArrayList<>();
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo");
        }
        balance = balance + amount;
        addLine("+" + amount);
    }

    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo");
        }
        balance = balance - amount;
        addLine("-" + amount);
    }

    public String printStatement() {
        String statement = "Date        Amount  Balance";
        for (String line : lines) {
            statement = statement + "\n" + line;
        }
        return statement;
    }

    // Crea la línea del extracto con la fecha de hoy, el importe y el saldo actual
    private void addLine(String amountText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String date = LocalDate.now().format(formatter);
        lines.add(String.format("%-10s %6s %8s", date, amountText, balance));
    }
}