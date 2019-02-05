package com.nuntteuniachim.otm.NavigationDrawer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nuntteuniachim.otm.Main.MainActivity;
import com.nuntteuniachim.otm.Payment.PaymentScheme;
import com.nuntteuniachim.otm.R;

import java.net.URISyntaxException;

//렉스웹뷰

public class LexActivity extends AppCompatActivity {
    private static final int MY_LOCATION_STORAGE = 1;  //권한값정의

    private WebView mainWebView;
    private ProgressBar progressBar; //웹뷰라 웹에서 소요되는 시간동안 프로그래스바를 띄우려고 했으나 결제창(lex웹뷰->결제웹뷰로 옮기는 와중)에서 오류가 생겨 없어짐

    private static final String APP_SCHEME = "iamporttest://";
    public Context mContext;

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermissionloction1(); //권한 체크

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lex);

        mContext = this.getApplicationContext();
        mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.setWebViewClient(new InicisWebViewClient(LexActivity.this));

        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(true);                      //웹뷰속웹뷰
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        mainWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 스크롤영역 차지
        mainWebView.setHorizontalScrollBarEnabled(false);              // 세로스크롤 제거
        mainWebView.setVerticalScrollBarEnabled(false);                // 가로세로 제거
        //mainWebView.scrollTo(0,200);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        mainWebView.setWebChromeClient(new WebChromeClient(){  //권한 설정
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        Intent intent = getIntent();
        Uri intentData = intent.getData();
        Bundle bundle = intent.getExtras();
        String TokenValue = bundle.getString("Token");  // MainActivity.java에서 intent로 넘어온 Token과 UUID값을 변수에 저장해서
        String testValue = bundle.getString("UUID");
        String urlget = "https://yourpage/post?uuid="+testValue+"&token="+TokenValue;
        //get방식으로 웹뷰로 url를 켜서 데이터를 보냄(이 데이터는 map_v1.html에서 받아서 저장 후 렉스로 흘러가 처음 고객정보를 물어볼 떄 customer_info 테이블에 저장됨

        if ( intentData == null ) {
            mainWebView.loadUrl(urlget);  //웹뷰 렉스 url
        } else {
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                mainWebView.loadUrl(redirectURL);
            }
        }
    }

    public class InicisWebViewClient extends WebViewClient {
        private Activity activity;
        public InicisWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("app://")) {                                                                          //스크립트(결제완료페이지)에서 '홈액티비티로 돌아가기'버튼을 눌러 app://로 시작하는 url값이 들어오면
                Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);                    //MainActivity로 이동
                startActivity(intent);
                return true;
            }
            else if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {  //스크립트에서 http://, https:// , javascript:로 시작하는 url값이 들어오면
                Intent intent = null;
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI처리
                    Uri uri = Uri.parse(intent.getDataString());
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;
                } catch (URISyntaxException ex) {
                    return false;
                } catch (ActivityNotFoundException e) {
                    if ( intent == null )	return false;
                    if ( handleNotFoundPaymentScheme(intent.getScheme()) )	return true;
                    String packageName = intent.getPackage();
                    if (packageName != null) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }

        protected boolean handleNotFoundPaymentScheme(String scheme) {
            //PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
            if ( PaymentScheme.ISP.equalsIgnoreCase(scheme) ) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
                return true;
            } else if ( PaymentScheme.BANKPAY.equalsIgnoreCase(scheme) ) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
                return true;
            }
            return false;
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

    private void checkPermissionloction1() { //권한 체크
        // Activity에서 실행하는경우
        if (Build.VERSION.SDK_INT>=23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                ) {
            //권한 확인이 안되었을때 PERMISSION_DENIED
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("위치 권한이 거부되었습니다.사용을 원하시면 설정에서 해당권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("음성 권한이 거부되었습니다.사용을 원하시면 설정에서 해당권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else{
                // 권한 확인이 되었을 때
                Toast.makeText(this, "위치 권한을 활성화 하였습니다.",Toast.LENGTH_SHORT);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO},
                        MY_LOCATION_STORAGE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions[], @NonNull int[] grantResults) {
        //사용자가 응답했을 때 이 매소드 호출로 재정의하기
        switch (requestCode) {
            case MY_LOCATION_STORAGE: {
                for(int i = 0; i < grantResults.length; i++) {
                    //grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0 ){
                        //권한 거부
                        Toast.makeText(this, "해당권한을 활성화 하셔야 합니다.",Toast.LENGTH_SHORT);
                        return;
                    } else {
                        Toast.makeText(this, "위치 권한을 활성화 하였습니다.",Toast.LENGTH_SHORT);
                    }
                }
                return;
            }
        }
    }
}