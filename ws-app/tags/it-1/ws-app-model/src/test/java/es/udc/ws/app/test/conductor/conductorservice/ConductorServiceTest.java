package es.udc.ws.app.test.conductor.conductorservice;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.BeforeClass;

import es.udc.ws.app.model.conductor.Conductor;
import es.udc.ws.app.model.conductorservice.ConductorService;
import es.udc.ws.app.model.conductorservice.ConductorServiceFactory;
import es.udc.ws.app.model.viaje.Jdbc3CcSqlViajeDao;
import es.udc.ws.app.model.viaje.SqlViajeDao;
import es.udc.ws.app.model.viaje.SqlViajeDaoFactory;
import es.udc.ws.app.model.viaje.Viaje;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import static es.udc.ws.app.model.util.ModelConstants.CONDUCTOR_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ConductorServiceTest {
	
	private final long NON_EXISTENT_CONDUCTOR_ID = -1;
	private final long NON_EXISTENT_VIAJE_ID = -1;
	private final String USER_ID = "ws-user";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD_NUMBER = "";

	
	private static ConductorService conductorService = null;
	private static SqlViajeDao viajeDao = null;
	private Long idConductor;
	@BeforeClass
	public static void init() {
		DataSource dataSource = new SimpleDataSource();
		
		DataSourceLocator.addDataSource(CONDUCTOR_DATA_SOURCE, dataSource);
		
		conductorService = ConductorServiceFactory.getService();
		viajeDao = SqlViajeDaoFactory.getDao();
	}
	
	private Conductor getValidConductor(String nombre) {
		return new Conductor(nombre,"Madrid","A4",(byte)3,(byte)5,-1,2);
	}
	private Conductor getValidConductor(String nombre, String ciudad) {
		return new Conductor(nombre,ciudad,"A4",(byte)11,(byte)21,400,2);
	}
	
	private Conductor getValidConductor(String nombre, String ciudad, byte horaIni, byte horaFin) {
		return new Conductor(nombre,ciudad,"A4",horaIni,horaFin,400,2);
	}
	
	private void borrarConductor(Long idConductor){
		try{
			conductorService.borrarConductor(idConductor);
		}catch(InstanceNotFoundException e){
			throw new RuntimeException(e);
		}
	}
	
	private Conductor crearConductor(Conductor conductor){
		
		Conductor conductorAnadido = null;
		try{
			conductorAnadido = conductorService.anadirConductor(conductor);
		}catch(InputValidationException e){
			throw new RuntimeException(e);
		}
		return conductorAnadido;
	}
	
	private Viaje getValidViaje(Long idConductor, String idUser){
		Calendar calendar = Calendar.getInstance();
		return new Viaje(idConductor, "a corua", "sdc",idUser, "1234567", calendar, 7);
	}
	
	public void borrarViaje(Long idViaje){
		DataSource dataSource = DataSourceLocator.getDataSource(CONDUCTOR_DATA_SOURCE);
		try(Connection connection = dataSource.getConnection()){
			try{
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				viajeDao.borrar(connection, idViaje);
				connection.commit();
			}catch(InstanceNotFoundException e){
				connection.commit();
				throw new RuntimeException(e);
			}catch(SQLException e){
				connection.rollback();
				throw new RuntimeException (e);
			}catch(RuntimeException | Error e){
				connection.rollback();
				throw e;
			}
		}catch(SQLException e){
			throw new RuntimeException (e);
		}
	}
	
	public void actualizarViaje(Viaje viaje){
		DataSource dataSource = DataSourceLocator.getDataSource(CONDUCTOR_DATA_SOURCE);
		 try (Connection connection = dataSource.getConnection()){
			 
			 try{
				 connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				 connection.setAutoCommit(false);
				 viajeDao.actualizar(connection, viaje);
				 connection.commit();
			 }catch(InstanceNotFoundException e){
				 connection.commit();
				 throw new RuntimeException (e);
			 }catch(SQLException e){
				 connection.rollback();
				 throw new RuntimeException(e);
			 }catch(RuntimeException | Error e){
				 connection.rollback();
				 throw e;
			 }
		 }catch (SQLException e){
			 throw new RuntimeException(e);
		 }
		
	}
	
	
	@Test
	public void testAnadirConductoryBuscarConductor() throws InputValidationException,InstanceNotFoundException{
		Conductor conductor = getValidConductor("Juan");
		Conductor conductorAnadido = null;
		
		conductorAnadido = conductorService.anadirConductor(conductor);
		Conductor conductorEncontrado = conductorService.buscarConductor(conductorAnadido.getIdConductor());
		idConductor = conductorAnadido.getIdConductor();
		
		assertEquals(conductorAnadido,conductorEncontrado);
		conductorService.borrarConductor(conductorAnadido.getIdConductor());
	}
	
	@Test 
	public void testAddInvalidConductor(){
		
		Conductor conductor = getValidConductor("User1","Ciudad1");
		Conductor conductorAnadido = null;
		boolean excepcionAtrapada = false;
		
		try{
			//Comprobacion de ciudad no nulo
			conductor.setCiudad(null);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprobacion de modeloCoche no nulo
			conductor = getValidConductor("User2","Ciudad2");
			conductor.setModeloCoche(null);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprueba que horaInicio está dentro del rango (negativo)
			conductor = getValidConductor("User3","Ciudad3");
			conductor.setHoraInicio((byte)-1);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprueba que horaInicio está dentro del rango (positivo)
			conductor = getValidConductor("User4","Ciudad4");
			conductor.setHoraInicio((byte)25);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprueba que horaFin está dentro del rango (negativo)
			conductor = getValidConductor("User5","Ciudad5");
			conductor.setHoraFin((byte)-1);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprueba que horaFin está dentro del rango (positivo)
			conductor = getValidConductor("User6","Ciudad6");
			conductor.setHoraFin((byte)25);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprobacion de puntuacionAcumulada no menor que (-1)
			conductor = getValidConductor("User8","Ciudad8");
			conductor.setPuntuacionAcumulada(-5);
			// Seteamos puntuacionAcumulada para que tome el valor -5, aunque cuando se añada en la BBDD tome el valor -1 automáticamente
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
				excepcionAtrapada=false;
			}catch (InputValidationException e){
				//excepcionAtrapada = false es redundante si entra aquí ya tiene ese valor, y no cambia
				excepcionAtrapada = true;
			}finally{
				borrarConductor(conductorAnadido.getIdConductor());
			}
			assertTrue(!excepcionAtrapada);
			excepcionAtrapada = false;
			
			//Comprobacion de totalViajes no menor que (-1)
			conductor = getValidConductor("User9","Ciudad9");
			conductor.setTotalViajes(-3);
			try{
				conductorAnadido = conductorService.anadirConductor(conductor);
			}catch (InputValidationException e){
				excepcionAtrapada = true;
			}
			assertTrue(excepcionAtrapada);		
			
		}finally{
			if (!excepcionAtrapada){
				borrarConductor(conductorAnadido.getIdConductor());
			}
		}
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBuscarConductorNoExistente() throws InstanceNotFoundException{
		conductorService.buscarConductor(NON_EXISTENT_CONDUCTOR_ID);
	}
	
	@Test
	public void testActualizar() throws InputValidationException, InstanceNotFoundException{
		Conductor conductor1 = getValidConductor("Juan", "coru");
		
		Conductor conductorAnadido = conductorService.anadirConductor(conductor1);
		conductorAnadido.setCiudad("sdc");
		conductorService.actualizarConductor(conductorAnadido);

		try{
			assertEquals(conductorAnadido.getPuntuacionAcumulada(), conductorService.buscarConductor(conductorAnadido.getIdConductor()).getPuntuacionAcumulada());
		}finally{
			conductorService.borrarConductor(conductorAnadido.getIdConductor());
		}

	}
	
	@Test(expected = InputValidationException.class)
	public void testActualizarConductorInvalido() throws InputValidationException,InstanceNotFoundException{
		
		Conductor conductor = crearConductor(getValidConductor("Yago"));
		
		try{
			conductor =  conductorService.buscarConductor(conductor.getIdConductor());
			conductor.setCiudad(null);
			conductorService.actualizarConductor(conductor);
		}finally{
			borrarConductor(conductor.getIdConductor());
		}
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testActualizarConductorNoExistente()throws InputValidationException,InstanceNotFoundException{
		Conductor conductor = getValidConductor("Alex");
		conductor.setIdConductor(NON_EXISTENT_CONDUCTOR_ID);
		conductor.setFechaAlta(Calendar.getInstance());
		conductorService.actualizarConductor(conductor);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBorrarConductor() throws InstanceNotFoundException{
		Conductor conductor = crearConductor(getValidConductor("Kike"));
		boolean excepcionCapturada = false;
		try{
			conductorService.borrarConductor(conductor.getIdConductor());
		}catch(InstanceNotFoundException e){
			excepcionCapturada=true;
		}
		assertTrue(!excepcionCapturada);
		conductorService.buscarConductor(conductor.getIdConductor());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBorrarConductorNoExistente()throws InstanceNotFoundException{
		conductorService.borrarConductor(NON_EXISTENT_CONDUCTOR_ID);
	}
	
	@Test
	public void testBuscarPorCiudad() throws InputValidationException, InstanceNotFoundException {
		
		Calendar fecha = Calendar.getInstance();
		byte hora = (byte) (fecha.get(Calendar.HOUR_OF_DAY));
		
		byte horaAntesValida = (byte) (hora - 2);
		byte horaDespuesValida = (byte) (hora + 2);
		
		
		if(horaAntesValida < 0){
			horaAntesValida += 24;
		}
		if (horaAntesValida > 23){
			horaAntesValida -= 24;
		}
		if(horaDespuesValida < 0){
			horaDespuesValida += 24;
		}
		if (horaDespuesValida > 23){
			horaDespuesValida -= 24;
		}
		
		
		Conductor conductor1 = getValidConductor("Kike92","coru",horaAntesValida,horaDespuesValida);
		Conductor conductor2 = getValidConductor("Pepe", "coru",horaAntesValida,horaDespuesValida);
		Conductor conductor3 = getValidConductor("Jose", "coru", (byte)3, (byte)5);
		Conductor conductor4 = getValidConductor("Julian", "Madrid");
		
		List<Conductor> lista = new ArrayList<Conductor>();
		Conductor conductorAnadido1 = null;
		Conductor conductorAnadido2 = null;
		Conductor conductorAnadido3 = null;
		Conductor conductorAnadido4 = null;
		
		 conductorAnadido1 = crearConductor(conductor1);
		 conductorAnadido2 = crearConductor(conductor2);
		 conductorAnadido3 = crearConductor(conductor3);
		 conductorAnadido4 = crearConductor(conductor4);
		
		 
		lista.add(conductorAnadido1);
		lista.add(conductorAnadido2);

		List<Conductor> listaObtenida = new ArrayList<Conductor>();
		
		try{
			listaObtenida = conductorService.buscarPorCiudad("coru");
			assertEquals(lista, listaObtenida);	
		}finally{
			borrarConductor(conductorAnadido1.getIdConductor());
			borrarConductor(conductorAnadido2.getIdConductor());
			borrarConductor(conductorAnadido3.getIdConductor());
			borrarConductor(conductorAnadido4.getIdConductor());
		}
	}
	
	@Test
	public void testContratarBuscarViaje()throws InstanceNotFoundException,InputValidationException{
		Conductor conductor = crearConductor(getValidConductor("Lazaro"));
		Viaje viaje = null;
		try{
			viaje = conductorService.contratarViaje(conductor.getIdConductor(), "Muros", "Cee", USER_ID, VALID_CREDIT_CARD_NUMBER);
			Viaje viajeEncontrado = conductorService.buscarViaje(viaje.getIdViaje());
			assertEquals(viaje,viajeEncontrado);
		}finally{
			if (viaje != null){
				borrarViaje(viaje.getIdViaje());
			}
			borrarConductor(conductor.getIdConductor());
		}
	}
	
	@Test(expected = InputValidationException.class)
	public void testContratarViajeTarjetaInvalida()throws InputValidationException,InstanceNotFoundException{
		Conductor conductor = crearConductor(getValidConductor("Señora"));
		try{
			Viaje viaje = conductorService.contratarViaje(conductor.getIdConductor(), "Aqui", "alla", "kike92", INVALID_CREDIT_CARD_NUMBER);
			borrarViaje(viaje.getIdViaje());
		}finally{
			borrarConductor(conductor.getIdConductor());
		}
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testContratarViajeConductorNoExistente()throws InputValidationException, InstanceNotFoundException{
		//Deberiamos usar NON_EXISTENT_CONDUCTOR_ID, pero usamos un 99, ya que la constante toma un valor negativo, que no permitimos en el campo Id
		Viaje viaje = conductorService.contratarViaje((long)99, "AQUI", "alla", "kike92", VALID_CREDIT_CARD_NUMBER);
		borrarViaje(viaje.getIdViaje());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBuscarViajeNoExistente()throws InstanceNotFoundException{
		Viaje viaje = conductorService.buscarViaje(NON_EXISTENT_VIAJE_ID);
	}
	
	@Test
	public void testPuntuarViaje() throws InputValidationException, InstanceNotFoundException{
		Conductor conductor = conductorService.anadirConductor(getValidConductor("juanete"));
		Viaje viaje = conductorService.contratarViaje(conductor.getIdConductor(), "AQUI", "alla", "kike92", VALID_CREDIT_CARD_NUMBER);
		conductorService.puntuarViaje(viaje.getIdViaje(), 10);
		Viaje viajePuntuado = conductorService.buscarViaje(viaje.getIdViaje());
		assertEquals(10, viajePuntuado.getPuntuacion());
		borrarConductor(conductor.getIdConductor());
		borrarViaje(viaje.getIdViaje());
	}
	
	
	@Test
	public void testConductorInactivo() throws InputValidationException, InstanceNotFoundException{
		Conductor conductor = conductorService.anadirConductor(getValidConductor("juanete"));
		Viaje viaje = conductorService.contratarViaje(conductor.getIdConductor(), "aqui", "alla", "alex94", VALID_CREDIT_CARD_NUMBER);
		conductorService.puntuarViaje(viaje.getIdViaje(),3);
		List<Conductor> lista = new ArrayList<Conductor>();
		List<Conductor> listaObtenida = new ArrayList<Conductor>();
		try{
			listaObtenida = conductorService.buscarPorCiudad("Madrid");
			assertEquals(lista, listaObtenida);	
		}finally{
			borrarConductor(conductor.getIdConductor());
			borrarViaje(viaje.getIdViaje());
		}
		
	}
	
	@Test
	public void testViajesConductor() throws InputValidationException, InstanceNotFoundException{
		Conductor conductor = conductorService.anadirConductor(getValidConductor("juanete"));
		Viaje viaje1 = conductorService.contratarViaje(conductor.getIdConductor(), "aqui", "alla", "alex94", VALID_CREDIT_CARD_NUMBER);
		Viaje viaje2 = conductorService.contratarViaje(conductor.getIdConductor(), "aqui", "alla", "alex94", VALID_CREDIT_CARD_NUMBER);

		List<Viaje> lista = new ArrayList<Viaje>();
		lista.add(viaje1);
		lista.add(viaje2);
		
		List<Viaje> listaObtenida = new ArrayList<Viaje>();////////
		try{
			listaObtenida =conductorService.viajesConductor(conductor.getIdConductor());
			assertEquals(lista, listaObtenida);	
		}finally{
			borrarConductor(conductor.getIdConductor());
			borrarViaje(viaje1.getIdViaje());
			borrarViaje(viaje2.getIdViaje());
		}
		
	}

	@Test
	public void testViajesUsuario() throws InputValidationException, InstanceNotFoundException{
		Conductor conductor1 = conductorService.anadirConductor(getValidConductor("paquito"));
		Conductor conductor2 = conductorService.anadirConductor(getValidConductor("chocolatero"));
		Viaje viaje1 = conductorService.contratarViaje(conductor1.getIdConductor(), "aqui", "alla", USER_ID, VALID_CREDIT_CARD_NUMBER);
		Viaje viaje2 = conductorService.contratarViaje(conductor2.getIdConductor(), "aqui", "alla", USER_ID, VALID_CREDIT_CARD_NUMBER);
	
		List<Viaje> lista = new ArrayList<Viaje>();
		lista.add(viaje1);
		lista.add(viaje2);
		
		List<Viaje> listaObtenida = new ArrayList<Viaje>();////////
		try{
			listaObtenida =conductorService.viajesUsuario(USER_ID);
			assertEquals(lista, listaObtenida);	
		}finally{
			borrarConductor(conductor1.getIdConductor());
			borrarConductor(conductor2.getIdConductor());
			borrarViaje(viaje1.getIdViaje());
			borrarViaje(viaje2.getIdViaje());
		}
		
	}
}


