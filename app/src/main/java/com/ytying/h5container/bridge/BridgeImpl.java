package com.ytying.h5container.bridge;

import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class BridgeImpl implements IBridge {

    public static void showToast(WebView webView, JSONObject param, final CallBack callBack) {
        String message = param.getString("msg");
        Log.v("vane", "11 === " + message);
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
        if (null != callBack) {
            try {
                JSONObject object = new JSONObject();
                object.put("key", "value");
                object.put("key1", "value1");
                Log.v("vane", "12 === " + object.toString());
                callBack.apply(getJSONObject(0, "ok", object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testThread(WebView webView, JSONObject param, final CallBack callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    JSONObject object = new JSONObject();
                    object.put("key", "value");
                    callback.apply(getJSONObject(0, "ok", object));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static JSONObject getJSONObject(int code, String msg, JSONObject result) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("msg", msg);
            object.put("result", result);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
