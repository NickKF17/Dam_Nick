// Smart Room Voice Control

const habitacion = document.getElementById('habitacion');
const persiana = document.getElementById('persiana');
const temperatura = document.getElementById('temperatura');
const estadoVoz = document.getElementById('estadoVoz');
const textoReconocido = document.getElementById('textoReconocido');
const ultimaAccion = document.getElementById('ultimaAccion');
const entradaManual = document.getElementById('entradaManual');

const TEMP_MIN = 16;
const TEMP_MAX = 30;
const TEMP_INICIAL = 22;

// Estado de la habitación
let temp = TEMP_INICIAL;
let luzEncendida = false;
let persianaAbierta = true;
let modoNoche = false;
let modoCine = false; // modo personalizado

function normalizar(texto) {
  return texto.toLowerCase().trim()
    .normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function informar(mensaje, error = false) {
  ultimaAccion.textContent = mensaje;
  ultimaAccion.classList.toggle('error', error);
}

// Palabras equivalentes para interpretar las órdenes (ya sin tildes)
const PALABRAS = {
  encender: ['encender', 'enciende', 'enciendo', 'prende', 'prender', 'activa la luz'],
  apagar: ['apagar', 'apaga', 'apago'],
  abrir: ['abrir', 'abre', 'abro', 'subir la persiana', 'sube la persiana'],
  cerrar: ['cerrar', 'cierra', 'cierro', 'bajar la persiana', 'baja la persiana'],
  subir: ['subir', 'sube', 'subo', 'aumentar', 'aumenta', 'incrementar', 'incrementa'],
  bajar: ['bajar', 'baja', 'bajo', 'reducir', 'reduce', 'disminuir', 'disminuye'],
  poner: ['poner', 'pon', 'pone', 'establecer', 'establece', 'fijar', 'fija', 'ajustar', 'ajusta', 'cambiar', 'cambia'],
  activar: ['activar', 'activa', 'activo', 'poner', 'pon', 'inicia', 'iniciar'],
  desactivar: ['desactivar', 'desactiva', 'desactivo', 'quitar', 'quita', 'salir del', 'sal del'],
  reiniciar: ['reiniciar', 'reinicia', 'resetear', 'resetea', 'restablecer', 'restablece']
};

// ¿El texto contiene alguna de las palabras de la lista?
function contiene(texto, lista) {
  return lista.some((palabra) => texto.includes(palabra));
}

// Primer número entero que aparezca en el texto (o null)
function extraerNumero(texto) {
  const resultado = texto.match(/-?\d+/);
  return resultado ? parseInt(resultado[0], 10) : null;
}

// ---------- Acciones sobre la habitación ----------

function pintarTemperatura() {
  temperatura.textContent = temp;
}

// Al tocar luz/persiana/temperatura a mano, los modos dejan de estar activos
function salirDeModos() {
  modoNoche = false;
  modoCine = false;
  habitacion.classList.remove('night', 'cine');
}

function encenderLuz(silencioso = false) {
  if (luzEncendida) { if (!silencioso) informar('La luz ya está encendida.', true); return false; }
  luzEncendida = true;
  habitacion.classList.add('light-on');
  if (!silencioso) informar('Luz encendida');
  return true;
}

function apagarLuz(silencioso = false) {
  if (!luzEncendida) { if (!silencioso) informar('La luz ya está apagada.', true); return false; }
  luzEncendida = false;
  habitacion.classList.remove('light-on');
  if (!silencioso) informar('Luz apagada');
  return true;
}

function abrirPersiana(silencioso = false) {
  if (persianaAbierta) { if (!silencioso) informar('La persiana ya está abierta.', true); return false; }
  persianaAbierta = true;
  persiana.classList.remove('closed');
  persiana.classList.add('open');
  if (!silencioso) informar('Persiana abierta');
  return true;
}

function cerrarPersiana(silencioso = false) {
  if (!persianaAbierta) { if (!silencioso) informar('La persiana ya está cerrada.', true); return false; }
  persianaAbierta = false;
  persiana.classList.remove('open');
  persiana.classList.add('closed');
  if (!silencioso) informar('Persiana cerrada');
  return true;
}

// Establece una temperatura validando el rango permitido
function establecerTemperatura(valor, silencioso = false) {
  if (valor < TEMP_MIN || valor > TEMP_MAX) {
    informar(`La temperatura debe estar entre ${TEMP_MIN} y ${TEMP_MAX} °C.`, true);
    return false;
  }
  if (valor === temp) {
    if (!silencioso) informar(`La temperatura ya es de ${temp} °C.`, true);
    return false;
  }
  temp = valor;
  pintarTemperatura();
  if (!silencioso) informar(`Temperatura: ${temp} °C`);
  return true;
}

function subirTemperatura(grados) { return establecerTemperatura(temp + grados); }
function bajarTemperatura(grados) { return establecerTemperatura(temp - grados); }

function activarModoNoche() {
  if (modoNoche) { informar('El modo noche ya está activado.', true); return; }
  salirDeModos();
  apagarLuz(true);
  cerrarPersiana(true);
  temp = 20;
  pintarTemperatura();
  modoNoche = true;
  habitacion.classList.add('night');
  informar('Modo noche activado: luz apagada, persiana cerrada y temperatura a 20 °C');
}

// Modo personalizado: MODO CINE
// 1) apaga la luz  2) cierra la persiana  3) pone la temperatura a 23 °C  4) activa el aspecto "cine"
function activarModoCine() {
  if (modoCine) { informar('El modo cine ya está activado.', true); return; }
  salirDeModos();
  apagarLuz(true);
  cerrarPersiana(true);
  temp = 23;
  pintarTemperatura();
  modoCine = true;
  habitacion.classList.add('cine');
  informar('Modo cine activado: luz apagada, persiana cerrada y temperatura a 23 °C');
}

function desactivarModos() {
  if (!modoNoche && !modoCine) { informar('No hay ningún modo activado.', true); return; }
  salirDeModos();
  informar('Modo desactivado');
}

function reiniciarHabitacion() {
  salirDeModos();
  luzEncendida = true;  // forzamos el cambio para que se actualice el aspecto
  apagarLuz(true);
  persianaAbierta = false;
  abrirPersiana(true);
  temp = TEMP_INICIAL;
  pintarTemperatura();
  informar('Habitación reiniciada: luz apagada, persiana abierta, 22 °C y modo noche desactivado');
}

// ---------- Interpretación de comandos ----------

function interpretarTemperatura(texto) {
  const numero = extraerNumero(texto);
  const quiereSubir = contiene(texto, PALABRAS.subir);
  const quiereBajar = contiene(texto, PALABRAS.bajar);
  const quierePoner = contiene(texto, PALABRAS.poner);

  if (!quiereSubir && !quiereBajar && !quierePoner) {
    informar('Falta indicar qué hacer con la temperatura (subir, bajar o poner).', true);
    return;
  }
  if (numero === null) {
    informar('Falta indicar cuántos grados.', true);
    return;
  }
  if (numero < 0 && !quierePoner) {
    informar('La cantidad de grados debe ser positiva.', true);
    return;
  }

  let cambiado;
  if (quiereSubir) cambiado = subirTemperatura(numero);
  else if (quiereBajar) cambiado = bajarTemperatura(numero);
  else cambiado = establecerTemperatura(numero);

  if (cambiado) {
    // El mensaje ya se ha mostrado; solo se desactivan los modos
    salirDeModos();
  }
}

function ejecutarComando(textoOriginal) {
  const texto = normalizar(textoOriginal);
  textoReconocido.textContent = textoOriginal;

  if (texto === '') {
    informar('No se ha introducido ningún comando.', true);
  } else if (contiene(texto, PALABRAS.reiniciar)) {
    reiniciarHabitacion();
  } else if (texto.includes('modo noche')) {
    if (contiene(texto, PALABRAS.desactivar)) desactivarModos();
    else activarModoNoche();
  } else if (texto.includes('modo cine')) {
    if (contiene(texto, PALABRAS.desactivar)) desactivarModos();
    else activarModoCine();
  } else if (texto.includes('modo')) {
    informar('Modo no reconocido. Prueba con "modo noche" o "modo cine".', true);
  } else if (texto.includes('temperatura') || texto.includes('grados')) {
    interpretarTemperatura(texto);
  } else if (texto.includes('luz') || texto.includes('lampara')) {
    if (contiene(texto, PALABRAS.encender)) { if (luzEncendida) encenderLuz(); else { salirDeModos(); encenderLuz(); } }
    else if (contiene(texto, PALABRAS.apagar)) { apagarLuz(); }
    else informar('Falta indicar qué hacer con la luz (encender o apagar).', true);
  } else if (texto.includes('persiana')) {
    if (contiene(texto, PALABRAS.abrir)) { if (persianaAbierta) abrirPersiana(); else { salirDeModos(); abrirPersiana(); } }
    else if (contiene(texto, PALABRAS.cerrar)) { cerrarPersiana(); }
    else informar('Falta indicar qué hacer con la persiana (abrir o cerrar).', true);
  } else if (contiene(texto, PALABRAS.encender) || contiene(texto, PALABRAS.apagar) ||
             contiene(texto, PALABRAS.abrir) || contiene(texto, PALABRAS.cerrar)) {
    informar('Falta indicar el elemento (luz o persiana).', true);
  } else {
    informar('Comando no reconocido.', true);
  }
}

// --- Reconocimiento de voz: no necesitas modificar esta parte ---
const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
let recognition = null;

if (SpeechRecognition) {
  recognition = new SpeechRecognition();
  recognition.lang = 'es-ES';
  recognition.continuous = true;
  recognition.interimResults = false;

  recognition.onstart = () => {
    estadoVoz.textContent = 'Escuchando';
    estadoVoz.className = 'badge listening';
  };

  recognition.onend = () => {
    estadoVoz.textContent = 'Detenido';
    estadoVoz.className = 'badge stopped';
  };

  recognition.onresult = (event) => {
    const ultimo = event.results[event.results.length - 1][0].transcript;
    ejecutarComando(ultimo);
  };

  recognition.onerror = (event) => informar(`Error de voz: ${event.error}`, true);
} else {
  informar('Este navegador no ofrece reconocimiento de voz. Usa la prueba manual.', true);
}

document.getElementById('btnIniciar').addEventListener('click', () => {
  if (recognition) recognition.start();
});

document.getElementById('btnDetener').addEventListener('click', () => {
  if (recognition) recognition.stop();
});

document.getElementById('btnProbar').addEventListener('click', () => {
  ejecutarComando(entradaManual.value);
});

entradaManual.addEventListener('keydown', (e) => {
  if (e.key === 'Enter') ejecutarComando(entradaManual.value);
});
