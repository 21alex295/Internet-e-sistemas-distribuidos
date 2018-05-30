package es.udc.ws.app.client.ui;

import java.util.List;

import es.udc.ws.app.client.service.ClientConductorService;
import es.udc.ws.app.client.service.ClientConductorServiceFactory;
import es.udc.ws.app.client.service.ClientUsuarioService;
import es.udc.ws.app.client.service.ClientUsuarioServiceFactory;
import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.app.client.service.exception.ConductorHoraValidaException;
import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.app.client.service.exception.ListaVaciaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class ConductorServiceClient {
	
	public static void main(String[] args) {
		
		if(args.length == 0) {
            printUsageAndExit();
        }
		
		ClientConductorService clientConductorService = ClientConductorServiceFactory.getService();

        if("-addDriver".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {4,5});

            // [add] ConductorServiceClient -addDriver <nombre> <ciudad> <modeloCoche> <horaInicio> <horaFin>

            try {
            	
                validarHoras(Byte.valueOf(args[4]),Byte.valueOf(args[5]));
                Long idConductor = clientConductorService.anadirConductor(new ClientConductorDto(null,
                        args[1], args[2], args[3],Byte.valueOf(args[4]), Byte.valueOf(args[5])));

                System.out.println("Conductor " + idConductor + " creado exitosamente");
            } catch (ConductorHoraValidaException e){
            	System.out.println("Hora invalida");
            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-deleteDriver".equalsIgnoreCase(args[0])){
        	validateArgs(args,2,new int[]{1});
        	
        	try{
        		clientConductorService.borrarConductor(Long.parseLong(args[1]));
        		System.out.println("Conductor con id: "+args[1]+" borrado exitosamente");
        	}catch(NumberFormatException | InstanceNotFoundException ex){
        		ex.printStackTrace(System.err);
        	}catch(Exception ex){
        		ex.printStackTrace(System.err);
        	}
        	
        }else if("-updateDriver".equalsIgnoreCase(args[0])){
        	validateArgs(args, 7, new int[] {1,5,6});

        	try{
        		validarHoras(Byte.valueOf(args[5]),Byte.valueOf(args[6]));
        		int acumulada = clientConductorService.verConductor(Long.valueOf(args[1])).getPuntuacionAcumulada();
        		int totalViajes = clientConductorService.verConductor(Long.valueOf(args[1])).getTotalViajes();

        		clientConductorService.actualizarConductor(new ClientConductorDto(Long.valueOf(args[1]),args[2],args[3],args[4],
        				Byte.valueOf(args[5]),Byte.valueOf(args[6]),acumulada, totalViajes));
        		System.out.println("Conductor "+args[1]+" actualizado");
        	} catch(ConductorNoEncontradoException e){
        		System.out.println("Conductor no existe");
        	} catch (ConductorHoraValidaException e){
            	System.out.println("Hora invalida");
        	}catch(NumberFormatException | InputValidationException | InstanceNotFoundException ex ){
        		ex.printStackTrace(System.err);
        	}catch(Exception e){
        		e.printStackTrace(System.err);
        	}
        	
        	//[ver viajes]  ConductorServiceClient -v <idConductor> 
        }else if("-findTravelsByDriver".equalsIgnoreCase(args[0])){
        	validateArgs(args, 2, new int[] {1});

        	try{
        		List<ClientViajeDto> listaViajes = clientConductorService.viajesPorConductor(Long.valueOf(args[1]));
        		//int i = 0;
        		if(listaViajes.isEmpty()){
        			throw new ListaVaciaException();
        			
        		}
        		for(ClientViajeDto i:listaViajes){
        			System.out.println("---> idViaje: "+i.getIdViaje()+"\n ");
        			System.out.println("	 idConductor: "+i.getIdConductor()+" \n");
           			System.out.println("	 Origen: "+i.getOrigen()+"\n ");
           			System.out.println("	 Destino: "+i.getDestino()+"\n ");
           			System.out.println("	 idUsuario: "+i.getIdUsuario()+"\n ");
           			System.out.println("	 Puntuacion: "+i.getPuntuacion()+"\n");


        		}
        	}catch (ListaVaciaException e){
				System.out.println("No hay conductores");
        	}catch (ConductorNoEncontradoException e){
				System.out.println("Conductor no existe");
        	}catch(NumberFormatException | InstanceNotFoundException ex ){
        		ex.printStackTrace(System.err);
        	}catch(Exception e){
        		e.printStackTrace(System.err);
        	}
        }else if("-findDriver".equalsIgnoreCase(args[0])){
        	validateArgs(args, 2, new int[] {1});

        	try{
        		ClientConductorDto conductorDto = clientConductorService.verConductor(Long.valueOf(args[1]));
    			System.out.println("--->idConductor: "+conductorDto.getIdConductor()+"\n ");
    			System.out.println("	 Nombre: "+conductorDto.getNombre()+" \n");
       			System.out.println("	 Ciudad: "+conductorDto.getCiudad()+"\n ");
       			System.out.println("	 Modelo de coche: "+conductorDto.getModeloCoche()+"\n ");
       			System.out.println("	 Hora Inicio: "+conductorDto.getHoraInicio()+"\n ");
       			System.out.println("	 Hora Fin: "+conductorDto.getHoraFin()+"\n");
       			if (conductorDto.getPuntuacionAcumulada() != -1 && conductorDto.getTotalViajes()!=0){
       			System.out.println("	 Puntuacion media: "+ 
       					conductorDto.getPuntuacionAcumulada()/conductorDto.getTotalViajes()+"\n");
       			}else{
       				System.out.println("	 Puntuacion media: Sin puntuar");
       			}

        	}catch(NumberFormatException | InstanceNotFoundException ex ){
        		ex.printStackTrace(System.err);
        	}catch(Exception e){
        		e.printStackTrace(System.err);
        	}
        }	
        	//[ver viajes]  ConductorServiceClient -v <idConductor> 
        
        
	}
	
    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {

		if(expectedArgs != args.length) {
			printUsageAndExit();
		}

		for(int i = 0 ; i< numericArguments.length ; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch(NumberFormatException n) {
				printUsageAndExit();
			}
		}
    }
    
    public static void validarHoras(Byte horaInicio ,Byte horaFin) throws ConductorHoraValidaException{   	
    	try{
	    	if((horaInicio<0 || horaInicio>23) || (horaFin<0 || horaFin>23)){
	    		throw new ConductorHoraValidaException();
	    	}
    	}catch(ConductorHoraValidaException e){
    		throw new ConductorHoraValidaException();
    	}
    	
    }
	
    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addDriver]      ConductorServiceClient -addDriver <nombre> <ciudad> <modeloCoche> <horaInicio> <horaFin>\n"+
        		"	 [deleteDriver]      ConductorServiceClient -deleteDriver <idConductor>\n" +
        		"	 [findDriver]   ConductorServiceClient -findDriver <idConductor>\n" +
                "	 [updateDriver]  CondcutorServiceClient -updateDriver <idConductor> <nombre> <ciudad> <modeloCoche> <horaInicio> <horaFin>\n "
                + "  [findTravelsByDriver]  ConductorServiceClient -findTravelsByDriver <idConductor> ");
    }

}
