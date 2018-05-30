package es.udc.ws.app.client.service.rest.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.dto.ClientConductoresDeUsuarioDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientConductoresDeUsuarioDtoConversor {


	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	public static Document toXml(List<ClientConductoresDeUsuarioDto> conductoresDeUsuario){
		Element appElement = new Element("conductoresDeUsuario",XML_NS);
		for(int i=0;i<conductoresDeUsuario.size();i++){
			ClientConductoresDeUsuarioDto xmlConductoresDeUsuarioDto =conductoresDeUsuario.get(i);
			Element conductoresDeUsuarioElement = toJDOMElement(xmlConductoresDeUsuarioDto);
			appElement.addContent(conductoresDeUsuarioElement);
		}
		return new Document(appElement);
	}
	
	public static Document toXml(ClientConductoresDeUsuarioDto conductoresDeUsuario){
		Element conductoresDeUsuarioElement = toJDOMElement(conductoresDeUsuario);
		return new Document(conductoresDeUsuarioElement);
	}
	
	
	public static Element toJDOMElement(ClientConductoresDeUsuarioDto conductoresDeUsuario){
		Element conductoresDeUsuarioElement = new Element("conductoresDeUsuario",XML_NS);
		
		Element idConductorElement = new Element("idConductor",XML_NS);
		idConductorElement.setText(Long.toString(conductoresDeUsuario.getIdConductor()));
		conductoresDeUsuarioElement.addContent(idConductorElement);
		
		Element idUsuarioElement = new Element("idUsuario",XML_NS);
		idUsuarioElement.setText((conductoresDeUsuario.getIdUsuario()));
		conductoresDeUsuarioElement.addContent(idUsuarioElement);
		
		Element nombreElement = new Element("nombre",XML_NS);
		nombreElement.setText(conductoresDeUsuario.getNombre());
		conductoresDeUsuarioElement.addContent(nombreElement);
		
		Element ciudadElement = new Element("ciudad",XML_NS);
		ciudadElement.setText(conductoresDeUsuario.getCiudad());
		conductoresDeUsuarioElement.addContent(ciudadElement);
		
		Element puntuacionAcumuladaElement = new Element("puntuacionAcumulada",XML_NS);
		puntuacionAcumuladaElement.setText(Integer.toString(conductoresDeUsuario.getPuntuacionAcumulada()));
		conductoresDeUsuarioElement.addContent(puntuacionAcumuladaElement);
		
		Element totalNotasElement = new Element("totalNotas",XML_NS);
		totalNotasElement.setText(Integer.toString(conductoresDeUsuario.getTotalNotas()));
		conductoresDeUsuarioElement.addContent(totalNotasElement);
		
		Element mediaPersonalElement = new Element("mediaPersonal",XML_NS);
		mediaPersonalElement.setText(Integer.toString(conductoresDeUsuario.getMediaPersonal()));
		conductoresDeUsuarioElement.addContent(mediaPersonalElement);
		
		return conductoresDeUsuarioElement;
	}
	


	// Recibe la "lectura del documento", y devuelve el objetoDTO
	// SAXBuilder es un parser de XML
	public static ClientConductoresDeUsuarioDto toClientConductoresDeUsuarioDto(InputStream conductoresDeUsuarioXml){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(conductoresDeUsuarioXml);
			Element rootElement = document.getRootElement();

			return toClientConductoresDeUsuarioDto(rootElement);
		}catch(ParsingException e){
			throw e;
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	
	//Igual que el de arriba pero sólo con un objeto, en vez de con el archivo
		public static ClientConductoresDeUsuarioDto toClientConductoresDeUsuarioDto(Element conductoresDeUsuarioElement)throws ParsingException, DataConversionException{
		if (!"conductoresDeUsuario".equals(conductoresDeUsuarioElement.getName())){
				throw new ParsingException("Elemento no reconocido '"+conductoresDeUsuarioElement.getName()+"' ('conductoresDeUsuario' esperado)");
			}
		
			Long idConductor = Long.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("idConductor",XML_NS));
			String idUsuario = (conductoresDeUsuarioElement.getChildText("idUsuario",XML_NS));



			String nombre = conductoresDeUsuarioElement.getChildTextNormalize("nombre",XML_NS);
			String ciudad = conductoresDeUsuarioElement.getChildTextNormalize("ciudad",XML_NS);
			int puntuacionAcumulada = Integer.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("puntuacionAcumulada",XML_NS));
			int totalNotas = Integer.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("totalNotas",XML_NS));
			int mediaPersonal = Integer.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("mediaPersonal",XML_NS));
			
			return new ClientConductoresDeUsuarioDto(idConductor,idUsuario,nombre,ciudad,puntuacionAcumulada,totalNotas,mediaPersonal);
		}
		public static List<ClientConductoresDeUsuarioDto> toClientConductoresDeUsuarioDtos(InputStream conductoresDeUsuarioXml) throws ParsingException{
			
			try{
				
	            SAXBuilder builder = new SAXBuilder();
	            Document document = builder.build(conductoresDeUsuarioXml);
	            Element rootElement = document.getRootElement();

	            if (!"conductoresDeUsuario".equalsIgnoreCase(rootElement.getName())) {
	                throw new ParsingException("Elemento no reconocido'" + rootElement.getName() + "' ('conductoresDeUsuario' esperado)");
	            }

	            List<Element> children = rootElement.getChildren();
	            List<ClientConductoresDeUsuarioDto> conductoresDeUsuarioDtos = new ArrayList<>(children.size());

	            for (int i = 0; i < children.size(); i++) {
	                Element element = children.get(i);

	                conductoresDeUsuarioDtos.add(toClientConductoresDeUsuarioDto(element));

	            }

	            return conductoresDeUsuarioDtos;
	            
			}catch(ParsingException ex){
				throw ex;
			}catch(Exception e){
				throw new ParsingException (e);
			}
			
		}


	
}
