package com.hjs.weibo.util;

public interface ConstantS
{
    // 应用的key 请到官方申请正式的appkey替换APP_KEY
    public static final String APP_KEY = "2127536535";

    // 应用的key 请到官方申请正式的App_Secret替换App_Secret
    public static final String App_Secret = "c34166870fd0cdf75387b67534ab9205";

    // 替换为开发者REDIRECT_URL
    public static final String REDIRECT_URL = "http://open.weibo.com/apps/2127536535/privilege/oauth";

    // 新支持scope：支持传入多个scope权限，用逗号分隔
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public static final String CLIENT_ID = "client_id";

    public static final String RESPONSE_TYPE = "response_type";

    public static final String USER_REDIRECT_URL = "redirect_uri";

    public static final String DISPLAY = "display";

    public static final String USER_SCOPE = "scope";

    public static final String PACKAGE_NAME = "packagename";

    public static final String KEY_HASH = "key_hash";
}
