**Optimización de código manteniendo el comportamiento**  
**Ejercicio 11: Switch vs Pattern Matching**  
**Código original**  
```  
public String getTipo(Usuario usuario) {  
     return switch (usuario.getTipo()) {  
         case 1 -> "Admin";  
         case 2 -> "Gestor";  
         case 3 -> "Usuario";  
         case 4 -> "Invitado";  
         default -> "Desconocido";  
     };  
 }  
   
```  
   
**Código optimizado**  
**`TipoUsuario.java`**  
```  
public enum TipoUsuario {  
     ADMIN(1, "Admin"),  
     GESTOR(2, "Gestor"),  
     USUARIO(3, "Usuario"),  
     INVITADO(4, "Invitado"),  
     DESCONOCIDO(-1, "Desconocido");  
   
     private static final Map<Integer, TipoUsuario> POR_CODIGO = Arrays.stream(values())  
             .collect(Collectors.toMap(TipoUsuario::getCodigo, Function.identity()));  
   
     private final int codigo;  
     private final String descripcion;  
   
     TipoUsuario(int codigo, String descripcion) {  
         this.codigo = codigo;  
         this.descripcion = descripcion;  
     }  
   
     public int getCodigo() {  
         return codigo;  
     }  
   
     public String getDescripcion() {  
         return descripcion;  
     }  
   
     public static TipoUsuario desdeCodigo(int codigo) {  
         return POR_CODIGO.getOrDefault(codigo, DESCONOCIDO);  
     }  
 }  
   
```  
**Uso**  
```  
public String getTipo(Usuario usuario) {  
     return TipoUsuario.desdeCodigo(usuario.getTipo()).getDescripcion();  
 }  
   
```  
```  
private static final int TIPO_ADMIN = 1;  
 private static final int TIPO_GESTOR = 2;  
 private static final int TIPO_USUARIO = 3;  
 private static final int TIPO_INVITADO = 4;  
   
 public String getTipo(Usuario usuario) {  
     return switch (usuario.getTipo()) {  
         case TIPO_ADMIN -> "Admin";  
         case TIPO_GESTOR -> "Gestor";  
         case TIPO_USUARIO -> "Usuario";  
         case TIPO_INVITADO -> "Invitado";  
         default -> "Desconocido";  
     };  
 }  
   
```  
**Ejercicio 12: Bucles vs Streams**  
**Código original**  
```  
public List<Integer> getPares(List<Integer> numeros) {  
     List<Integer> resultado = new ArrayList<>();  
     for (int i = 0; i < numeros.size(); i++) {  
         if (numeros.get(i) % 2 == 0) {  
             resultado.add(numeros.get(i));  
         }  
     }  
     return resultado;  
 }  
   
```  
**Código optimizado**  
```  
public List<Integer> getPares(List<Integer> numeros) {  
     return numeros.stream()  
             .filter(numero -> numero % 2 == 0)  
             .collect(Collectors.toCollection(ArrayList::new));  
 }  
   
```  
**Ejercicio 13: Map vs Switch**  
**Código original**  
```  
public int getPrecioEnvio(String pais) {  
     return switch (pais.toLowerCase()) {  
         case "españa" -> 5;  
         case "francia" -> 8;  
         case "alemania" -> 10;  
         case "italia" -> 7;  
         case "portugal" -> 4;  
         case "reino unido" -> 12;  
         case "belgica" -> 9;  
         case "paises bajos" -> 11;  
         default -> 15;  
     };  
 }  
   
```  
   
**Código optimizado**  
```  
private static final int PRECIO_ENVIO_POR_DEFECTO = 15;  
   
 private static final Map<String, Integer> PRECIOS_ENVIO = Map.of(  
         "españa", 5,  
         "francia", 8,  
         "alemania", 10,  
         "italia", 7,  
         "portugal", 4,  
         "reino unido", 12,  
         "belgica", 9,  
         "paises bajos", 11);  
   
 public int getPrecioEnvio(String pais) {  
     return PRECIOS_ENVIO.getOrDefault(pais.toLowerCase(), PRECIO_ENVIO_POR_DEFECTO);  
 }  
   
```  
**Ejercicio 14: Null Checking**  
**Código original**  
```  
public String getNombre(Usuario usuario) {  
     if (usuario == null) {  
         return "Anónimo";  
     }  
   
     if (usuario.getNombre() == null) {  
         return "Sin nombre";  
     }  
   
     return usuario.getNombre();  
 }  
   
```  
   
**Código optimizado**  
```  
private static final String NOMBRE_ANONIMO = "Anónimo";  
 private static final String NOMBRE_SIN_NOMBRE = "Sin nombre";  
   
 public String getNombre(Usuario usuario) {  
     return Optional.ofNullable(usuario)  
             .map(u -> Optional.ofNullable(u.getNombre()).orElse(NOMBRE_SIN_NOMBRE))  
             .orElse(NOMBRE_ANONIMO);  
 }  
   
```  
**Ejercicio 15: Construcción de cadenas**  
**Código original**  
```  
String mensaje = "El usuario " + usuario.getNombre() + " con ID " + usuario.getId()  
         + " ha realizado " + pedido.getCantidad() + " compras por un total de " + pedido.getTotal() + " euros.";  
   
```  
   
**Código optimizado**  
**Opción A**:Con `String.format` clásico (cualquier versión de Java):  
```  
String mensaje = String.format(  
         "El usuario %s con ID %s ha realizado %s compras por un total de %s euros.",  
         usuario.getNombre(), usuario.getId(), pedido.getCantidad(), pedido.getTotal());  
   
```  
**Opción B: `StringBuilder`**  
```  
String mensaje = new StringBuilder()  
         .append("El usuario ").append(usuario.getNombre())  
         .append(" con ID ").append(usuario.getId())  
         .append(" ha realizado ").append(pedido.getCantidad())  
         .append(" compras por un total de ").append(pedido.getTotal())  
         .append(" euros.")  
         .toString();  
   
```  
