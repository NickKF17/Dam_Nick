**Refactorización de Code Smells**  
   
    
   
  **Ejercicio 7: Código Duplicado**  
   
    
   
  **Código original**  
```  
public void guardarEstudiante(Estudiante estudiante) {      
       if (estudiante == null) {      
           throw new IllegalArgumentException();      
       }      
       validarEstudiante(estudiante);      
       repositorio.save(estudiante);      
       logger.info("Estudiante guardado");      
   }      
         
   public void guardarProfesor(Profesor profesor) {      
       if (profesor == null) {      
           throw new IllegalArgumentException();      
       }      
       validarProfesor(profesor);      
       repositorio.save(profesor);      
       logger.info("Profesor guardado");      
   }      
         
      
   
```  
**Code smells identificados**  
  
|  |  |  
| - | - |  
|   |    |  
|   |    |  
| **Code smell** |  **Descripción** |  
| **Código duplicado** |  Ambos métodos siguen el mismo algoritmo: comprobar nulo, validar, guardar y registrar en el log. Solo cambian el tipo de entidad, el validador y el texto del log. |  
| **Excepción sin mensaje** |  `IllegalArgumentException()` no indica qué argumento era inválido, lo que dificulta el diagnóstico. |  
| **Mensaje de log hardcodeado** |  El texto se repite y se mantiene a mano en cada método. |  
  
   
   
   
    
   
    
   
  **Código refactorizado**  
```  
public void guardarEstudiante(Estudiante estudiante) {      
       guardar(estudiante, this::validarEstudiante, "Estudiante");      
   }      
         
   public void guardarProfesor(Profesor profesor) {      
       guardar(profesor, this::validarProfesor, "Profesor");      
   }      
         
   private <T> void guardar(T entidad, Consumer<T> validador, String tipoEntidad) {      
       if (entidad == null) {      
           throw new IllegalArgumentException(tipoEntidad + " no puede ser nulo");      
       }      
       validador.accept(entidad);      
       repositorio.save(entidad);      
       logger.info("{} guardado", tipoEntidad);      
   }      
         
      
   
```  
**Ejercicio 8: Método Largo**  
   
    
   
  **Código original**  
```  
public void procesarPedido(Pedido pedido) {      
       // Validar cliente      
       if (pedido.getCliente() == null) {      
           logger.error("Cliente nulo");      
           return;      
       }      
         
       // Validar dirección      
       if (pedido.getDireccion() == null || pedido.getDireccion().isBlank()) {      
           logger.error("Dirección vacía");      
           return;      
       }      
         
       // Calcular totales      
       BigDecimal subtotal = BigDecimal.ZERO;      
       for (Item item : pedido.getItems()) {      
           subtotal = subtotal.add(item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));      
       }      
         
       // Aplicar descuento      
       BigDecimal descuento = BigDecimal.ZERO;      
       if (pedido.getCliente().tieneDescuento()) {      
           descuento = subtotal.multiply(new BigDecimal("0.10"));      
       }      
         
       // Calcular IVA      
       BigDecimal iva = subtotal.subtract(descuento).multiply(new BigDecimal("0.21"));      
       BigDecimal total = subtotal.subtract(descuento).add(iva);      
         
       // Guardar pedido      
       repositorio.guardar(pedido);      
         
       // Enviar email      
       emailService.enviar(pedido.getCliente().getEmail(), "Pedido procesado");      
   }      
         
      
   
```  
**Code smells identificados**  
  
|  |  |  
| - | - |  
|   |    |  
|   |    |  
| **Code smell** |  **Descripción** |  
| **Método largo (Long Method)** |  Un solo método hace validación, cálculo de subtotal, descuento, IVA, persistencia y notificación. |  
| **Comentarios como separadores de secciones** |  Cada comentario (`// Calcular IVA`, etc.) señala un bloque que debería ser un método con ese nombre. |  
| **Magic numbers** |  `0.10` y `0.21` aparecen sin nombre ni contexto. |  
| **Variable no utilizada (dead code)** |  `total` se calcula pero nunca se usa. |  
| **Múltiples responsabilidades** |  Viola el Principio de Responsabilidad Única (SRP). |  
| **Errores silenciosos** |  Ante datos inválidos solo se registra en el log y se hace `return`; el llamador no se entera de que el pedido no se procesó. |  
  
   
   
   
    
   
    
   
  **Código refactorizado**  
