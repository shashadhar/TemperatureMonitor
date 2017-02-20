package com.shashadhardas.temperaturelistener;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import utils.Utils;

/**
 * Created by shashadhar on 05-02-2017.
 */

public class BaseActivity extends AppCompatActivity {


    ProgressDialog dialog;
    Handler handler = new Handler();
    String errorMessage;
    String successMessage;
    static PopupWindow noInternetConnectivityDialog;
    Runnable displayNoInternet = new Runnable() {

        @Override
        public void run() {
            displayNoConnectivityErrorDialog();

        }
    };
    public class CloseDialog implements Runnable {
        PopupWindow dialog;

        public CloseDialog(PopupWindow dialog) {
            this.dialog = dialog;
        }

        public void run() {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(TemperatureApplication.ACTION_NETWORK_CONNECTION_CHANGED)) {
                    showNetWorkError();
                }

            } catch (Exception e) {

            }
        }
    };

    public void showNetWorkError() {
        try {
            if (Utils.isNetworkAvailable(BaseActivity.this)) {
                if (noInternetConnectivityDialog != null) {
                    handler.postDelayed(new CloseDialog(noInternetConnectivityDialog), 300);
                }
                TemperatureApplication.getApplication().subscribeEvent();

            } else {
                handler.post(displayNoInternet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            IntentFilter intentFilter =new IntentFilter();
            intentFilter.addAction(TemperatureApplication.ACTION_NETWORK_CONNECTION_CHANGED);
            registerReceiver(networkChangeReceiver,intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        try {
            showNetWorkError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    void displayNoConnectivityErrorDialog() {

        try {

            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.no_internect_connectivity_layout, null);
            if (noInternetConnectivityDialog == null) {
                noInternetConnectivityDialog = new PopupWindow(layout,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                noInternetConnectivityDialog.setAnimationStyle(R.style.PopupAnimation);
                noInternetConnectivityDialog.showAtLocation(getWindow().getDecorView().getRootView(),
                        Gravity.BOTTOM, 0, 0);
                final View remove= layout.findViewById(R.id.remove_dialog);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.postDelayed(new CloseDialog(noInternetConnectivityDialog), 300);
                    }
                });

            } else {
                noInternetConnectivityDialog.dismiss();
                noInternetConnectivityDialog = null;
                noInternetConnectivityDialog = new PopupWindow(layout,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                noInternetConnectivityDialog.setAnimationStyle(R.style.PopupAnimation);
                noInternetConnectivityDialog.showAtLocation(getWindow().getDecorView().getRootView(),
                        Gravity.BOTTOM, 0, 0);
                final View remove=layout.findViewById(R.id.remove_dialog);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.postDelayed(new CloseDialog(noInternetConnectivityDialog), 300);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BaseCompatActivity", "Error displaying dialog");
        }
    }




}
