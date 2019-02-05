package com.nuntteuniachim.otm.Payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nuntteuniachim.otm.Main.MainActivity;
import com.nuntteuniachim.otm.R;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

//필요하지는 않음
//결제완료웹뷰

public class PaymentcompleteActivity extends AppCompatActivity {
    private WebView mainWebView;
    private static final String APP_SCHEME = "iamporttest://";
    public Context mContext;

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentcomplete);
        mContext = this.getApplicationContext();

        mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.setWebViewClient(new WebViewClientClass()); //new InicisWebViewClient(PaymentcompleteActivity.this)
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        Intent intent = getIntent();
        Uri intentData = intent.getData();

        if ( intentData == null ) {
            mainWebView.loadUrl("https://yourpage/payment_complete"); //####################url바뀌면 잘 바꿔줘야함
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            //지금은 결제후 다시 url로 옴!
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                mainWebView.loadUrl(redirectURL);
            }
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("app://")) {
                Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }
            else {
                view.loadUrl(url);
                return true;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.toString();
        if ( url.startsWith(APP_SCHEME) ) {
            String redirectURL = url.substring(APP_SCHEME.length()+3);
            mainWebView.loadUrl(redirectURL);
        }
    }
    @Override
    public void onBackPressed() {  //웹뷰 내에서 뒤로가기 기능 구현
        if (mainWebView.getOriginalUrl().equalsIgnoreCase(URL)) {
            super.onBackPressed();
        }else if(mainWebView.canGoBack()){
            mainWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }

}