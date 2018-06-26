package com.scholat.law.ontrack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

public class MyUtils {
    /*类MyUtils中自定义权限是否开启方法*/
    public static boolean selfPermissionGranted(String permission, Context context){
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Integer.valueOf(Build.VERSION.SDK) >= Build.VERSION_CODES.M){
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            }else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                    == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return  result;
    }
}
