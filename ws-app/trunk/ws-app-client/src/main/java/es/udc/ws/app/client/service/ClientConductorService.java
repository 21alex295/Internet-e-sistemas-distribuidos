package es.udc.ws.app.client.service;

import java.util.List;

import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientConductorService {
	
	public Long anadirConductor(ClientConductorDto conductor) throws InputValidationException;
	
	public void borrarConductor(Long idConductor) throws InstanceNotFoundException;
	
	public void actualizarConductor(ClientConductorDto conductor)throws InputValidationException, InstanceNotFoundException, ConductorNoEncontradoException;
	
	public ClientConductorDto verConductor(Long idConductor)throws InstanceNotFoundException, ConductorNoEncontradoException;
	
	//public List<ClientConductorDto> encontrarConductoresDisponibles(String ciudad) throws InputValidationException, InstanceNotFoundException;

	List<ClientViajeDto> viajesPorConductor(Long idConductor)
			throws InstanceNotFoundException, ConductorNoEncontradoException;

	
}
