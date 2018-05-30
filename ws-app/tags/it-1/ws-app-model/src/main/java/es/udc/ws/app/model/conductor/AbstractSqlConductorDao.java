package es.udc.ws.app.model.conductor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public abstract class AbstractSqlConductorDao implements SqlConductorDao {
	
	@Override
	public Conductor buscar (Connection connection, Long idConductor) throws InstanceNotFoundException{
		String queryString = "SELECT nombre,ciudad,modeloCoche,horaInicio,horaFin,fechaAlta,puntuacionAcumulada,totalViajes FROM Conductor WHERE idConductor = ? ";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			
			int i=1;
			preparedStatement.setLong(i++, idConductor.longValue());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(!resultSet.next()){
				throw new InstanceNotFoundException(idConductor,
                        Conductor.class.getName());
			}
			
			i = 1;
			String nombre = resultSet.getString(i++);
			String ciudad = resultSet.getString(i++);
			String modeloCoche = resultSet.getString(i++);
			byte horaInicio = resultSet.getByte(i++);
			byte horaFin = resultSet.getByte(i++);
			Calendar fechaAlta = Calendar.getInstance();
			fechaAlta.setTime(resultSet.getTimestamp(i++));
			int puntuacionAcumulada = resultSet.getInt(i++);
			int totalViajes = resultSet.getInt(i++);
			
			return new Conductor(idConductor,nombre, ciudad, modeloCoche,horaInicio, horaFin, fechaAlta, puntuacionAcumulada,totalViajes);

			
			
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void borrar(Connection connection, Long idConductor) throws InstanceNotFoundException{
		
		String queryString = "DELETE FROM Conductor WHERE" + " idConductor = ?";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
			
			int i = 1;
			preparedStatement.setLong(i++, idConductor);
			int removedRows = preparedStatement.executeUpdate();
			if (removedRows == 0) {
				throw new InstanceNotFoundException(idConductor, 
						Conductor.class.getName());		
			}


		}catch (SQLException e){
			throw new RuntimeException(e);
		}
		
		
	}
public List<Conductor> buscarPorCiudad(Connection connection, String ciudad) throws InstanceNotFoundException {
		
		Calendar fecha = Calendar.getInstance();
		byte hora = (byte) (fecha.get(Calendar.HOUR_OF_DAY));

        /* Create "queryString". */
        String queryString = "SELECT idConductor, nombre, ciudad, modeloCoche, horaInicio, horaFin,fechaAlta, puntuacionAcumulada,totalViajes "
        				   + "FROM Conductor "
        				   + "WHERE (puntuacionAcumulada = -1 OR puntuacionAcumulada/totalViajes>=5) AND (ciudad = ? AND ? BETWEEN horaInicio AND horaFin)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

        	int i=1;
            preparedStatement.setString(i++, ciudad);
            preparedStatement.setByte(i++, hora);
            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Conductor> conductores = new ArrayList<Conductor>();

            while (resultSet.next()){
                i = 1; 
                Long idConductor = resultSet.getLong(i++);
                String nombre = resultSet.getString(i++);
    			String city = resultSet.getString(i++);
    			String modeloCoche = resultSet.getString(i++);
    			byte horaInicio = resultSet.getByte(i++);
    			byte horaFin = resultSet.getByte(i++);
    			Calendar fechaAlta = Calendar.getInstance();
    			fechaAlta.setTime(resultSet.getTimestamp(i++));
    			int puntuacionAcumulada = resultSet.getInt(i++);
    			int viajesTotales = resultSet.getInt(i++);

                conductores.add(new Conductor(idConductor, nombre, city, modeloCoche, horaInicio, horaFin, fechaAlta, puntuacionAcumulada, viajesTotales));
            }
            
       
            /* Return conductores. */
            return conductores;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
	@Override
	public void actualizar(Connection connection, Conductor conductor) throws InstanceNotFoundException{
		
		/* Creamos la query de actualización */
        String queryString = "UPDATE Conductor"
                + " SET  ciudad = ?, modeloCoche = ?, horaInicio = ?, horaFin = ?"
                + " WHERE idConductor = ?";
        

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
        	
        	int i = 1;
        	preparedStatement.setString(i++, conductor.getCiudad());
        	preparedStatement.setString(i++, conductor.getModeloCoche());
        	preparedStatement.setByte(i++, conductor.getHoraInicio());
        	preparedStatement.setByte(i++, conductor.getHoraFin()); 	
        	preparedStatement.setLong(i++, conductor.getIdConductor());
        	
        	/*Ejecutamos la query*/
        	int updatedRows = preparedStatement.executeUpdate();
        	
        	if (updatedRows == 0) {
        		throw new InstanceNotFoundException(conductor.getIdConductor(),
        				Conductor.class.getName());
        	}
        	
        } catch(SQLException e){
        	throw new RuntimeException(e);
        }
		
	}

}
