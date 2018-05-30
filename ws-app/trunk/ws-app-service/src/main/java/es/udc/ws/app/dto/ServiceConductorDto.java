package es.udc.ws.app.dto;

public class ServiceConductorDto {

	private Long idConductor;
	private String nombre;
	private String ciudad;
	private String modeloCoche;
	private byte horaInicio;
	private byte horaFin;
	private int puntuacionAcumulada;
	private int totalViajes;
	
	public ServiceConductorDto(Long idConductor, String nombre, String ciudad, String modeloCoche, byte horaInicio,
			byte horaFin, int puntuacionAcumulada, int totalViajes) {
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


	public ServiceConductorDto(String nombre, String ciudad, String modeloCoche, byte horaInicio,
			byte horaFin, int puntuacionAcumulada, int totalViajes) {
		super();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.modeloCoche = modeloCoche;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalViajes = totalViajes;
	}

	
	
	public Long getIdConductor() {
		return idConductor;
	}
	public void setIdConductor(Long idConductor) {
		this.idConductor = idConductor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public int getPuntuacionAcumulada() {
		return puntuacionAcumulada;
	}
	public void setPuntuacionAcumulada(int puntuacionAcumulada) {
		this.puntuacionAcumulada = puntuacionAcumulada;
	}
	public int getTotalViajes() {
		return totalViajes;
	}
	public void setTotalViajes(int totalViajes) {
		this.totalViajes = totalViajes;
	}
	public String getModeloCoche() {
		return modeloCoche;
	}
	public void setModeloCoche(String modeloCoche) {
		this.modeloCoche = modeloCoche;
	}
	public byte getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(byte horaInicio) {
		this.horaInicio = horaInicio;
	}
	public byte getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(byte horaFin) {
		this.horaFin = horaFin;
	}
}
