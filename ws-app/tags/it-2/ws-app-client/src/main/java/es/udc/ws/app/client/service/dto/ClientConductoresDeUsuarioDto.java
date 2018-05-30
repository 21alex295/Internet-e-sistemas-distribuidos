package es.udc.ws.app.client.service.dto;

public class ClientConductoresDeUsuarioDto {
	private Long idConductor;
	private String idUsuario;
	private String nombre;
	private String ciudad;
	private int puntuacionAcumulada;
	private int totalNotas;
	private int mediaPersonal;
	
	
	
	public ClientConductoresDeUsuarioDto(Long idConductor, String idUsuario, String nombre, String ciudad,
			int puntuacionAcumulada, int totalNotas, int mediaPersonal) {
		super();
		this.idConductor = idConductor;
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.puntuacionAcumulada = puntuacionAcumulada;
		this.totalNotas = totalNotas;
		this.mediaPersonal = mediaPersonal;
	}
	public int getPuntuacionAcumulada() {
		return puntuacionAcumulada;
	}
	public void setPuntuacionAcumulada(int puntuacionAcumulada) {
		this.puntuacionAcumulada = puntuacionAcumulada;
	}
	public int getTotalNotas() {
		return totalNotas;
	}
	public void setTotalNotas(int totalNotas) {
		this.totalNotas = totalNotas;
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
	public int getMediaPersonal() {
		return mediaPersonal;
	}
	public void setMediaPersonal(int mediaPersonal) {
		this.mediaPersonal = mediaPersonal;
	}
	
	
}
