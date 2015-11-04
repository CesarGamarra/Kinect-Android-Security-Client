package com.svalero.services;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.svalero.beans.Conexion;
import com.svalero.datos.Data_utils;
import com.svalero.herramientas.ConexionSQLite;
import com.svalero.seguridadkinect.MainActivity;
import com.svalero.seguridadkinect.NuevaConexion;

public class ServiceConexion {
	public void add(Conexion conexion){
		ConexionSQLite con=new ConexionSQLite(NuevaConexion.getInstance().getBaseContext(),"CONEXIONES", null, 1);
		
		SQLiteDatabase db=con.getWritableDatabase();
		
		if(db != null)
        {
            db.execSQL("Insert into CONEXIONES (nombre,usuario,pass,ip,puerto) " +
                       "VALUES ('" + conexion.getNombreConexion()+"','"
                       +conexion.getUsuario() + "', '" + conexion.getPass()+"','"+
                       conexion.getIpConexion() + "', " + conexion.getPuerto()+")");
        db.close();
        }
	}
	
	public void update(Conexion conexion){
		ConexionSQLite con=new ConexionSQLite(MainActivity.getInstance().getBaseContext(),"CONEXIONES", null, 1);
		
		SQLiteDatabase db=con.getWritableDatabase();
		
		if(db != null)
        {
			db.execSQL("Update CONEXIONES set nombre='"+conexion.getNombreConexion()+
					"', usuario='"+conexion.getUsuario()+
					"', pass='"+conexion.getPass()+
					"', ip='"+conexion.getIpConexion()+
					"', puerto="+conexion.getPuerto()+
					" WHERE idConexion="+conexion.getIdConexion());
            db.close();
        }
		
	}
	
	public void delete(int idConexion){
		ConexionSQLite con=new ConexionSQLite(MainActivity.getInstance().getBaseContext(),"CONEXIONES", null, 1);
		
		SQLiteDatabase db=con.getWritableDatabase();
		
		if(db != null)
        {
            db.execSQL("Delete from CONEXIONES where idConexion="+idConexion);
            db.close();
        }
	}
	
	
	public void findAll(){
		Conexion datosConexion=new Conexion();
		ArrayList<Conexion> listaConexiones=new ArrayList<Conexion>();
		
		try{
			ConexionSQLite con=new ConexionSQLite(MainActivity.getInstance().getBaseContext(),"CONEXIONES", null, 1);
			
			SQLiteDatabase db=con.getWritableDatabase();
			Cursor cursor = db.rawQuery("Select * from CONEXIONES",null);
			
			//Nos aseguramos de que existe al menos un registro
			if (cursor.moveToFirst()) {
			     //Recorremos el cursor hasta que no haya más registros
			     do {
			    	 datosConexion.setIdConexion(cursor.getInt(0));
			         datosConexion.setNombreConexion(cursor.getString(1));
			         datosConexion.setUsuario(cursor.getString(2));
			         datosConexion.setPass(cursor.getString(3));
			         datosConexion.setIpConexion(cursor.getString(4));
			         datosConexion.setPuerto(cursor.getInt(5));
			         listaConexiones.add(datosConexion);
			         datosConexion=new Conexion();
			     } while(cursor.moveToNext());
			}
			db.close();
			Data_utils.setListaConexiones(listaConexiones);
			MainActivity.getInstance().creaLista();
		}catch(Exception ex){
			
		}
	}
}