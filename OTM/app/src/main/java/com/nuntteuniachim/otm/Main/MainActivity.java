package com.nuntteuniachim.otm.Main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.nuntteuniachim.otm.Menu.MenuActivity;
import com.nuntteuniachim.otm.NavigationDrawer.MypageActivity;
import com.nuntteuniachim.otm.R;
import com.nuntteuniachim.otm.Setting.NotificationActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener { // 네비게이션 드로어
    private static final int MY_LOCATION_STORAGE = 1;  //권한값정의

    public SharedPreferences ISFIRSTPre;                   //처음실행여부 저장할 SharedPreferences 선언
    private SharedPreferences.Editor ISFIRSTPreEdit;
    private final String TOPIC = "goodoc";                //fcm firebase 토픽 선언

    private SharedPreferences UUIDPre;                    //UUID값 저장할 SharedPreferences 선언
    private SharedPreferences.Editor UUIDPreEdit;

    private SharedPreferences TokenPre;                   //Token값 저장할 SharedPreferences 선언
    private SharedPreferences.Editor TokenPreEdit;

    ViewPager viewPager; //viewPager(광고)

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            checkPermissionloction_first(); //권한 체크

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TokenPre = getSharedPreferences("settingtoken", 0);                //token값 저장 위해 getSharedPreferences 사용
            TokenPreEdit = TokenPre.edit();                                                   //0 == SharedPreferences 읽기, 쓰기 가능

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    TokenPreEdit.putString("TokenValue", token);                              //token값ㄴ
                    TokenPreEdit.commit();                                                    //저장
                }
            });

            //viewPager
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);    //viewpager 광고넘어가는거
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {   //viewpager 밑에 버튼
                public void onPageScrollStateChanged(int arg0) { }
                public void onPageScrolled(int arg0, float arg1, int arg2) { }
                public void onPageSelected(int position) {
                    // TODO Auto-generated method stub
                    ImageView image = (ImageView) findViewById(R.id.imageView1);
                    if (position == 0){  // 첫번째 페이지
                        image.setImageDrawable(getResources().getDrawable(R.drawable.statebar1, getApplicationContext().getTheme()));
                    }else if (position == 1){   //두번째 페이지
                        image.setImageDrawable(getResources().getDrawable(R.drawable.statebar2, getApplicationContext().getTheme()));
                    }else if (position == 2){   //세번째 페이지
                        image.setImageDrawable(getResources().getDrawable(R.drawable.statebar3, getApplicationContext().getTheme()));
                    }else if (position == 3){   //네번째 페이지
                        image.setImageDrawable(getResources().getDrawable(R.drawable.statebar4, getApplicationContext().getTheme()));
                    }else{
                        image.setImageDrawable(getResources().getDrawable(R.drawable.statebar5, getApplicationContext().getTheme()));
                    }
                }
            });

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new MainActivity.MyTimerTask(),2000,6000);    //timer로 MyTimerTask(광고) 딜레이/간격 설정

            //UUID getSharedPreferences 저장부분
            ISFIRSTPre = getSharedPreferences("isFirst2", MainActivity.MODE_PRIVATE); //최초 실행 여부 확인 위해 getSharedPreferences 사용
            ISFIRSTPreEdit = ISFIRSTPre.edit();

            UUIDPre = getSharedPreferences("setting", 0);                     //UUID값 저장 위해 getSharedPreferences 사용
            UUIDPreEdit = UUIDPre.edit();                                                    //0 == SharedPreferences 읽기, 쓰기 가능

            boolean first = ISFIRSTPre.getBoolean("isFirst2", false);
            if(first==false){                                                               //최초 실행시
                ISFIRSTPreEdit.putBoolean("isFirst2",true);
                UUIDPreEdit.putString("UUIDValue", UUIDTest());                             //UUID값
                UUIDPreEdit.commit();                                                       //저장
                ISFIRSTPreEdit.commit();
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);  //알림허용(디폴트)
            }

            //액션바+플로팅메세지+네비게이션드로어 설정
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                         //app_bar_main.xml의 액션바
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);          //네비게이션 드로어 여닫기
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   // 네비게이션 드로어 클릭시 함수 선언
            navigationView.getMenu().getItem(0).setChecked(true);                    // 디폴트로 홈 클릭(홈에 회색 줄)
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));     // 첫 화면 설정(맨 처음 들어갔을 때의 화면)
            navigationView.setNavigationItemSelectedListener(this);
            //

            //이미지버튼 클릭 이벤트
            ImageButton cafe = (ImageButton) findViewById(R.id.button_cafe);  //이미지버튼(카페)클릭
            cafe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("Token", TokenPre.getString("TokenValue", "0")); //TokenPre라는 getSharedPreferences에 저장되어있는 TokenValue값을 Token라는 이름으로 넘겨줌
                    bundle.putString("UUID", UUIDPre.getString("UUIDValue", "0"));    //UUIDPre라는 getSharedPreferences에 저장되어있는 UUIDValue값을 UUID라는 이름으로 넘겨줌
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

            ImageButton hamburger = (ImageButton) findViewById(R.id.button_hamburger);  //이미지버튼(햄버거)클릭
            hamburger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Toast toast = Toast.makeText(getApplicationContext(),"아직 지원되지 않는 서비스입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            ImageButton sandwich = (ImageButton) findViewById(R.id.button_sandwich);  //이미지버튼(샌드위치)클릭
            sandwich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Toast toast = Toast.makeText(getApplicationContext(),"아직 지원되지 않는 서비스입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            //
        }

    private void checkPermissionloction_first() { //권한 체크
        if (Build.VERSION.SDK_INT>=23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else{
                Toast.makeText(this, "위치 권한을 활성화 하였습니다.",Toast.LENGTH_SHORT);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO},
                        MY_LOCATION_STORAGE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                    //액션바 설정버튼
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;
        return true ; //반드시 true 리턴
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                              //우측상단 톱니바퀴(설정)버튼 이벤트
        switch (item.getItemId()) {
            case R.id.action_settings :
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class); //버튼클릭시 NotificationActivity(설정창) 호출
                startActivity(intent);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {                                                      // drawer 뒤로가기 구현
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {  //drawer가 열린 상태면
            drawer.closeDrawer(GravityCompat.START);     //drawer를 닫아주고
        } else {                                         //아니면
            super.onBackPressed();                       //뒤로가기
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {                           // 햄버거바(네비게이션드로어)의 메뉴 '클릭시'
        int id = item.getItemId();
        Fragment fragment = null;
        String title = getString(R.string.app_name);  //타이틀 저장

        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_mypage) {
            Intent intent = new Intent(MainActivity.this, MypageActivity.class); //버튼클릭시 MypageActivity 호출

            Bundle bundle = new Bundle();
            bundle.putString("UUID", UUIDPre.getString("UUIDValue", "0"));  //UUIDPre라는 getSharedPreferences에 저장되어있는 UUIDValue값을 UUID라는 이름으로 넘겨줌
            intent.putExtras(bundle);

            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class); //버튼클릭시 PaymentActivity 호출
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Toast toast = Toast.makeText(getApplicationContext(),"아직 지원되지 않는 서비스입니다.", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (getSupportActionBar() != null) {                                            //액션바 타이틀 설정해주기
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);                                        //드로어의 메뉴 버튼 클릭시 해당 id에 맡는 Activity를 startActivity()로 실행하고 드로어를 닫아준다
        return true;
    }

    public static String UUIDTest() {                                                   //UUID 생성
        UUID one = UUID.randomUUID();
        System.out.println("UUID One: " + one.toString());
        return one.toString();
    }

    public class MyTimerTask extends TimerTask {                                        //광고 넘기는 순서 조정(광고가 줄거나 늘면 수정해야함)
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()==0){    //총 5개(0>1>2>3>4>0)
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem()==1) {
                        viewPager.setCurrentItem(2);
                    }else if(viewPager.getCurrentItem()==2) {
                        viewPager.setCurrentItem(3);
                    }else if(viewPager.getCurrentItem()==3) {
                        viewPager.setCurrentItem(4);
                    }else{
                        viewPager.setCurrentItem(0); //다시 첫 페이지로
                    }
                }
            });
        }
    }
}
