package com.scholat.law.ontrack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Note extends Activity implements View.OnClickListener{

    private Button textbtn, imgbtn, videobtn;
    private ListView lv;
    private Intent i;
    private MyAdapter adapter;
    private ProjectDB projectDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
    }

    public void initView() {
        lv = findViewById(R.id.list);
        textbtn = findViewById(R.id.text);
        imgbtn = findViewById(R.id.img);
        videobtn = findViewById(R.id.video);
        textbtn.setOnClickListener(this);
        imgbtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        projectDB = new ProjectDB(this);
        dbReader = projectDB.getReadableDatabase();
    }

    @Override
    public void onClick(View v) {
        i = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.text:
                i.putExtra("flag", "1");
                startActivity(i);
                break;

            case R.id.img:
                i.putExtra("flag", "2");
                startActivity(i);
                break;

            case R.id.video:
                i.putExtra("flag", "3");
                startActivity(i);
                break;
        }
    }

    public void selectDB() {
        cursor = dbReader.query(ProjectDB.TABLE_NAME, null, null, null, null,
                null, null);
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
