package es.udc.ws.app.restservice.xml;

import java.io.InputStream;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.dto.ServiceConductorDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceConductorDtoConversor {
	
	// Creamos XML
	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	// Convierte un conductor con sus datos a XML
	public static Document toXml(ServiceConductorDto conductor){
		Element conductorElement = toJDOMElement(conductor);
		return new Document(conductorElement);
	}
	
	// Igual que el anterior pero para una lista de conductores
	public static Document toXml(List<ServiceConductorDto> conductor){
		Element appElement = new Element("conductor",XML_NS);
		for(int i=0;i<conductor.size();i++){
			ServiceConductorDto xmlConductorDto = conductor.get(i);
			Element conductorElement = toJDOMElement(xmlConductorDto);
			appElement.addContent(conductorElement);
		}
		return new Document(appElement);
	}
	
	//convierte un conductor en un elemento XML
	public static Element toJDOMElement(ServiceConductorDto conductor){
		Element conductorElement = new Element("conductor",XML_NS);
		
		if (conductor.getIdConductor() != null){
			Element idConductorElement = new Element("idConductor",XML_NS);
			idConductorElement.setText(Long.toString(conductor.getIdConductor()));
			conductorElement.addContent(idConductorElement);
		}
		Element nombreElement = new Element("nombre",XML_NS);
		nombreElement.setText(conductor.getNombre());
		conductorElement.addContent(nombreElement);
		
		Element ciudadElement = new Element("ciudad",XML_NS);
		ciudadElement.setText(conductor.getCiudad());
		conductorElement.addContent(ciudadElement);
		
		Element modeloCocheElement = new Element("modeloCoche",XML_NS);
		modeloCocheElement.setText(conductor.getModeloCoche());
		conductorElement.addContent(modeloCocheElement);

		Element horaInicioElement = new Element("horaInicio",XML_NS);
		horaInicioElement.setText(Byte.toString(conductor.getHoraInicio()));
		conductorElement.addContent(horaInicioElement);
		
		Element horaFinElement = new Element("horaFin",XML_NS);
		horaFinElement.setText(Byte.toString(conductor.getHoraFin()));
		conductorElement.addContent(horaFinElement);

		Element puntuacionAcumuladaElement = new Element("puntuacionAcumulada",XML_NS);
		puntuacionAcumuladaElement.setText(Integer.toString(conductor.getPuntuacionAcumulada()));
		conductorElement.addContent(puntuacionAcumuladaElement);
		
		Element totalViajesElement = new Element("totalViajes",XML_NS);
		totalViajesElement.setText(Integer.toString(conductor.getTotalViajes()));
		conductorElement.addContent(totalViajesElement);
		
		return conductorElement;
	}
	
	// Recibe la "lectura del documento", y devuelve el objetoDTO
	// SAXBuilder es un parser de XML
	public static ServiceConductorDto toServiceConductorDto(InputStream conductorXml){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(conductorXml);
			Element rootElement = document.getRootElement();
			return toServiceConductorDto(rootElement);
		}catch(ParsingException e){
			throw e;
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	
	//Igual que el de arriba pero sólo con un objeto, en vez de con el archivo
	public static ServiceConductorDto toServiceConductorDto(Element conductorElement)throws ParsingException, DataConversionException{
		if (!"conductor".equals(conductorElement.getName())){
			throw new ParsingException("Elemento no reconocido '"+conductorElement.getName()+"' ('conductor' esperado)");
		}
		Element idConductorElement =  conductorElement.getChild("idConductor",XML_NS);
		Long identificador = null;
		if(idConductorElement != null){
			identificador = Long.valueOf(idConductorElement.getTextTrim());
		}
		String nombre = conductorElement.getChildTextNormalize("nombre",XML_NS);
		String ciudad = conductorElement.getChildTextNormalize("ciudad",XML_NS);
		String modeloCoche = conductorElement.getChildTextNormalize("modeloCoche",XML_NS);
		byte horaInicio = Byte.valueOf(conductorElement.getChildTextTrim("horaInicio",XML_NS));
		byte horaFin = Byte.valueOf(conductorElement.getChildTextTrim("horaFin",XML_NS));
		
		int puntuacionAcumulada = Integer.valueOf(conductorElement.getChildTextTrim("puntuacionAcumulada",XML_NS));
		int totalViajes = Integer.valueOf(conductorElement.getChildTextTrim("totalViajes",XML_NS));
		
		return new ServiceConductorDto(identificador,nombre,ciudad,modeloCoche,horaInicio,horaFin,puntuacionAcumulada,totalViajes);
	}
	
}
