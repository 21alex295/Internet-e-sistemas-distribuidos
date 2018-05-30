package es.udc.ws.app.restservice.xml;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.app.model.exceptions.PuntuacionRepetidaException;
import es.udc.ws.app.restservice.exceptions.ViajeDeOtroUsuarioException;
import es.udc.ws.app.restservice.exceptions.ViajeNoExisteException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class XmlServiceExceptionConversor {
	
	public final static Namespace XML_NS= Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	// Convierte a XML una excepcion
	public static Document toInputValidationExceptionXml(InputValidationException ex) throws IOException{
		Element exceptionElement = new Element("InputValidationException",XML_NS);
		Element messageElement = new Element("message",XML_NS);
		messageElement.setText(ex.getMessage());
		exceptionElement.addContent(messageElement);
		return new Document(exceptionElement);
	}
	
	// Convierte a XML una excepcion
	public static Document toInstanceNotFoundException(InstanceNotFoundException ex) throws IOException{
		
		Element exceptionElement = new Element("InstanceNotFoundException",XML_NS);
		
		if(ex.getInstanceId() != null){
			Element instanceIdElement = new Element("instaceId",XML_NS);
			instanceIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(instanceIdElement);
		}
		
		if(ex.getInstanceType() != null ){
			Element instanceTypeElement = new Element("instaceType",XML_NS);
			instanceTypeElement.setText(ex.getInstanceType());
			exceptionElement.addContent(instanceTypeElement);
		}
		
		return new Document(exceptionElement);
	}
	
	// Convierte a XML una excepcion
	
	
	public static Document toPuntuacionRepetidaException(PuntuacionRepetidaException e) throws IOException{
		
		Element exceptionElement = new Element("ViajePuntuadoException",XML_NS);
				
		return new Document(exceptionElement);
	}
	
	public static Document toConductorNoEncontradoException(ConductorNoEncontradoException e) throws IOException{
		
		Element exceptionElement = new Element("ConductorNoEncontradoException",XML_NS);
				
		return new Document(exceptionElement);
	}
	
	public static Document toViajeDeOtroUsuarioException(ViajeDeOtroUsuarioException e) throws IOException{
		
		Element exceptionElement = new Element("ViajeDeOtroUsuarioException",XML_NS);
				
		return new Document(exceptionElement);
	}

	public static Document toViajeNoExisteException(ViajeNoExisteException e) throws IOException{
	
	Element exceptionElement = new Element("toViajeNoExisteException",XML_NS);
			
	return new Document(exceptionElement);
}
	


	

}
