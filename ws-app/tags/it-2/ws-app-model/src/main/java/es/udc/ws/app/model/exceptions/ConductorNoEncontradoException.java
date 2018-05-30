package es.udc.ws.app.model.exceptions;

public class ConductorNoEncontradoException extends Exception {
	public ConductorNoEncontradoException() {
		super("Conductor no encontrado");
	}

}
