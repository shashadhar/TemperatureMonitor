package utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by shashadhar on 05-02-2017.
 */
public class Utils {

    public  static String getDateInMMMDDYYYY(long timeInMillis){
        try {
            Calendar cal= Calendar.getInstance();
            cal.setTimeInMillis(timeInMillis);
            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            df.setCalendar(cal);
            Date dt = cal.getTime();
            String date_str = df.format(dt);
            return date_str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isNetworkAvailable(final Context context) {
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




}
