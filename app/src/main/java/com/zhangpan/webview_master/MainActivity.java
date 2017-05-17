package com.zhangpan.webview_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private String url;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
    }

    private void initData() {
        this.mContext = MainActivity.this;

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/BridgeWebView/index.html");//本地模板

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    //这一步很关键,web端传过来的有可能是其它编码格式
                    url = URLDecoder.decode(url, "utf-8");

                    //通过判断拦截到的url是否含有pre，来辨别是http请求还是调用android方法的请求
                    String[] parts = url.split("[?]");
                    String code = parts[0];

                    String pre = "app://";
                    if (!url.contains(pre)) {
                        //该url是http请求，用webview加载url
                        view.loadUrl(url);
                        return true;
                    }

                    //该url是调用android方法的请求，通过解析url中的参数来执行相应方法
                    String params = JavaScriptManager.getJSParams(url, code);
                    //在这里实现各种android方法的逻辑代码
                    JavaScriptManager.invokeAndroidMethod(mContext, code, params, mWebView);
                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    view.loadUrl(url);
                    return true;
                }
            }
        });
    }

    private void initListener() {

    }
}
