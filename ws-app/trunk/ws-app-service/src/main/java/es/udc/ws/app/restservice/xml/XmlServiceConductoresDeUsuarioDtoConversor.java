package es.udc.ws.app.restservice.xml;

import java.io.InputStream;
import java.util.List;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.dto.ServiceConductoresDeUsuarioDto;
import es.udc.ws.app.model.conductor.Conductor;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceConductoresDeUsuarioDtoConversor {
	
	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	public static Document toXml(List<ServiceConductoresDeUsuarioDto> conductoresDeUsuario){
		Element appElement = new Element("conductoresDeUsuario",XML_NS);
		for(int i=0;i<conductoresDeUsuario.size();i++){
			ServiceConductoresDeUsuarioDto xmlConductoresDeUsuarioDto =conductoresDeUsuario.get(i);
			Element conductoresDeUsuarioElement = toJDOMElement(xmlConductoresDeUsuarioDto);
			appElement.addContent(conductoresDeUsuarioElement);
		}
		return new Document(appElement);
	}
	
	public static Document toXml(ServiceConductoresDeUsuarioDto conductoresDeUsuario){
		Element conductoresDeUsuarioElement = toJDOMElement(conductoresDeUsuario);
		return new Document(conductoresDeUsuarioElement);
	}
	
	public static ServiceConductoresDeUsuarioDto toServiceConductoresDeUsuarioDto(Conductor conductor, String idUsuario){
		return new ServiceConductoresDeUsuarioDto(conductor.getIdConductor(),idUsuario,conductor.getNombre(),conductor.getCiudad(),0,0,0);
	}
	
	
	public static Element toJDOMElement(ServiceConductoresDeUsuarioDto conductoresDeUsuario){
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
	public static ServiceConductoresDeUsuarioDto toServiceConductoresDeUsuarioDto(InputStream conductoresDeUsuarioXml){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(conductoresDeUsuarioXml);
			Element rootElement = document.getRootElement();

			return toServiceConductoresDeUsuarioDto(rootElement);
		}catch(ParsingException e){
			throw e;
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	
	//Igual que el de arriba pero sólo con un objeto, en vez de con el archivo
		public static ServiceConductoresDeUsuarioDto toServiceConductoresDeUsuarioDto(Element conductoresDeUsuarioElement)throws ParsingException, DataConversionException{
		if (!"conductoresDeUsuario".equals(conductoresDeUsuarioElement.getName())){
				throw new ParsingException("Elemento no reconocido '"+conductoresDeUsuarioElement.getName()+"' ('conductoresDeUsuario' esperado)");
			}
		
			Element idUsuarioElement =  conductoresDeUsuarioElement.getChild("idUsuario",XML_NS);
			Long identificador = Long.valueOf(idUsuarioElement.getTextTrim());	
			String idUsuario = (conductoresDeUsuarioElement.getChildText("idConductor",XML_NS));
			String nombre = conductoresDeUsuarioElement.getChildTextNormalize("nombre",XML_NS);
			String ciudad = conductoresDeUsuarioElement.getChildTextNormalize("ciudad",XML_NS);
			int puntuacionAcumulada = Integer.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("puntuacionAcumulada",XML_NS));
			int totalNotas = Integer.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("totalNotas",XML_NS));
			int mediaPersonal = Integer.valueOf(conductoresDeUsuarioElement.getChildTextNormalize("mediaPersonal",XML_NS));
			
			return new ServiceConductoresDeUsuarioDto(identificador,idUsuario,nombre,ciudad,puntuacionAcumulada,totalNotas,mediaPersonal);
		}




}
