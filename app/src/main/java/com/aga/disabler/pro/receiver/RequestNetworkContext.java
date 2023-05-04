package com.aga.disabler.pro.receiver;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;

public class RequestNetworkContext {
private HashMap<String, Object> params = new HashMap<>();
private HashMap<String, Object> headers = new HashMap<>();

private Context activity;

private int requestType = 0;

public RequestNetworkContext(Context activity) {
this.activity = activity;
}

public void setHeaders(HashMap<String, Object> headers) {
this.headers = headers;
}

public void setParams(HashMap<String, Object> params, int requestType) {
this.params = params;
this.requestType = requestType;
}

public HashMap<String, Object> getParams() {
return params;
}

public HashMap<String, Object> getHeaders() {
return headers;
}

public Context getActivity() {
return activity;
}

public int getRequestType() {
return requestType;
}

public void startRequestNetwork(String method, String url, String tag, RequestListener requestListener) {
RequestNetworkControllerContext.getInstance().execute(this, method, url, tag, requestListener);
}

public interface RequestListener {
public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders);
public void onErrorResponse(String tag, String message);
}
}
