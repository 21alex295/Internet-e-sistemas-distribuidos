package es.udc.ws.app.model.conductor;

import java.util.Calendar;

public class Conductor {
	
	private Long idConductor;
	private String nombre;
	private String ciudad;
	private String modeloCoche;
	private byte horaInicio;
	private byte horaFin;
	private Calendar fechaAlta;
	private int puntuacionAcumulada;
	private int totalViajes;
	
	public Conductor(Long idConductor, String nombre, String ciudad, int puntuacionAcumulada, int totalViajes){
		super();
		this.idConductor = idConductor;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalViajes = totalViajes;
	}
	
	public Conductor(Long idConductor, String nombre, String ciudad, String modeloCoche, byte horaInicio, byte horaFin,
			Calendar fechaAlta, int puntuacionAcumulada, int totalViajes) {
		super();
		this.idConductor = idConductor;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.modeloCoche = modeloCoche;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.fechaAlta = fechaAlta;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalViajes = totalViajes;
	}
	
	public Conductor(Long idConductor, String nombre, String ciudad, String modeloCoche, byte horaInicio, byte horaFin,
			 int puntuacionAcumulada, int totalViajes) {
		super();
		this.idConductor = idConductor;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.modeloCoche = modeloCoche;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalViajes = totalViajes;
	}
	
	public Conductor( String nombre, String ciudad, String modeloCoche, byte horaInicio, byte horaFin,
			Calendar fechaAlta, int puntuacionAcumulada, int totalViajes) {
		super();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.modeloCoche = modeloCoche;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.fechaAlta = fechaAlta;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalViajes = totalViajes;
	}

	
	public Conductor( String nombre, String ciudad, String modeloCoche, byte horaInicio, byte horaFin, int puntuacionAcumulada,
			 int totalViajes) {
		super();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.modeloCoche = modeloCoche;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalViajes = totalViajes;
	}
	

	public Calendar getFechaAlta() {
		return fechaAlta;
	}


	public void setFechaAlta(Calendar fechaAlta) {
		this.fechaAlta = fechaAlta;
		if (fechaAlta != null){
			this.fechaAlta.set(Calendar.MILLISECOND,0);
		}
	}

	public int getTotalViajes() {
		return totalViajes;
	}


	public void setTotalViajes(int totalViajes) {
		this.totalViajes = totalViajes;
	}


	public int getPuntuacionAcumulada() {
		return puntuacionAcumulada;
	}


	public void setPuntuacionAcumulada(int puntuacionAcumulada) {
		this.puntuacionAcumulada = puntuacionAcumulada;
	}


	public void setHoraInicio(byte horaInicio) {
		this.horaInicio = horaInicio;
	}


	public void setHoraFin(byte horaFin) {
		this.horaFin = horaFin;
	}


	public byte getHoraInicio() {
		return horaInicio;
	}


	public byte getHoraFin() {
		return horaFin;
	}


	public Long getIdConductor() {
		return idConductor;
	}
	
	public void setIdConductor(Long idConductor){
		this.idConductor = idConductor;
	}


	public String getNombre() {
		return nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getModeloCoche() {
		return modeloCoche;
	}

	public void setModeloCoche(String modeloCoche) {
		this.modeloCoche = modeloCoche;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ciudad == null) ? 0 : ciudad.hashCode());
		result = prime * result + ((fechaAlta == null) ? 0 : fechaAlta.hashCode());
		result = prime * result + horaFin;
		result = prime * result + horaInicio;
		result = prime * result + ((idConductor == null) ? 0 : idConductor.hashCode());
		result = prime * result + ((modeloCoche == null) ? 0 : modeloCoche.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + puntuacionAcumulada;
		result = prime * result + totalViajes;
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
		Conductor other = (Conductor) obj;
		if (ciudad == null) {
			if (other.ciudad != null)
				return false;
		} else if (!ciudad.equals(other.ciudad))
			return false;
		if (fechaAlta == null) {
			if (other.fechaAlta != null)
				return false;
		} else if (!fechaAlta.equals(other.fechaAlta))
			return false;
		if (horaFin != other.horaFin)
			return false;
		if (horaInicio != other.horaInicio)
			return false;
		if (idConductor == null) {
			if (other.idConductor != null)
				return false;
		} else if (!idConductor.equals(other.idConductor))
			return false;
		if (modeloCoche == null) {
			if (other.modeloCoche != null)
				return false;
		} else if (!modeloCoche.equals(other.modeloCoche))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (puntuacionAcumulada != other.puntuacionAcumulada)
			return false;
		if (totalViajes != other.totalViajes)
			return false;
		return true;
	}

	
}