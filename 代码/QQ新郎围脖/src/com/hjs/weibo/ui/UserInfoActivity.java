package com.hjs.weibo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 这个activity由微博内容超链接进入
 * 
 * @author hjs
 * 
 */
public class UserInfoActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);

        // 得到链接路径参数
        textView.setText(this.getIntent().getData().getPath());

        this.setContentView(textView);

    }
}
