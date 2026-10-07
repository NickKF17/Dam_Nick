package org.example;

public class ServicioPedidos {
    private final Inventario inventario;
    private final Notificador notificador;

    public ServicioPedidos(Inventario inventario, Notificador notificador) {
        this.inventario = inventario;
        this.notificador = notificador;
    }

    public boolean procesar(Pedido pedido) {
        if (!inventario.hayStock(pedido.productoId(), pedido.cantidad())) {
            return false;
        }

        notificador.enviar(
                pedido.emailCliente(),
                "Pedido confirmado: " + pedido.cantidad() + " unidad(es) de " + pedido.productoId()
        );
        return true;
    }
}
