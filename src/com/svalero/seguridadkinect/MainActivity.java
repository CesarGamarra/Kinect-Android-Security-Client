package com.svalero.seguridadkinect;

import com.svalero.beans.Conexion;
import com.svalero.datos.Data_utils;
import com.svalero.services.ServiceConexion;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity {
	private ListaConexionesAdaptador adaptador;
	ListView lista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_conexiones);
		main=this;
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		buscarLista();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // icono de la aplicación pulsado en la barra de acción; ir a inicio
	            intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.Nueva:
	        	intent = new Intent(getBaseContext(), NuevaConexion.class);
				startActivity(intent);
	        	return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private static MainActivity main;
    public static MainActivity getInstance(){
    	return main;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void buscarLista(){
		ServiceConexion serviceConexion=new ServiceConexion();
		serviceConexion.findAll();
	}
	
	public synchronized void creaLista(){
		lista = (ListView)findViewById(R.id.list);
		registerForContextMenu(lista);
		try{
			this.adaptador=new ListaConexionesAdaptador(getBaseContext(),R.layout.row, Data_utils.getListaConexiones());
			lista.setAdapter(this.adaptador);
			lista.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long id) {
					
					seleccionConexion(lista, v, position, id);
				}
			});
		}catch(Exception ex){
			Log.e("ListaConexiones", ex.getMessage());
		}
	}
	protected void seleccionConexion(ListView l, View v, int position, long id) {
		try{
			Conexion conexion = (Conexion) l.getItemAtPosition(position);
			//Data_utils.setIdConexionActual(conexion.getIdConexion());
			Data_utils.setIdConexionActual(position);
			
			Intent myIntent = new Intent(this, Visualizacion.class);
			
			startActivity(myIntent);
		}catch(Exception ex){
			Log.e("TabOnClick", ex.getMessage());
		}
	}	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.submenu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Conexion con=new Conexion();
		AdapterView.AdapterContextMenuInfo info =
	            (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId()) {
	        case R.id.Modificar:
	        	Log.e("entro", "modificar");
	        	
	        	con=(Conexion)lista.getItemAtPosition(info.position);
	        	Data_utils.setIdConexionActual(info.position);
				
				Intent myIntent = new Intent(this, ModificarConexion.class);
				
				startActivity(myIntent);
	        	
	    		
	            //lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
	            return true;
	        case R.id.Eliminar:
	        	Log.e("entro", "eliminar");
	        	
	    		con=(Conexion)lista.getItemAtPosition(info.position);
	    		Log.e("pulsado","pulsado"+ con.getIdConexion());
	    		ServiceConexion serviceConexion=new ServiceConexion();
	    		serviceConexion.delete(con.getIdConexion());
	    		buscarLista();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
		}
	}
}
