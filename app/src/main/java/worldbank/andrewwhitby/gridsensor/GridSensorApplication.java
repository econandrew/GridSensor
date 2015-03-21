package worldbank.andrewwhitby.gridsensor;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 20/03/15.
 */
public class GridSensorApplication extends Application implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{
    public ImageView statusImage;

    public Drawable statusImagePowered;
    public Drawable statusImageUnpowered;
    public Drawable statusImageUSB;

    public List<String> logHistory = new ArrayList<String>();
    public ArrayAdapter<String> logHistoryAdapter;

    public enum Status {
        POWERED,
        UNPOWERED,
        USB;
    };
    private Status status;

    public String displayText;
    Location lastLocation;
    private int prevPlug = 0;
    private LocationClient locationClient;
    private boolean batteryInfoReceiverRegistered = false;

    @Override
    public void onCreate() {
        Log.d("Main", "onCreate");
        super.onCreate();
        locationClient = new LocationClient(this, this, this);
        locationClient.connect();
        loadImages();
        logHistoryAdapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.log_history_item, logHistory);
    }

    private void loadImages() {
        SVG svgPowered = SVGParser.getSVGFromResource(getResources(), R.raw.status_powered, 0xFF000000, 0xFFFFBF2A);
        this.statusImagePowered = svgPowered.createPictureDrawable();

        SVG svgUnpowered = SVGParser.getSVGFromResource(getResources(), R.raw.status_unpowered, 0xFF000000, 0xFFFF0000);
        this.statusImageUnpowered = svgUnpowered.createPictureDrawable();

        SVG svgUSB = SVGParser.getSVGFromResource(getResources(), R.raw.status_usb, 0xFF000000, 0xFF828282);
        this.statusImageUSB = svgUSB.createPictureDrawable();

        Log.d("Main",this.statusImageUSB.toString());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.unregisterReceiver(this.batteryInfoReceiver);
    }

    public void updateDisplay() {
        if (this.status == Status.USB) {
            statusImage.setImageDrawable(this.statusImageUSB);
        } else if (this.status == Status.POWERED) {
            statusImage.setImageDrawable(this.statusImagePowered);
        } else {
            statusImage.setImageDrawable(this.statusImageUnpowered);
        }
    }

    private void logStatusChange() {
        String statusText;
        if (this.status == Status.USB) {
            statusText = "Charging (USB)";
        } else if (this.status == Status.POWERED) {
            statusText = "Charging (AC)";
        } else {
            statusText = "Not charging";
        }

        String latlon;
        if (lastLocation != null) {
            latlon = Double.toString(lastLocation.getLatitude()) + "," + Double.toString(lastLocation.getLongitude());
        } else {
            latlon = ",";
        }

        String smsContent = statusText + "," + latlon;
        String smsDest = "+14152372271";
        String logMessage = "Sent SMS to "+smsDest+": '"+smsContent+"'";
        Log.d("Main", logMessage);
        this.logHistoryAdapter.add(logMessage);

        SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(smsDest, null, smsContent, null, null);
    }

    private void updateStatus(Intent intent) {

        int currPlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d("Main", "Charging plug status: " + Integer.toString(currPlug) + " was " + Integer.toString(prevPlug));
        if (prevPlug != currPlug) {
            String statusText;

            if (currPlug == BatteryManager.BATTERY_PLUGGED_USB) {
                this.status = Status.USB;
            } else if (currPlug == BatteryManager.BATTERY_PLUGGED_AC) {
                this.status = Status.POWERED;
            } else {
                this.status = Status.UNPOWERED;
            }

            updateDisplay();
            logStatusChange();

            prevPlug = currPlug;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("Main", "Google connection connected");
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