```  
private static final BigDecimal TASA_DESCUENTO = new BigDecimal("0.10");      
   private static final BigDecimal TASA_IVA = new BigDecimal("0.21");      
         
   public void procesarPedido(Pedido pedido) {      
       if (!esPedidoValido(pedido)) {      
           return;      
       }      
         
       pedido.setTotal(calcularTotal(pedido));      
         
       repositorio.guardar(pedido);      
       notificarCliente(pedido);      
   }      
         
   // ---------- Validación ----------      
         
   private boolean esPedidoValido(Pedido pedido) {      
       return tieneClienteValido(pedido) && tieneDireccionValida(pedido);      
   }      
         
   private boolean tieneClienteValido(Pedido pedido) {      
       if (pedido.getCliente() == null) {      
           logger.error("Cliente nulo");      
           return false;      
       }      
       return true;      
   }      
         
   private boolean tieneDireccionValida(Pedido pedido) {      
       String direccion = pedido.getDireccion();      
       if (direccion == null || direccion.isBlank()) {      
           logger.error("Dirección vacía");      
           return false;      
       }      
       return true;      
   }      
         
   // ---------- Cálculos ----------      
         
   private BigDecimal calcularTotal(Pedido pedido) {      
       BigDecimal subtotal = calcularSubtotal(pedido);      
       BigDecimal descuento = calcularDescuento(pedido.getCliente(), subtotal);      
       BigDecimal baseImponible = subtotal.subtract(descuento);      
       BigDecimal iva = calcularIva(baseImponible);      
       return baseImponible.add(iva);      
   }      
         
   private BigDecimal calcularSubtotal(Pedido pedido) {      
       BigDecimal subtotal = BigDecimal.ZERO;      
       for (Item item : pedido.getItems()) {      
           subtotal = subtotal.add(calcularImporteItem(item));      
       }      
       return subtotal;      
   }      
         
   private BigDecimal calcularImporteItem(Item item) {      
       return item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));      
   }      
         
   private BigDecimal calcularDescuento(Cliente cliente, BigDecimal subtotal) {      
       if (cliente.tieneDescuento()) {      
           return subtotal.multiply(TASA_DESCUENTO);      
       }      
       return BigDecimal.ZERO;      
   }      
         
   private BigDecimal calcularIva(BigDecimal baseImponible) {      
       return baseImponible.multiply(TASA_IVA);      
   }      
         
   // ---------- Notificación ----------      
         
   private void notificarCliente(Pedido pedido) {      
       emailService.enviar(pedido.getCliente().getEmail(), "Pedido procesado");      
   }      
         
      
   
```  
**Ejercicio 9: Magic Numbers**  
   
    
   
  **Código original**  
```  
public class Calculadora {      
       public BigDecimal calcularImpuesto(BigDecimal monto) {      
           return monto.multiply(new BigDecimal("0.21"));      
       }      
         
       public boolean esMayorDeEdad(int edad) {      
           return edad >= 18;      
       }      
         
       public int obtenerDiasPlazo(String tipo) {      
           return switch (tipo) {      
               case "normal" -> 30;      
               case "urgente" -> 7;      
               case "express" -> 1;      
               default -> 30;      
           };      
       }      
   }      
         
      
   
```  
**Code smells identificados**  
  
|  |  |  
| - | - |  
|   |    |  
|   |    |  
| **Code smell** |  **Descripción** |  
| **Magic numbers** |  `0.21`, `18`, `30`, `7` y `1` no explican su significado. |  
| **Magic strings** |  `"normal"`, `"urgente"` y `"express"` son literales propensos a errores de tipeo y no los verifica el compilador. |  
| **Valor duplicado** |  `30` aparece dos veces (caso `normal` y `default`), y si cambia hay que acordarse de ambos. |  
| **Primitive Obsession** |  Un `String` representa un concepto del dominio (el tipo de plazo) que merece su propio tipo. |  
  
   
   
   
    
   
    
   
  **Código refactorizado**  
   
    
   
  **`TipoPlazo.java`**  
```  
public enum TipoPlazo {      
       NORMAL(30),      
       URGENTE(7),      
       EXPRESS(1);      
         
       public static final TipoPlazo POR_DEFECTO = NORMAL;      
         
       private final int dias;      
         
       TipoPlazo(int dias) {      
           this.dias = dias;      
       }      
         
       public int getDias() {      
           return dias;      
       }      
         
       public static TipoPlazo desdeTexto(String texto) {      
           for (TipoPlazo tipo : values()) {      
               if (tipo.name().equalsIgnoreCase(texto)) {      
                   return tipo;      
               }      
           }      
           return POR_DEFECTO;      
       }      
   }      
         
      
   
```  
**`Calculadora.java`**  
```  
public class Calculadora {      
         
       private static final BigDecimal TASA_IMPUESTO = new BigDecimal("0.21");      
       private static final int EDAD_MAYORIA = 18;      
         
       public BigDecimal calcularImpuesto(BigDecimal monto) {      
           return monto.multiply(TASA_IMPUESTO);      
       }      
         
       public boolean esMayorDeEdad(int edad) {      
           return edad >= EDAD_MAYORIA;      
       }      
         
       public int obtenerDiasPlazo(TipoPlazo tipo) {      
           return tipo.getDias();      
       }      
         
       // Sobrecarga para mantener compatibilidad con código que aún usa String      
       public int obtenerDiasPlazo(String tipo) {      
           return obtenerDiasPlazo(TipoPlazo.desdeTexto(tipo));      
       }      
   }      
         
      
   
```  
**Ejercicio 10: Nombres Pobres**  
   
    
   
  **Código original**  
```  
public class C {      
       public int p(int a, int b) {      
           int x = a + b;      
           int y = a - b;      
           return x * y;      
       }      
   }      
         
      
   
```  
**Code smells identificados**  
  
|  |  |  
| - | - |  
|   |    |  
|   |    |  
| **Code smell** |  **Descripción** |  
| **Nombre de clase críptico** |  `C` no dice nada sobre la responsabilidad de la clase. |  
| **Nombre de método críptico** |  `p` no revela qué hace. |  
| **Parámetros y variables de una letra** |  `a`, `b`, `x`, `y` obligan al lector a deducir el significado leyendo el cuerpo. |  
  
   
   
   
    
   
    
   
  **Análisis**  
   
    
   
  El método calcula `(a + b) * (a - b)`, que algebraicamente es **a² − b²**, es decir, una *diferencia de cuadrados*.  
   
    
   
  **Código refactorizado**  
```  
public class CalculadoraAlgebraica {      
         
       public int calcularDiferenciaDeCuadrados(int primerNumero, int segundoNumero) {      
           int suma = primerNumero + segundoNumero;      
           int diferencia = primerNumero - segundoNumero;      
           return suma * diferencia;      
       }      
   }      
         
      
   
```  
