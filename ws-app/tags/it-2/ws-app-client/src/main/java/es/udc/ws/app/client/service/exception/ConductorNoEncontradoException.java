package es.udc.ws.app.client.service.exception;

public class ConductorNoEncontradoException extends Exception {
	public ConductorNoEncontradoException(String message) {
		super("Conductor no existe");
	}
}
