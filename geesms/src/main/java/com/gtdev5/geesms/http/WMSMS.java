package com.gtdev5.geesms.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gtdev5.geesms.callback.BaseCallback;
import com.gtdev5.geesms.callback.DataCallBack;
import com.gtdev5.geesms.contants.API;
import com.gtdev5.geesms.contants.Contants;
import com.gtdev5.geesms.util.MapUtils;
import com.gtdev5.geesms.util.SystemUtils;
import com.gtdev5.geesms.util.Utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zeoy
 * PackageName ModelTest
 * 2018年7月24日15:17:50
 *          短信发送主类
 */

public class WMSMS {
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


    private static WMSMS mWMSMS;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Context context;

    public static final int GET_HTTP_TYPE = 1;//get请求
    public static final int POST_HTTP_TYPE = 2;//post请求
    public static final int UPLOAD_HTTP_TYPE = 3;//上传请求
    public static final int DOWNLOAD_HTTP_TYPE = 4;//下载请求

    private Request request = null;

    private MessageDigest alga;

    private Map<String,String> resultMap;
    private Gson gson;

    private WMSMS(Context context){
        try {
            mOkHttpClient = new OkHttpClient();
            mOkHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS);
            mHandler = new Handler(Looper.getMainLooper());
            gson = new Gson();
            alga = MessageDigest.getInstance("SHA-1");
            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WMSMS getInstance(Context context){
        if (mWMSMS == null){
            synchronized (WMSMS.class){
                if (mWMSMS == null){
                    mWMSMS = new WMSMS(context);
                }
            }
        }
        return mWMSMS;
    }

    /**
     * @param tel                    发送的电话
     * @param tplcode                短信模板码
     * @param tplparms               模板参数
     * @param signstr                签名字符串
     * @param callback
     */
    public void postSendSMS(String tel,String tplcode,String tplparms,String signstr,BaseCallback callback){
        post(API.COMMON_URL+API.SEND_SMS,MapUtils.getInstance(context).getSendMap(tel,tplcode,tplparms,signstr),callback);
    }


    /**
     * @param tel                  电话
     * @param tplcode              短信模板码
     * @param varname              参数名称
     * @param varvalue             参数值
     * @param signstr               签名字符串
     * @param callback              回调
     */
    public void postSendSMS(String tel,String tplcode,String varname,String varvalue,String signstr,BaseCallback callback){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"")
                .append(varname)
                .append("\":\"")
                .append(varvalue)
                .append("\"}");
        postSendSMS(tel,tplcode,stringBuilder.toString(),signstr,callback);
    }

    /**
     * 查询短信余量api
     * @param callback
     */
    public void postQuerrySMS(BaseCallback callback){
        post(API.COMMON_URL+API.SEND_SMS,MapUtils.getInstance(context).getQuerryMap(),callback);
    }

  /*  *//**
     *          提供对外调用的请求接口
     * @param callBack      回调接口
     * @param url           路径
     * @param type          请求类型
     * @param paramKey      请求参数
     * @param paramValue    请求值
     *//*
    public static void httpsNetWorkRequest(final DataCallBack callBack, final String url, final int type, final String[] paramKey, final Object[] paramValue){
            getInstance().inner_httpsNetWorkRequest(callBack,url,type,paramKey,paramValue);
    }*/

