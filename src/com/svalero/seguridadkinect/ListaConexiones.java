package com.svalero.seguridadkinect;

import com.svalero.beans.Conexion;
import com.svalero.datos.Data_utils;
import com.svalero.services.ServiceConexion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ListaConexiones extends Activity{
	private ListaConexionesAdaptador adaptador;
	ListView lista;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_conexiones);
		listaConexiones=this;
		buscarLista();
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
			/*lista.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.e("entro", "entro");
					return false;
				}
			});*/
		}catch(Exception ex){
			Log.e("ListaConexiones", ex.getMessage());
		}
	}
	
	protected void seleccionConexion(ListView l, View v, int position, long id) {
		try{
			Conexion conexion = (Conexion) l.getItemAtPosition(position);
			Data_utils.setIdConexionActual(conexion.getIdConexion());
			//Bundle bundle = new Bundle();
			//bundle.putString("idConexion", String.valueOf(conexion.getIdConexion()));
			Intent myIntent = new Intent(this, Visualizacion.class);
			//myIntent.putExtras(bundle);
			
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
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	private static ListaConexiones listaConexiones;
    public static ListaConexiones getInstance(){
    	return listaConexiones;
    }
}
