# CalculadoraFinanceira

## Descripción

Biblioteca Java para realizar cálculos financieros habituales: interés simple y compuesto, valor presente y futuro, cuotas de préstamos, VAN y TIR. Usa `BigDecimal` para evitar errores de redondeo.

## Instalación

### Maven

```xml
<dependency>
    <groupId>com.ejemplo</groupId>
    <artifactId>calculadora-financeira</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Gradle

```groovy
dependencies {
    implementation 'com.ejemplo:calculadora-financeira:1.1.0'
}
```

## Ejemplo de uso básico

```java
import com.ejemplo.financeira.CalculadoraFinanceira;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // 10.000 € al 5 % anual durante 3 años
        BigDecimal valorFinal = CalculadoraFinanceira.interesCompuesto(
                new BigDecimal("10000"), new BigDecimal("0.05"), 3);
        System.out.println(valorFinal); // 11576.25
    }
}
```

## Métodos disponibles

| Método | Parámetros | Retorno | Descripción |
|--------|-----------|---------|-------------|
| `interesSimple` | `capital`, `tasa`, `periodos` | `BigDecimal` | Interés con capitalización simple. |
| `interesCompuesto` | `capital`, `tasa`, `periodos` | `BigDecimal` | Valor final con capitalización compuesta. |
| `valorFuturo` | `valorPresente`, `tasa`, `periodos` | `BigDecimal` | Valor futuro de una cantidad actual. |
| `valorPresente` | `valorFuturo`, `tasa`, `periodos` | `BigDecimal` | Valor actual de una cantidad futura. |
| `cuotaPrestamo` | `principal`, `tasaAnual`, `meses` | `BigDecimal` | Cuota mensual constante de un préstamo. |
| `van` | `tasa`, `flujos...` | `BigDecimal` | Valor Actual Neto. |
| `tir` | `flujos...` | `BigDecimal` | Tasa Interna de Retorno. |

## Licencia

Este proyecto se distribuye bajo la licencia **MIT**.
