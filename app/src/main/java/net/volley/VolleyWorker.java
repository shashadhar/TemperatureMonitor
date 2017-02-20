package net.volley;

import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import net.NetworkError;
import net.NetworkException;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shashadhar on 05-02-2017.
 */
public class VolleyWorker {

    public static String USER_SESSION_INVALID_CODE = "402";
    public static String REQUEST_ENTITY_TOO_LARGE = "10413";


    public static<T> void doNetworkOperation(VolleyRequest request, final VolleyResponse.ApiListener listener,
                                             final VolleyResponse.ErrorListener errorListener, final VolleyResponse.AuthErrorListener authErrorListener, Class<T> clazz) {
        try {
            Map<String, String> headers = new HashMap<>();
            if (request.contentType != null) {
                headers.put("Content-Type", request.contentType);
            }
            GsonRequest<T> gsonRequest = new GsonRequest<>(request.method, request.url, clazz, headers,
                    new Response.Listener<T>() {
                        @Override
                        public void onResponse(T response) {
                            try {
                                listener.onResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                                VolleyLog.e("Error response %s", e);
                                if (errorListener != null) {
                                    errorListener.onError(new NetworkException());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        String body="";
                        //get response body and parse with appropriate encoding
                        if(error.networkResponse.data!=null) {
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        String errorString;
                        if(body!=null){
                            errorString=body;
                        }else{
                            errorString=error.getMessage();
                        }
                        errorListener.onError(new NetworkException(errorString));
                    } catch (Exception e) {
                        errorListener.onError(new NetworkException("Exception"));
                    }
                }
            });
            VolleyLog.e("Request url %s", request.url);
            if (request.method == Request.Method.POST) {
                gsonRequest.setRequestBody(request.data);
                if (request.data != null) {
                    VolleyLog.e("Request body %s", request.data);
                }
            }
            if (request.tag != null) {
                gsonRequest.setTag(request.tag);
            }
            gsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyRequestQueue.getInstance(request.context).addToRequestQueue(gsonRequest);
            VolleyLog.e("Request time", "Request");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void doStringResponseNetworkOperation(final VolleyRequest request, final VolleyResponse.ApiListener listener,
                                                        final VolleyResponse.ErrorListener errorListener, final VolleyResponse.AuthErrorListener authErrorListener) {
        try {
            final Map<String, String> headers = new HashMap<>();
            if (request.contentType != null) {
                headers.put("Content-Type", request.contentType);
            }

            StringRequest stringRequest = new StringRequest(request.method, request.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                listener.onResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                                VolleyLog.e("Error response %s", e);
                                if (errorListener != null) {
                                    errorListener.onError(new NetworkException());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String response = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        VolleyLog.e("Error response %s", response);
                        if(response.contains("Request Entity Too Large")){
                            errorListener.onError(new NetworkException(REQUEST_ENTITY_TOO_LARGE, "Post payload exceeds permitted limit!"));
                            return;
                        }
                        NetworkError networkError = new Gson().fromJson(response, NetworkError.class);
                        String message = networkError.message != null ? networkError.message : networkError.nodeMessage;
                        NetworkException ne = new NetworkException(networkError.errorCode, message);
                        if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            if (networkError.errorCode.equals(USER_SESSION_INVALID_CODE)) {
                                errorListener.onError(ne);
                            } else {
                                authErrorListener.onAuthError();
                            }
                        } else {
                            errorListener.onError(ne);
                        }
                    } catch (Exception e) {
                        if (errorListener != null) {
                            errorListener.onError(new NetworkException());
                        }
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return request.data == null ? null : request.data.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", request.data, "utf-8");
                        return null;
                    }
                }

            };


            VolleyLog.e("Request url %s", request.url);
            if (request.method == Request.Method.POST) {
                if (request.data != null) {
                    VolleyLog.e("Request body %s", request.data);
                }
            }
            if (request.tag != null) {
                stringRequest.setTag(request.tag);
            }
            VolleyRequestQueue.getInstance(request.context).addToRequestQueue(stringRequest);
            VolleyLog.e("Request time", "Request");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static String getDeviceIdentifiers() {
        String imei = "";
        try {
            //imei = DeviceInformation.getIMEI();
        } catch (Exception e) {
            //e.printStackTrace();
            //imei = SettingsManager.getInstance().getUniqueDeviceId();
        }
        String osName = "ANDROID";
        String osVersion = Build.VERSION.RELEASE;
        return imei + "::" + "Mobile" + "::" + osName + "::" + osVersion;
    }
}
