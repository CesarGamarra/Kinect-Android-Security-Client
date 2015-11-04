package com.svalero.seguridadkinect;

import com.svalero.beans.Conexion;
import com.svalero.datos.Data_utils;
import com.svalero.services.ServiceConexion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModificarConexion extends Activity{
	private EditText edtNombre;
	private EditText edtUsuario;
	private EditText edtPass;
	private EditText edtIp;
	private EditText edtPuerto;
	private Button btnGuardarCambios;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_conexion);
        
        ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        
        edtNombre = (EditText) findViewById(R.id.edtNombre);
	    edtUsuario = (EditText) findViewById(R.id.edtUsuario);
	    edtPass = (EditText) findViewById(R.id.edtPass);
	    edtIp = (EditText) findViewById(R.id.edtIp);
	    edtPuerto = (EditText) findViewById(R.id.edtPuerto);
	    btnGuardarCambios = (Button) findViewById(R.id.btnGuardarCambios);
        
	    final int idConexion=Data_utils.getIdConexionActual();
	    
	    edtNombre.setText(Data_utils.getListaConexiones().get(idConexion).getNombreConexion());
	    edtUsuario.setText(Data_utils.getListaConexiones().get(idConexion).getUsuario());
	    edtPass.setText(Data_utils.getListaConexiones().get(idConexion).getPass());
	    edtIp.setText(Data_utils.getListaConexiones().get(idConexion).getIpConexion());
	    edtPuerto.setText(Data_utils.getListaConexiones().get(idConexion).getPuerto()+"");
	    
	    btnGuardarCambios.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Conexion conexion=new Conexion();
				conexion.setIdConexion(Data_utils.getListaConexiones().get(idConexion).getIdConexion());
				conexion.setNombreConexion(edtNombre.getText().toString());
				conexion.setUsuario(edtUsuario.getText().toString());
				conexion.setPass(edtPass.getText().toString());
				conexion.setIpConexion(edtIp.getText().toString());
				conexion.setPuerto(Integer.valueOf(edtPuerto.getText().toString()));
				
				ServiceConexion serviceConexion=new ServiceConexion();
				serviceConexion.update(conexion);
				
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
			}
		});
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
	        /*case R.id.Nueva:
	        	intent = new Intent(getBaseContext(), NuevaConexion.class);
				startActivity(intent);
	        	return true;*/

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
