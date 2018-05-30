package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceViajeDto;
import es.udc.ws.app.model.viaje.Viaje;

public class ViajeToViajeDtoConversor {
	
	public static List<ServiceViajeDto> toViajeDto(List<Viaje>viajes){
		List<ServiceViajeDto> viajeDtos = new ArrayList<>(viajes.size());
		for(int i=0; i<viajes.size();i++){
			Viaje viaje = viajes.get(i);
			viajeDtos.add(toViajeDto(viaje));
		}
		return viajeDtos;
	}
	
	public static ServiceViajeDto toViajeDto(Viaje viaje){
		return new ServiceViajeDto(viaje.getIdViaje(),viaje.getIdConductor(),viaje.getOrigen(),
				viaje.getDestino(), viaje.getIdUsuario(), viaje.getTarjetaCredito(),
				viaje.getFechaReserva(),viaje.getPuntuacion());
	}
	
	public static Viaje toViaje(ServiceViajeDto viaje){
		return new Viaje(viaje.getIdViaje(),viaje.getIdConductor(),viaje.getOrigen(),
				viaje.getDestino(), viaje.getIdUsuario(), viaje.getTarjetaCredito(),
				viaje.getFechaReserva(),viaje.getPuntuacion());
	}
	

}
