package com.hjs.weibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.hjs.weibo.R;
import com.hjs.weibo.logic.IWeiboActivity;

public class LogoActivity extends Activity implements IWeiboActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 取消标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.logo);

        ImageView imageView = (ImageView) this.findViewById(R.id.ImageView01);

        AlphaAnimation startAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        startAlphaAnimation.setDuration(3000);
        imageView.startAnimation(startAlphaAnimation);

        startAlphaAnimation.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0)
            {
                Intent intent = new Intent(LogoActivity.this,
                        LoginActivity.class);

                LogoActivity.this.startActivity(intent);

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation arg0)
            {
            }

            @Override
            public void onAnimationStart(Animation arg0)
            {
            }
        });
    }

    @Override
    public void init()
    {

    }

    @Override
    public void refresh(Object... param)
    {

    }

}
