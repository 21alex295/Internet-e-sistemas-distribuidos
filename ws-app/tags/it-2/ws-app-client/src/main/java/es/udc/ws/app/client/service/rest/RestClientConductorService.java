package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.service.ClientConductorService;
import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.app.client.service.dto.ClientConductoresDeUsuarioDto;
import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.app.client.service.rest.xml.XmlClientConductorDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientConductoresDeUsuarioDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientViajeDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class RestClientConductorService implements ClientConductorService{

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientConductorService.endpointAddress";
	private String endpointAddress;
	
	@Override
	public Long anadirConductor(ClientConductorDto conductor) throws InputValidationException {
		
		try{
			HttpResponse response = Request.Post(getEndpointAddress()+"conductor").bodyStream(toInputStream(conductor),ContentType.
					create("application/xml")).execute().returnResponse();
			validateStatusCode(HttpStatus.SC_CREATED,response);
			
			return XmlClientConductorDtoConversor.toClientConductorDto(response.getEntity().getContent()).getIdConductor();
			
		}catch(InputValidationException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void borrarConductor(Long idConductor) throws InstanceNotFoundException {
		
        try {

            HttpResponse response = Request.Delete(getEndpointAddress() + "conductor/" + idConductor).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
	}
	@Override
	public ClientConductorDto verConductor(Long idConductor) throws InstanceNotFoundException, ConductorNoEncontradoException {
		
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "conductor/" + idConductor).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);
            return XmlClientConductorDtoConversor.toClientConductorDto(response.getEntity().getContent());
        } catch(ConductorNoEncontradoException e){
        	throw e;
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
	}
	
	
	private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }
	
    private InputStream toInputStream(ClientConductorDto conductor) {

        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            outputter.output(XmlClientConductorDtoConversor.toXml(conductor), xmlOutputStream);

            return new ByteArrayInputStream(xmlOutputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    
    private void validateStatusCode(int successCode, HttpResponse response) throws InstanceNotFoundException,
            InputValidationException, ParsingException, ConductorNoEncontradoException {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
				case HttpStatus.SC_CONFLICT:
					throw XmlClientExceptionConversor.fromConductorNoEncontradoExceptionXml(response.getEntity().getContent());
                case HttpStatus.SC_NOT_FOUND:
                	throw XmlClientExceptionConversor.fromConductorNoEncontradoExceptionXml(response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw XmlClientExceptionConversor.fromInputValidationExceptionXml(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

	@Override
	public void actualizarConductor(ClientConductorDto conductor) throws InputValidationException, InstanceNotFoundException, ConductorNoEncontradoException {
		
		try{			
			HttpResponse response = Request.Put(getEndpointAddress()+"conductor/"+conductor.getIdConductor()).
					bodyStream(toInputStream(conductor),ContentType.create("application/xml")).execute().returnResponse();
			
			validateStatusCode(HttpStatus.SC_NO_CONTENT,response);
		}catch(ConductorNoEncontradoException e){
			throw e;
		}catch(InputValidationException | InstanceNotFoundException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	@Override
	public List<ClientViajeDto> viajesPorConductor(Long idConductor) throws InstanceNotFoundException,ConductorNoEncontradoException {
		try {
			HttpResponse response = Request.Get(getEndpointAddress() + "viaje?idConductor="
					+ URLEncoder.encode(Long.toString(idConductor), "UTF-8")).execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);

			List<ClientViajeDto> viajesConductor = XmlClientViajeDtoConversor
					.toClientViajeDtos(response.getEntity().getContent());
			return viajesConductor;
		} catch(ConductorNoEncontradoException e){
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



}
