package es.udc.ws.app.model.conductorservice;

import javax.sql.DataSource;

import es.udc.ws.app.model.conductor.Conductor;
import es.udc.ws.app.model.conductor.SqlConductorDao;
import es.udc.ws.app.model.conductor.SqlConductorDaoFactory;
import es.udc.ws.app.model.viaje.SqlViajeDao;
import es.udc.ws.app.model.viaje.SqlViajeDaoFactory;
import es.udc.ws.app.model.viaje.Viaje;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.app.model.util.ModelConstants.CONDUCTOR_DATA_SOURCE;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class ConductorServiceImpl implements ConductorService {

	private final DataSource dataSource;
	private SqlConductorDao conductorDao = null;
	private SqlViajeDao viajeDao = null;

	public ConductorServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(CONDUCTOR_DATA_SOURCE);
		conductorDao = SqlConductorDaoFactory.getDao();
		viajeDao = SqlViajeDaoFactory.getDao();
	}
	
	private void validarConductor(Conductor conductor) throws InputValidationException{
		PropertyValidator.validateMandatoryString("nombre", conductor.getNombre());
		PropertyValidator.validateMandatoryString("ciudad", conductor.getCiudad());
		PropertyValidator.validateMandatoryString("modeloCoche", conductor.getModeloCoche());
		PropertyValidator.validateLong("horaInicio", (long)conductor.getHoraInicio(), 0, 23);
		PropertyValidator.validateLong("horaFin", (long)conductor.getHoraFin(), 0, 23);
		PropertyValidator.validatePastDate("fechaAlta", conductor.getFechaAlta());
		PropertyValidator.validateNotNegativeLong("totalViajes", (long)conductor.getTotalViajes());
	}
	
	private void validarViaje(Viaje viaje) throws InputValidationException{
		PropertyValidator.validateMandatoryString("origen", viaje.getOrigen());
		PropertyValidator.validateMandatoryString("destino", viaje.getDestino());
		PropertyValidator.validateCreditCard(viaje.getTarjetaCredito());
		PropertyValidator.validateNotNegativeLong("idConductor", viaje.getIdConductor());
		PropertyValidator.validateMandatoryString("idUsuario", viaje.getIdUsuario());
		PropertyValidator.validateLong("puntuacion", viaje.getPuntuacion(),-1,10);
	}


	@Override
	public Conductor anadirConductor(Conductor conductor) throws InputValidationException {
		
		
		conductor.setFechaAlta(Calendar.getInstance()); //obtenemos los datos de fecha del sistema
		validarConductor(conductor);
		
		conductor.setPuntuacionAcumulada(-1);
		conductor.setTotalViajes(0);
		
		if (conductor.getPuntuacionAcumulada()< (-1)){
			throw new InputValidationException("Invalid: puntuacionAcumulada must be greater than -1");
		}

			try(Connection connection = dataSource.getConnection()){
				try  {
				//Controla el comportamiento del bloqueo y de las versiones de fila de las instrucciones Transact-SQL
				/*Las instrucciones no pueden leer datos que hayan sido modificados, pero aún no confirmados, por otras transacciones.
				Ninguna otra transacción puede modificar los datos leídos por la transacción actual hasta que la transacción actual finalice.
				Otras transacciones no pueden insertar filas nuevas con valores de clave que pudieran estar incluidos en el intervalo de claves 
				leído por las instrucciones de la transacción actual hasta que ésta finalice.
				*/
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				//Establece el modo de confirmación automática para este SQLServerConnection objeto 
				//en el estado determinado, en este caso lo deshabilitamos
				connection.setAutoCommit(false);
				
				Conductor crearConductor = conductorDao.crear(connection, conductor);
				
				connection.commit();
				
				return crearConductor;
				
				}catch (SQLException e){
					connection.rollback();
					throw new RuntimeException(e);
				}catch(RuntimeException | Error e){
					connection.rollback();
					throw e;
				}
			}catch(SQLException e){
				throw new RuntimeException(e);
			}
		}

	@Override
	public Conductor buscarConductor(Long idConductor) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			return conductorDao.buscar(connection, idConductor);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void borrarConductor(Long idConductor) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			try {

				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				conductorDao.borrar(connection, idConductor);
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException();
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void actualizarConductor(Conductor conductor) throws InstanceNotFoundException, InputValidationException {
		
		validarConductor(conductor);
		
		if (conductor.getPuntuacionAcumulada() < (-1)){
			throw new InputValidationException("Invalid: puntuacionAcumulada must be greater than -1");
		}
		
		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				conductorDao.actualizar(connection, conductor);
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException();
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Conductor> buscarPorCiudad(String ciudad) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			return conductorDao.buscarPorCiudad(connection, ciudad);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Viaje contratarViaje(Long idConductor, String origen, String destino, String idUsuario, String tarjetaCredito)
			throws InputValidationException, InstanceNotFoundException {
		
		Calendar fechaReserva = Calendar.getInstance();

		int puntuacion = -1;
		Viaje viaje = new Viaje(idConductor, origen, destino, idUsuario, tarjetaCredito, puntuacion);
		// Añadimos fechaReserva aparte, para evitar problema de
		// milisegundos, que se resuelve en set.Fecha
		viaje.setFechaReserva(fechaReserva);
		validarViaje(viaje);
		
		try (Connection connection = dataSource.getConnection()) {
			try {
				//conductor no es usado, pero su utilidad consiste en que fuerza el fallo si contratamos un viaje con un conductor que no existe
				Conductor conductor = conductorDao.buscar(connection, idConductor);
								
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				Viaje viajeAnadido = viajeDao.crear(connection, viaje);
				connection.commit();
				return viajeAnadido;
			/*} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;*/
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Viaje buscarViaje(Long idViaje) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			Viaje viaje = viajeDao.buscar(connection, idViaje);
			return viaje;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private void actualizarViaje(Viaje viaje) throws InstanceNotFoundException, InputValidationException {
		
		validarViaje(viaje);
		
		
		
		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				viajeDao.actualizar(connection, viaje);
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException();
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public void puntuarViaje(Long idViaje, int puntuacion) throws InstanceNotFoundException, InputValidationException{
		if (puntuacion >= 0){
			Viaje viaje = buscarViaje(idViaje);
			viaje.setPuntuacion(puntuacion);
			Conductor conductor = buscarConductor(viaje.getIdConductor());
			if (conductor.getPuntuacionAcumulada() == -1){
				conductor.setPuntuacionAcumulada(0);
			}
			int puntuacionTotal = puntuacion + conductor.getPuntuacionAcumulada();
			conductor.setPuntuacionAcumulada(puntuacionTotal);
			conductor.setTotalViajes(conductor.getTotalViajes() + 1);
		
			actualizarViaje(viaje);
			actualizarConductor(conductor);
			if((conductor.getPuntuacionAcumulada()/conductor.getTotalViajes()) < 5){
				System.out.println("usuario inactivo");
			}
		}else{
			throw new InputValidationException("Puntuacion invalida");
		}
	}
	
	@Override
	public List<Viaje> viajesConductor(Long idConductor) throws InstanceNotFoundException{
		
		try (Connection connection = dataSource.getConnection()){
			return viajeDao.viajesConductor(connection, idConductor);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public List<Viaje> viajesUsuario(String idUsuario) throws InstanceNotFoundException{
		
		try (Connection connection = dataSource.getConnection()){
			return viajeDao.viajesUsuario(connection, idUsuario);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	

}
