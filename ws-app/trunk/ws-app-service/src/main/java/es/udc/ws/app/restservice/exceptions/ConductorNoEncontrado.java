package es.udc.ws.app.restservice.exceptions;

@SuppressWarnings("serial")
public class ConductorNoEncontrado extends Exception {
	public ConductorNoEncontrado() {
		super("Conductor no encontrado");
	}

}
