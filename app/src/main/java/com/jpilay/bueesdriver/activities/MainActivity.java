package com.jpilay.bueesdriver.activities;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jpilay.bueesdriver.R;
import com.jpilay.bueesdriver.network.Network;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

@SuppressLint("ServiceCast")
public class MainActivity extends AppCompatActivity {
	
	public float prevLat=0,prevLong=0;
	public boolean flagTracking = false;
	public IntentFilter lftIntentFilter = null;
	public Spinner sp_chofer;
	public Spinner sp_plate, sp_route;
	public Button btn_acerca,btn_start,btn_stop,btn_exit;
	
	//LocationClient mLocationClient;

	@SuppressLint("ServiceCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_activity);

		//AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		
		//Get widgets from layouts.		
		sp_plate = (Spinner) findViewById(R.id.sp_placas);
		sp_route = (Spinner) findViewById(R.id.sp_rutas);
		btn_acerca = (Button) findViewById(R.id.btn_acerca);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		
		//Event for button about.
		btn_acerca.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

                AlertDialog alertDialog1 = new AlertDialog.Builder(
                        MainActivity.this).create();

                // Setting Dialog Title
                alertDialog1.setTitle("Acerca De");

                // Setting Dialog Message
                alertDialog1
                        .setMessage("Esta aplicacion es un demo de Joffre Pilay.\n\n\nTodos los derechos reservados");

                // Setting Icon to Dialog
                alertDialog1.setIcon(R.drawable.logoueesweb);

                // Showing Alert Message
                alertDialog1.show();
			}

		});
		
		// Event for button start.
		btn_start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				
				if (Network.checkInternetConnection(getApplicationContext())) {
					if (Network.isGpsEnabled(getApplicationContext())) {

						flagTracking = true;
						
						//Register a receiver in manifest.

						lftIntentFilter = new IntentFilter(
								LocationLibraryConstants
										.getLocationChangedPeriodicBroadcastAction());
						registerReceiver(lftBroadcastReceiver, lftIntentFilter);
						
						//Starting Tracking.
						LocationLibrary
								.startAlarmAndListener(getApplicationContext());
						LocationLibrary
								.forceLocationUpdate(getApplicationContext());
						showDialog("Conectado al sistema BUEES.", true, true,
								false, "Estamos transmitiendo su posición.");

					} else
						Toast.makeText(getApplicationContext(),
								"No tiene el gps encendido.", Toast.LENGTH_LONG)
								.show();
				} else
					Toast.makeText(getApplicationContext(),
							"No tiene conexión a internet.", Toast.LENGTH_LONG)
							.show();							

			}

		});
		
		// Event for button stop.
		btn_stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				
				// Stop tracking and desactive broadcast receiver.
				flagTracking = false;
				
				LocationLibrary.stopAlarmAndListener(getApplicationContext());
				unregisterReceiver(lftBroadcastReceiver);
				lftIntentFilter = null;
				showDialog("Desconectando del sistema BUEES.", false, false,
						true, "Se ha desconectado correctamente del sistema.");

			}

		});

		// Event for button exit.
		btn_exit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				showDialogExit();
			}

		});		
		
		/*
		//Executing getInformation
		new getInformation().execute();
		*/

		//Button enable when turnOn tracking.
		if (flagTracking){
			btn_stop.setEnabled(true);
			btn_start.setEnabled(false);
		}else{
			btn_stop.setEnabled(false);
			btn_start.setEnabled(true);
			
		}
		
		
	}

	
	public void showDialog(String messageDialog, final boolean flagTracking,
			final boolean btn_start, final boolean btn_stop, final String messageToast) {

		final ProgressDialog dialog;
		Handler startTracking = new Handler();

		// Creating progress dialog.
		dialog = new ProgressDialog(MainActivity.this);
		dialog.setMessage(messageDialog);
		dialog.setIndeterminate(false);
		dialog.setCancelable(false);
		dialog.show();

		startTracking.postDelayed(new Runnable() {
			public void run() {

				MainActivity.this.flagTracking = flagTracking;
				MainActivity.this.btn_stop.setEnabled(btn_start);
				MainActivity.this.btn_start.setEnabled(btn_stop);
				dialog.dismiss();

				Toast.makeText(getApplicationContext(), messageToast,
						Toast.LENGTH_SHORT).show();

			}
		}, 2000);

	}
	
	public void showDialogExit(){

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				MainActivity.this);
		dialog.setTitle("--- Buees Driver ---");
		dialog.setMessage("Se cerraran todas las conexiones.\n¿Estás seguro de salir?");
		
		dialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						
						// Stop tracking and desactive broadcast receiver.
						if(flagTracking)
							LocationLibrary.stopAlarmAndListener(getApplicationContext());
							
						if (lftIntentFilter != null)
							unregisterReceiver(lftBroadcastReceiver);
							
						MainActivity.this.finish();
					}
				});
		
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		dialog.show();

	} 
	
	@Override
	public void onResume() {
		super.onResume();

		// Cancel any notification we may have received from
		// TestBroadcastReceiver
		//((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1234);
		

	}
	
	@Override
	public void finish() {
		Log.i("BueesDriver","call finish()");
		super.finish();
	}
	
	@Override
	public void onDestroy() {
		Log.i("BueesDriver","call onDestroy()");
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Log.i("BueesDriver","Holi backPressed()");
		moveTaskToBack(true);
		
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void verifyingPosition(final LocationInfo locationInfo) {

		if (locationInfo.anyLocationDataReceived()) {

			float lastLat = locationInfo.lastLat, lastLong = locationInfo.lastLong; 
			
			if (lastLat != prevLat && lastLong != prevLong){

				prevLat = lastLat;
				prevLong = lastLong;
				
				Log.i("BueesDriver","posicion recibida");

				//new sendInformation().execute(String.valueOf(lastLat),
				//		String.valueOf(lastLong));
				
			}else{
				Log.i("BueesDriver", "posicion repetida");
				LocationLibrary.forceLocationUpdate(getApplicationContext());
			}
		} else {

			Log.i("BueesDriver", "posicion no recibida");

		}

	}

	private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {

			// Extract the location info in the broadcast.
			final LocationInfo locationInfo = (LocationInfo) intent
					.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);

			// Verifying position  
			verifyingPosition(locationInfo);

		}


	};

	/*
	private class sendInformation extends AsyncTask<String, String, String> {

		String result = "error";
		
		@Override
		protected void onPreExecute() {
			/*
			if(result.equals("error")){
				Toast.makeText(getApplicationContext(),
						"Error, Porfavor cierre y vuelva abrir la aplicación.", Toast.LENGTH_LONG).show();
				
			}
			if(result.equals("internet")){
				
				Toast.makeText(getApplicationContext(),
						"No tiene conexión a internet.", Toast.LENGTH_LONG)
						.show();
				
			}
		}

		@Override
		protected String doInBackground(String... args) {
			
			if (NetworkFunction.isNetworkEnabled(MainActivity.this
					.getApplicationContext())) {
				
				// Urls
				String url_tracking = "http://104.131.65.227:9000/api/CaTrack/";

				// JSONParser
				JSONParser jp = new JSONParser();

				// JSONObject
				JSONObject jo = null;

				try {

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("ruta", ((Route)sp_route
							.getSelectedItem()).getId()));
					params.add(new BasicNameValuePair("placa", ((Plate)sp_plate
							.getSelectedItem()).getId()));
					params.add(new BasicNameValuePair("latitud", args[0]));
					params.add(new BasicNameValuePair("longitud", args[1]));

					// Send data tracking.
					jo = jp.postJSONFromUrl(url_tracking, params);

					if (jo != null) {
						Log.e("BueesDriver", "Correct to get response api ");
						result = "ok"; 
					}else
						Log.e("BueesDriver", "Error to get response api ");
					

				} catch (Exception e) {
					Log.e("BueesDriver", "Error to post in api " + e.toString());

				}
			}else 
				result = "internet";
				
			return result;

		}

	}
	
	private class getInformation extends AsyncTask<String, String, Boolean> {

		private ProgressDialog pDialog;
		private JSONObject obj_plate;
		private JSONObject obj_route;
		private JSONArray arr_plate;
		private JSONArray arr_route;
		private ArrayAdapter<Plate> adapter_plate;
		private ArrayAdapter<Route> adapter_route;
		private Boolean result = false;

		@Override
		protected void onPreExecute() {

			// Creating Arrays Adapters for information of bus.
			adapter_plate = new ArrayAdapter<Plate>(getApplicationContext(),
					R.layout.list_item);
			adapter_route = new ArrayAdapter<Route>(getApplicationContext(),
					R.layout.list_item);

			// Creating Dialog Progress.
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setTitle("Cargando Información");
			pDialog.setMessage("Espere Porfavor...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		
		@Override
		protected void onPostExecute(Boolean success) {

			pDialog.dismiss();
			if (success) {
				sp_plate.setAdapter(adapter_plate);
				sp_route.setAdapter(adapter_route);

			} else
				Toast.makeText(
						getApplicationContext(),
						"No se encuentra Placas o Rutas registradas!\nPorfavor, cierre y vuelva a abrir la aplicacion.",
						Toast.LENGTH_LONG).show();

		}


		@Override
		protected Boolean doInBackground(String... args) {
			
			// Urls
			String url_plate = "http://104.131.65.227:9000/api/PlacaBus/?format=json";
			String url_route = "http://104.131.65.227:9000/api/Rutas/?format=json";
			
			//JSONParser
			JSONParser jp = new JSONParser();
			
			try {

				// Get data plates and routes.
				arr_plate = jp.getJSONArrayFromUrl(url_plate,"results");
				arr_route = jp.getJSONArrayFromUrl(url_route,"results");

				
				if (arr_plate.length() != 0 && arr_route.length() != 0) {

					for (int i = 0; i < arr_plate.length(); i++) {

						try {
							
							// Get object plate.
							obj_plate = arr_plate.getJSONObject(i);

							// Get id and plate.
							String id = obj_plate.getString("id");
							String plate = obj_plate.getString("placa_bus");

							adapter_plate.add(new Plate(id, plate));

							Log.i("BueesDriver", "Object plate: " + plate);

						} catch (JSONException e) {

							Log.e("BueesDriver", "Error to get json_plate: "
									+ e.toString());

						}

					}

					for (int i = 0; i < arr_route.length(); i++) {

						try {

							//Get object route.
							obj_route = arr_route.getJSONObject(i);

							//Get id and route.
							String id = obj_route.getString("id");
							String route = obj_route.getString("ruta");

							adapter_route.add(new Route(id,route));

							Log.i("BueesDriver", "Object route: " + route);

						} catch (JSONException e) {

							Log.e("BueesDriver", "Error to get json_route: "
									+ e.toString());

						}

					}
					
					result = true;

				}				

			} catch (Exception e) {
				Log.e("BueesDriver", "Error to get json_arrays: "
						+ e.toString());

			}
			
			return result;

		}

	}*/

}