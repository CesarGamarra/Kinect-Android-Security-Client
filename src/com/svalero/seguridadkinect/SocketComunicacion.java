package com.svalero.seguridadkinect;

import java.net.URI;
import java.net.URISyntaxException;

import com.svalero.datos.Data_utils;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

import android.os.Handler;
import android.util.Log;

public class SocketComunicacion {
	WebSocket websocket;
	private final static Handler manejador = new Handler();
	static String mensaje;
	
	public void initWebSocketClient() {
		Thread threadComunicacion=new Thread(){
			public void run(){
				final String TAG="nlas";
		        Log.e(TAG, "initWebSocketClient");
		        try {
		        	int idConexion=Data_utils.getIdConexionActual();
		        	String ipAdress=Data_utils.getListaConexiones().get(idConexion).getIpConexion();
		    		int puerto=Data_utils.getListaConexiones().get(idConexion).getPuerto()+1;
		            URI url = new URI("ws://"+ipAdress+":"+puerto+"/");
		            websocket = new WebSocketConnection(url);

		            // Register Event Handlers
		            websocket.setEventHandler(new WebSocketEventHandler() {
		                    public void onOpen()
		                    {
		                            Log.e(TAG, "--open");
		                    }

		                    public void onMessage(WebSocketMessage message)
		                    {
		                            Log.e(TAG, "--received message: " + message.getText());
		                            mensaje=message.getText();
		                            manejador.post(proceso);
		                    }

		                    public void onClose()
		                    {
		                            Log.e(TAG, "--close"); 
		                    }
		            });
		            websocket.connect();
		        }
		        catch (Exception ex) {
		                ex.printStackTrace();
		        }
			}
		};
		threadComunicacion.start();
    }
	
	private final static Runnable proceso = new Runnable(){
		public void run() {
			try{
				comprobarMensaje(mensaje);
			}catch(Exception ex){
				Log.e("Error Mensaje", ex.getMessage());
			}
		}
	};
	
	private static void comprobarMensaje(String mensaje){
		if(mensaje.equalsIgnoreCase("SKELETON DETECTADO")){
			Visualizacion.getInstance().lanzarNotificacion();
		}
	}
	
	public void send(String mensaje) throws WebSocketException{
		websocket.send(mensaje);
	}
}
