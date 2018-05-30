package es.udc.ws.app.model.viaje;

import java.util.Calendar;

public class Viaje {

	private Long idViaje;
	private Long idConductor;
	private String origen;
	private String destino;
	private String idUsuario;
	private String tarjetaCredito;
	private Calendar fechaReserva;
	private int puntuacion;
	
	
	
	
	
	public Viaje(Long idConductor, String origen, String destino,String idUsuario, String tarjetaCredito,
			Calendar fechaReserva, int puntuacion) {
		super();
		this.idConductor = idConductor;
		this.origen = origen;
		this.destino = destino;
		this.idUsuario = idUsuario;
		this.tarjetaCredito = tarjetaCredito;
		this.fechaReserva = fechaReserva;
		this.puntuacion = puntuacion;
	}
	

	public Viaje(Long idViaje, Long idConductor, String origen, String destino, String idUsuario, String tarjetaCredito,
			Calendar fechaReserva, int puntuacion) {
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

	public Viaje( Long idConductor, String origen, String destino, String idUsuario, String tarjetaCredito,
			int puntuacion) {
		super();
		this.idConductor = idConductor;
		this.origen = origen;
		this.destino = destino;
		this.idUsuario = idUsuario;
		this.tarjetaCredito = tarjetaCredito;
		this.puntuacion = puntuacion;
	}


	public Long getIdViaje() {
		return idViaje;
	}


	public Long getIdConductor() {
		return idConductor;
	}


	public void setIdConductor(Long idConductor) {
		this.idConductor = idConductor;
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


	public String getIdUsuario() {
		return idUsuario;
	}


	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}


	public String getTarjetaCredito() {
		return tarjetaCredito;
	}


	public void setTarjetaCredito(String tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}


	public int getPuntuacion() {
		return puntuacion;
	}


	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}


	public Calendar getFechaReserva() {
		return fechaReserva;
	}


	public void setFechaReserva(Calendar fechaReserva) {
		this.fechaReserva = fechaReserva;
		if (fechaReserva != null){
			this.fechaReserva.set(Calendar.MILLISECOND,0);
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result + ((fechaReserva == null) ? 0 : fechaReserva.hashCode());
		result = prime * result + ((idConductor == null) ? 0 : idConductor.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((idViaje == null) ? 0 : idViaje.hashCode());
		result = prime * result + ((origen == null) ? 0 : origen.hashCode());
		result = prime * result + puntuacion;
		result = prime * result + ((tarjetaCredito == null) ? 0 : tarjetaCredito.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viaje other = (Viaje) obj;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (fechaReserva == null) {
			if (other.fechaReserva != null)
				return false;
		} else if (!fechaReserva.equals(other.fechaReserva))
			return false;
		if (idConductor == null) {
			if (other.idConductor != null)
				return false;
		} else if (!idConductor.equals(other.idConductor))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (idViaje == null) {
			if (other.idViaje != null)
				return false;
		} else if (!idViaje.equals(other.idViaje))
			return false;
		if (origen == null) {
			if (other.origen != null)
				return false;
		} else if (!origen.equals(other.origen))
			return false;
		if (puntuacion != other.puntuacion)
			return false;
		if (tarjetaCredito == null) {
			if (other.tarjetaCredito != null)
				return false;
		} else if (!tarjetaCredito.equals(other.tarjetaCredito))
			return false;
		return true;
	}

	
}
