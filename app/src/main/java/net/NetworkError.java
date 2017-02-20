package net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shashadhar on 05-02-2017.
 */
public class NetworkError {

    @SerializedName("code")
    public String errorCode;

    @SerializedName("text")
    public String message;

    @SerializedName("message")
    public String nodeMessage;

}
