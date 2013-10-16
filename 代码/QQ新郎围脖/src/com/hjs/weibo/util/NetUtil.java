package com.hjs.weibo.util;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil
{
    public static boolean checkNet(Context context)
    {
        // 获取手机所有连接管理对象，包括wifi,net等
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            // 获取网络连接管理对象
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()
                    && NetworkInfo.State.CONNECTED == networkInfo.getState())
            {
                return true;
            }
        }
        return false;
    }

    // 从一个URL取得一个图片
    public static BitmapDrawable getImageFromUrl(String url)
    {
        BitmapDrawable icon = null;

        Log.i("NetUtil", url);
        
        try
        {
            URL iconUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) iconUrl
                    .openConnection();

            icon = new BitmapDrawable(connection.getInputStream());

            connection.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return icon;
    }
}
