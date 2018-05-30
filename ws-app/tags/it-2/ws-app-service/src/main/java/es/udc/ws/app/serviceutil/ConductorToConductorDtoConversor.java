package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceConductorDto;
import es.udc.ws.app.model.conductor.Conductor;

public class ConductorToConductorDtoConversor {
	
	public static List<ServiceConductorDto> toConductorDto(List<Conductor>conductores){
		List<ServiceConductorDto> conductorDtos = new ArrayList<>(conductores.size());
		for(int i=0; i<conductores.size();i++){
			Conductor conductor = conductores.get(i);
			conductorDtos.add(toConductorDto(conductor));
		}
		
		return conductorDtos;
	}
	
	public static ServiceConductorDto toConductorDto(Conductor conductor){
		return new ServiceConductorDto(conductor.getIdConductor(),conductor.getNombre(),conductor.getCiudad(),
				conductor.getModeloCoche(), conductor.getHoraInicio(), conductor.getHoraFin(),
				conductor.getPuntuacionAcumulada(),conductor.getTotalViajes());
	}
	
	public static Conductor toConductor(ServiceConductorDto conductor){
		return new Conductor(conductor.getIdConductor(),conductor.getNombre(),conductor.getCiudad(),
				conductor.getModeloCoche(),conductor.getHoraInicio(),conductor.getHoraFin(),
				conductor.getPuntuacionAcumulada(),conductor.getTotalViajes());
	}

}
