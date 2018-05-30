package es.udc.ws.app.restservice.exceptions;

@SuppressWarnings("serial")
public class ViajeDeOtroUsuarioException extends Exception{
	public ViajeDeOtroUsuarioException() {
		super("Viaje de otro usuario");
	}
}
