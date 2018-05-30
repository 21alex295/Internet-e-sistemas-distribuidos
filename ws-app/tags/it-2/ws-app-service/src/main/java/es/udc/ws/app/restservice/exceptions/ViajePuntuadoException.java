package es.udc.ws.app.restservice.exceptions;

@SuppressWarnings("serial")
public class ViajePuntuadoException extends Exception{

	public ViajePuntuadoException() {
		super("Este viaje ya ha sido puntuado");
	}

}
