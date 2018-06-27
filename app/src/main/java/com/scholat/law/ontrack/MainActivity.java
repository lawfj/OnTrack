package com.scholat.law.ontrack;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button bt1;
    private Button bt2;
    private ImageView iv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }

    public void initView(){
        bt1 = findViewById(R.id.button1_icon);
        bt2 = findViewById(R.id.button2_step);
        iv1 = findViewById(R.id.imageView1_map);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button1_icon:
//                intent.putExtra("flag", "1");
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.button2_step:
                intent = new Intent(this, TraceActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
