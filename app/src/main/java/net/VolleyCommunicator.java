package net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.JsonObject;

import net.volley.VolleyRequest;
import net.volley.VolleyResponse;
import net.volley.VolleyWorker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.AppProperties;

/**
 * Created by shashadhar on 05-02-2017.
 */
public class VolleyCommunicator {

    private final Context mCtx;

    public VolleyCommunicator(Context context) {
        this.mCtx = context;
    }
    private String getEndpointUrl(String endpoint) {
        String rootUrl = AppProperties.getInstance().getAppProperty("SERVER_URL");
        return rootUrl + AppProperties.getInstance().getAppProperty(endpoint);
    }


    /*public  void getEarthQuakeList(String startTime,String endTime,double magnitude,final NetworkResponse.Listener listener, final NetworkResponse.ErrorListener errorListener, String tag) {
        try {
            String url = getEndpointUrl("query");
            url += "&" + "starttime=" + startTime;
            url += "&" + "endtime=" + endTime;
            url += "&" + "minmagnitude=" + magnitude;

            String requestBody = "";
            final VolleyRequest volleyRequest = new VolleyRequest();
            volleyRequest.url = url;
            volleyRequest.method = Request.Method.GET;
            volleyRequest.data = requestBody;
            volleyRequest.contentType = "application/json";
            volleyRequest.context = mCtx;
            volleyRequest.tag = tag;
            Log.e("Communicator","Url="+url);
            VolleyWorker.doNetworkOperation(volleyRequest,
                    new VolleyResponse.ApiListener() {
                        @Override
                        public <T> void onResponse(T response) {
                            try {

                            } catch (Exception e) {
                                listener.onResponse(null);
                            }
                        }
                    }, new VolleyResponse.ErrorListener() {
                        @Override
                        public void onError(NetworkException error) {
                            try {
                                errorListener.onError(error);
                            } catch (Exception e) {
                                NetworkException ex = new NetworkException(null, "Error in response");
                                errorListener.onError(ex);
                                e.printStackTrace();
                            }
                        }
                    },null, EarthQuake.class);
        } catch (Exception e) {
            NetworkException ex = new NetworkException(null, null);
            errorListener.onError(ex);
            e.printStackTrace();
        }
    }*/


    public  void getSensors(final NetworkResponse.Listener listener, final NetworkResponse.ErrorListener errorListener, String tag) {
        try {
            String url = getEndpointUrl("get_sensors");
            String requestBody = "";
            final VolleyRequest volleyRequest = new VolleyRequest();
            volleyRequest.url = url;
            volleyRequest.method = Request.Method.GET;
            volleyRequest.data = requestBody;
            volleyRequest.contentType = "application/json";
            volleyRequest.context = mCtx;
            volleyRequest.tag = tag;
            Log.e("Communicator","Url="+url);
            VolleyWorker.doNetworkOperation(volleyRequest,
                    new VolleyResponse.ApiListener() {
                        @Override
                        public <T> void onResponse(T response) {
                            try {
                                String[] sensors=(String[])response;
                                List<String>  sensorsList=new ArrayList<String>(Arrays.asList(sensors));
                                listener.onResponse(sensorsList);

                            } catch (Exception e) {
                                listener.onResponse(null);
                            }
                        }
                    }, new VolleyResponse.ErrorListener() {
                        @Override
                        public void onError(NetworkException error) {
                            try {
                                errorListener.onError(error);
                            } catch (Exception e) {
                                NetworkException ex = new NetworkException(null, "Error in response");
                                errorListener.onError(ex);
                                e.printStackTrace();
                            }
                        }
                    },null,String[].class);
        } catch (Exception e) {
            NetworkException ex = new NetworkException(null, null);
            errorListener.onError(ex);
            e.printStackTrace();
        }
    }
    public  void getConfig(final NetworkResponse.Listener listener, final NetworkResponse.ErrorListener errorListener, String tag) {
        try {
            String url = getEndpointUrl("get_config");
            String requestBody = "";
            final VolleyRequest volleyRequest = new VolleyRequest();
            volleyRequest.url = url;
            volleyRequest.method = Request.Method.GET;
            volleyRequest.data = requestBody;
            volleyRequest.contentType = "application/json";
            volleyRequest.context = mCtx;
            volleyRequest.tag = tag;
            Log.e("Communicator","Url="+url);
            VolleyWorker.doNetworkOperation(volleyRequest,
                    new VolleyResponse.ApiListener() {
                        @Override
                        public <T> void onResponse(T response) {
                            try {
                             JsonObject object=(JsonObject)response;
                             listener.onResponse(object);
                            } catch (Exception e) {
                                listener.onResponse(null);
                            }
                        }
                    }, new VolleyResponse.ErrorListener() {
                        @Override
                        public void onError(NetworkException error) {
                            try {
                                errorListener.onError(error);
                            } catch (Exception e) {
                                NetworkException ex = new NetworkException(null, "Error in response");
                                errorListener.onError(ex);
                                e.printStackTrace();
                            }
                        }
                    },null, JsonObject.class);
        } catch (Exception e) {
            NetworkException ex = new NetworkException(null, null);
            errorListener.onError(ex);
            e.printStackTrace();
        }
    }

}
