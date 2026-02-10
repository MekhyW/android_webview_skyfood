package com.autoxing.delivery;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private WebView webView; //Web page controls
    private View launcherView; //Startup Image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        getExternalFilesDir("").getAbsolutePath(); //This line must be reserved for creating the directory.

        launcherView = findViewById(R.id.launch_view);
        launcherView.setOnClickListener(view -> MainActivity.this.loadWebView());

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient(){
            Boolean pageFinished = false;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                 Log.i(TAG,"onPageStarted");
                pageFinished = false;
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG,"onPageFinished"+view.getProgress());
                if(view.getProgress()==100&&pageFinished==false){
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(() -> launcherView.setVisibility(View.GONE));
                    }).start();
                    pageFinished = true;
                }
                super.onPageFinished(view, url);
            }
        });

        //Like injecting multiple objects into Android using JavaScript
        webView.addJavascriptInterface(MainActivity.this, "app");
        this.loadWebView();

        //Android calling JavaScript methods of WebView
        findViewById(R.id.btn).setOnClickListener(view -> webView.loadUrl("javascript:callJSHello("+Math.random()+")"));

        Log.i(TAG,"onCreate");
    }

    private void loadWebView() {
        launcherView.setVisibility(View.VISIBLE);

        //Run this project: https://github.com/AutoxingTech/AX_SDK1.0_Example,
        //after: webView.loadUrl("https://xxxxx/sdk/v1.0/example");
        //You can experience the SDK functionality immediately.
        //file:///android_asset/dist/index.html Android and JavaScript interaction demo
        webView.loadUrl("file:///android_asset/dist/index.html");
    }

    @android.webkit.JavascriptInterface
    public String actionFromJsHello(String args) {
        return args+Math.random()+"android_返回结果";
    }

    @android.webkit.JavascriptInterface
    public void actionFromJsWebRefresh() {
        Log.i(TAG,"Refresh webpage");
        runOnUiThread(MainActivity.this::loadWebView);
    }

}