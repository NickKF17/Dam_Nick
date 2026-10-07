# Ejemplos de Mockito con JUnit 5

Proyecto didáctico con un servicio pequeño de pedidos y pruebas que sustituyen sus dependencias por mocks. Para ejecutar todas las pruebas:

```bash
mvn test
```

## Conceptos

- **Mock básico y stubbing** — `mockBasicoPermiteControlarUnaDependencia` configura una respuesta con `when(...).thenReturn(...)`. Un mock no ejecuta la implementación real de la dependencia.
- **Verificación de interacciones** — `procesaPedidoCuandoHayStockYNotificaAlCliente` usa `verify(...)` para comprobar llamadas; `noNotificaCuandoNoHayStock` usa `never()` para asegurar que una interacción no ocurrió.
- **Simulación de excepciones** — `propagaLaExcepcionDeLaDependencia` configura `thenThrow(...)` y comprueba la excepción con `assertThrows`.
- **Anotaciones con JUnit 5** — `@ExtendWith(MockitoExtension.class)` inicializa `@Mock` y crea `ServicioPedidos` con esas dependencias mediante `@InjectMocks`.
- **Captura de argumentos** — `capturadorDeArgumentosPermiteInspeccionarLaLlamada` usa `ArgumentCaptor` para comprobar los valores enviados al notificador.

## Clases del ejemplo

- `Pedido` contiene los datos de la solicitud.
- `Inventario` y `Notificador` son dependencias definidas como interfaces.
- `ServicioPedidos` coordina la comprobación de stock y el envío de la confirmación.
- `ServicioPedidosTest` enseña los conceptos de Mockito con pruebas aisladas, sin servicios externos.
