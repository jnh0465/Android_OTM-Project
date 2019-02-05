package com.nuntteuniachim.otm.NavigationDrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nuntteuniachim.otm.R;

public class SendActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final EditText edittext=(EditText)findViewById(R.id.editText);
        final TextView textView=(TextView)findViewById(R.id.textview);

        Button button=(Button)findViewById(R.id.f2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext.requestFocus();
                textView.setText(edittext.getText());
            }
        });
    }
}
