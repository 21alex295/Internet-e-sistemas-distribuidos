package es.udc.ws.app.model.viaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlViajeDao implements SqlViajeDao {
	
	protected AbstractSqlViajeDao(){
		
	}
	@Override
	public Viaje buscar(Connection connection, Long idViaje) throws InstanceNotFoundException{
		String queryString = "SELECT idConductor, origen, destino, idUsuario, tarjetaCredito, fechaReserva, puntuacion"
				+ " FROM  Viaje WHERE idViaje = ?";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
				int i = 1;
				preparedStatement.setLong(i++, idViaje.longValue());
				
				ResultSet resultSet = preparedStatement.executeQuery();
				if (!resultSet.next()){
					throw new InstanceNotFoundException(idViaje, Viaje.class.getName());
				}
				
				i = 1;
				Long idConductor = resultSet.getLong(i++);
				String origen = resultSet.getString(i++);
				String destino = resultSet.getString(i++);
				String idUsuario = resultSet.getString(i++);
				String tarjetaCredito = resultSet.getString(i++);
				Calendar fechaReserva = Calendar.getInstance();
				fechaReserva.setTime(resultSet.getTimestamp(i++));
				int puntuacion = resultSet.getInt(i++);
				
				return new Viaje(idViaje, idConductor, origen, destino, idUsuario, tarjetaCredito, fechaReserva, puntuacion );

		} catch (SQLException e){
			throw new RuntimeException(e);
		}

	}

	@Override 
	public void actualizar(Connection connection, Viaje viaje) throws InstanceNotFoundException{
		String queryString = "UPDATE Viaje SET puntuacion = ? WHERE idViaje = ?";
	
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
			int i = 1;
			
            preparedStatement.setInt(i++, viaje.getPuntuacion());
			preparedStatement.setLong(i++, viaje.getIdViaje());

            
            int updatedRows = preparedStatement.executeUpdate();
            
            if (updatedRows == 0){
            	throw new InstanceNotFoundException(viaje.getIdConductor(),Viaje.class.getName());
            }
            
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}
	@Override 
	public void borrar(Connection connection, Long idViaje) throws InstanceNotFoundException{
		String queryString = "DELETE FROM Viaje WHERE idViaje = ?";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
			int i = 1;
			preparedStatement.setLong(i++,  idViaje);
			int removedRows = preparedStatement.executeUpdate();
			
			if (removedRows == 0){
				throw new InstanceNotFoundException(idViaje,Viaje.class.getName());
			}
			
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Viaje> viajesConductor(Connection connection, Long idConductor) throws InstanceNotFoundException, ConductorNoEncontradoException{
		String queryString = "SELECT idViaje, idConductor, origen, destino, idUsuario, tarjetaCredito, fechaReserva, puntuacion FROM viaje WHERE idConductor = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
			int i = 1;
			boolean conductorEncontrado = false;
			preparedStatement.setLong(i++, idConductor);
			 ResultSet resultSet = preparedStatement.executeQuery();

	            List<Viaje> viajes = new ArrayList<Viaje>();
	            
	            while(resultSet.next()){
					i = 1;
					Long idViaje = resultSet.getLong(i++);
					Long idConductorQuery = resultSet.getLong(i++);
					String origen = resultSet.getString(i++);
					String destino = resultSet.getString(i++);
					String idUsuario = resultSet.getString(i++);
					String tarjetaCredito = resultSet.getString(i++);
					Calendar fechaReserva = Calendar.getInstance();
					fechaReserva.setTime(resultSet.getTimestamp(i++));
					int puntuacion = resultSet.getInt(i++);
					conductorEncontrado = true;
					
					viajes.add(new Viaje(idViaje, idConductorQuery, origen, destino, idUsuario, tarjetaCredito, fechaReserva, puntuacion));		
				}
	            
	            if(!conductorEncontrado){
	            	throw new ConductorNoEncontradoException();
	            }
	            
				return viajes;
				
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public List<Viaje> viajesUsuario(Connection connection, String idUsuario) throws InstanceNotFoundException{
		String queryString = "SELECT idUsuario, idViaje, idConductor, origen, destino,  tarjetaCredito, fechaReserva, puntuacion FROM viaje WHERE idUsuario = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
			int i = 1;
			preparedStatement.setString(i++, idUsuario);
			 ResultSet resultSet = preparedStatement.executeQuery();
	            
	            List<Viaje> viajes = new ArrayList<Viaje>();
	            
	            while(resultSet.next()){
					i = 1;
					
					String idUsuarioQuery = resultSet.getString(i++);
					Long idViaje = resultSet.getLong(i++);
					Long idConductor = resultSet.getLong(i++);
					String origen = resultSet.getString(i++);
					String destino = resultSet.getString(i++);
					String tarjetaCredito = resultSet.getString(i++);
					Calendar fechaReserva = Calendar.getInstance();
					fechaReserva.setTime(resultSet.getTimestamp(i++));
					int puntuacion = resultSet.getInt(i++);
					
					viajes.add(new Viaje(idViaje, idConductor, origen, destino, idUsuarioQuery, tarjetaCredito, fechaReserva, puntuacion));		
				}
	            
				return viajes;
				
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
		
	}
}
