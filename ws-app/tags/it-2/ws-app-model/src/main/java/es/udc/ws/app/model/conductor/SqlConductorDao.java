package es.udc.ws.app.model.conductor;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlConductorDao {
	
	public Conductor crear(Connection connection, Conductor conductor);
	
	public Conductor buscar (Connection connection, Long idConductor) throws InstanceNotFoundException,ConductorNoEncontradoException;

	public void borrar(Connection connection, Long idConductor) throws InstanceNotFoundException;
	
	public List<Conductor> buscarPorCiudad(Connection connection, String ciudad, byte hora) /*throws InstanceNotFoundException*/;

	public void actualizar(Connection connection, Conductor conductor) throws InstanceNotFoundException;

}
