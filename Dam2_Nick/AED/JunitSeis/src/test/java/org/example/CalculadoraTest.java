package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {
    private static int pruebasEjecutadas;
    private Calculadora calculadora;

    @BeforeAll
    static void antesDeTodasLasPruebas() {
        pruebasEjecutadas = 0;
    }

    @BeforeEach
    void antesDeCadaPrueba() {
        calculadora = new Calculadora();
    }

    @AfterEach
    void despuesDeCadaPrueba() {
        pruebasEjecutadas++;
        calculadora = null;
    }

    @AfterAll
    static void despuesDeTodasLasPruebas() {
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
    }

    @Test
    void demuestraAssertionsHabituales() {
        assertEquals(5, calculadora.sumar(2, 3));
        assertNotEquals(6, calculadora.sumar(2, 3));
        assertTrue(calculadora.esPar(4));
        assertFalse(calculadora.esPar(3));

        String valorNulo = null;
        assertNull(valorNulo);
        assertNotNull(calculadora);

        Calculadora mismaReferencia = calculadora;
        assertSame(calculadora, mismaReferencia);
        assertNotSame(calculadora, new Calculadora());

        assertArrayEquals(new int[]{2, 3, 5}, new int[]{2, 3, calculadora.sumar(2, 3)});
        assertIterableEquals(List.of(2, 5, 3), List.of(
                calculadora.sumar(1, 1),
                calculadora.sumar(2, 3),
                calculadora.restar(5, 2)));
        assertLinesMatch(
                List.of("resultado: 5", "estado: .*"),
                calculadora.describirResultado(5).lines().toList());

        assertAll("operaciones",
                () -> assertEquals(1, calculadora.restar(3, 2)),
                () -> assertEquals(12, calculadora.multiplicar(3, 4)),
                () -> assertEquals(2.5, calculadora.dividir(5, 2)));

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> calculadora.dividir(10, 0));
        assertEquals("El divisor no puede ser cero", excepcion.getMessage());
        assertThrowsExactly(IllegalArgumentException.class, () -> calculadora.dividir(1, 0));

        assertDoesNotThrow(() -> calculadora.dividir(10, 2));
        assertTimeout(Duration.ofSeconds(1), () -> calculadora.sumar(20, 22));
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> calculadora.sumar(20, 22));
        assertInstanceOf(IllegalArgumentException.class, excepcion);
    }

    @ParameterizedTest
    @ValueSource(ints = {-4, -2, 0, 2, 4})
    void reconoceNumerosPares(int numero) {
        assertTrue(calculadora.esPar(numero));
    }

    @ParameterizedTest
    @CsvSource({"1, 2, 3", "-2, 5, 3", "10, 0, 10"})
    void sumaValoresCsv(int a, int b, int esperado) {
        assertEquals(esperado, calculadora.sumar(a, b));
    }

    @ParameterizedTest
    @MethodSource("casosDeDivision")
    void divideConCasosProporcionados(int dividendo, int divisor, double esperado) {
        assertEquals(esperado, calculadora.dividir(dividendo, divisor));
    }

    private static Stream<Arguments> casosDeDivision() {
        return Stream.of(
                Arguments.of(10, 2, 5.0),
                Arguments.of(9, 3, 3.0),
                Arguments.of(7, 2, 3.5));
    }
}
