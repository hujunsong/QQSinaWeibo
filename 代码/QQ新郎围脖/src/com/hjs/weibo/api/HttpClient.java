package com.hjs.weibo.api;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjs.weibo.api.model.Comment;
import com.hjs.weibo.api.model.Status;
import com.hjs.weibo.api.model.User;
import com.hjs.weibo.util.ConstantS;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.HttpManager;

public class HttpClient
{
    public static String TAG = "HttpClient";

    public static String temp_access_token;

    public static Gson gson = new Gson();

    /**
     * 实现由code申请的Access_token
     * 
     * @param code
     */
    public static Oauth2AccessToken getTokenByCode(String code)
    {
        final Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();

        // 现在来填充参数,注意这里不能有空格
        String url = "https://api.weibo.com/oauth2/access_token";

        WeiboParameters params = new WeiboParameters();

        /*
         * params的中文就是参数的意思,所有将参数放在里面,所需的请求参数新浪API里面
         * 都有http://open.weibo.com/wiki/OAuth2/access_token
         */
        params.add("client_id", ConstantS.APP_KEY);
        params.add("client_secret", ConstantS.App_Secret);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", ConstantS.REDIRECT_URL);

        try
        {
            String Json = HttpManager.openUrl(url, "POST", params, null);

            JSONObject jsonObject = new JSONObject(Json);

            String access_token = jsonObject.getString("access_token");

            temp_access_token = access_token;

            // 有效期
            String expires_in = jsonObject.getString("expires_in");

            oauth2AccessToken.setToken(access_token);
            oauth2AccessToken.setExpiresIn(expires_in);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }

        return oauth2AccessToken;
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博
     */
    public static List<Status> friends_timeline(int page, int count)
    {
        List<Status> statusList = null;

        // 现在来填充参数,注意这里不能有空格
        String url = "https://api.weibo.com/2/statuses/friends_timeline.json";

        WeiboParameters params = new WeiboParameters();

        /*
         * params的中文就是参数的意思,所有将参数放在里面,所需的请求参数新浪API里面
         * 都有http://open.weibo.com/wiki/OAuth2/access_token
         */
        params.add("access_token", temp_access_token);
        params.add("page", page == 0 ? 1 : page);
        params.add("count", count == 0 ? 20 : count);

        try
        {
            String Json = HttpManager.openUrl(url, "GET", params, null);

            String jsonObject = new JSONObject(Json).getString("statuses");

            Type listType = new TypeToken<List<Status>>()
            {
            }.getType();

            statusList = gson.fromJson(jsonObject, listType);

        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }

        return statusList;
    }

    /**
     * 获取微博评论
     */
    public static List<Comment> comments_show(long id, int page, int count)
    {
        List<Comment> commentList = null;

        // 现在来填充参数,注意这里不能有空格
        String url = "https://api.weibo.com/2/comments/show.json";

        WeiboParameters params = new WeiboParameters();

        /*
         * params的中文就是参数的意思,所有将参数放在里面,所需的请求参数新浪API里面
         * 都有http://open.weibo.com/wiki/OAuth2/access_token
         */
        params.add("access_token", temp_access_token);
        params.add("id", id);
        params.add("page", page == 0 ? 1 : page);
        params.add("count", count == 0 ? 20 : count);

        try
        {
            String Json = HttpManager.openUrl(url, "GET", params, null);

            String jsonObject = new JSONObject(Json).getString("comments");

            Type listType = new TypeToken<List<Comment>>()
            {
            }.getType();

            commentList = gson.fromJson(jsonObject, listType);

        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }

        return commentList;
    }

    /**
     * 获取用户ID
     */
    public static long account_get_uid()
    {
        // 现在来填充参数,注意这里不能有空格
        String url = "https://api.weibo.com/2/account/get_uid.json";

        WeiboParameters params = new WeiboParameters();

        /*
         * params的中文就是参数的意思,所有将参数放在里面,所需的请求参数新浪API里面
         * 都有http://open.weibo.com/wiki/OAuth2/access_token
         */
        params.add("access_token", temp_access_token);

        try
        {
            String Json = HttpManager.openUrl(url, "GET", params, null);

            String jsonObject = new JSONObject(Json).getString("uid");

            return gson.fromJson(jsonObject, long.class);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博
     */
    public static User users_show()
    {
        // 现在来填充参数,注意这里不能有空格
        String url = "https://api.weibo.com/2/users/show.json";

        WeiboParameters params = new WeiboParameters();

        /*
         * params的中文就是参数的意思,所有将参数放在里面,所需的请求参数新浪API里面
         * 都有http://open.weibo.com/wiki/OAuth2/access_token
         */
        params.add("access_token", temp_access_token);
        params.add("uid", account_get_uid());

        try
        {
            String Json = HttpManager.openUrl(url, "GET", params, null);

            return gson.fromJson(Json, User.class);

        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }

        return null;
    }
}