    /**
     *          内部处理请求的方法
     * @param callBack      回调接口
     * @param url           路径
     * @param type          请求类型
     * @param paramKey      请求参数
     * @param paramValue    请求值
     */
    private void inner_httpsNetWorkRequest(final DataCallBack callBack,final String url,final int type,final String[] paramKey,final Object[] paramValue){
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();

        Map<String,String> map = new TreeMap<String,String>();

        map.put("appid", SystemUtils.getChannelInfo(context, Contants.APPID_CHANNEL));
//        map.put("key",CPResourceUtils.getString("appkey"));
        map.put("sign",null);
       // map.put("device",CPResourceUtils.getDevice());

        if (paramKey != null){
            for (int i = 0; i < paramKey.length; i++) {
                map.put(paramKey[i],String.valueOf(paramValue[i]));
            }
            resultMap = sortMapByKey(map);
        }

        String str="";
        int num = 0;

        boolean isFirst = true;
        switch (type){
            case GET_HTTP_TYPE:
                request = new Request.Builder().url(API.COMMON_URL+url).build();
                break;
            case POST_HTTP_TYPE:
                /**
                 * 循环遍历获取key值，拼接sign字符串
                 */
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    if (entry.getValue() == null){
                        continue;
                    }
                    num++;
                    if (isFirst){
                        str += entry.getKey() + "=" + entry.getValue().trim();
                        isFirst = !isFirst;
                    }else {
                        str = str.trim();
                        str += "&" + entry.getKey() + "=" + entry.getValue().trim();
                        if (num == resultMap.size() - 1){
                            str += "&" +SystemUtils.getChannelInfo(context,Contants.APPKEY_CHANNEL);
                        }
                    }
//                    if (isFirst){
//                        str = str.trim();
//                        if (entry.getKey().equals("key")){
//                            str += entry.getKey()+"="+entry.getValue();
//                        }else {
//                            str += entry.getKey() + "=" + android.util.Base64.encodeToString(entry.getValue().getBytes(), android.util.Base64.DEFAULT).trim();
//                        }
//                        isFirst  = !isFirst;
//                    }else {
//                        str = str.trim();
//                        if (entry.getKey().equals("key")){
//                            str += "&"+entry.getKey()+ "=" + entry.getValue();
//                        }else {
//                            str += "&" + entry.getKey() + "=" + android.util.Base64.encodeToString(entry.getValue().getBytes(), android.util.Base64.DEFAULT).trim();
//                        }
//                    }
                }

                    str = str.replace("\n","");//去除换行
                    str = str.replace("\\s","");//去除空格
//                Log.e("testaaaa",str);
                    isFirst = !isFirst;
                    alga.update(str.getBytes());

                /**
                 * 循环遍历value值，添加到表单
                 */
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value == null) {
                        value = null;
                    }
                    if (key.equals("sign")) {
                        value = Utils.byte2hex(alga.digest());
                    }else if (key.equals("key")){
                        continue;
                    }
                    builder.add(key,value);
                }

              /*  RequestBody body = RequestBody.create(FORM_CONTENT_TYPE,);*/
                    requestBody = builder.build();
                request = new Request.Builder().url(API.COMMON_URL+ url).post(requestBody).build();
                break;
            case UPLOAD_HTTP_TYPE:
                MultipartBody.Builder multipartBody = new MultipartBody.Builder("-----").setType(MultipartBody.FORM);
                if (paramKey != null && paramValue != null){
                    for (int i = 0; i < paramKey.length; i++) {
                        multipartBody.addFormDataPart(paramKey[i],String.valueOf(paramValue[i]));
                    }
                    requestBody = multipartBody.build();
                }
                request = new Request.Builder().url(API.COMMON_URL+url).post(requestBody).build();
                break;
                default:
                    break;
        }

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request,e,callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    deliverDataFailure(request,e,callBack);
                }
                deliverDataSuccess(result,callBack);
            }
        });
    }

    /**
     *          分发失败的时候回调
     * @param request
     * @param e
     * @param callBack
     */
    private void deliverDataFailure(final Request request, final IOException e,final DataCallBack callBack){
        mHandler.post(()->{
           if (callBack != null){
               callBack.requestFailure(request,e);
           }
        });
    }

    /**
     *          分发成功的时候回调
     * @param result
     * @param callBack
     */
    private void deliverDataSuccess(final String result,final DataCallBack callBack){
        mHandler.post(()->{
            if (callBack != null){
                try {
                    callBack.requestSuceess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *          map根据key值比较大小
     * @param map
     * @return
     */
    private static Map<String,String> sortMapByKey(Map<String,String> map){
        if (map == null || map.isEmpty()){
            return null;
        }

        Map<String,String> sortMap = new TreeMap<String,String>((str1,str2)-> str1.compareTo(str2));
        sortMap.putAll(map);
        return sortMap;
    }


    /**
     *      内部处理Map集合
     *      得到from表单 (post请求)
     * @return
     */
    private RequestBody getRequestBody(Map<String,String> map){
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        resultMap = sortMapByKey(map);

        Log.e("请求参数：","map:"+resultMap.toString());

        String str="";
        int num = 0;

        boolean isFirst = true;

        /**
         * 循环遍历获取key值，拼接sign字符串
         */
        for (Map.Entry<String, String> entry :
                resultMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            num++;
           /* if (isFirst) {
                str += entry.getKey() + "=" + Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                isFirst = !isFirst;
            } else {
                str = str.trim();
                str += "&" + entry.getKey() + "=" + Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                if (num == resultMap.size() - 1) {
                    str += "&" + "key" + "=" + CPResourceUtils.getString("appkey");
                }
            }*/
            if (isFirst) {
                str += entry.getKey() + "=" + entry.getValue().trim();
                isFirst = !isFirst;
            } else {
                str = str.trim();
                str += "&" + entry.getKey() + "=" + entry.getValue().trim();
                if (num == resultMap.size() - 1) {
                    str += "&" + SystemUtils.getChannelInfo(context,Contants.APPKEY_CHANNEL);
                }
            }

        }


        str = str.replace("\n","");//去除换行
        str = str.replace("\\s","");//去除空格

        Log.e("请求参数：","string:"+str);
//        Log.e("testaaaa",str);
        isFirst = !isFirst;
        alga.update(str.getBytes());


        /**
         * 循环遍历value值，添加到表单
         */
        for (Map.Entry<String, String> entry :
                resultMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (key.equals("sign")) {
                value = Utils.byte2hex(alga.digest());
            }else if (key.equals("key")){
                continue;
            }
            builder.add(key,value);
        }

        requestBody = builder.build();
        return requestBody;
    }




    /**
     *      内部提供的post请求方法
     * @param url           请求路径
     * @param params        请求参数(表单)
     * @param callback      回调函数
     */
    private void post(String url, Map<String,String> params, final BaseCallback callback){
        //请求之前调用(例如加载动画)
        callback.onRequestBefore();
          mOkHttpClient.newCall(getRequest(url,params)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //返回失败
                callbackFailure(call.request(),callback,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    //返回成功回调
                    String result = response.body().string();
                    Log.e("请求数据：",result);
                    if (callback.mType == String.class){
                        //如果我们需要返回String类型
                        callbackSuccess(response,result,callback);
                    }else {
                        //如果返回是其他类型,则用Gson去解析

                        try {
                            Object o = gson.fromJson(result, callback.mType);
                            callbackSuccess(response,o,callback);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            callbackError(response,callback,e);
                        }
                    }
                }else {
                    callbackError(response,callback,null);
                }

            }
        });

    }

    /**
     *      得到Request
     * @param url           请求路径
     * @param params        from表单
     * @return
     */
    private Request getRequest(String url,Map<String,String> params){
        //可以从这么划分get和post请求，暂时只支持post
        Log.e("请求参数：","url:"+url);
         return new Request.Builder().url(url).post(getRequestBody(params)).build();
    }

    /**
     *      在主线程中执行成功回调
     * @param response      请求响应
     * @param o             类型
     * @param callback      回调函数
     */
    private void callbackSuccess(final Response response, final Object o, final BaseCallback<Object> callback){
        mHandler.post(()->callback.onSuccess(o));
    }

    /**
     *      在主线程中执行错误回调
     * @param response      请求响应
     * @param callback      回调函数
     * @param e             响应错误异常
     */
    private void callbackError(final Response response,final BaseCallback callback,Exception e){
        mHandler.post(()->callback.onError(response.code(),e));
    }

    /**
     *      在主线程中执行失败回调
     * @param request       请求链接
     * @param callback      回调韩素和
     * @param e             响应错误异常
     */
    private void callbackFailure(final Request request,final BaseCallback callback,final Exception e){
        mHandler.post(()->callback.onFailure(e));
    }



    public static String getRandom(int n){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0;i<n;i++){
            stringBuilder.append(random.nextInt(9));
        }
        return stringBuilder.toString();
    }





}
