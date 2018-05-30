package es.udc.ws.app.model.conductorservice;

import javax.sql.DataSource;

import es.udc.ws.app.model.conductor.Conductor;
import es.udc.ws.app.model.conductor.SqlConductorDao;
import es.udc.ws.app.model.conductor.SqlConductorDaoFactory;
import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.app.model.exceptions.PuntuacionRepetidaException;
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
		if (conductor.getNombre() != null){
			PropertyValidator.validateMandatoryString("nombre", conductor.getNombre());
		}
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
	public Conductor buscarConductor(Long idConductor) throws InstanceNotFoundException,ConductorNoEncontradoException {
		try (Connection connection = dataSource.getConnection()) {
			return conductorDao.buscar(connection, idConductor);
		} catch(ConductorNoEncontradoException e){
			throw e;
		}catch (SQLException e) {
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
		
		PropertyValidator.validateMandatoryString("ciudad", conductor.getCiudad());
		PropertyValidator.validateMandatoryString("modeloCoche", conductor.getModeloCoche());
		PropertyValidator.validateLong("horaInicio", (long)conductor.getHoraInicio(), 0, 23);
		PropertyValidator.validateLong("horaFin", (long)conductor.getHoraFin(), 0, 23);
		
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
	public List<Conductor> buscarPorCiudad(String ciudad) /*throws InstanceNotFoundException*/ {
				
		Calendar fecha = Calendar.getInstance();
		byte hora = (byte) (fecha.get(Calendar.HOUR_OF_DAY));
		
		try (Connection connection = dataSource.getConnection()) {
			return conductorDao.buscarPorCiudad(connection, ciudad, hora);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Viaje contratarViaje(Long idConductor, String origen, String destino, String idUsuario, String tarjetaCredito)
			throws InputValidationException, InstanceNotFoundException,ConductorNoEncontradoException{
		
		Calendar fechaReserva = Calendar.getInstance();

		int puntuacion = -1;
		Viaje viaje = new Viaje(idConductor, origen, destino, idUsuario, tarjetaCredito, puntuacion);
		// Añadimos fechaReserva aparte, para evitar problema de
		// milisegundos, que se resuelve en set.Fecha
		viaje.setFechaReserva(fechaReserva);
		
			
		
		try (Connection connection = dataSource.getConnection()) {
			validarViaje(viaje);
			try {
				//conductor no es usado, pero su utilidad consiste en que fuerza el fallo si contratamos un viaje con un conductor que no existe
								
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				Conductor conductor = conductorDao.buscar(connection, idConductor);
				
				
				
				Calendar fecha = Calendar.getInstance();
				byte hora = (byte) (fecha.get(Calendar.HOUR_OF_DAY));
				Viaje viajeAnadido = null;
				if (hora <= conductor.getHoraFin() && hora >= conductor.getHoraInicio()){
					viajeAnadido = viajeDao.crear(connection, viaje);
				}
				
				connection.commit();
				return viajeAnadido;
			} catch(ConductorNoEncontradoException e){
				throw e;
			}catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Viaje buscarViaje(Long idViaje) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			Viaje viaje = viajeDao.buscar(connection, idViaje);
			return viaje;
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private void actualizarViaje(Viaje viaje) throws InstanceNotFoundException, InputValidationException {
		
		
		
		
		try (Connection connection = dataSource.getConnection()) {
			validarViaje(viaje);
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
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public void puntuarViaje(Long idViaje, int puntuacion) throws InstanceNotFoundException, InputValidationException, PuntuacionRepetidaException,ConductorNoEncontradoException{
		if (puntuacion >= 0){
			try (Connection connection = dataSource.getConnection()) {
			
				try{
					connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					connection.setAutoCommit(false);
					Viaje viaje = buscarViaje(idViaje);
					
					try{
						if(viaje.getPuntuacion() >= 0){
							throw new PuntuacionRepetidaException();
						}
					}finally{
						
					}
					
					
					viaje.setPuntuacion(puntuacion);
					validarViaje(viaje);
					System.out.println("idViaje en conductorService: " + viaje.getIdViaje() );

					Conductor conductor = buscarConductor(viaje.getIdConductor());
				
					if (conductor.getPuntuacionAcumulada() == -1){
						conductor.setPuntuacionAcumulada(0);
					}
					int puntuacionTotal = puntuacion + conductor.getPuntuacionAcumulada();
					conductor.setPuntuacionAcumulada(puntuacionTotal);
					conductor.setTotalViajes(conductor.getTotalViajes() + 1);
					
				//	System.out.println("conductor.puntuacionAcu en conductorService: " + conductor.getPuntuacionAcumulada() );
				//	System.out.println("conductor.totalViajes   en conductorService: " + conductor.getTotalViajes() );

					actualizarViaje(viaje);
					actualizarConductor(conductor);
					if((conductor.getPuntuacionAcumulada()/conductor.getTotalViajes()) < 5)
						System.out.println("usuario inactivo");
					connection.commit();
				}catch(ConductorNoEncontradoException e){
					throw e;
				
				}catch (InstanceNotFoundException e) {
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
		}else{
			throw new InputValidationException("Puntuacion invalida");
		}
	}
	
	@Override
	public List<Viaje> viajesConductor(Long idConductor) throws InstanceNotFoundException, ConductorNoEncontradoException{
		
		try (Connection connection = dataSource.getConnection()){
			return viajeDao.viajesConductor(connection, idConductor);
		}catch(ConductorNoEncontradoException e){
			throw new ConductorNoEncontradoException();
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
