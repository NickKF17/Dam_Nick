package ordenes.ejecucion;

import ordenes.documentos.Documento;
import ordenes.documentos.Movimientos;
import ordenes.documentos.Talon;
import ordenes.transferencia.OrdenDTO;
import ordenes.transferencia.SolicitudDTO;
import ordenes.validacion.ValidadorOrden;

import java.util.ArrayList;
import java.util.List;

public class SistemaOrdenes {

    private final ValidadorOrden validador = new ValidadorOrden();

    public SolicitudDTO procesar(OrdenDTO orden) {
        validador.validar(orden);

        List<Documento> documentos = new ArrayList<>();
        String tipo = orden.getTipoOrden();

        if (tipo == null || tipo.isBlank()) {
            documentos.add(new Talon());
            documentos.add(new Movimientos());
        } else if (tipo.equals(ValidadorOrden.TALONARIO)) {
            documentos.add(new Talon());
        } else {
            documentos.add(new Movimientos());
        }
        return new SolicitudDTO(documentos);
    }
}