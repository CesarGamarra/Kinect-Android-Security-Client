package com.svalero.seguridadkinect;

import java.util.List;

import com.svalero.beans.Conexion;
import com.svalero.services.ServiceConexion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NuevaConexion extends Activity{
	private EditText edtNombre;
	private EditText edtUsuario;
	private EditText edtPass;
	private EditText edtIp;
	private EditText edtPuerto;
	private Button btnGuardar;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_conexion);
        
        nueva = this;
        
        ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        
        edtNombre = (EditText) findViewById(R.id.edtNombre);
	    edtUsuario = (EditText) findViewById(R.id.edtUsuario);
	    edtPass = (EditText) findViewById(R.id.edtPass);
	    edtIp = (EditText) findViewById(R.id.edtIp);
	    edtPuerto = (EditText) findViewById(R.id.edtPuerto);
	    btnGuardar = (Button) findViewById(R.id.btnGuardar);
        
	    btnGuardar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Conexion conexion=new Conexion();
				conexion.setNombreConexion(edtNombre.getText().toString());
				conexion.setUsuario(edtUsuario.getText().toString());
				conexion.setPass(edtPass.getText().toString());
				conexion.setIpConexion(edtIp.getText().toString());
				conexion.setPuerto(Integer.valueOf(edtPuerto.getText().toString()));
				
				ServiceConexion serviceConexion=new ServiceConexion();
				serviceConexion.add(conexion);
				
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
    
    private static NuevaConexion nueva;
    public static NuevaConexion getInstance(){
    	return nueva;
    }
}