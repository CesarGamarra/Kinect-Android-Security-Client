package com.svalero.beans;

public class Conexion {
	private int idConexion;
	private String nombreConexion;
	private String usuario;
	private String pass;
	private String ipConexion;
	private int puerto;
	
	public int getIdConexion() {
		return idConexion;
	}
	public void setIdConexion(int idConexion) {
		this.idConexion = idConexion;
	}
	public String getNombreConexion() {
		return nombreConexion;
	}
	public void setNombreConexion(String nombreConexion) {
		this.nombreConexion = nombreConexion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getIpConexion() {
		return ipConexion;
	}
	public void setIpConexion(String ipConexion) {
		this.ipConexion = ipConexion;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
}
