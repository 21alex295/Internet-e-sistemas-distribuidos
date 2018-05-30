package es.udc.ws.app.client.ui;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.client.service.ClientConductorService;
import es.udc.ws.app.client.service.ClientConductorServiceFactory;
import es.udc.ws.app.client.service.ClientUsuarioService;
import es.udc.ws.app.client.service.ClientUsuarioServiceFactory;
import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.app.client.service.dto.ClientConductoresDeUsuarioDto;
import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.app.client.service.exception.ConductorFueraDeHoraException;
import es.udc.ws.app.client.service.exception.ConductorInactivoException;
import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.app.client.service.exception.PuntuacionRepetidaException;
import es.udc.ws.app.client.service.exception.ViajeDeOtroUsuarioException;
import es.udc.ws.app.client.service.exception.ViajeNoExisteException;

import es.udc.ws.util.exceptions.InputValidationException;

public class UsuarioServiceClient {

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsageAndExit();
		}

		ClientUsuarioService clientViajeService = ClientUsuarioServiceFactory.getService();
		ClientUsuarioService clientConductoresDeUsuarioService =  ClientUsuarioServiceFactory.getService();
		ClientConductorService clientConductorService = ClientConductorServiceFactory.getService();

		if ("-addTravel".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] { 1 });

			// [añadir] ViajeServiceClient -a <idConductor> <origen> <destino>
			// <idUsuario> <tarjetaCredito>

			try {
				ClientViajeDto clientViajeDto = new ClientViajeDto(null, Long.valueOf(args[1]), args[2], args[3],
						args[4], args[5]);
				
				ClientConductorDto conductor;
				conductor = clientConductorService.verConductor(Long.valueOf(args[1]));
				
				
				byte horaInicio = conductor.getHoraInicio();
				byte horaFin = conductor.getHoraFin();
				
				Calendar fechaReserva = Calendar.getInstance();

				int horaQuiero = fechaReserva.get(Calendar.HOUR_OF_DAY);
								
				if(horaQuiero<horaInicio || horaQuiero>horaFin){
					throw new ConductorFueraDeHoraException();
				}

				if(conductor.getTotalViajes()>0 && conductor.getPuntuacionAcumulada()>=0){
					double media = conductor.getPuntuacionAcumulada()/conductor.getTotalViajes();
					
					if(media<5){
						throw new ConductorInactivoException();
					}
					
				}

				ClientViajeDto viajeDto = clientViajeService.contratarViaje(clientViajeDto);
				// System.out.println(args[1]);
				System.out.println("Viaje " + viajeDto.getIdViaje() + " contratado exitosamente");
			}catch (ConductorFueraDeHoraException e){
				System.out.println("Conductor fuera de hora de trabajo");
			}catch (ConductorInactivoException e){
					System.out.println("Conductor Inactivo");
			
			} catch (ConductorNoEncontradoException e){
				System.out.println("Conductor no encontrado");
			}catch(InputValidationException e){
				System.out.println("Tarjeta no válida");
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-addTravelPoints".equalsIgnoreCase(args[0])) {
			validateArgs(args, 4, new int[] { 1, 3 });

			// [puntuar] ViajeServiceClient -p <idViaje> <idUsuario>
			// <puntuacion>
			
			try {
				clientViajeService.puntuarViaje(Long.valueOf(args[1]), args[2], Integer.valueOf(args[3]));
				System.out.println("Viaje " + args[1] + " puntuado exitosamente");
			}catch (ViajeNoExisteException e){
				System.out.println("Viaje no existe");
			}catch (ViajeDeOtroUsuarioException e){
				System.out.println("Viaje de otro usuario");
			}catch (PuntuacionRepetidaException e){
					System.out.println("Viaje ya puntuado");
				
			} catch (NumberFormatException | InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			} 
		} else if ("-findTravelsByUser".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] {});

			// [ver viajes] ViajeServiceClient -s <idUsuario>

			try {
				List<ClientViajeDto> listaViajes = clientViajeService.viajesPorUsuario(args[1]);
				if(listaViajes.isEmpty()){
					System.out.println("Viajes NO encontrados!");
				}else{
					System.out.println("Viajes encontrados!");
				}

				for (ClientViajeDto i : listaViajes) {
					System.out.println("--->idViaje: " + i.getIdViaje() + "\n ");
					System.out.println("	 idConductor: " + i.getIdConductor() + " \n");
					System.out.println("	 Origen: " + i.getOrigen() + "\n ");
					System.out.println("	 Destino: " + i.getDestino() + "\n ");
					System.out.println("	 idUsuario: " + i.getIdUsuario() + "\n ");
					System.out.println("	 Puntuacion: " + i.getPuntuacion() + "\n");

				}
			

			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-findDriversByUser".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] {});

			// [ver conductores] ViajeServiceClient -c <idUsuario>

			try {
				List<ClientConductoresDeUsuarioDto> listaConductores = clientConductoresDeUsuarioService.conductoresDeUsuario(args[1]);
				System.out.println("Conductores encontrados para : " + args[1]);
				for (ClientConductoresDeUsuarioDto i : listaConductores) {
					System.out.println("--->idConductores: " + i.getIdConductor() + "\n ");
					System.out.println("	 idUsuario: " + i.getIdUsuario() + " \n");
					System.out.println("	 Nombre: " + i.getNombre() + "\n ");
					System.out.println("	 Ciudad: " + i.getCiudad() + "\n ");
					System.out.println("	 Puntuacion Media: " + i.getMediaPersonal() + "\n");

				}

			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}else if ("-findAvailableDrivers".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] {});

			// [ver conductores] ViajeServiceClient -c <ciudad>

			try {
				List<ClientConductorDto> listaConductores = clientViajeService.encontrarConductoresDisponibles(args[1]);
				
				if(listaConductores.isEmpty()){
				System.out.println("No se encontraron conductores para : " + args[1]);
				}else{
					System.out.println("Conductores encontrados para : " + args[1]);
				}
				for (ClientConductorDto i : listaConductores) {
					System.out.println("--->idConductores: " + i.getIdConductor() + "\n ");
					System.out.println("	 Nombre: " + i.getNombre() + "\n ");
					System.out.println("	 Ciudad: " + i.getCiudad() + "\n ");
					System.out.println("	 ModeloCoche: " + i.getModeloCoche() + "\n ");
					System.out.println("	 HoraInicio: " + i.getHoraInicio() + "\n ");
					System.out.println("	 HoraFin: " + i.getHoraFin() + "\n ");
					System.out.println("	 PuntuacionAcumulada: " + i.getPuntuacionAcumulada() + "\n ");


				}

			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}

	}

	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {

		if (expectedArgs != args.length) {
			printUsageAndExit();
		}

		for (int i = 0; i < numericArguments.length; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}
	
	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n"
				+ "    [addTravel]      ViajeServiceClient -a <idConductor> <origen> <destino> <idUsuario> <tarjetaCredito>\n "
				+ "    [addTravelPoints]        ViajeServiceClient -addTravelPoints <idViaje> <idUsuario> <puntuacion>" 
				+ "    [findDriversByUser]      ViajeServiceClient -findDriversByUser <idUsuario>\n "
				+ "    [findTravelsByUser]      ViajeServiceClient -findTravelsByUser <idUsuario>\n "
				+ "    [findAvailableDrivers]      ViajeServiceClient -findAvailableDrivers <ciudad>\n ");
	}
}
