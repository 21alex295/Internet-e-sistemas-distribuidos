package es.udc.ws.app.model.viaje;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlViajeDao {
	
	public Viaje crear(Connection connection, Viaje viaje);
	public Viaje buscar(Connection connection, Long idViaje) throws InstanceNotFoundException;
	public void actualizar(Connection connection, Viaje viaje) throws InstanceNotFoundException;
	public void borrar(Connection connection, Long idViaje) throws InstanceNotFoundException;
	public  List<Viaje> viajesConductor(Connection connection, Long idConductor) throws InstanceNotFoundException;
	public  List<Viaje> viajesUsuario(Connection connection, String idUsuario) throws InstanceNotFoundException;
}
