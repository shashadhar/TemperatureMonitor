package net.socket;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shashadhardas.temperaturelistener.AllSensors;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Sensor;
import model.SensorData;
import model.SensorDataResponse;
import utils.AppProperties;

/**
 * Created by shashadhar on 11-02-2017.
 */

public class SocketManager {
    private static SocketManager instance;
    public static String EVENT_CONNECTION = "connection";
    public static String EVENT_DATA = "data";
    public static String EVENT_SUBSCRIBE = "subscribe";
    public static String EVENT_UNSUBSCRIBE = "unsubscribe";
    public static final String EVENT_DISCONNECT = "disconnect";

    private Socket mSocket;

    private SocketManager() {
        createSocket();
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            return instance = new SocketManager();
        } else {
            return instance;
        }
    }

    private void createSocket() {
        try {
            mSocket = IO.socket(AppProperties.getInstance().getAppProperty("SERVER_URL"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                if(args!=null) {
                    Log.e("OnConnect", args.toString());
                    JSONObject data = (JSONObject) args[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                if(args!=null) {
                    Log.e("OnDisConnect", args.toString());
                    JSONObject data = (JSONObject) args[0];

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                if(args!=null) {
                    Log.e("OnConnectionError", args.toString());
                    JSONObject data = (JSONObject) args[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                if(args!=null) {
                    Log.e("OnMessage", String.valueOf(args.length));
                    if(args.length>0) {
                        JSONObject data = (JSONObject) args[0];
                        if(args[0]!=null) {
                            Log.e("data", args[0].toString());
                            SensorDataResponse sensorData=new Gson().fromJson(args[0].toString(), SensorDataResponse.class);
                            if(sensorData!=null) {
                                AllSensors.getInstance().setSensorData(sensorData);
                            }
                        }else{
                            Log.e("data", "NULL");
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    };

    public void connectEvents() {
        try {
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(EVENT_CONNECTION, onConnect);
            mSocket.on(EVENT_DATA, onNewMessage);
           /* for(String sensorName: AllSensors.getInstance().getSensors().keySet()){
                mSocket.emit(EVENT_SUBSCRIBE, sensorName);
            }*/
            mSocket.emit(EVENT_SUBSCRIBE, "temperature9");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void connectToSocket() {
        try {
            mSocket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void clearAll() {
        try {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(EVENT_CONNECTION, onConnect);
            mSocket.off(EVENT_DATA, onNewMessage);
            for(String sensorName: AllSensors.getInstance().getSensors().keySet()){
                mSocket.emit(EVENT_SUBSCRIBE, sensorName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}




