package es.udc.ws.app.model.conductorservice;

import java.util.List;

import es.udc.ws.app.model.conductor.Conductor;
import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.app.model.exceptions.PuntuacionRepetidaException;
import es.udc.ws.app.model.exceptions.ViajeNoExisteException;
import es.udc.ws.app.model.viaje.Viaje;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ConductorService {

	public Conductor anadirConductor(Conductor conductor) throws InputValidationException;
	public Conductor buscarConductor(Long idConductor) throws InstanceNotFoundException,ConductorNoEncontradoException;
	public void borrarConductor(Long idConductor) throws InstanceNotFoundException;
	public List<Conductor> buscarPorCiudad(String ciudad)/* throws InstanceNotFoundException*/;
	public void actualizarConductor(Conductor conductor) throws InstanceNotFoundException,InputValidationException;
	public Viaje contratarViaje(Long idConductor, String origen, String destino, String user, String tarjetaCredito)
			throws InputValidationException, InstanceNotFoundException,ConductorNoEncontradoException;
	public Viaje buscarViaje(Long idViaje) throws InstanceNotFoundException;
	public void puntuarViaje(Long idViaje, int puntuacion) throws InstanceNotFoundException, InputValidationException,ConductorNoEncontradoException,PuntuacionRepetidaException;
	public List<Viaje> viajesConductor(Long idConductor) throws InstanceNotFoundException, ConductorNoEncontradoException;
	public List<Viaje> viajesUsuario(String idUsuario) throws InstanceNotFoundException;
	
}

