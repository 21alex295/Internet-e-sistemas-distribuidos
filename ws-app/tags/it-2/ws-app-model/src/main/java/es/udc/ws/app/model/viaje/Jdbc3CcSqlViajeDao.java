package es.udc.ws.app.model.viaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;



public class Jdbc3CcSqlViajeDao extends AbstractSqlViajeDao {
	
	@Override
	public Viaje crear(Connection connection, Viaje viaje){
		String queryString = "INSERT INTO Viaje (idConductor, origen, destino, idUsuario, "
				+"tarjetaCredito, fechaReserva, puntuacion) VALUES (?,?,?,?,?,?,?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){
			int i = 1;
			preparedStatement.setLong(i++, viaje.getIdConductor());
			preparedStatement.setString(i++, viaje.getOrigen());
			preparedStatement.setString(i++, viaje.getDestino());
			preparedStatement.setString(i++, viaje.getIdUsuario());
			preparedStatement.setString(i++, viaje.getTarjetaCredito());
			Timestamp fechaReserva = new Timestamp(viaje.getFechaReserva().getTime().getTime());
			preparedStatement.setTimestamp(i++, fechaReserva);
			preparedStatement.setInt(i++, viaje.getPuntuacion());
			preparedStatement.executeUpdate();
			
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (!resultSet.next()){
				throw new SQLException(
						"JDBC driver did not return generated key.");
			}
			Long idViaje = resultSet.getLong(1);
			
			return new Viaje(idViaje, viaje.getIdConductor(),viaje.getOrigen(), viaje.getDestino(), 
					viaje.getIdUsuario(),viaje.getTarjetaCredito(), viaje.getFechaReserva(), viaje.getPuntuacion());
			
		} catch (SQLException e){
			throw new RuntimeException(e);
		}

	}
	


}
