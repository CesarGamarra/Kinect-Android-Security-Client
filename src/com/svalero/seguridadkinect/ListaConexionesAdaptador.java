package com.svalero.seguridadkinect;

import java.util.ArrayList;
import com.svalero.beans.Conexion;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListaConexionesAdaptador extends ArrayAdapter<Conexion> {
	private ArrayList<Conexion> misConexiones;
	private Context myContext;
	
	public ListaConexionesAdaptador(Context context, int layoutFila,
			ArrayList<Conexion> lstConexiones) {
		super(context, layoutFila, lstConexiones);
		this.misConexiones = lstConexiones;
		this.myContext = context;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			LayoutInflater vi = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		Conexion con = misConexiones.get(position);
		if(con != null){
			TextView txtIdConexion = (TextView) v.findViewById(R.id.txtIdConexion);
			TextView txtNombre = (TextView) v.findViewById(R.id.txtNombreConexion);
			
			try{
				if(txtNombre != null){
					txtNombre.setText(con.getNombreConexion());
				}
				if(txtIdConexion != null){
					txtIdConexion.setText(""+con.getIdConexion());
				}
			}catch(Exception ex){
				Log.e("Row", ex.getMessage());
			}
		}
		return v;
	}	
}
