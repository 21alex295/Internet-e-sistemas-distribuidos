 package es.udc.ws.app.model.conductor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlConductorDao extends AbstractSqlConductorDao{

	@Override
	public Conductor crear(Connection connection, Conductor conductor) {

		String queryString = "INSERT INTO conductor (nombre, ciudad, modeloCoche, horaInicio, horaFin, fechaAlta, "
				+" puntuacionAcumulada, totalViajes) VALUES (?,?,?,?,?,?,?,?)";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){
			
			int i = 1;
			preparedStatement.setString(i++, conductor.getNombre());
			preparedStatement.setString(i++, conductor.getCiudad());
			preparedStatement.setString(i++, conductor.getModeloCoche());
			preparedStatement.setByte(i++, conductor.getHoraInicio());			
			preparedStatement.setByte(i++, conductor.getHoraFin());	
			Timestamp fechaAlta = conductor.getFechaAlta() != null ? new Timestamp(conductor.getFechaAlta().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, fechaAlta);
			preparedStatement.setInt(i++, conductor.getPuntuacionAcumulada());			
			preparedStatement.setInt(i++, conductor.getTotalViajes());			
			
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			
			if (!resultSet.next()){
				throw new SQLException("JDBC driver did not return generated key");
			}

			Long idConductor = resultSet.getLong(1);
			
			return new Conductor(idConductor, conductor.getNombre(), conductor.getCiudad(), conductor.getModeloCoche(),conductor.getHoraInicio(),
					conductor.getHoraFin(), conductor.getFechaAlta(), conductor.getPuntuacionAcumulada(), conductor.getTotalViajes());
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}		
	}
	

}
