package com.svalero.datos;

import java.util.ArrayList;

import com.svalero.beans.Conexion;

public class Data_utils {
	private static ArrayList<Conexion> listaConexiones=new ArrayList<Conexion>();
	private static int idConexionActual;

	public static ArrayList<Conexion> getListaConexiones() {
		return listaConexiones;
	}

	public static void setListaConexiones(ArrayList<Conexion> listaConexiones) {
		Data_utils.listaConexiones = listaConexiones;
	}

	public static int getIdConexionActual() {
		return idConexionActual;
	}

	public static void setIdConexionActual(int idConexionActual) {
		Data_utils.idConexionActual = idConexionActual;
	}
	
}
