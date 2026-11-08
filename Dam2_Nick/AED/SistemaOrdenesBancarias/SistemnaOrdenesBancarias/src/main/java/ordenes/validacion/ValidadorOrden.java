package ordenes.validacion;

import ordenes.ejecucion.OrdenNoValida;
import ordenes.transferencia.OrdenDTO;

public class ValidadorOrden {

    public static final String TALONARIO = "Talonario";
    public static final String MOVIMIENTOS = "Movimientos";

    public static final String ERR_ORDEN_NULA = "Orden nula";
    public static final String ERR_BANCO = "Código de banco incorrecto";
    public static final String ERR_BANCO_DIGITOS = "Código de banco incorrecto (nº de dígitos incorrecto)";
    public static final String ERR_BANCO_NO_NUMERICO = "Código de banco incorrecto (carácter no numérico)";
    public static final String ERR_SUCURSAL = "Código de sucursal incorrecto";
    public static final String ERR_CUENTA = "Número de cuenta incorrecto";
    public static final String ERR_CLAVE = "Clave personal incorrecta";
    public static final String ERR_TIPO_ORDEN = "Tipo de orden incorrecto";

    public void validar(OrdenDTO orden) {
        if (orden == null) {
            throw new OrdenNoValida(ERR_ORDEN_NULA);
        }
        validarBanco(orden.getBanco());
        validarSucursal(orden.getSucursal());
        validarCuenta(orden.getCuenta());
        validarClave(orden.getClave());
        validarTipoOrden(orden.getTipoOrden());
    }

    // en blanco es válido; si no, 3 dígitos entre 200 y 999
    private void validarBanco(String banco) {
        if (banco == null || banco.isBlank()) {
            return;
        }
        if (!banco.matches("\\d+")) {
            throw new OrdenNoValida(ERR_BANCO_NO_NUMERICO);
        }
        if (banco.length() != 3) {
            throw new OrdenNoValida(ERR_BANCO_DIGITOS);
        }
        if (Integer.parseInt(banco) < 200) {
            throw new OrdenNoValida(ERR_BANCO);
        }
    }

    // 4 dígitos, primer dígito > 0 (1000-9999)
    private void validarSucursal(int sucursal) {
        if (sucursal < 1000 || sucursal > 9999) {
            throw new OrdenNoValida(ERR_SUCURSAL);
        }
    }

    // 5 dígitos (0-99999)
    private void validarCuenta(int cuenta) {
        if (cuenta < 0 || cuenta > 99999) {
            throw new OrdenNoValida(ERR_CUENTA);
        }
    }

    // alfanumérico de exactamente 5 posiciones
    private void validarClave(String clave) {
        if (clave == null || !clave.matches("[A-Za-z0-9]{5}")) {
            throw new OrdenNoValida(ERR_CLAVE);
        }
    }

    // en blanco, "Talonario" o "Movimientos"
    private void validarTipoOrden(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return;
        }
        if (!tipo.equals(TALONARIO) && !tipo.equals(MOVIMIENTOS)) {
            throw new OrdenNoValida(ERR_TIPO_ORDEN);
        }
    }
}