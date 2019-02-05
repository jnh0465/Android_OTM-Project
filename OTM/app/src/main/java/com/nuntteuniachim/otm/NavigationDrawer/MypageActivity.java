package com.nuntteuniachim.otm.NavigationDrawer;

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

//결제리스트페이지 웹뷰

public class MypageActivity extends AppCompatActivity {
    private WebView mainWebView;
    private static final String APP_SCHEME = "iamporttest://";
    public Context mContext;

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentcomplete);
        mContext = this.getApplicationContext();

        mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.setWebViewClient(new MypageActivity.WebViewClientClass()); //new InicisWebViewClient(PaymentcompleteActivity.this)
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
        Bundle bundle = intent.getExtras();
        String testValue = bundle.getString("UUID"); //uuid를받아
        String urlget = "https://yourpage?uuid="+testValue; //post_list로 보내서 uuid값으롱 orderlist테이블 쿼리 후 payment_list.html을 띄워 결제리스트 페이지를 확인하

        if ( intentData == null ) {
            mainWebView.loadUrl(urlget);  //웹뷰 렉스 url
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
        }else{
            super.onBackPressed();
        }
    }

}