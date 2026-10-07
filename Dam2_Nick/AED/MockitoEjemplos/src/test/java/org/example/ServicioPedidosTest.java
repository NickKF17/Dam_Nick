package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioPedidosTest {
    @Mock
    private Inventario inventario;

    @Mock
    private Notificador notificador;

    @InjectMocks
    private ServicioPedidos servicioPedidos;

    @Test
    void mockBasicoPermiteControlarUnaDependencia() {
        when(inventario.hayStock("LIBRO-1", 2)).thenReturn(true);

        assertTrue(inventario.hayStock("LIBRO-1", 2));
    }

    @Test
    void procesaPedidoCuandoHayStockYNotificaAlCliente() {
        Pedido pedido = new Pedido("LIBRO-1", 2, "ana@example.com");
        when(inventario.hayStock("LIBRO-1", 2)).thenReturn(true);

        boolean procesado = servicioPedidos.procesar(pedido);

        assertTrue(procesado);
        verify(inventario).hayStock("LIBRO-1", 2);
        verify(notificador).enviar(
                "ana@example.com",
                "Pedido confirmado: 2 unidad(es) de LIBRO-1"
        );
    }

    @Test
    void noNotificaCuandoNoHayStock() {
        Pedido pedido = new Pedido("LIBRO-2", 5, "ana@example.com");
        when(inventario.hayStock("LIBRO-2", 5)).thenReturn(false);

        assertFalse(servicioPedidos.procesar(pedido));

        verify(inventario).hayStock("LIBRO-2", 5);
        verify(notificador, never()).enviar(anyString(), anyString());
    }

    @Test
    void propagaLaExcepcionDeLaDependencia() {
        Pedido pedido = new Pedido("LIBRO-3", 1, "ana@example.com");
        when(inventario.hayStock("LIBRO-3", 1))
                .thenThrow(new IllegalStateException("Inventario no disponible"));

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> servicioPedidos.procesar(pedido)
        );

        assertEquals("Inventario no disponible", excepcion.getMessage());
        verify(notificador, never()).enviar(anyString(), anyString());
    }

    @Test
    void capturadorDeArgumentosPermiteInspeccionarLaLlamada() {
        Pedido pedido = new Pedido("LIBRO-4", 3, "luis@example.com");
        when(inventario.hayStock("LIBRO-4", 3)).thenReturn(true);

        servicioPedidos.procesar(pedido);

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> mensajeCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificador).enviar(emailCaptor.capture(), mensajeCaptor.capture());

        assertEquals("luis@example.com", emailCaptor.getValue());
        assertEquals("Pedido confirmado: 3 unidad(es) de LIBRO-4", mensajeCaptor.getValue());
    }
}
