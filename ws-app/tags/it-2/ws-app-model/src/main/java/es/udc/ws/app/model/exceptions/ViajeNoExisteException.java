package es.udc.ws.app.model.exceptions;

public class ViajeNoExisteException extends Exception{

	public ViajeNoExisteException() {
		super("Viaje no existe");
	}
}
