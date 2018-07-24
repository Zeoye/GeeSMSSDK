package com.gtdev5.geesms.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by cheng
 * PackageName ModelTest
 * 2018/1/4 13:42
 *      工具类
 */

public class Utils {
        private static MessageDigest digest;

//    public static Map<String,String> stringMap = new HashMap<>();

    /**
     *      得到年月日
     * @param type      格式
     * @return
     */
    public static String getDate(String type){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    /**
     *          判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (str  == null || str.equals("") || str.equals("null")){
            return true;
        }
        return false;
    }

    /**
     *          判断字符串不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     *          数组转字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     *          判断当前网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        boolean result = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null){
                NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
                if (infos != null){
                    for (int i = 0; i < infos.length; i++) {
                        if (infos[i].getState() == NetworkInfo.State.CONNECTED){
                            result = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *      判断当前网络状态是否是wifi
     * @param context
     * @return
     */
    public static boolean isWifi(Context context){
        boolean result = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null){
                NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
                if (infos != null){
                    for (int i = 0; i < infos.length; i++) {
                        if (infos[i].getType() == ConnectivityManager.TYPE_WIFI){
                            result = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *      判断是否是中国电信,联通,移动的正确电话号码
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone){
        if (phone == null){
            return false;
        }
        	/*
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通）
	    */
        String telRegex = "[1][35768]\\d{9}";
        return phone.matches(telRegex);
    }





}
