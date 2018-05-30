package es.udc.ws.app.restservice.exceptions;

@SuppressWarnings("serial")
public class ViajeNoExisteException extends Exception{
	public ViajeNoExisteException() {
		super("Viaje no encontrado");
	}
}
