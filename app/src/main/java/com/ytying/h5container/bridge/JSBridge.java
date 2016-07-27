package com.ytying.h5container.bridge;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class JSBridge {
    private static Map<String, HashMap<String, Method>> exposedMethods = new HashMap<>();

    public static void register(String exposedName, Class<? extends IBridge> clazz) {
        Log.v("vane", "1 === " + exposedName);

        if (!exposedMethods.containsKey(exposedName)) {
            try {
                exposedMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static HashMap<String, Method> getAllMethod(Class clazz) throws Exception {
        HashMap<String, Method> mMethodsMap = new HashMap<>();
        Method[] methods = clazz.getDeclaredMethods();
        Log.v("vane","wa === " + methods.length);
        for (Method method : methods) {
            String name;
            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC) || (name = method.getName()) == null) {
                Log.v("vana","wa === " + method.getName());
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if (null != parameters && parameters.length == 3) {
                if (parameters[0] == WebView.class && parameters[1] == JSONObject.class && parameters[2] == CallBack.class) {
                    mMethodsMap.put(name, method);
                }
            }
        }
        return mMethodsMap;
    }

    public static String callJava(WebView webView, String uriString) {
        String methodName = "";
        String className = "";
        String param = "{}";
        String port = "";

        if (!TextUtils.isEmpty(uriString) && uriString.startsWith("JSBridge")) {
            Uri uri = Uri.parse(uriString);
            className = uri.getHost();
            param = uri.getQuery();
            port = uri.getPort() + "";
            String path = uri.getPath();

            Log.v("vane", "3 === " + methodName);
            Log.v("vane", "4 === " + className);
            Log.v("vane", "5 === " + param);
            Log.v("vane", "6 === " + port);
            Log.v("vane", "7 === " + path);

            if (!TextUtils.isEmpty(path)) {
                methodName = path.replace("/", "");
                Log.v("vane", "8 === " + methodName);
            }
        }

        if (exposedMethods.containsKey(className)) {
            Log.v("vane", "9 === " + className);
            HashMap<String, Method> methodHashMap = exposedMethods.get(className);
            Log.v("vane", "10 === " + methodHashMap.size());

            if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                Log.v("vane", "10 === " + methodName);
                Method method = methodHashMap.get(methodName);
                if (method != null) {
                    try {
                        Log.v("vane", "11 === " + className + methodName);
                        method.invoke(null, webView, JSON.parseObject(param), new CallBack(webView, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
