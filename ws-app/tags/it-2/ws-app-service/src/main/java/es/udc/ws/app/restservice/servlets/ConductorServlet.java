package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.udc.ws.app.dto.ServiceConductorDto;
import es.udc.ws.app.model.conductor.Conductor;
import es.udc.ws.app.model.conductorservice.ConductorServiceFactory;
import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.app.restservice.xml.XmlServiceConductorDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.serviceutil.ConductorToConductorDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

@SuppressWarnings("serial")
public class ConductorServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		
		ServiceConductorDto xmlConductor;
		
		try{
			//Intenta coller Stream da petición e convertilo a XML nun principio,e despois a DTO
			xmlConductor = XmlServiceConductorDtoConversor.toServiceConductorDto(request.getInputStream());
		}catch(ParsingException ex){
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
			return;
		}
		
		// Convirte o DTO a un conductor para añadilo
		Conductor conductor = ConductorToConductorDtoConversor.toConductor(xmlConductor);
		//System.out.println(conductor.getCiudad());
		
		// Añade o objeto(conductor)
		try{
			conductor = ConductorServiceFactory.getService().anadirConductor(conductor);
		}catch(InputValidationException ex){
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(ex),null);
			return;
		}
		// Desfai a conversión (conductor -> conductorDto)
		ServiceConductorDto conductorDto = ConductorToConductorDtoConversor.toConductorDto(conductor);
		
		//Crea unha URL e añádea a headers (almacena nombre e contenido)
		String conductorUrl = ServletUtils.normalizePath(request.getRequestURL().toString())+ "/"+ conductor.getIdConductor();
		Map<String,String> headers = new HashMap<>(1);
		headers.put("Location",conductorUrl);
		
		//Respuesta
		ServletUtils.writeServiceResponse(response,HttpServletResponse.SC_CREATED,XmlServiceConductorDtoConversor
				.toXml(conductorDto), headers);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		String path = ServletUtils.normalizePath(request.getPathInfo());
		if(path == null || path.length() == 0 ){
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException("Invalid Request: "+"Invalid idConductor")), null);
			return;
		}
		String conductorIdAsString= path.substring(1);
		Long idConductor;
		try{
			idConductor = Long.valueOf(conductorIdAsString);
		}catch(NumberFormatException ex){
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException("Invalid Request: "+"Invalid idConductor"
			+conductorIdAsString)), null);
			return;
		}
		
		try{
			ConductorServiceFactory.getService().borrarConductor(idConductor);
		}catch(InstanceNotFoundException ex){
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,XmlServiceExceptionConversor.
					toInstanceNotFoundException(ex), null);
			return;
		}
		
		ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NO_CONTENT, null, null);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		
		String path = ServletUtils.normalizePath(request.getPathInfo());
		if (path == null || path.length() == 0){
			String ciudad = request.getParameter("ciudad");
			System.out.println(ciudad);
			List<Conductor> conductor = ConductorServiceFactory.getService().buscarPorCiudad(ciudad);
			//System.out.println(conductor.get(0).getCiudad());
			List<ServiceConductorDto> conductorDtos = ConductorToConductorDtoConversor.toConductorDto(conductor);
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_OK, 
					XmlServiceConductorDtoConversor.toXml(conductorDtos), null);

		}else{
			String conductorIdAsString = path.substring(1);
			Long conductorId;
			try{
				conductorId = Long.valueOf(conductorIdAsString);
				System.out.println(conductorId);
				System.out.println(path.substring(1));

			}catch (NumberFormatException ex){
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,XmlServiceExceptionConversor.
						toInputValidationExceptionXml(new InputValidationException("Invalid request: " + "Invalid conductorId '"
								+ conductorIdAsString + " ' ")), null);
				return;
			}
			
			
			Conductor conductor;
			try{
				conductor = ConductorServiceFactory.getService().buscarConductor(conductorId);
			}catch(ConductorNoEncontradoException e){
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,XmlServiceExceptionConversor.
						toConductorNoEncontradoException(e), null);
				return;
			}catch(InstanceNotFoundException ex){
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,XmlServiceExceptionConversor.
						toInstanceNotFoundException(ex), null);
				return;
			}
			
			ServiceConductorDto conductorDto = ConductorToConductorDtoConversor.toConductorDto(conductor);
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_OK, XmlServiceConductorDtoConversor.toXml(conductorDto), null);

			
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String path = ServletUtils.normalizePath(request.getPathInfo());
        
        if (path == null || path.length() == 0) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,XmlServiceExceptionConversor.
            		toInputValidationExceptionXml(new InputValidationException("Invalid Request: " + "invalid idConductor")),null);
            return;
        }
        
        String idConductorAsString = path.substring(1);
        Long idConductor;
        
        try {
            idConductor = Long.valueOf(idConductorAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,XmlServiceExceptionConversor.
            		toInputValidationExceptionXml(new InputValidationException("Invalid Request: " + "invalid idConductor '" + 
            				idConductorAsString + "'")),null);
            return;
        }

        ServiceConductorDto conductorDto;
        
        try {
            conductorDto = XmlServiceConductorDtoConversor.toServiceConductorDto(request.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
            return;
        }
        
        if (!idConductor.equals(conductorDto.getIdConductor())) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,XmlServiceExceptionConversor.
            		toInputValidationExceptionXml(new InputValidationException("Invalid Request: " + "invalid idConductor")),null);
            return;
        }
        
        Conductor conductor = ConductorToConductorDtoConversor.toConductor(conductorDto);
        
        try{
        	Conductor conductorExistente = ConductorServiceFactory.getService().buscarConductor(conductor.getIdConductor());
        }catch(ConductorNoEncontradoException e){
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,XmlServiceExceptionConversor.
            		toConductorNoEncontradoException(e), null);
        }catch(InstanceNotFoundException e){
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,XmlServiceExceptionConversor.
            		toInstanceNotFoundException(e), null);
        }
        
        try {
            ConductorServiceFactory.getService().actualizarConductor(conductor);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,XmlServiceExceptionConversor.
            		toInputValidationExceptionXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,XmlServiceExceptionConversor.
            		toInstanceNotFoundException(ex), null);
            return;
        }
        
        ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NO_CONTENT, null, null);    
	}

}
