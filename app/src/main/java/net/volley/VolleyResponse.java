package net.volley;


import net.NetworkException;

/**
 * Created by shashadhar on 05-02-2017.
 */
public interface VolleyResponse {

    interface ApiListener {
        <T> void onResponse(T response);
    }

    interface ErrorListener{
        void onError(NetworkException error);
    }

    interface AuthErrorListener{
        void onAuthError();
    }
}
