package org.example;

public class Calculadora {
    public int sumar(int a, int b) {
        return a + b;
    }

    public int restar(int a, int b) {
        return a - b;
    }

    public int multiplicar(int a, int b) {
        return a * b;
    }

    public double dividir(double dividendo, double divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("El divisor no puede ser cero");
        }
        return dividendo / divisor;
    }

    public boolean esPar(int numero) {
        return numero % 2 == 0;
    }

    public String describirResultado(int resultado) {
        return "resultado: " + resultado + System.lineSeparator() + "estado: correcto";
    }
}
