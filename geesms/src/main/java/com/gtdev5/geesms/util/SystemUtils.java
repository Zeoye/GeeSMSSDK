package com.gtdev5.geesms.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.gtdev5.geesms.contants.Contants;

import java.util.Locale;

/**
 * Created by cheng
 * PackageName APP_Lock
 * 2018/1/22 10:06
 *      系统工具类
 */

public class SystemUtils {



    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机屏幕宽度
     * @param context
     * @return
     */
    public static int getWith(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取手机屏幕高度
     * @param context
     * @return
     */
    public static int getHeight(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取渠道信息
     * @param context
     * @return
     */
    public static String getChannelInfo(Context context){

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (applicationInfo==null){
                return Contants.CHANNEL_DEFAULT;
            }
            Bundle bundle = applicationInfo.metaData;
            if (bundle == null){
                return Contants.CHANNEL_DEFAULT;
            }
            String s = bundle.getString(Contants.CHANNEL);
            if (Utils.isEmpty(s)){
                return Contants.CHANNEL_DEFAULT;
            }else {
                return s;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Contants.CHANNEL_DEFAULT;
        }
    }

    /**
     * 获取渠道信息
     * @param context
     * @return
     */
    public static String getChannelInfo(Context context,String channel){

        Bundle metaData = null;
        String metaValue = null;
        if (context == null || channel == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(channel);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SMS","请先在注册文件中添加metadata："+Contants.APPID_CHANNEL+"和"+Contants.APPKEY_CHANNEL);
        }
        return metaValue;// xxx
    }

//    /**
//     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
//     *
//     * @return 手机IMEI
//     */
//    public static String getIMEI(Context ctx) {
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//        if (tm != null) {
//            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return TODO;
//            }
//            return tm.getDeviceId();
//        }
//        return null;
//    }

}
