package com.scholat.law.ontrack;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button bt1;
    private ImageView iv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }

    public void initView(){
        bt1 = findViewById(R.id.button1_icon);
        iv1 = findViewById(R.id.imageView1_map);

        bt1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MapActivity.class);
        switch (v.getId()){
            case R.id.button1_icon:
//                intent.putExtra("flag", "1");
                startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
