package com.landon.andorid_js;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnNext;
    private WebView webView;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        btnNext = findViewById(R.id.btn_next);
        webView = findViewById(R.id.webView);

        initFWebView();


        /**
         * 写在前
         *
         *     setToken方法，是JS首先发起一个动作，把token值，传递到Android端
         *
         *     getToken方法，是JS首先发起一个动作，通过此方法的return值，来获取到Token
         *
         *     btnNext的点击事件，是Android端首先发起一个动作，通过注入javascript:btnNext()的方法，与JS交互，并
         *     通过JS里面的方法f_android.showBtnNext() 调用Android端的方法，同传递回数据，达到数据回调。
         *
         *
         *
         */
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void initFWebView() {

        final WebSettings websettings = webView.getSettings();
        websettings.setDomStorageEnabled(true);
        websettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = mContext.getApplicationContext().getCacheDir().getAbsolutePath();
        websettings.setAppCachePath(appCachePath);
        websettings.setAllowFileAccess(true);
        //上面的这几行，使用于开启localStorage的，webView是默认关闭的。（主要是JS读取本地数据要用的，类似与Android的SD卡权限）

        websettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new FJsAbout(), "f_android");

        webView.loadUrl("file:///android_asset/test.html");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Android-btn的msg";
                webView.loadUrl("javascript:btnNext('" + msg + "')");
            }
        });
    }

    private class FJsAbout {
        @JavascriptInterface
        public void setToken(String token) {
            Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getToken() {
            return "这个数值是从Android拿到的Token";
        }

        @JavascriptInterface
        public void showBtnNext(String btnNext) {
            Toast.makeText(mContext, btnNext, Toast.LENGTH_SHORT).show();
        }
    }
}
