package com.scholat.law.ontrack;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);//加载视图的权限
        layout = (LinearLayout) inflater.inflate(R.layout.cell, null);
        TextView contenttv = layout.findViewById(R.id.list_content);
        TextView timetv = layout.findViewById(R.id.list_time);
        ImageView imgiv = layout.findViewById(R.id.list_img);
        ImageView videoiv = layout.findViewById(R.id.list_video);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("path"));
        String urlvideo = cursor.getString(cursor.getColumnIndex("video"));
        contenttv.setText(content);
        timetv.setText(time);
//        videoiv.setImageBitmap(getVideoThumbnail(urlvideo, 200, 200,
//                MediaStore.Images.Thumbnails.MICRO_KIND));
//        imgiv.setImageBitmap(getImageThumbnail(url, 200, 200));
        return layout;
    }
}
