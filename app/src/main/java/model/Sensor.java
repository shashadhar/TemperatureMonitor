package model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.DataChangedListener;

/**
 * Created by shashadhar on 19-02-2017.
 */

public class Sensor {
    private String name;
    private Config config;
    private HashMap<Long,Float> recentScaleData;
    private HashMap<Long,Float> minuteScaleData;

    public final static String RECENT="recent";
    public final static String MINUTE="minute";

    private List<DataChangedListener> listeners;

    public List<DataChangedListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<DataChangedListener> listeners) {
        this.listeners = listeners;
    }

    public  void addListener(DataChangedListener listener){
        try {
            if(listeners==null){
                listeners=new ArrayList<>();
            }
            listeners.remove(listener);
            listeners.add(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void removeLister(DataChangedListener listener){
        try {
            if(listeners!=null && listeners.size()>0){
                for(DataChangedListener listener1:listeners){
                    if (listener1.equals(listener)) {
                        listeners.remove(listener);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public HashMap<Long,Float> getRecentScaleData() {
        return recentScaleData;
    }

    public void setRecentScaleData(HashMap<Long,Float> recentScaleData) {
        this.recentScaleData = recentScaleData;
    }

    public HashMap<Long,Float> getMinuteScaleData() {
        return minuteScaleData;
    }

    public void setMinuteScaleData(HashMap<Long,Float> minuteScaleData) {
        this.minuteScaleData = minuteScaleData;
    }

    public  void addToRecentScale(SensorDataResponse data){

        Log.e("Adding:","key:"+(Double.valueOf(data.getKey()).longValue()));
        if(recentScaleData ==null){
            recentScaleData =new HashMap<>();
        }
        float value=0;
        try {
            value=Float.parseFloat(data.getVal());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Double.valueOf(data.getKey()).longValue()
        recentScaleData.put((Double.valueOf(data.getKey()).longValue()),value);
        updateListener(RECENT);

    }

    private void updateListener(String type) {
        try {
            if(listeners!=null && listeners.size()>0){
                for (DataChangedListener listener:listeners) {
                    listener.dataUpdated(type);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void removeFromRecentScale(SensorDataResponse data){
        Log.e("removing:","key:"+(Double.valueOf(data.getKey()).longValue()));
        if(recentScaleData !=null){
            recentScaleData.remove((Double.valueOf(data.getKey()).longValue()));
            updateListener(RECENT);
        }
    }

    public  void addToMinuteScale(SensorDataResponse data){
        try {
            if(minuteScaleData ==null){
                minuteScaleData =new HashMap<>();
            }
            float value=0;
            try {
                value=Float.parseFloat(data.getVal());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            minuteScaleData.put((Double.valueOf(data.getKey()).longValue()),value);
            updateListener(MINUTE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
    public  void removeFromMinuteScale(SensorDataResponse data){
        try {
            if(minuteScaleData !=null){
                minuteScaleData.remove((Double.valueOf(data.getKey()).longValue()));
                updateListener(MINUTE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public  void updateData(SensorDataResponse data){
        try {
            String scaleType=data.getScale();
            if(scaleType!=null && !scaleType.isEmpty()){
                switch (scaleType){
                    case "recent":{
                    addToRecentScale(data);
                    }
                    break;
                    case "minute":{
                        addToMinuteScale(data);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void deleteData(SensorDataResponse data){
        try {
            String scaleType=data.getScale();
            if(scaleType!=null && !scaleType.isEmpty()){
                switch (scaleType){
                    case "recent":{
                        removeFromRecentScale(data);
                    }
                    break;
                    case "minute":{
                        removeFromMinuteScale(data);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
