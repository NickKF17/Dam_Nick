import ordenes.documentos.Documento;
import ordenes.documentos.Movimientos;
import ordenes.documentos.Talon;
import ordenes.ejecucion.OrdenNoValida;
import ordenes.ejecucion.SistemaOrdenes;
import ordenes.transferencia.OrdenDTO;
import ordenes.validacion.ValidadorOrden;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdenDTO_test {

    private final SistemaOrdenes sistema = new SistemaOrdenes();

    private List<Documento> documentos(String banco, int sucursal, int cuenta, String clave, String orden) {
        return sistema.procesar(new OrdenDTO(banco, sucursal, cuenta, clave, orden)).getDocumentos();
    }

    private void assertError(String esperado, String banco, int sucursal, int cuenta, String clave, String orden) {
        OrdenNoValida e = assertThrows(OrdenNoValida.class,
                () -> sistema.procesar(new OrdenDTO(banco, sucursal, cuenta, clave, orden)));
        assertEquals(esperado, e.getMessage());
    }

    // casos válidos (1-4)

    @Test
    void caso1_bancoEnBlancoTalonario() {
        List<Documento> docs = documentos("", 1000, 0, "AB12C", "Talonario");
        assertEquals(1, docs.size());
        assertInstanceOf(Talon.class, docs.get(0));
    }

    @Test
    void caso2_limiteInferiorBancoMovimientos() {
        List<Documento> docs = documentos("200", 5000, 12345, "A1b2C", "Movimientos");
        assertEquals(1, docs.size());
        assertInstanceOf(Movimientos.class, docs.get(0));
    }

    @Test
    void caso3_limitesSuperioresOrdenEnBlancoAmbosDocumentos() {
        List<Documento> docs = documentos("999", 9999, 99999, "Z9z9Z", "");
        assertEquals(2, docs.size());
        assertInstanceOf(Talon.class, docs.get(0));
        assertInstanceOf(Movimientos.class, docs.get(1));
    }

    @Test
    void caso4_valorNormalTalonario() {
        List<Documento> docs = documentos("500", 5000, 54321, "Qw3rT", "Talonario");
        assertEquals(1, docs.size());
        assertInstanceOf(Talon.class, docs.get(0));
    }

    @Test
    void ordenNullEnviaAmbosDocumentos() {
        assertEquals(2, documentos("500", 5000, 54321, "Qw3rT", null).size());
    }

    // errores de banco (5-9)

    @Test
    void caso5_bancoEmpiezaPorCero() {
        assertError(ValidadorOrden.ERR_BANCO, "050", 5000, 12345, "A1b2C", "Talonario");
    }

    @Test
    void caso6_bancoJustoPorDebajoDelLimite() {
        assertError(ValidadorOrden.ERR_BANCO, "199", 5000, 12345, "A1b2C", "Talonario");
    }

    @Test
    void caso7_bancoConMenosDeTresDigitos() {
        assertError(ValidadorOrden.ERR_BANCO_DIGITOS, "20", 5000, 12345, "A1b2C", "Talonario");
    }

    @Test
    void caso8_bancoConMasDeTresDigitos() {
        assertError(ValidadorOrden.ERR_BANCO_DIGITOS, "2000", 5000, 12345, "A1b2C", "Talonario");
    }

    @Test
    void caso9_bancoNoNumerico() {
        assertError(ValidadorOrden.ERR_BANCO_NO_NUMERICO, "2A0", 5000, 12345, "A1b2C", "Talonario");
    }

    // sucursal

    @Test
    void sucursalPorDebajoDelLimite() {
        assertError(ValidadorOrden.ERR_SUCURSAL, "500", 999, 12345, "A1b2C", "Talonario");
        assertError(ValidadorOrden.ERR_SUCURSAL, "500", 500, 12345, "A1b2C", "Talonario");
    }

    @Test
    void sucursalConMasDeCuatroDigitos() {
        assertError(ValidadorOrden.ERR_SUCURSAL, "500", 10000, 12345, "A1b2C", "Talonario");
    }

    // cuenta

    @Test
    void cuentaConMasDeCincoDigitos() {
        assertError(ValidadorOrden.ERR_CUENTA, "500", 5000, 100000, "A1b2C", "Talonario");
    }

    @Test
    void cuentaNegativa() {
        assertError(ValidadorOrden.ERR_CUENTA, "500", 5000, -1, "A1b2C", "Talonario");
    }

    // clave

    @Test
    void claveConMenosDeCincoPosiciones() {
        assertError(ValidadorOrden.ERR_CLAVE, "500", 5000, 12345, "A1b2", "Talonario");
    }

    @Test
    void claveConMasDeCincoPosiciones() {
        assertError(ValidadorOrden.ERR_CLAVE, "500", 5000, 12345, "A1b2C3", "Talonario");
    }

    @Test
    void claveEnBlancoONula() {
        assertError(ValidadorOrden.ERR_CLAVE, "500", 5000, 12345, "", "Talonario");
        assertError(ValidadorOrden.ERR_CLAVE, "500", 5000, 12345, null, "Talonario");
    }

    @Test
    void claveConCaracteresNoAlfanumericos() {
        assertError(ValidadorOrden.ERR_CLAVE, "500", 5000, 12345, "A1#2C", "Talonario");
    }

    // orden

    @Test
    void ordenDistintaDeLasPermitidas() {
        assertError(ValidadorOrden.ERR_TIPO_ORDEN, "500", 5000, 12345, "A1b2C", "Extracto");
    }

    @Test
    void ordenNulaLanzaOrdenNoValida() {
        OrdenNoValida e = assertThrows(OrdenNoValida.class, () -> sistema.procesar(null));
        assertEquals(ValidadorOrden.ERR_ORDEN_NULA, e.getMessage());
    }
}