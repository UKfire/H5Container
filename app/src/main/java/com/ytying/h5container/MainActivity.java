package com.ytying.h5container;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new DoWebChromeClient());
//        webView.loadUrl("file:///android_asset/index.html");
//        JSBridge.register("bridge", BridgeImpl.class);

        BaseWebviewFragment fragment = new BaseWebviewFragment("http://www.baidu.com", false);
        FragmentManager manager = this.getFragmentManager();

        manager.beginTransaction().add(fragment, "").commit();

        manager.beginTransaction().show(fragment);
    }

}
