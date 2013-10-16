package com.hjs.weibo.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.hjs.weibo.R;
import com.hjs.weibo.logic.IWeiboActivity;

public class MainActivity extends TabActivity implements IWeiboActivity
{
    // 首页
    public static final String TAB_HOME = "tab_home";

    // 信息
    public static final String TAB_MSG = "tab_msg";

    // 我的资料
    public static final String TAB_MYRES = "tab_myres";

    // 搜索
    public static final String TAB_SEARCH = "tab_search";

    // 更多
    public static final String TAB_MORE = "tab_more";

    private TabHost tabHost;

    private RadioGroup radioGroup;

    @Override
    public void init()
    {
    }

    @Override
    public void refresh(Object... param)
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.maintabs);

        tabHost = this.getTabHost();

        // 主页
        TabSpec homeTabSpec = tabHost.newTabSpec(TAB_HOME).setIndicator(
                TAB_HOME);

        homeTabSpec
                .setContent(new Intent(MainActivity.this, HomeActivity.class));

        tabHost.addTab(homeTabSpec);

        // 信息
        TabSpec msgTabSpec = tabHost.newTabSpec(TAB_MSG).setIndicator(TAB_MSG);

        msgTabSpec.setContent(new Intent(MainActivity.this, MsgActivity.class));

        tabHost.addTab(msgTabSpec);

        // 我的资料
        TabSpec myresTabSpec = tabHost.newTabSpec(TAB_MYRES).setIndicator(
                TAB_MYRES);

        myresTabSpec
                .setContent(new Intent(MainActivity.this, MsgActivity.class));

        tabHost.addTab(myresTabSpec);

        // 搜索
        TabSpec searchTabSpec = tabHost.newTabSpec(TAB_SEARCH).setIndicator(
                TAB_SEARCH);

        searchTabSpec.setContent(new Intent(MainActivity.this,
                MsgActivity.class));

        tabHost.addTab(searchTabSpec);

        // 更多
        TabSpec moreTabSpec = tabHost.newTabSpec(TAB_MORE).setIndicator(
                TAB_MORE);

        moreTabSpec
                .setContent(new Intent(MainActivity.this, MsgActivity.class));

        tabHost.addTab(moreTabSpec);

        radioGroup = (RadioGroup) findViewById(R.id.main_radio);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int checkedId)
            {
                switch (checkedId)
                {
                // 首页
                case R.id.radio_button0:
                    tabHost.setCurrentTabByTag(TAB_HOME);
                    break;
                // 信息
                case R.id.radio_button1:
                    tabHost.setCurrentTabByTag(TAB_MSG);
                    break;
                // 我的资料
                case R.id.radio_button2:
                    tabHost.setCurrentTabByTag(TAB_MYRES);
                    break;
                // 搜索
                case R.id.radio_button3:
                    tabHost.setCurrentTabByTag(TAB_SEARCH);
                    break;
                // 更多
                case R.id.radio_button4:
                    tabHost.setCurrentTabByTag(TAB_MORE);
                    break;
                }
            }
        });

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        init();
    }

}
