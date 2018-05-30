package es.udc.ws.app.client.service;

import java.util.List;

import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.app.client.service.dto.ClientConductoresDeUsuarioDto;
import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.app.client.service.exception.PuntuacionRepetidaException;
import es.udc.ws.app.client.service.exception.ViajeDeOtroUsuarioException;
import es.udc.ws.app.client.service.exception.ViajeNoExisteException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientUsuarioService {

	public  ClientViajeDto contratarViaje(ClientViajeDto viaje) throws InputValidationException, ConductorNoEncontradoException;
	
	public  void puntuarViaje(Long idViaje,String idUsuario, Integer puntuacion) 
			throws InstanceNotFoundException, InputValidationException, PuntuacionRepetidaException, ViajeDeOtroUsuarioException, ViajeNoExisteException;
	
	//public List<ClientConductorDto> conductoresPorUsuario(String idUsuario) throws InstanceNotFoundException;
	
	//List<ClientViajeDto> viajesPorConductor(Long idConductor) throws InstanceNotFoundException, ConductorNoEncontradoException;
	
	public List<ClientViajeDto> viajesPorUsuario(String idUsuario) throws InstanceNotFoundException;
	
	public List<ClientConductoresDeUsuarioDto> conductoresDeUsuario(String idUsuario);
	
	public List<ClientConductorDto> encontrarConductoresDisponibles(String ciudad)
			throws InputValidationException, InstanceNotFoundException;
}
