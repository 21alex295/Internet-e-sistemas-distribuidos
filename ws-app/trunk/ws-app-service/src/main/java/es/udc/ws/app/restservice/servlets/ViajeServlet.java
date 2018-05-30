package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceViajeDto;
import es.udc.ws.app.model.conductorservice.ConductorServiceFactory;
import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.app.model.exceptions.PuntuacionRepetidaException;
import es.udc.ws.app.model.viaje.Viaje;
import es.udc.ws.app.restservice.exceptions.ViajeDeOtroUsuarioException;
import es.udc.ws.app.restservice.exceptions.ViajeNoExisteException;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceViajeDtoConversor;
import es.udc.ws.app.serviceutil.ViajeToViajeDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

@SuppressWarnings("serial")
public class ViajeServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = ServletUtils.normalizePath(request.getPathInfo());
		Long idViaje;
		if ((path != null)) {

			String subpath = path.substring(1);
			String[] list = subpath.split("/");
			if (list[1].equalsIgnoreCase("puntuar")) {

				String idUsuario = request.getParameter("idUsuario");
				String puntuacionAsString = request.getParameter("puntuacion");
				int puntuacion = 0;

				try {
					puntuacion = Integer.valueOf(puntuacionAsString);
				} catch (NumberFormatException ex) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
							XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
									"Invalid request: " + "Invalid viajeId '" + list[0] + " ' ")),
							null);
					return;
				}

				try {
					idViaje = Long.valueOf(list[0]);
				} catch (NumberFormatException ex) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
							XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
									"Invalid request: " + "Invalid viajeId '" + list[0] + " ' ")),
							null);
					return;
				}

				Viaje viajeEncontrado;

				try {
					viajeEncontrado = ConductorServiceFactory.getService().buscarViaje(idViaje);

					if (!viajeEncontrado.getIdUsuario().equals(idUsuario)) {
						ServletUtils
								.writeServiceResponse(response,
										HttpServletResponse.SC_UNAUTHORIZED, XmlServiceExceptionConversor
												.toViajeDeOtroUsuarioException(new ViajeDeOtroUsuarioException()),
										null);
						return;
					}
				} catch (InstanceNotFoundException ex) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
							XmlServiceExceptionConversor.toViajeNoExisteException(new ViajeNoExisteException()), null);
					return;

				}
				try {
					ConductorServiceFactory.getService().puntuarViaje(idViaje, puntuacion);

				} catch (ConductorNoEncontradoException e) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
							XmlServiceExceptionConversor.toConductorNoEncontradoException(e), null);
					return;

				} catch (InstanceNotFoundException ex) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
							XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
					return;
				} catch (InputValidationException ex) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
							XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
					return;
				} catch (PuntuacionRepetidaException e) {
					ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_FORBIDDEN,
							XmlServiceExceptionConversor.toPuntuacionRepetidaException(e), null);
				}
			}
		} else if (path == null || path.length() == 0) {

			ServiceViajeDto xmlViaje;

			try {
				// Intenta coller Stream da petición e convertilo a XML nun
				// principio,e despois a DTO

				xmlViaje = XmlServiceViajeDtoConversor.toServiceViajeDto(request.getInputStream());

			} catch (ParsingException ex) {
				ServletUtils
						.writeServiceResponse(response,
								HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
										.toInputValidationExceptionXml(new InputValidationException(ex.getMessage())),
								null);
				return;
			}

			// Convirte o DTO a un viaje para añadilo
			Viaje viaje = ViajeToViajeDtoConversor.toViaje(xmlViaje);
			// System.out.println(viaje.getIdViaje());

			// Añade o objeto(viaje)
			try {
				viaje = ConductorServiceFactory.getService().contratarViaje(viaje.getIdConductor(), viaje.getOrigen(),
						viaje.getDestino(), viaje.getIdUsuario(), viaje.getTarjetaCredito());

			} catch (ConductorNoEncontradoException e) {
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
						XmlServiceExceptionConversor.toConductorNoEncontradoException(e), null);
			} catch (InputValidationException ex) {
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
						XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
				return;
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
						XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			}

			// Desfai a conversión (viaje -> viajeDto)
			ServiceViajeDto viajeDto = ViajeToViajeDtoConversor.toViajeDto(viaje);

			// Crea unha URL e añádea a headers (almacena nombre e contenido)
			String viajeUrl = ServletUtils.normalizePath(request.getRequestURL().toString()) + "/" + viaje.getIdViaje();
			Map<String, String> headers = new HashMap<>(1);
			headers.put("Location", viajeUrl);

			// Respuesta
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_CREATED,
					XmlServiceViajeDtoConversor.toXml(viajeDto), headers);

		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String idUsuario;

		if (request.getParameter("idConductor") != null) {
			try {
				long idConductor = Long.valueOf(request.getParameter("idConductor"));
				List<Viaje> viajes = ConductorServiceFactory.getService().viajesConductor(idConductor);
				List<ServiceViajeDto> viajeDtos = ViajeToViajeDtoConversor.toViajeDto(viajes);
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_OK,
						XmlServiceViajeDtoConversor.toXmlBuscar(viajeDtos), null);

			} catch (ConductorNoEncontradoException e) {
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_CONFLICT,
						XmlServiceExceptionConversor.toConductorNoEncontradoException(e), null);
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
						XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			}
		} else if ((idUsuario = request.getParameter("idUsuario")) != null) {
			System.out.println(idUsuario);
			try {
				List<Viaje> viajes = ConductorServiceFactory.getService().viajesUsuario(idUsuario);
				List<ServiceViajeDto> viajeDtos = ViajeToViajeDtoConversor.toViajeDto(viajes);
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_OK,
						XmlServiceViajeDtoConversor.toXmlBuscar(viajeDtos), null);

			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
						XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			}
		}

	}
}
