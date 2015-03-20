package worldbank.org.gridsensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.GoogleApiClient.*;
//import com.google.android.gms.location.LocationServices;

//import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.*;
import com.google.android.gms.location.LocationClient;


public class MainActivity extends ActionBarActivity {
//    TextView txtView;
//    //GooglePlayServicesClient googleApiClient;
//    Location lastLocation;
//    //boolean locationChanged=false;
//    private int prevPlug = 0;
//    private LocationClient locationClient;
//    private boolean batteryInfoReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((GridSensorApplication) this.getApplication()).txtView = (TextView) findViewById(R.id.display_text);
////        googleApiClient = new GooglePlayServicesClient.Builder(this)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
////                .addApi(LocationServices.API)
////                .build();
//        locationClient = new LocationClient(this, this, this);
//        locationClient.connect();

    }

    @Override
    protected void onStart() {
        super.onStart();
        ((GridSensorApplication) super.getApplication()).updateDisplayText();
//        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        this.unregisterReceiver(this.batteryInfoReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void updateStatus(Intent intent) {
//        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                status == BatteryManager.BATTERY_STATUS_FULL;
//
//
//        int currPlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
//        Log.d("Main", "Charging plug status: " + Integer.toString(currPlug) + " was " + Integer.toString(prevPlug));
//        if (prevPlug != currPlug) {
//            String statusText;
//
//            if (currPlug == BatteryManager.BATTERY_PLUGGED_USB) {
//                statusText = "Charging (USB)";
//            } else if (currPlug == BatteryManager.BATTERY_PLUGGED_AC) {
//                statusText = "Charging (AC)";
//            } else {
//                statusText = "Not charging";
//            }
//
//            txtView.setText(statusText + " [" + Integer.toString(currPlug) + "]");
//
//            String latlon;
//            if (lastLocation != null) {
//                latlon = Double.toString(lastLocation.getLatitude()) + "," + Double.toString(lastLocation.getLongitude());
//            } else {
//                latlon = ",";
//            }
//
//            String smsContent = statusText + "," + latlon;
//            String smsDest = "+14152372271";
//            Log.d("Main", "Sent SMS to "+smsDest+": '"+smsContent+"'");
//
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(smsDest, null, smsContent + "," + latlon, null, null);
//
//            prevPlug = currPlug;
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle connectionHint) {
//        Log.d("Main", "Google connection connected");
////        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        lastLocation = locationClient.getLastLocation();
//        if (!batteryInfoReceiverRegistered) {
//            this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//            batteryInfoReceiverRegistered = true;
//        }
//    }
//
//    @Override
//    public void onDisconnected() {
//        Log.d("Main", "Google connection disconnected");
//    }
//
////    @Override
////    public void onConnectionSuspended(int i) {
////        Log.d("Main", "Google connection suspended");
////    }
//
//    /**
//     * Created by andrew on 19/03/15.
//     */
//    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            updateStatus(intent);
//        }
//    };
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.d("Main", "Google connection failed");
//    }
}


