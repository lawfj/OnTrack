package com.scholat.law.ontrack;

import android.app.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final int REQUEST_CODE = 1;//请求码

    private Button bt1;
    private Button bt2;
    private Button bt3;
    private ImageView iv1;

    private ProjectDB projectDB;
    private SQLiteDatabase dbWriter;

    private ImageView mResultContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView(){
        bt1 = findViewById(R.id.button1_icon);
        bt2 = findViewById(R.id.button2_step);
        bt3 = findViewById(R.id.button3_record);
        iv1 = findViewById(R.id.imageView1_map);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.button1_icon:
//                intent.putExtra("flag", "1");
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.button2_step:
                intent = new Intent(this, AddContent.class);
                startActivity(intent);
                break;
            case R.id.button3_record:
                //跳转系统相机
                intent.setAction("android.media.action.IMAGE_CAPTURE");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK && data != null) {
                //说明成功返回图片
                Bitmap result = data.getParcelableExtra("data");
                if (result != null) {
                    mResultContainer.setImageBitmap(result);
                }
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //取消或者失败
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addDB(){
        ContentValues cv = new ContentValues();
        cv.put(ProjectDB.CONTENT, "hello!");
        cv.put(ProjectDB.TIME, getTime());
        dbWriter.insert(ProjectDB.TABLE_NAME, null, cv);
    }

    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    public String createFileName() {
        String fileName = getTime() + ".jpg";
        return fileName;
    }

    public void savePhotoToSDcard(String path, String photoName, Bitmap photoBitmap){
        if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if(photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (FileNotFoundException e){
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e){
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
