package com.zhangpan.webview_master;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZhangPan on 2017/5/16
 */
public class JavaScriptManager {
    /**
     * 获取JS传来的参数
     * @param url
     * @param pre
     * @return
     */
    public static String getJSParams(String url, String pre) {
        String params = "";
        if (url.contains(pre)) {
            int index = url.indexOf(pre);
            int end = index + pre.length();
            params = url.substring(end + 1);
        }
        return params;
    }

    /**
     *JS调用Android中的方法,根据code去判断调用具体的方法
     * @param mContext
     * @param code
     * @param params
     * @param mWebView
     */
    public static void invokeAndroidMethod(Context mContext, String code, String params, final WebView mWebView) {
        if (code.equals("app://showgame.toast")) {
            try {
                JSONObject json = new JSONObject(params);
                String toast = json.optString("data");
                Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }else if (code.equals("app://showgame.getHotelData")) {
            try {
                final JSONObject json = new JSONObject(params);
                final String callback = json.optString("callback");
                json.put("hotel_name", "维多利亚大酒店");
                json.put("order_status", "已支付");
                json.put("orderId", "201612291809626");
                json.put("seller", "携程");
                json.put("expire_time", "2017年1月6日 23:00");
                json.put("price", "688.0");
                json.put("back_price", "128.0");
                json.put("pay_tpye", "支付宝支付");
                json.put("room_size", "3间房");
                json.put("room_count", "3");
                json.put("in_date", "2017年1月6日 12:00");
                json.put("out_date", "2017年1月8日 12:00");
                json.put("contact", "赵子龙先生");
                json.put("phone", "18888888888");
                json.put("server_phone", "0755-85699309");
                json.put("address", "深圳市宝安区兴东地铁站旁边");

                invokeJavaScript(mContext, callback, json.toString(), mWebView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    /**
     * 统一管理所有android调用js方法
     *
     * @param callback js回调方法名
     * @param json     传递json数据
     */
    public static void invokeJavaScript(Context mContext, final String callback, final String json, final WebView mWebView) {
        Toast.makeText(mContext, "回调js方法：" + callback + ", 参数：" + json, Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(callback)) return;
        //调用js方法必须在主线程
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:" + callback + "(" + json + ")");
            }
        });
    }
}
