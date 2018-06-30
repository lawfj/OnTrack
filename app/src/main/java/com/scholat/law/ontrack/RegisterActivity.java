package com.scholat.law.ontrack;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity{

    private TextView tv_main_title;
    private TextView tv_back;
    private Button btn_register;

    private EditText et_user_name, et_psw, et_psw_again;

    //标题布局
    private RelativeLayout rl_title_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {

        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back = findViewById(R.id.tv_back);
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);

        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_user_psw);
        et_psw_again = findViewById(R.id.et_user_psw_again);

        Drawable drawableAccount = getResources().getDrawable(R.drawable.account);
        Drawable drawablePassword = getResources().getDrawable(R.drawable.password);
        drawableAccount.setBounds(0,0,40,40);
        drawablePassword.setBounds(0,0,40,40);
        et_user_name.setCompoundDrawables(drawableAccount,null,null,null);
        et_psw.setCompoundDrawables(drawablePassword,null,null,null);
        et_psw_again.setCompoundDrawables(drawablePassword,null,null,null);


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
