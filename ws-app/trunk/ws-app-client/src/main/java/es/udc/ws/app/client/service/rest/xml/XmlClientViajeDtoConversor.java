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

import es.udc.ws.app.client.service.dto.ClientViajeDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientViajeDtoConversor {
	
	public final static Namespace XML_NS =
			Namespace.getNamespace("http://ws.udc.es/app/xml");
	
	public static Document toXmlBuscar(ClientViajeDto viaje)throws IOException{
		
		Element viajeElement = toJDOMElementBuscar(viaje);
		
		return new Document(viajeElement);
	}
	
	public static Element toJDOMElementBuscar(ClientViajeDto viaje){
		
		Element viajeElement = new Element("viaje",XML_NS);
		
		if (viaje.getIdViaje() != null){			
			Element idViajeElement = new Element("idViaje",XML_NS);
			idViajeElement.setText(viaje.getIdViaje().toString());
			viajeElement.addContent(idViajeElement);			
		}
        
        Element idConductorViajeElement = new Element("idConductor", XML_NS);
        idConductorViajeElement.setText(viaje.getIdConductor().toString());
        viajeElement.addContent(idConductorViajeElement);
		
        Element origenViajeElement = new Element("origen", XML_NS);
        origenViajeElement.setText(viaje.getOrigen());
        viajeElement.addContent(origenViajeElement);
        
        Element destinoViajeElement = new Element("destino", XML_NS);
        destinoViajeElement.setText(viaje.getDestino());
        viajeElement.addContent(destinoViajeElement);
        
        Element idUsuarioViajeElement = new Element("idUsuario", XML_NS);
        idUsuarioViajeElement.setText(viaje.getIdUsuario());
        viajeElement.addContent(idUsuarioViajeElement);
        
        Element fechaReservaViajeElement = new Element("fechaReserva", XML_NS);
        fechaReservaViajeElement.setText(viaje.getFechaReserva().toString());
        viajeElement.addContent(fechaReservaViajeElement);
        
        
        
        Element puntuacionViajeElement = new Element("puntuacion", XML_NS);
        puntuacionViajeElement.setText(Integer.toString(viaje.getPuntuacion()));
        viajeElement.addContent(puntuacionViajeElement);
        
        
        return viajeElement;

	}
	
public static Document toXml(ClientViajeDto viaje)throws IOException{
		
		Element viajeElement = toJDOMElement(viaje);
		
		return new Document(viajeElement);
	}
	
	public static Element toJDOMElement(ClientViajeDto viaje){
		
		Element viajeElement = new Element("viaje",XML_NS);
		
		if (viaje.getIdViaje() != null){			
			Element idViajeElement = new Element("idViaje",XML_NS);
			idViajeElement.setText(viaje.getIdViaje().toString());
			viajeElement.addContent(idViajeElement);			
		}
        
        Element idConductorViajeElement = new Element("idConductor", XML_NS);
        idConductorViajeElement.setText(viaje.getIdConductor().toString());
        viajeElement.addContent(idConductorViajeElement);
		
        Element origenViajeElement = new Element("origen", XML_NS);
        origenViajeElement.setText(viaje.getOrigen());
        viajeElement.addContent(origenViajeElement);
        
        Element destinoViajeElement = new Element("destino", XML_NS);
        destinoViajeElement.setText(viaje.getDestino());
        viajeElement.addContent(destinoViajeElement);
        
        Element idUsuarioViajeElement = new Element("idUsuario", XML_NS);
        idUsuarioViajeElement.setText(viaje.getIdUsuario());
        viajeElement.addContent(idUsuarioViajeElement);
        
        Element tarjetaCreditoViajeElement = new Element("tarjetaCredito", XML_NS);
        tarjetaCreditoViajeElement.setText(viaje.getTarjetaCredito());
        viajeElement.addContent(tarjetaCreditoViajeElement);
        
        /*Element fechaReservaElement = getFechaReserva(viaje.getFechaReserva());
        viajeElement.addContent(fechaReservaElement);*/
        
        Element puntuacionViajeElement = new Element("puntuacion", XML_NS);
        puntuacionViajeElement.setText(Integer.toString(viaje.getPuntuacion()));
        viajeElement.addContent(puntuacionViajeElement);
        return viajeElement;

	}
	
	
	public static ClientViajeDto toClientViajeDto(InputStream viajeXml) throws ParsingException{
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(viajeXml);
			Element rootElement = document.getRootElement();
			
			return toClientViajeDto(rootElement);
		}catch(ParsingException ex){
			throw ex;
		}catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	
	public static List<ClientViajeDto> toClientViajeDtos(InputStream viajeXml) throws ParsingException{
		
		try{
			
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(viajeXml);
            Element rootElement = document.getRootElement();

            if (!"viaje".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Elemento no reconocido'" + rootElement.getName() + "' ('viaje' esperado)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientViajeDto> viajeDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                viajeDtos.add(toClientViajeDto(element));
            }

            return viajeDtos;
            
		}catch(ParsingException ex){
			throw ex;
		}catch(Exception e){
			throw new ParsingException (e);
		}
		
	}

	private static ClientViajeDto toClientViajeDto(Element viajeElement) throws ParsingException, DataConversionException {
		
	    if (!"viaje".equals(viajeElement.getName())) {
	        throw new ParsingException("Elemento no reconocido '"+ viajeElement.getName() + "' ('viaje' esperado)");
	    }
	    Element idElement = viajeElement.getChild("idViaje", XML_NS);
	    Long idViaje = null;
	
	    if (idElement != null) {
	        idViaje = Long.valueOf(idElement.getTextTrim());
	    }
	
	    Long idConductor = Long.valueOf(viajeElement.getChildTextNormalize("idConductor", XML_NS));
	
	    String origen = viajeElement.getChildTextNormalize("origen", XML_NS);
	    
	    String destino = viajeElement.getChildTextNormalize("destino", XML_NS);
	    
	    String idUsuario = viajeElement.getChildTextNormalize("idUsuario", XML_NS);
	    
	    String tarjetaCredito = viajeElement.getChildTextNormalize("tarjetaCredito", XML_NS);
	   
	    int puntuacion = Integer.valueOf(viajeElement.getChildTextNormalize("puntuacion", XML_NS));
	    
	
	    return new ClientViajeDto(idViaje, idConductor, origen, destino, idUsuario, tarjetaCredito, puntuacion);
	}
	
   /* private static Element getFechaReserva(Calendar fechaReserva) {

        Element fechaReservaElement = new Element("fechaReserva", XML_NS);
        int day = fechaReserva.get(Calendar.DAY_OF_MONTH);
        int month = fechaReserva.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = fechaReserva.get(Calendar.YEAR);

        fechaReservaElement.setAttribute("day", Integer.toString(day));
        fechaReservaElement.setAttribute("month", Integer.toString(month));
        fechaReservaElement.setAttribute("year", Integer.toString(year));

        return fechaReservaElement;

    }*/
    
	
	
	public static Document toXmlPuntuar(List<ClientViajeDto> viaje){
		Element appElement = new Element("viaje",XML_NS);
		for(int i=0;i<viaje.size();i++){
			ClientViajeDto xmlViajeDto = viaje.get(i);
			Element viajeElement = toJDOMElementPuntuar(xmlViajeDto);
			appElement.addContent(viajeElement);
		}
		return new Document(appElement);
	}

		public static Element toJDOMElementPuntuar(ClientViajeDto viaje){
			
			Element viajeElement = new Element("viaje",XML_NS);
			
			if (viaje.getIdViaje() != null){			
				Element idViajeElement = new Element("idViaje",XML_NS);
				idViajeElement.setText(viaje.getIdViaje().toString());
				viajeElement.addContent(idViajeElement);			
			}
	        
	        Element idUsuarioViajeElement = new Element("idUsuario", XML_NS);
	        idUsuarioViajeElement.setText(viaje.getIdUsuario());
	        viajeElement.addContent(idUsuarioViajeElement);
	        
	        Element puntuacionViajeElement = new Element("puntuacion", XML_NS);
	        puntuacionViajeElement.setText(Integer.toString(viaje.getPuntuacion()));
	        viajeElement.addContent(puntuacionViajeElement);
	        
	        return viajeElement;

		}
    

}
