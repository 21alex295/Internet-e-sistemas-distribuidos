package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.service.ClientUsuarioService;
import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.app.client.service.dto.ClientConductoresDeUsuarioDto;
import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.app.client.service.exception.PuntuacionRepetidaException;
import es.udc.ws.app.client.service.exception.ViajeDeOtroUsuarioException;
import es.udc.ws.app.client.service.exception.ViajeNoExisteException;
import es.udc.ws.app.client.service.rest.xml.XmlClientConductorDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientConductoresDeUsuarioDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientViajeDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class RestClientUsuarioService implements ClientUsuarioService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientUsuarioService.endpointAddress";
	private String endpointAddress;

	@Override
	public ClientViajeDto contratarViaje(ClientViajeDto viaje) throws InputValidationException, ConductorNoEncontradoException {
		try {
			HttpResponse response = Request.Post(getEndpointAddress() + "viaje")
					.bodyStream(toInputStream(viaje), ContentType.create("application/xml")).execute().returnResponse();
			validateStatusCode(HttpStatus.SC_CREATED, response);

			return XmlClientViajeDtoConversor.toClientViajeDto(response.getEntity().getContent());
		}catch (ConductorNoEncontradoException e){
			throw e;
		} catch (InputValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void puntuarViaje(Long idViaje, String idUsuario, Integer puntuacion)
			throws InstanceNotFoundException, InputValidationException, PuntuacionRepetidaException, ViajeDeOtroUsuarioException, ViajeNoExisteException {
		try {

			/* System.out.println(viaje.getPuntuacion());*/
			HttpResponse response = Request.Post(getEndpointAddress() + "viaje/" + idViaje + "/puntuar").
                    bodyForm(
                            Form.form().
                            add("idViaje", Long.toString(idViaje)).
                            add("idUsuario", idUsuario).
                            add("puntuacion", Integer.toString(puntuacion)).
                            build()).
                    execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);
		}catch(ViajeNoExisteException e){
			throw e;
		}catch(ViajeDeOtroUsuarioException e){
			throw e;
		}catch(PuntuacionRepetidaException e){
			throw e;
		} catch (InputValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*@Override
	public List<ClientConductorDto> conductoresPorUsuario(String idUsuario) throws InstanceNotFoundException {
		try {
			HttpResponse response = Request.Get(getEndpointAddress() + "viaje/" + URLEncoder.encode(idUsuario, "UTF-8")).execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);

			List<ClientConductorDto> conductorDtos = XmlClientConductorDtoConversor
					.toClientConductorDtos(response.getEntity().getContent());
			return conductorDtos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}*/

	@Override
	public List<ClientConductorDto> encontrarConductoresDisponibles(String ciudad) throws InputValidationException, InstanceNotFoundException {
		
		try{			
			HttpResponse response = Request.Get(getEndpointAddress()+"conductor?ciudad=" + URLEncoder.encode(ciudad, "UTF-8")).execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK,response);
			
            
            List<ClientConductorDto> conductoresDisponibles = XmlClientConductorDtoConversor.toClientConductorDtos(response.getEntity().getContent());
            return conductoresDisponibles;
            
		}catch(InputValidationException | InstanceNotFoundException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public List<ClientViajeDto> viajesPorUsuario(String idUsuario) throws InstanceNotFoundException{
		try {

			HttpResponse response = Request
					.Get(getEndpointAddress() + "viaje?idUsuario=" + URLEncoder.encode(idUsuario, "UTF-8")).execute()
					.returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);

			List<ClientViajeDto> viajesUsuario = XmlClientViajeDtoConversor
					.toClientViajeDtos(response.getEntity().getContent());
			return viajesUsuario;
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<ClientConductoresDeUsuarioDto> conductoresDeUsuario(String idUsuario) {

		try{
			
	           HttpResponse response = Request.Get(getEndpointAddress() + "conductoresDeUsuario/"+ URLEncoder.encode(idUsuario, "UTF-8")).execute().returnResponse();
	            validateStatusCode(HttpStatus.SC_OK, response);

            List<ClientConductoresDeUsuarioDto> viajesUsuario = XmlClientConductoresDeUsuarioDtoConversor.toClientConductoresDeUsuarioDtos(response.getEntity().getContent());
			   System.out.println("idUsuario en client rest: " + idUsuario);

            return viajesUsuario;
		}catch(Exception e){
			throw new RuntimeException(e);
		}	
	

	}
	

	private synchronized String getEndpointAddress() {
		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}
		return endpointAddress;
	}

	private InputStream toInputStream(ClientViajeDto viaje) {

		try {

			ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

			outputter.output(XmlClientViajeDtoConversor.toXml(viaje), xmlOutputStream);

			return new ByteArrayInputStream(xmlOutputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, InputValidationException, ParsingException, PuntuacionRepetidaException, ConductorNoEncontradoException, ViajeDeOtroUsuarioException,
					ViajeNoExisteException{

		try {

			int statusCode = response.getStatusLine().getStatusCode();

			/* Success? */
			if (statusCode == successCode) {
				return;
			}

			/* Handler error. */
			switch (statusCode) {

			case HttpStatus.SC_NOT_FOUND:
				throw XmlClientExceptionConversor.fromViajeNoExisteExceptionXml(response.getEntity().getContent());

			case HttpStatus.SC_BAD_REQUEST:
				throw XmlClientExceptionConversor.fromInputValidationExceptionXml(response.getEntity().getContent());
			
			case HttpStatus.SC_CONFLICT:
				throw XmlClientExceptionConversor.fromConductorNoEncontradoExceptionXml(response.getEntity().getContent());
			case HttpStatus.SC_FORBIDDEN:
				throw XmlClientExceptionConversor.fromPuntuacioRepetidaExceptionXml(response.getEntity().getContent());
			case HttpStatus.SC_UNAUTHORIZED:
				throw XmlClientExceptionConversor.fromViajeDeOtroUsuarioExceptionXml(response.getEntity().getContent());
			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
