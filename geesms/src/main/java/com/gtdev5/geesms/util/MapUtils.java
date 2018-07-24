package com.gtdev5.geesms.util;

import android.content.Context;
import android.provider.SyncStateContract;

import com.gtdev5.geesms.contants.Contants;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

/**
 * Created by Zeoy
 * PackageName ModelTest
 * 2018年7月24日15:18:25
 *      数据对接管理类
 */

public class MapUtils {

    private  Context mContext;
    private static MapUtils instance;
    private MapUtils(Context context){
        this.mContext = context;
    }
    public static MapUtils getInstance(Context context){
        if (instance == null){
            synchronized (MapUtils.class){
                if (instance == null){
                    instance = new MapUtils(context);
                }
            }
        }
        return instance;
    }



    /**
     *      通用Map
     *      (无参的方法通用调取)
     * @return
     */
    public  Map<String,String> getCurrencyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("appid",SystemUtils.getChannelInfo(mContext, Contants.APPID_CHANNEL));
        map.put("sign",null);
       // map.put("device",CPResourceUtils.getDevice());
        return map;
    }

    public  Map<String,String> getSendMap(String tel,String tplcode,String tplparms,String signstr){
        Map<String,String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("act","send");
        map.put("tel",tel);
        map.put("tplcode",tplcode);
        map.put("tplparam",tplparms);
        map.put("extno","");
        map.put("signstr",signstr);
        return map;
    }

    public Map<String,String> getQuerryMap(){
        Map<String,String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("act","balance");
        return map;
    }

}
