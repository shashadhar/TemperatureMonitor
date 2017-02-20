package utils;

import android.content.res.Resources;

import com.shashadhardas.temperaturelistener.R;
import com.shashadhardas.temperaturelistener.TemperatureApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by shashadhar on 05-02-2017.
 */
public class AppProperties extends Properties {

    private static final String DEFAULT_VALUE = "";

    private static AppProperties instance = null;

    public static AppProperties getInstance(){
        if(instance == null){
            instance = new AppProperties();
            instance.loadProperties();
        }
        return instance;
    }

    private AppProperties(){

    }

    private void loadProperties(){
        loadProperties(R.raw.app);
        loadProperties(R.raw.endpoint);
    }

    private void loadProperties(int resourceId){
        Resources resources = TemperatureApplication.getApplication().getApplicationContext().getResources();
        InputStream rawResource = resources.openRawResource(resourceId);
        try {
            load(rawResource);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getAppProperty(String key){
        return getProperty(key, DEFAULT_VALUE);
    }



}
