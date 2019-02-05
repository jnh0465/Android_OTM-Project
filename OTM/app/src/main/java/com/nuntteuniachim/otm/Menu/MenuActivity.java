package com.nuntteuniachim.otm.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nuntteuniachim.otm.NavigationDrawer.LexActivity;
import com.nuntteuniachim.otm.R;

// 카페클릭시 브랜드선택 페이지

public class MenuActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mListView = (ListView)findViewById(R.id.listView);
        dataSetting();
    }

    private void dataSetting(){
        MyAdapter mMyAdapter = new MyAdapter();

        mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.market1), "투썸플레이스", "아메리카노,카페라떼"); //아이템추가
        mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.market2), "스타벅스", "아메리카노,카페모카");
        mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.market3), "요거프레소", "요거트,카페라떼");
        mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.market4), "엔제리너스", "프라페,카페라떼");

        mListView.setAdapter(mMyAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                String testValue = bundle.getString("UUID");
                if(position == 0) { //0번 포지션(투썸플레이스) 버튼을 누르면 렉스액티비티로 넘어가게 함
                    intent = new Intent(MenuActivity.this, LexActivity.class); //버튼클릭시 LexActivity 호출
                    bundle.putString("UUID", testValue);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"아직 지원되지 않는 서비스입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}

