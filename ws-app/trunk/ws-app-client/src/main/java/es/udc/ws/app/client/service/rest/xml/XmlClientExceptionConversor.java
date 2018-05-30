package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.exception.ConductorNoEncontradoException;
import es.udc.ws.app.client.service.exception.PuntuacionRepetidaException;
import es.udc.ws.app.client.service.exception.ViajeDeOtroUsuarioException;
import es.udc.ws.app.client.service.exception.ViajeNoExisteException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientExceptionConversor {
	
	public final static Namespace XML_NS = 
			Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	public static InputValidationException fromInputValidationExceptionXml(InputStream ex) 
			throws ParsingException{
		try{
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();
			
			Element message = rootElement.getChild("message",XML_NS);
			
			return new InputValidationException(message.getText());
		}catch(JDOMException | IOException e){
			throw new ParsingException(e);
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static InstanceNotFoundException fromInstanceNotFoundExceptionXml(InputStream ex)
			throws ParsingException{
		try{
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();
			
			Element instanceId = rootElement.getChild("instanceId",XML_NS);
			Element instanceType = rootElement.getChild("instanceType",XML_NS);
			return new InstanceNotFoundException(instanceId.getText(),instanceType.getText());
		}catch(JDOMException | IOException e){
			throw new ParsingException(e);
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static PuntuacionRepetidaException fromPuntuacioRepetidaExceptionXml(InputStream ex)
			throws ParsingException{
		try{
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();
			
			Element instanceId = rootElement.getChild("instanceId",XML_NS);
			Element instanceType = rootElement.getChild("instanceType",XML_NS);
			return new PuntuacionRepetidaException("ERROR: YA PUNTUADO");
		}catch(JDOMException | IOException e){
			throw new ParsingException(e);
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static ConductorNoEncontradoException fromConductorNoEncontradoExceptionXml(InputStream ex)
			throws ParsingException{
		try{
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();
			
			Element instanceId = rootElement.getChild("instanceId",XML_NS);
			Element instanceType = rootElement.getChild("instanceType",XML_NS);
			return new ConductorNoEncontradoException("ERROR: CONDUCTOR NO EXISTENTE");
		}catch(JDOMException | IOException e){
			throw new ParsingException(e);
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static ViajeDeOtroUsuarioException fromViajeDeOtroUsuarioExceptionXml(InputStream ex)
			throws ParsingException{
		try{
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();
			
			Element instanceId = rootElement.getChild("instanceId",XML_NS);
			Element instanceType = rootElement.getChild("instanceType",XML_NS);
			return new ViajeDeOtroUsuarioException();
		}catch(JDOMException | IOException e){
			throw new ParsingException(e);
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static ViajeNoExisteException fromViajeNoExisteExceptionXml(InputStream ex)
			throws ParsingException{
		try{
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();
			
			Element instanceId = rootElement.getChild("instanceId",XML_NS);
			Element instanceType = rootElement.getChild("instanceType",XML_NS);
			return new ViajeNoExisteException();
		}catch(JDOMException | IOException e){
			throw new ParsingException(e);
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}

}
