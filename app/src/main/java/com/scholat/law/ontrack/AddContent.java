package com.scholat.law.ontrack;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends Activity implements View.OnClickListener{

    private String val;
    private Button savebtn, deletebtn;
    private EditText ettext;
    private ImageView c_img;
    private VideoView v_video;

    private ProjectDB notesDB;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val = getIntent().getStringExtra("flag");
        savebtn = findViewById(R.id.save);
        deletebtn = findViewById(R.id.delete);
        ettext = findViewById(R.id.ettext);
        c_img = findViewById(R.id.c_img);
        v_video = findViewById(R.id.c_video);

        savebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);
        notesDB = new ProjectDB(this);
        dbWriter = notesDB.getWritableDatabase();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
                finish();
                break;

            case R.id.delete:
                finish();
                break;
        }
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(ProjectDB.CONTENT, ettext.getText().toString());
        cv.put(ProjectDB.TIME, getTime());
//        cv.put(ProjectDB.PATH, phoneFile + "");
//        cv.put(ProjectDB.VIDEO, videoFile + "");
//        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
}
