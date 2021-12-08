package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//enum Action: String {
//        case stopAll = "stopAll"
//        case forwardSlow = "forwardSlow"
//        case forwardFast = "forwardFast"
//        case backwardSlow = "backwardSlow"
//        case backwardFast = "backwardFast"
//        case turnLeft = "turnLeft"
//        case turnRight = "turnRight"
//        case raiseNeck = "raiseNeck"
//        case lowerNeck = "lowerNeck"
//        case camUp = "camUp"
//        case camDown = "camDown"
//        case camLeft = "camLeft"
//        case camRight = "camRight"
//        }
enum PopoActions {
    STOP_ALL("stopAll"),
    FORWARD_SLOW("forwardSlow"),
    FORWARD_FAST("forwardFast"),
    BACKWARD_SLOW("backwardSlow"),
    BACKWARD_FAST("backwardFast"),
    TURN_LEFT("turnLeft"),
    TURN_RIGHT("turnRight"),
    RAISE_NECK("raiseNeck"),
    LOWER_NECK("lowerNeck"),
    CAM_UP("camUp"),
    CAM_DOWN("camDown"),
    CAM_LEFT("camLeft"),
    CAM_RIGHT("camRight");
    private final String text;
    PopoActions(final String text) {
        this.text = text;
    }

    static PopoActions convertFromString(String str) {
        switch (str) {
            case "stopAll":
                return STOP_ALL;
            case "raiseNeck":
                return RAISE_NECK;
        }
        return STOP_ALL;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}

enum AppActions {
    WASHER_START("washerStart");
    private final String text;
    AppActions(final String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}

public class GeaApi {
    interface GeaApiResultString {
        void onSuccess(String str);
    }

    interface GeaApiResultBoolean {
        void onSuccess(Boolean b);
    }

    interface GeaApiResultPopoAction {
        void onSuccess(PopoActions json);
    }

    private RequestQueue queue = null;
    public RequestQueue getQueue() {
        if(queue == null) {
            queue = Volley.newRequestQueue(this.context);
        }
        return queue;
    }

    private Context context = null;
    GeaApi(Context context) {
        this.context = context;
    }

    public void heartBit(GeaApiResultString callback) {
        String url = "http://geakorea.codns.com:3333/popo/";
        StringRequest request = new StringRequest(Request.Method.GET, url, (str) -> {
            callback.onSuccess(str);
        } , null);
        getQueue().add(request);
    }

    public void checkConnection(GeaApiResultBoolean callback) {
        String url = "http://geakorea.codns.com:3333/popo/cloud";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, (response) -> {
            try {
                boolean b = response.getBoolean("connection");
                callback.onSuccess(b);
            } catch (JSONException e) { }
        }, null);
        getQueue().add(request);
    }

    public void connect(GeaApiResultBoolean callback) {
        String url = "http://geakorea.codns.com:3333/popo/cloud/connect";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, (response) -> {
            try {
                boolean b = response.getBoolean("success");
                callback.onSuccess(b);
            } catch (JSONException e) {

            }
        }, null);
        getQueue().add(request);
    }

    public void disconnect(GeaApiResultBoolean callback) {
        String url = "http://geakorea.codns.com:3333/popo/cloud/disconnect";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, (response) -> {
            try {
                boolean b = response.getBoolean("success");
                callback.onSuccess(b);
            } catch (JSONException e) {

            }
        }, null);
        getQueue().add(request);
    }

    public void popoAction(PopoActions action, GeaApiResultBoolean callback) {
        String jsonString = String.format("{\"kind\": \"popo#action\", \"action\": \"%s\"}",action.toString());
        String url = "http://geakorea.codns.com:3333/popo/send";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, (response) -> {
            try {
                boolean b = response.getBoolean("success");
                callback.onSuccess(b);
            } catch (JSONException e) {

            }
        }, null) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonString.getBytes(StandardCharsets.UTF_8);
            }
        };
        getQueue().add(request);
    }

    public void currentPopoAction(GeaApiResultPopoAction callback) {
        String url = "http://geakorea.codns.com:3333/popo/get/action";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, (response) -> {
            try {
                String str = response.getString("message");
                callback.onSuccess(PopoActions.convertFromString(str));
            } catch (JSONException e) {

            }
        }, null);
        getQueue().add(request);
    }

    public void appAction(AppActions action, GeaApiResultBoolean callback) {
        String jsonString = String.format("{\"kind\": \"popo#operation\", \"action\": \"%s\"}",action.toString());
        String url = "http://geakorea.codns.com:3333/popo/send";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, (response) -> {
            try {
                boolean b = response.getBoolean("success");
                callback.onSuccess(b);
            } catch (JSONException e) {

            }
        }, null) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonString.getBytes(StandardCharsets.UTF_8);
            }
        };
        getQueue().add(request);
    }

    public void currentAppAction(GeaApiResultString callback) {
        String url = "http://geakorea.codns.com:3333/popo/get/status";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, (response) -> {
            try {
                String str = response.getString("message");
                callback.onSuccess(str);
            } catch (JSONException e) {

            }
        }, null);
        getQueue().add(request);
    }
}
