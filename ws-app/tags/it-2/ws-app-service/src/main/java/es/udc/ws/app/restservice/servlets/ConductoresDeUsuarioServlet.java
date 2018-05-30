package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceConductoresDeUsuarioDto;
import es.udc.ws.app.model.conductorservice.ConductorServiceFactory;
import es.udc.ws.app.model.exceptions.ConductorNoEncontradoException;
import es.udc.ws.app.model.viaje.Viaje;
import es.udc.ws.app.restservice.xml.XmlServiceConductoresDeUsuarioDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class ConductoresDeUsuarioServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String path = ServletUtils.normalizePath(request.getPathInfo());
		String idUsuario = path.substring(1);

		try {
			ArrayList<ServiceConductoresDeUsuarioDto> conductoresDeUsuarioDto = new ArrayList<ServiceConductoresDeUsuarioDto>();
			ArrayList<Long> idConductores = new ArrayList<Long>();

			List<Viaje> viajes = ConductorServiceFactory.getService().viajesUsuario(idUsuario);
			for (Viaje i : viajes) {

				if (!idConductores
						.contains(
								XmlServiceConductoresDeUsuarioDtoConversor
										.toServiceConductoresDeUsuarioDto(ConductorServiceFactory.getService()
												.buscarConductor(i.getIdConductor()), i.getIdUsuario())
										.getIdConductor())) {

					ServiceConductoresDeUsuarioDto conductor = XmlServiceConductoresDeUsuarioDtoConversor
							.toServiceConductoresDeUsuarioDto(
									ConductorServiceFactory.getService().buscarConductor(i.getIdConductor()),
									i.getIdUsuario());

					idConductores.add(conductor.getIdConductor());
					conductoresDeUsuarioDto.add(conductor);

					conductor.setPuntuacionAcumulada(conductor.getPuntuacionAcumulada() + i.getPuntuacion());
					conductor.setTotalNotas(conductor.getTotalNotas() + 1);
					if (conductor.getTotalNotas() != 0) {
						conductor.setMediaPersonal(conductor.getPuntuacionAcumulada() / conductor.getTotalNotas());
					}

				} else if (i.getPuntuacion() != -1) {
					ServiceConductoresDeUsuarioDto conductor = conductoresDeUsuarioDto
							.get(conductoresDeUsuarioDto.size() - 1);

					conductor.setPuntuacionAcumulada(conductor.getPuntuacionAcumulada() + i.getPuntuacion());
					conductor.setTotalNotas(conductor.getTotalNotas() + 1);
					if (conductor.getTotalNotas() != 0) {
						conductor.setMediaPersonal(conductor.getPuntuacionAcumulada() / conductor.getTotalNotas());
					}
				}
			}

			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_OK,
					XmlServiceConductoresDeUsuarioDtoConversor.toXml(conductoresDeUsuarioDto), null);

		} catch (ConductorNoEncontradoException e) {
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
					XmlServiceExceptionConversor.toConductorNoEncontradoException(e), null);
			return;
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
					XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		}

	}

}