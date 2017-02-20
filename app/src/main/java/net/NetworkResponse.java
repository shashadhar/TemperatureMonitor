package net;

/**
 * Created by shashadhar on 05-02-2017.
 */
public interface NetworkResponse {

    interface Listener{
        void onResponse(Object result);
    }
    interface ErrorListener{
        void onError(NetworkException error);
    }
}
