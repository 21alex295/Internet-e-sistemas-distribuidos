package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.dto.ClientConductorDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientConductorDtoConversor {
	
	public final static Namespace XML_NS =
			Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	public static Document toXml(ClientConductorDto conductor)throws IOException{
		
		Element conductorElement = toJDOMElement(conductor);
		
		return new Document(conductorElement);
	}
	
	public static Element toJDOMElement(ClientConductorDto conductor){
		
		Element conductorElement = new Element("conductor",XML_NS);
		
		if (conductor.getIdConductor() != null){			
			Element idConductorElement = new Element("idConductor",XML_NS);
			idConductorElement.setText(conductor.getIdConductor().toString());
			conductorElement.addContent(idConductorElement);			
		}
		
        Element nombreConductorElement = new Element("nombre", XML_NS);
        nombreConductorElement.setText(conductor.getNombre());
        conductorElement.addContent(nombreConductorElement);
		
        Element ciudadConductorElement = new Element("ciudad", XML_NS);
        ciudadConductorElement.setText(conductor.getCiudad());
        conductorElement.addContent(ciudadConductorElement);
        
        Element modeloCocheConductorElement = new Element("modeloCoche", XML_NS);
        modeloCocheConductorElement.setText(conductor.getModeloCoche());
        conductorElement.addContent(modeloCocheConductorElement);
        
        Element horaInicioConductorElement = new Element("horaInicio", XML_NS);
        horaInicioConductorElement.setText(Byte.toString(conductor.getHoraInicio()));
        conductorElement.addContent(horaInicioConductorElement);
        
        Element horaFinConductorElement = new Element("horaFin", XML_NS);
        horaFinConductorElement.setText(Byte.toString(conductor.getHoraFin()));
        conductorElement.addContent(horaFinConductorElement);
        
        Element puntuacionAcumuladaConductorElement = new Element("puntuacionAcumulada", XML_NS);
        puntuacionAcumuladaConductorElement.setText(Integer.toString(conductor.getPuntuacionAcumulada()));
        conductorElement.addContent(puntuacionAcumuladaConductorElement);
        
        Element totalViajesConductorElement = new Element("totalViajes", XML_NS);
        totalViajesConductorElement.setText(Integer.toString(conductor.getTotalViajes()));
        conductorElement.addContent(totalViajesConductorElement);
        
        return conductorElement;

	}
	
	public static ClientConductorDto toClientConductorDto(InputStream conductorXml) throws ParsingException{
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(conductorXml);
			Element rootElement = document.getRootElement();
			
			return toClientConductorDto(rootElement);
		}catch(ParsingException ex){
			throw ex;
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static List<ClientConductorDto> toClientConductorDtos(InputStream conductorXml) throws ParsingException{
		
		try{
			
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(conductorXml);
            Element rootElement = document.getRootElement();

            if (!"conductor".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Elemento no reconocido'" + rootElement.getName() + "' ('conductor' esperado)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientConductorDto> conductorDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                conductorDtos.add(toClientConductorDto(element));
            }

            return conductorDtos;
            
		}catch(ParsingException ex){
			throw ex;
		}catch(Exception e){
			throw new ParsingException (e);
		}
		
	}
	
	
	
	 private static ClientConductorDto toClientConductorDto(Element conductorElement) throws ParsingException, DataConversionException {
	        if (!"conductor".equals(conductorElement.getName())) {
	            throw new ParsingException("Elemento no reconocido '"+ conductorElement.getName() + "' ('conductor' esperado)");
	        }
	        Element idElement = conductorElement.getChild("idConductor", XML_NS);
	        Long id = null;

	        if (idElement != null) {
	            id = Long.valueOf(idElement.getTextTrim());
	        }

	        String nombre = conductorElement.getChildTextNormalize("nombre", XML_NS);

	        String ciudad = conductorElement.getChildTextNormalize("ciudad", XML_NS);
	        
	        String modeloCoche = conductorElement.getChildTextNormalize("modeloCoche", XML_NS);

	        byte horaInicio = Byte.valueOf(conductorElement.getChildTextTrim("horaInicio", XML_NS));
	        
	        byte horaFin = Byte.valueOf(conductorElement.getChildTextTrim("horaFin", XML_NS));

	        int puntuacionAcumulada = Integer.valueOf(conductorElement.getChildTextTrim("puntuacionAcumulada", XML_NS));
	        
	        int totalViajes = Integer.valueOf(conductorElement.getChildTextTrim("totalViajes", XML_NS));

	        return new ClientConductorDto(id, nombre, ciudad, modeloCoche, horaInicio, horaFin, puntuacionAcumulada, totalViajes);
	    }
	
}
