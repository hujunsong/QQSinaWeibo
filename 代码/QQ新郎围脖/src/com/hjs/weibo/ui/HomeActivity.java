package com.hjs.weibo.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjs.weibo.R;
import com.hjs.weibo.api.model.Status;
import com.hjs.weibo.api.model.Comment;
import com.hjs.weibo.logic.IWeiboActivity;
import com.hjs.weibo.logic.MainService;
import com.hjs.weibo.logic.Task;
import com.hjs.weibo.ui.adapter.CommentListAdapter;
import com.hjs.weibo.ui.adapter.StatusListAdapter;

/**
 * 微博首页
 * 
 * @author hjs
 * 
 */
public class HomeActivity extends Activity implements IWeiboActivity
{

    // 刷新微博
    public static final int REFRESH_WEIBO = 0;

    // 刷新图像
    public static final int REFRESH_ICON = 1;

    // 刷新用户
    public static final int REFRESH_USER = 2;

    // 刷新评论
    public static final int REFRESH_COMMENT = 3;

    // 首页微博信息
    private ListView statuesListView = null;

    // 首页微博回复信息
    private ListView commentListView = null;

    private View progress = null;

    // 用户名
    private TextView userTextView = null;

    // 微博创建时间为格林乔治时间
    Calendar gregorianCalendar = GregorianCalendar.getInstance();

    // 当前是第几页
    private int nowPage = 1;

    // 每页条数
    private int pageSize = 5;

    @Override
    public void init()
    {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nowPage", nowPage);
        paramMap.put("pageSize", pageSize);

        // 加载当前用户的微博首页信息
        Task task = new Task(Task.TASK_GET_USER_HOMETIMEINLINE, paramMap);

        MainService.addTask(task);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refresh(Object... param)
    {
        switch (((Integer) param[0]).intValue())
        {
        // 如果是更新微博
        case REFRESH_WEIBO:

            if (param.length == 2 && param[1] != null)
            {
                if (nowPage == 1)
                {
                    StatusListAdapter statusListAdapter = new StatusListAdapter(
                            this, (List<Status>) param[1]);

                    statuesListView.setAdapter(statusListAdapter);

                    progress.setVisibility(View.GONE);
                }
                else
                {
                    ((StatusListAdapter) statuesListView.getAdapter())
                            .addMoreData((List<Status>) param[1]);
                }
            }

            break;

        // 如果是更新评论
        case REFRESH_COMMENT:
            ((StatusListAdapter) statuesListView.getAdapter())
                    .notifyDataSetChanged();
            break;

        // 如果是更新头像
        case REFRESH_ICON:

            // 触发listviewAdapter重新调用getview方法
            ((StatusListAdapter) statuesListView.getAdapter())
                    .notifyDataSetChanged();

            break;

        // 如果是更新用户
        case REFRESH_USER:

            if (MainService.nowUser != null)
            {
                userTextView.setText(MainService.nowUser.getName());
            }

            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.home);

        statuesListView = (ListView) findViewById(R.id.freelook_listview);

        // 给listView注册上下文菜单
        registerForContextMenu(statuesListView);

        statuesListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3)
            {
                // "更多"的id
                if (arg3 == -1)
                {
                    nowPage++;

                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("nowPage", nowPage);
                    paramMap.put("pageSize", pageSize);

                    // 加载当前用户的微博首页信息
                    Task task = new Task(Task.TASK_GET_USER_HOMETIMEINLINE,
                            paramMap);

                    MainService.addTask(task);
                }
            }

        });

        View titleView = findViewById(R.id.freelook_title);

        Button titleLeftButton = (Button) titleView
                .findViewById(R.id.title_bt_left);

        Button titleRightButton = (Button) titleView
                .findViewById(R.id.title_bt_right);

        userTextView = (TextView) titleView.findViewById(R.id.textView);

        if (MainService.nowUser != null)
        {
            userTextView.setText(MainService.nowUser.getName());
        }

        progress = (View) findViewById(R.id.progress);

        MainService.allActivity.add(this);

        init();
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
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(1, 1, 0, "设置").setIcon(R.drawable.setting);
        menu.add(1, 2, 1, "账号").setIcon(R.drawable.switchuser);
        menu.add(1, 3, 2, "官方").setIcon(R.drawable.officialweibo);
        menu.add(2, 4, 3, "意见").setIcon(R.drawable.comment);
        menu.add(2, 5, 4, "关于").setIcon(R.drawable.aboutweibo);
        menu.add(2, 6, 5, "退出").setIcon(R.drawable.menu_exit);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        // 设置
        case 1:
            break;
        // 账号
        case 2:
            break;
        // 官方
        case 3:
            break;
        // 意见
        case 4:
            break;
        // 关于
        case 5:
            Toast.makeText(this, "关于", Toast.LENGTH_LONG).show();
            break;
        // 退出
        case 6:
            MainService.exitApp(this);
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            MainService.promptExit(this);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item
                .getMenuInfo();

        Toast.makeText(this, adapterContextMenuInfo.id + "", Toast.LENGTH_LONG)
                .show();

        switch (item.getItemId())
        {
        // 转发
        case 1:
            // Intent intent = new Intent(this, Transmit.Activity);
            // intent.putExtra("statusId", adapterContextMenuInfo.id);
            // startActivityForResult(intent, 0);

            break;
        // 评论
        case 2:
            break;
        // 收藏
        case 3:
            break;
        // 查看
        case 4:
            break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * 对上下文菜单操作后回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        /*
         * menuInfo将menuInfo转换为AdapterContextMenuInfo后，可以取得当前listView的条目
         */
        AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;

        // 如果是刷新的更多则不弹出上下文菜单
        if (adapterContextMenuInfo.id != 0 && adapterContextMenuInfo.id != -1)
        {
            menu.setHeaderTitle("弹出菜单");

            menu.add(1, 1, 0, adapterContextMenuInfo.id + "转发");
            menu.add(1, 2, 1, adapterContextMenuInfo.id + "评论");
            menu.add(1, 3, 2, adapterContextMenuInfo.id + "收藏");
            menu.add(1, 4, 3, adapterContextMenuInfo.id + "查看");
        }
    }

}
