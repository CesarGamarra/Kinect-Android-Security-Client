package com.svalero.seguridadkinect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import com.svalero.datos.Data_utils;
import de.roderick.weberknecht.WebSocketException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Visualizacion extends Activity{
	
	private static ImageView image;
	Socket socket = null;
	OutputStream outputStream = null;
	InputStream inputStream = null;
	public static Bitmap bitmap;
	
	private final static Handler manejador = new Handler();
	
	private static boolean identificacionCorrecta=false;
	private static boolean recibeImagen=true;
	
	private Timer timer;
	
	final SocketComunicacion com=new SocketComunicacion();
	
	private Button btnDesconectar;
	private Button btnInclinacion;
	private TextView txtInclinacion;
	ArrayAdapter<CharSequence> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visualizacion);
		
		visualizacion=this;
		recibeImagen=true;
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner_inclinacion);
		adapter = ArrayAdapter.createFromResource(this,
		        R.array.inclinaciones, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int itemPosition, long arg3) {
				String inclinacion=adapter.getItem(itemPosition).toString();
				try {
					if(!inclinacion.equalsIgnoreCase("Inclinacion")){
						com.send("ANGULO "+inclinacion);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		image=(ImageView)findViewById(R.id.pantalla);
        image.setBackgroundColor(Color.BLACK);
        
        btnDesconectar=(Button)findViewById(R.id.btnDesconectar);
        
        btnDesconectar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(btnDesconectar.getText().toString().equalsIgnoreCase("Pausar")){
						btnDesconectar.setText("Reanudar");
						cerrarSocketImagen();
					}else{
						btnDesconectar.setText("Pausar");
						connect();
					}
				} catch (Exception e) {
					
				}
			}
		});
        
        timer = new Timer();
        TimerTask timertask = new TimerTask(){
        	public void run(){
        		manejador.post(proceso);
        	}
        };
        timer.schedule(timertask, 0, 100);
        
        connect();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_visualizacion, menu);
		return true;
	}
	
	private static Visualizacion visualizacion;
    public static Visualizacion getInstance(){
    	return visualizacion;
    }
	
	public void lanzarNotificacion(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notManager =(NotificationManager) getSystemService(ns);
		
		int icono = R.drawable.ic_launcher;
		CharSequence textoEstado = "Movimiento detectado";
		long hora = System.currentTimeMillis();
		 
		Notification notif = new Notification(icono, textoEstado, hora);
		
		
		Context contexto = getApplicationContext();
		CharSequence titulo = "Mensaje de Alerta";
		CharSequence descripcion = "Se ha detectado movimiento en la camara de seguridad";
		 
		Intent notIntent = new Intent(contexto,Visualizacion.class);
		
		PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, notIntent, 0);
		 
		notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
		
		
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		 
		
		notif.defaults |= Notification.DEFAULT_SOUND;
		notif.defaults |= Notification.DEFAULT_VIBRATE;
		//notif.defaults |= Notification.DEFAULT_LIGHTS;
		
		
		notManager.notify(1, notif);
	}
	
	public void cerrarSocketImagen(){
		try {
			recibeImagen=false;
			if(socket.isConnected())
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	cerrarSocketImagen();
	            intent = new Intent(this, MainActivity.class);
	            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.ConectarKinect:
				try {
					com.send("CONECTAR");
				} catch (WebSocketException e) {
					e.printStackTrace();
				}
	        	return true;
	        case R.id.DesconectarKinect:
				try {
					com.send("DESCONECTAR");
				} catch (WebSocketException e) {
					e.printStackTrace();
				}
	        	return true;
	        case R.id.CapturarImagen:
				guardarImagen(getBaseContext(), "imagenSeguridad", bitmap);
	        	return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	private final static Runnable proceso = new Runnable(){
		public void run() {
			try{
				image.setImageBitmap(bitmap);
			}catch(Exception ex){
				Log.e("Error imagen", ex.getMessage());
			}
		}
	};
	
	private final Runnable identificacion = new Runnable(){
		public void run() {
			try{
				if(identificacionCorrecta==true){
					Toast.makeText(Visualizacion.getInstance().getBaseContext(), "Identificacion correcta",Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(Visualizacion.getInstance().getBaseContext(), "Identificacion incorrecta",Toast.LENGTH_LONG).show();
					cerrarSocketImagen();
		            Intent intent = new Intent(Visualizacion.getInstance(), MainActivity.class);
		            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(intent);
				}
			}catch(Exception ex){
				Log.e("Identificacion", ex.getMessage());
			}
		}
	};
	
	private void connect(){
    	Thread threadConexion = new Thread(){
    		public void run(){
    			int idConexion=Data_utils.getIdConexionActual();
		    	try {
		    		String ipAdress=Data_utils.getListaConexiones().get(idConexion).getIpConexion();
		    		int puerto=Data_utils.getListaConexiones().get(idConexion).getPuerto();
		    		
		            socket = new Socket(ipAdress, puerto);
		            outputStream = socket.getOutputStream();
		            inputStream = socket.getInputStream();
		            
		            String user=Data_utils.getListaConexiones().get(idConexion).getUsuario();
		            String pass=Data_utils.getListaConexiones().get(idConexion).getPass();
		            
		            outputStream.write((user+","+pass).getBytes());
		            byte[] recibido = new byte[30];
		            int bytesRec =inputStream.read(recibido);
		            String datosRecibidos=new String(recibido,0,bytesRec);
		            //Log.e("Recibido", datosRecibidos);
		            
		            if(datosRecibidos.equalsIgnoreCase("IDENTIFICACION CORRECTA")){
		            	com.initWebSocketClient();
		            	identificacionCorrecta=true;
		            	recibeImagen=true;
		            	manejador.post(identificacion);
		            
			            Bitmap bit=Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
	
			            ByteArrayOutputStream out = new ByteArrayOutputStream();
			            
			            while(recibeImagen){
			            	bit=null;
			            	bit=Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
			            	out = new ByteArrayOutputStream();
				    	    int length = 0;
				    	    int maximo=1228800;
				    	    byte[] data = new byte[maximo];
				    	    while (out.size()!=1228800) {
				    	    	length = inputStream.read(data,0,maximo);
				    	    	maximo=maximo-length;
				    	        out.write(data,0,length);
				    	    }
				            
				            bit.copyPixelsFromBuffer(ByteBuffer.wrap(out.toByteArray()));
				            
				            bitmap=bit;
			            }
		            }else{
		            	identificacionCorrecta=false;
		            	manejador.post(identificacion);
		            }
			    }
		    	catch (UnknownHostException e) {
    			  // TODO Auto-generated catch block
    			  e.printStackTrace();
    			 } catch (Exception e) {
    			  // TODO Auto-generated catch block
    			  e.printStackTrace();
    			 }
    		}
    	};
    	threadConexion.start();
    }
	
	private void guardarImagen (Context context, String nombre, Bitmap imagen){
		try{
			Images.Media.insertImage(getContentResolver(), imagen, nombre, null);
			Toast.makeText(getBaseContext(), "Imagen guardada correctamente", Toast.LENGTH_LONG).show();
		}catch(Exception ex){
			Toast.makeText(getBaseContext(), "Error al guardar la imagen", Toast.LENGTH_LONG).show();
		}
	}
}
