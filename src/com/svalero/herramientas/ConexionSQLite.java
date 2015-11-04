package com.svalero.herramientas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLite extends SQLiteOpenHelper{
	String sqlCreate = "Create table CONEXIONES (idConexion INTEGER primary key autoincrement, nombre TEXT,usuario TEXT," +
			"pass TEXT,ip TEXT, puerto INTEGER)";
	
	public ConexionSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Se elimina la versión anterior de la tabla
		db.execSQL("DROP TABLE IF EXISTS Usuarios");
		 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
	}

}
