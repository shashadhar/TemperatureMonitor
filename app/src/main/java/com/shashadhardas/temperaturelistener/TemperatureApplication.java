package com.shashadhardas.temperaturelistener;

import android.app.Application;

import net.NetworkException;
import net.NetworkResponse;
import net.VolleyCommunicator;
import net.socket.SocketManager;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by shashadhar on 11-02-2017.
 */

public class TemperatureApplication extends Application {
   public  static String ACTION_SENSOR_INITIALISED="action_sensor_initialised";
    public static final String ACTION_NETWORK_CONNECTION_CHANGED = "com.ACTION_NETWORK_CONNECTION_CHANGED";
   static TemperatureApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        SocketManager.getInstance();
        AllSensors.getInstance();
    }

  public static  TemperatureApplication getApplication(){
      return  instance;
  }
  public  void subscribeEvent(){
      try {
          SocketManager.getInstance().connectEvents();
          SocketManager.getInstance().connectToSocket();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

}
