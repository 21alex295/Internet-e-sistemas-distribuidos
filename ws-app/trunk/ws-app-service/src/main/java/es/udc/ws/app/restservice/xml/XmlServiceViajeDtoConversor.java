package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import es.udc.ws.app.dto.ServiceViajeDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceViajeDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/app/xml");

	public static Document toXml(List<ServiceViajeDto> viaje) {
		Element appElement = new Element("viaje", XML_NS);
		for (int i = 0; i < viaje.size(); i++) {
			ServiceViajeDto xmlViajeDto = viaje.get(i);
			Element viajeElement = toJDOMElement(xmlViajeDto);
			appElement.addContent(viajeElement);
		}
		return new Document(appElement);
	}

	public static Document toXml(ServiceViajeDto viaje) {
		Element viajeElement = toJDOMElement(viaje);
		return new Document(viajeElement);
	}

	public static Element toJDOMElement(ServiceViajeDto viaje) {
		Element viajeElement = new Element("viaje", XML_NS);
		if (viaje.getIdViaje() != null) {
			Element idViajeElement = new Element("idViaje", XML_NS);
			idViajeElement.setText(Long.toString(viaje.getIdViaje()));
			viajeElement.addContent(idViajeElement);
		}

		Element idConductorElement = new Element("idConductor", XML_NS);
		idConductorElement.setText(Long.toString(viaje.getIdConductor()));
		viajeElement.addContent(idConductorElement);

		Element origenElement = new Element("origen", XML_NS);
		origenElement.setText(viaje.getOrigen().toString());
		viajeElement.addContent(origenElement);

		Element destinoElement = new Element("destino", XML_NS);
		destinoElement.setText(viaje.getDestino().toString());
		viajeElement.addContent(destinoElement);

		Element idUsuarioElement = new Element("idUsuario", XML_NS);
		idUsuarioElement.setText(viaje.getIdUsuario());
		viajeElement.addContent(idUsuarioElement);

		Element tarjetaCreditoElement = new Element("tarjetaCredito", XML_NS);
		tarjetaCreditoElement.setText(viaje.getTarjetaCredito().toString());
		viajeElement.addContent(tarjetaCreditoElement);

		Element fechaReservaViajeElement = new Element("fechaReserva", XML_NS);

		fechaReservaViajeElement.setText(viaje.getFechaReserva().toString());
		viajeElement.addContent(fechaReservaViajeElement);

		Element puntuacionViajeElement = new Element("puntuacion", XML_NS);
		puntuacionViajeElement.setText(Integer.toString(viaje.getPuntuacion()));
		viajeElement.addContent(puntuacionViajeElement);

		return viajeElement;
	}

	public static Document toXmlBuscar(ServiceViajeDto viaje) throws IOException {

		Element viajeElement = toJDOMElementBuscar(viaje);

		return new Document(viajeElement);
	}

	public static Document toXmlBuscar(List<ServiceViajeDto> viaje) {
		Element appElement = new Element("viaje", XML_NS);
		for (int i = 0; i < viaje.size(); i++) {
			ServiceViajeDto xmlViajeDto = viaje.get(i);
			Element viajeElement = toJDOMElementBuscar(xmlViajeDto);
			appElement.addContent(viajeElement);
		}
		return new Document(appElement);
	}

	public static Element toJDOMElementBuscar(ServiceViajeDto viaje) {

		Element viajeElement = new Element("viaje", XML_NS);

		if (viaje.getIdViaje() != null) {
			Element idViajeElement = new Element("idViaje", XML_NS);
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
		System.out.println(viaje.getFechaReserva());
		viajeElement.addContent(fechaReservaViajeElement);

		Element puntuacionViajeElement = new Element("puntuacion", XML_NS);
		puntuacionViajeElement.setText(Integer.toString(viaje.getPuntuacion()));
		viajeElement.addContent(puntuacionViajeElement);

		return viajeElement;

	}

	public static Document toXmlPuntuar(List<ServiceViajeDto> viaje) {
		Element appElement = new Element("viaje", XML_NS);
		for (int i = 0; i < viaje.size(); i++) {
			ServiceViajeDto xmlViajeDto = viaje.get(i);
			Element viajeElement = toJDOMElementPuntuar(xmlViajeDto);
			appElement.addContent(viajeElement);
		}
		return new Document(appElement);
	}

	public static Element toJDOMElementPuntuar(ServiceViajeDto viaje) {

		Element viajeElement = new Element("viaje", XML_NS);

		if (viaje.getIdViaje() != null) {
			Element idViajeElement = new Element("idViaje", XML_NS);
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

	// Recibe la "lectura del documento", y devuelve el objetoDTO
	// SAXBuilder es un parser de XML
	public static ServiceViajeDto toServiceViajeDto(InputStream viajeXml) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(viajeXml);
			Element rootElement = document.getRootElement();

			return toServiceViajeDto(rootElement);
		} catch (ParsingException e) {
			throw e;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	// Igual que el de arriba pero sólo con un objeto, en vez de con el archivo
	public static ServiceViajeDto toServiceViajeDto(Element viajeElement)
			throws ParsingException, DataConversionException {
		if (!"viaje".equals(viajeElement.getName())) {
			throw new ParsingException("Elemento no reconocido '" + viajeElement.getName() + "' ('viaje' esperado)");
		}
		Element idViajeElement = viajeElement.getChild("idViaje", XML_NS);
		Long identificador = null;
		if (idViajeElement != null) {
			identificador = Long.valueOf(idViajeElement.getTextTrim());
		}

		Long idConductor = Long.valueOf(viajeElement.getChildText("idConductor", XML_NS));
		String origen = viajeElement.getChildTextNormalize("origen", XML_NS);
		String destino = viajeElement.getChildTextNormalize("destino", XML_NS);
		String idUsuario = viajeElement.getChildTextNormalize("idUsuario", XML_NS);
		String tarjetaCredito = viajeElement.getChildTextNormalize("tarjetaCredito", XML_NS);

		return new ServiceViajeDto(identificador, idConductor, origen, destino, idUsuario, tarjetaCredito);
	}

	public static ServiceViajeDto toServiceViajeDtoPuntuar(InputStream viajeXml) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(viajeXml);
			Element rootElement = document.getRootElement();

			return toServiceViajeDtoPuntuar(rootElement);
		} catch (ParsingException e) {
			throw e;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	// Igual que el de arriba pero sólo con un objeto, en vez de con el archivo
	public static ServiceViajeDto toServiceViajeDtoPuntuar(Element viajeElement)
			throws ParsingException, DataConversionException {
		if (!"viaje".equals(viajeElement.getName())) {
			throw new ParsingException("Elemento no reconocido '" + viajeElement.getName() + "' ('viaje' esperado)");
		}
		Element idViajeElement = viajeElement.getChild("idViaje", XML_NS);
		Long identificador = null;
		if (idViajeElement != null) {
			identificador = Long.valueOf(idViajeElement.getTextTrim());
		}

		String idUsuario = viajeElement.getChildTextNormalize("idUsuario", XML_NS);
		int puntuacion = Integer.valueOf(viajeElement.getChildText("puntuacion", XML_NS));

		return new ServiceViajeDto(identificador, idUsuario, puntuacion);
	}

}
