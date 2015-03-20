package worldbank.org.gridsensor;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/**
 * Created by andrew on 20/03/15.
 */
public class GridSensorApplication extends Application implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{
    public TextView txtView;
    public String displayText;
    //GooglePlayServicesClient googleApiClient;
    Location lastLocation;
    //boolean locationChanged=false;
    private int prevPlug = 0;
    private LocationClient locationClient;
    private boolean batteryInfoReceiverRegistered = false;

    @Override
    public void onCreate() {
        Log.d("Main", "onCreate");
        super.onCreate();
        //        googleApiClient = new GooglePlayServicesClient.Builder(this)
        //                .addConnectionCallbacks(this)
        //                .addOnConnectionFailedListener(this)
        //                .addApi(LocationServices.API)
        //                .build();
        locationClient = new LocationClient(this, this, this);
        locationClient.connect();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.unregisterReceiver(this.batteryInfoReceiver);
    }

    public void updateDisplayText() {
        txtView.setText(displayText);
    }


    private void updateStatus(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;


        int currPlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d("Main", "Charging plug status: " + Integer.toString(currPlug) + " was " + Integer.toString(prevPlug));
        if (prevPlug != currPlug) {
            String statusText;

            if (currPlug == BatteryManager.BATTERY_PLUGGED_USB) {
                statusText = "Charging (USB)";
            } else if (currPlug == BatteryManager.BATTERY_PLUGGED_AC) {
                statusText = "Charging (AC)";
            } else {
                statusText = "Not charging";
            }

            displayText = statusText + " [" + Integer.toString(currPlug) + "]";
            updateDisplayText();

            String latlon;
            if (lastLocation != null) {
                latlon = Double.toString(lastLocation.getLatitude()) + "," + Double.toString(lastLocation.getLongitude());
            } else {
                latlon = ",";
            }

            String smsContent = statusText + "," + latlon;
            String smsDest = "+14152372271";
            Log.d("Main", "Sent SMS to "+smsDest+": '"+smsContent+"'");

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(smsDest, null, smsContent, null, null);

            prevPlug = currPlug;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("Main", "Google connection connected");
        //        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        lastLocation = locationClient.getLastLocation();
        if (!batteryInfoReceiverRegistered) {
            this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryInfoReceiverRegistered = true;
        }
    }

    @Override
    public void onDisconnected() {
        Log.d("Main", "Google connection disconnected");
    }

    //    @Override
    //    public void onConnectionSuspended(int i) {
    //        Log.d("Main", "Google connection suspended");
    //    }

    /**
     * Created by andrew on 19/03/15.
     */
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStatus(intent);
        }
    };

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Main", "Google connection failed");
    }
}
