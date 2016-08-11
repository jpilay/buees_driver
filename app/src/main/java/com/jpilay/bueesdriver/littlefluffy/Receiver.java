package com.jpilay.bueesdriver.littlefluffy;

import com.jpilay.bueesdriver.R;
import com.jpilay.bueesdriver.activities.MainActivity;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("BueesDriver","Receiver onReceive()");

		final LocationInfo locationInfo = (LocationInfo) intent
				.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);

		/*
		// Construct the notification.
		Notification notification = new Notification(R.drawable.logoueesweb,
				"Ubicacion actualizada: " + 
				locationInfo.getTimestampAgeInSeconds() +
				" Segundos despues: ", System.currentTimeMillis());

		// Intent notification.
		Intent contentIntent = new Intent(context, MainActivity.class);
		
		PendingIntent contentPendingIntent = PendingIntent.getActivity(context,
				0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(
						context,
						"Actualización de ubicación de bus recibido",
						"Timestamped " + 
						LocationInfo.formatTimeAndDay(
								locationInfo.lastLocationUpdateTimestamp,
								true),
						contentPendingIntent);

		// Trigger the notification.
		((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE))
				.notify(
						1234,
						notification);*/

	}
}