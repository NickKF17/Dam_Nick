package tddmicroexercises.telemetrysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticControlsTest
{
    // Texto que simulamos que devuelve el servidor
    private static final String RESPUESTA = "LAST TX rate................ 100 MBPS";

    // Mock: un TelemetryClient falso, así no dependemos del cliente real (que es aleatorio)
    @Mock
    private TelemetryClient telemetryClient;

    private TelemetryDiagnosticControls controls;

    @BeforeEach
    public void setUp()
    {
        // Antes de cada test creamos la clase a probar pasándole el mock
        controls = new TelemetryDiagnosticControls(telemetryClient);
    }

    @Test
    public void CheckTransmission_should_send_a_diagnostic_message_and_receive_a_status_message_response() throws Exception
    {
        // Primero getOnlineStatus devuelve false (desconectado) y después true (conectado)
        when(telemetryClient.getOnlineStatus()).thenReturn(false, true);
        when(telemetryClient.receive()).thenReturn(RESPUESTA);

        controls.checkTransmission();

        // Comprobamos que se envió el mensaje de diagnóstico y se guardó la respuesta
        verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        assertEquals(RESPUESTA, controls.getDiagnosticInfo());
    }

    @Test
    public void CheckTransmission_should_connect_only_once_if_first_attempt_works() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false, true);
        when(telemetryClient.receive()).thenReturn(RESPUESTA);

        controls.checkTransmission();

        verify(telemetryClient, times(1)).connect("*111#");
    }

    @Test
    public void CheckTransmission_should_retry_if_first_attempt_fails() throws Exception
    {
        // Falla la primera conexión y funciona la segunda
        when(telemetryClient.getOnlineStatus()).thenReturn(false, false, true, true);
        when(telemetryClient.receive()).thenReturn(RESPUESTA);

        controls.checkTransmission();

        verify(telemetryClient, times(2)).connect("*111#");
        assertEquals(RESPUESTA, controls.getDiagnosticInfo());
    }

    @Test
    public void CheckTransmission_should_throw_exception_after_three_failed_attempts() throws Exception
    {
        // Siempre desconectado
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        Exception e = assertThrows(Exception.class, () -> controls.checkTransmission());

        assertEquals("Unable to connect.", e.getMessage());
        verify(telemetryClient, times(3)).connect("*111#");
    }

    @Test
    public void CheckTransmission_should_not_send_if_connection_fails() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        assertThrows(Exception.class, () -> controls.checkTransmission());

        // Si no hay conexión no se debe enviar ni recibir nada
        verify(telemetryClient, never()).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        verify(telemetryClient, never()).receive();
    }

    @Test
    public void CheckTransmission_should_clear_old_diagnostic_info_if_connection_fails() throws Exception
    {
        controls.setDiagnosticInfo("info antigua");
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        assertThrows(Exception.class, () -> controls.checkTransmission());

        assertEquals("", controls.getDiagnosticInfo());
    }
}
