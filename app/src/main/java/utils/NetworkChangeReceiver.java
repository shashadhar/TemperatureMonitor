package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.shashadhardas.temperaturelistener.TemperatureApplication;

/**
 * Created by shashadhar on 05-02-2017.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TemperatureApplication.getApplication().sendBroadcast(new Intent(TemperatureApplication.ACTION_NETWORK_CONNECTION_CHANGED));
    }
}
