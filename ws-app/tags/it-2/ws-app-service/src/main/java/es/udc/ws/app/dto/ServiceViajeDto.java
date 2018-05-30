package es.udc.ws.app.dto;

import java.util.Calendar;

public class ServiceViajeDto {
	private Long idViaje;
	private Long idConductor;
	private String origen;
	private String destino;
	private String idUsuario;
	private String tarjetaCredito;
	private Calendar fechaReserva;
	private int puntuacion;

    public ServiceViajeDto(){
    	
    }
    
	public ServiceViajeDto(Long idViaje, Long idConductor, String origen, String destino, String idUsuario,
			String tarjetaCredito, Calendar fechaReserva, int puntuacion) {
		super();
		this.idViaje = idViaje;
		this.idConductor = idConductor;
		this.origen = origen;
		this.destino = destino;
		this.idUsuario = idUsuario;
		this.tarjetaCredito = tarjetaCredito;
		this.fechaReserva = fechaReserva;
		this.puntuacion = puntuacion;
	}


	public ServiceViajeDto(Long idViaje, Long idConductor, String origen, String destino, String idUsuario, String tarjetaCredito) {
		super();
		this.idViaje = idViaje;
		this.idConductor = idConductor;
		this.origen = origen;
		this.destino = destino;
		this.idUsuario = idUsuario;
		this.tarjetaCredito = tarjetaCredito;
	}

	public ServiceViajeDto(Long idViaje, Long idConductor, String idUsuario, int puntuacion) {
		super();
		this.idViaje = idViaje;
		this.idConductor = idConductor;
		this.idUsuario = idUsuario;
		this.puntuacion = puntuacion;
	}
	

	public ServiceViajeDto(Long idViaje, String idUsuario, int puntuacion) {
		super();
		this.idViaje = idViaje;
		this.idUsuario = idUsuario;
		this.puntuacion = puntuacion;
	}



	public Long getIdViaje() {
		return idViaje;
	}

	public void setIdViaje(Long idViaje) {
		this.idViaje = idViaje;
	}

	public Long getIdConductor() {
		return idConductor;
	}

	public void setIdConductor(Long idConductor) {
		this.idConductor = idConductor;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	
	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getTarjetaCredito() {
		return tarjetaCredito;
	}

	public void setTarjetaCredito(String tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	public Calendar getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Calendar fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

    
 
}
