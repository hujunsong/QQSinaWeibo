package com.hjs.weibo.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hjs.weibo.R;
import com.hjs.weibo.api.HttpClient;
import com.hjs.weibo.logic.IWeiboActivity;
import com.hjs.weibo.logic.MainService;
import com.hjs.weibo.logic.Task;
import com.hjs.weibo.util.ConstantS;
import com.hjs.weibo.util.NetUtil;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.AccessTokenKeeper;

/**
 * 
 * @author hjs
 * 
 */
public class LoginActivity extends Activity implements IWeiboActivity
{
    // 登录刷新
    public static int REFRESH_LOGIN = 0;

    /** 显示认证后的信息，如AccessToken */
    private TextView mText;

    /** 微博API接口类，提供登陆等功能 */
    private Weibo mWeibo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当sdk支持sso时有效 */
    private SsoHandler mSsoHandler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        mText = (TextView) findViewById(R.id.show);

        mWeibo = Weibo.getInstance(ConstantS.APP_KEY, ConstantS.REDIRECT_URL,
                ConstantS.SCOPE);

        // 获取token测试button
        findViewById(R.id.auth).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mWeibo.anthorize(LoginActivity.this, new AuthDialogListener());
            }
        });

        // 触发sso测试button
        findViewById(R.id.sso).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSsoHandler = new SsoHandler(LoginActivity.this, mWeibo);

                mSsoHandler.authorize(new AuthDialogListener(), null);
            }
        });

        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        if (mAccessToken != null && mAccessToken.isSessionValid())
        {
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss",
                    new Locale("zh", "CN")).format(new Date(mAccessToken
                    .getExpiresTime()));

            mText.setText("access_token 仍在有效期内,无需再次登录: \naccess_token:"
                    + mAccessToken.getToken() + "\n有效期：" + date);

            HttpClient.temp_access_token = mAccessToken.getToken();
        }
        else
        {
            mText.setText("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，"
                    + "目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
        }
    }

    /**
     * 微博认证授权回调类。 1. SSO登陆时，需要在{@link #onActivityResult}
     * 中调用mSsoHandler.authorizeCallBack后， 该回调才会被执行。 2. 非SSO登陆时，当授权后，就会被执行。
     * 当授权成功后，请保存该access_token、expires_in等信息到SharedPreferences中。
     */
    class AuthDialogListener implements WeiboAuthListener
    {
        @Override
        public void onComplete(Bundle values)
        {
            String code = values.getString("code");

            mAccessToken = HttpClient.getTokenByCode(code);

            if (mAccessToken.isSessionValid())
            {
                HttpClient.temp_access_token = mAccessToken.getToken();

                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
                        new Locale("zh", "CN")).format(new Date(mAccessToken
                        .getExpiresTime()));

                mText.setText("认证成功: \r\n access_token: "
                        + mAccessToken.getToken() + "\r\n" + "expires_in: "
                        + mAccessToken.getExpiresTime() + "\r\n有效期：" + date);

                AccessTokenKeeper.keepAccessToken(LoginActivity.this,
                        mAccessToken);

                Toast.makeText(LoginActivity.this, "认证成功", Toast.LENGTH_SHORT)
                        .show();

                // 获取用户数据
                MainService.addTask(new Task(Task.TASK_GET_USER_INFO, null));

                // 进入首页
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);

                startActivity(intent);

                finish();
            }
        }

        @Override
        public void onError(WeiboDialogError e)
        {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel()
        {
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e)
        {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的Activity必须重写onActivityResult
        if (mSsoHandler != null)
        {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        init();
    }

    @Override
    public void init()
    {
        if (NetUtil.checkNet(this))
        {
            if (!MainService.isRun)
            {
                MainService.isRun = true;

                Intent intent = new Intent("com.hjs.weibo.logic.MainService");
                startService(intent);
            }

            if (mAccessToken != null && mAccessToken.isSessionValid())
            {
                // 获取用户数据
                MainService.addTask(new Task(Task.TASK_GET_USER_INFO, null));

                // 进入首页
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);

                startActivity(intent);

                finish();
            }
        }
        else
        {
            MainService.AlertNetError(this);
        }
    }

    /**
     * 登录界面不由MainService刷新
     */
    @Override
    public void refresh(Object... param)
    {
    }
}
