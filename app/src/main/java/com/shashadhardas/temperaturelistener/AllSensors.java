package com.shashadhardas.temperaturelistener;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.NetworkException;
import net.NetworkResponse;
import net.VolleyCommunicator;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import model.Config;
import model.Sensor;
import model.SensorData;
import model.SensorDataResponse;
import utils.DataChangedListener;

import static android.R.id.list;

/**
 * Created by shashadhar on 19-02-2017.
 */

public class AllSensors {
     static  AllSensors instance;
    HashMap<String,  Sensor> sensors=new HashMap<>();
    private  boolean isInitialised=false;

    public boolean isInitialised() {
        return isInitialised;
    }

    public void setInitialised(boolean initialised) {
        isInitialised = initialised;
    }

    public void setSensors(HashMap<String, Sensor> sensors) {
        this.sensors = sensors;
    }

    public HashMap<String, Sensor> getSensors() {
        return sensors;
    }

    public Sensor getSensorByName(String name){
        if(sensors!=null){
            return sensors.get(name);
        }
        return  null;
    }

    private AllSensors(){
        getSensorsList();
    }
    public static AllSensors getInstance(){
        if(instance==null){
          return   instance=new AllSensors();
        }else{
            return  instance;
        }
    }

public void setSensorData(SensorDataResponse data){
    String sensorName=data.getSensor();
    if(sensorName!=null && !sensorName.isEmpty()){
        Sensor sensorToUpdate=sensors.get(sensorName);
        String type=data.getType();
        switch(type){
            case "update":{
             sensorToUpdate.updateData(data);
            }
            break;
            case "delete":{
                sensorToUpdate.deleteData(data);
            }
            break;
        }
    }


}


private void getSensorsList(){
    try {
        final VolleyCommunicator communicator=new VolleyCommunicator(TemperatureApplication.getApplication().getApplicationContext());
        communicator.getSensors(new NetworkResponse.Listener() {
            @Override
            public void onResponse(Object result) {
                List<String>sensorList=(List<String>)result;
               if(sensorList!=null && sensorList.size()>0){
                   for(String s:sensorList){
                       Sensor sensor=new Sensor();
                       sensor.setName(s);
                       sensors.put(s,sensor);
                   }
                getSensorConfig();
              }




            }
        }, new NetworkResponse.ErrorListener() {
            @Override
            public void onError(NetworkException error) {
                Log.e("All sensors","Error in getting sensors");
            }
        },"App");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void getSensorConfig(){
    try {
        final VolleyCommunicator communicator=new VolleyCommunicator(TemperatureApplication.getApplication().getApplicationContext());
        communicator.getConfig(new NetworkResponse.Listener() {
            @Override
            public void onResponse(Object result) {
                JsonObject object=(JsonObject) result;
                if(object!=null){
                    for(String s:sensors.keySet()){
                       JsonElement configObject=object.get(s);
                        if(configObject!=null) {
                            Config config = new Gson().fromJson(configObject, Config.class);
                            if(config!=null){
                                sensors.get(s).setConfig(config);
                            }
                        }
                    }

                }
               TemperatureApplication.getApplication().subscribeEvent();
               isInitialised=true;
               Intent intent=new Intent(TemperatureApplication.ACTION_SENSOR_INITIALISED);
               TemperatureApplication.getApplication().getApplicationContext().sendBroadcast(intent);

            }
        }, new NetworkResponse.ErrorListener() {
            @Override
            public void onError(NetworkException error) {
                Log.e("All sensors","Error in getting sensors config");
            }
        },"tag");
    } catch (Exception e) {
        e.printStackTrace();
    }


}

public  void addLister(String sensorName, DataChangedListener listener){
    try {
        if(sensors!=null && sensors.size()>0 && sensors.get(sensorName)!=null) {
            if(listener!=null)
            sensors.get(sensorName).addListener(listener);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public  void removeListener(String sensorName, DataChangedListener listener){
        try {
            if(sensors!=null && sensors.size()>0 && sensors.get(sensorName)!=null) {
                if(listener!=null)
                    sensors.get(sensorName).removeLister(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
