package com.hjs.weibo.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.hjs.weibo.R;
import com.hjs.weibo.api.HttpClient;
import com.hjs.weibo.api.model.Comment;
import com.hjs.weibo.api.model.Status;
import com.hjs.weibo.api.model.User;
import com.hjs.weibo.ui.HomeActivity;
import com.hjs.weibo.ui.LoginActivity;
import com.hjs.weibo.util.ConstantS;
import com.hjs.weibo.util.NetUtil;
import com.weibo.sdk.android.Weibo;

public class MainService extends Service implements Runnable
{
    // 创建 微博 API API接口类对象
    public static Weibo weibo = Weibo.getInstance(ConstantS.APP_KEY,
            ConstantS.REDIRECT_URL, ConstantS.SCOPE);

    public static User nowUser = null;

    // 保存所有Activity
    public static ArrayList<Activity> allActivity = new ArrayList<Activity>();

    // 保存前端Activity编号
    public static int lastActivityId = 0;

    // 保存用户头像
    @SuppressLint("UseSparseArrays")
    public static Map<Long, BitmapDrawable> allUserIcon = new HashMap<Long, BitmapDrawable>(
            3);

    // 所有任务
    public static ArrayList<Task> allTask = new ArrayList<Task>(8);

    // 更新UI
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            IWeiboActivity weiboActivity = null;

            switch (msg.what)
            {
            case Task.TASK_USER_LOGIN:
                weiboActivity = (IWeiboActivity) getActivityByName("LoginActivity");

                // 刷新首页
                weiboActivity.refresh(LoginActivity.REFRESH_LOGIN, msg.obj);

                break;
            case Task.TASK_GET_USER_INFO:
                weiboActivity = (IWeiboActivity) getActivityByName("HomeActivity");

                // 刷新用户名
                weiboActivity.refresh(HomeActivity.REFRESH_USER, msg.obj);
                break;

            case Task.TASK_GET_USER_HOMETIMEINLINE:
                weiboActivity = (IWeiboActivity) getActivityByName("HomeActivity");

                // 刷新微博
                weiboActivity.refresh(HomeActivity.REFRESH_WEIBO, msg.obj);
                break;

            case Task.TASK_GET_USER_HOMETIMEINLINE_COMMENTS:
                weiboActivity = (IWeiboActivity) getActivityByName("HomeActivity");

                // 刷新微博
                weiboActivity.refresh(HomeActivity.REFRESH_COMMENT, msg.obj);
                break;

            case Task.TASK_GET_USER_IMAGE_ICON:
                weiboActivity = (IWeiboActivity) getActivityByName("HomeActivity");

                // 刷新头像
                weiboActivity.refresh(HomeActivity.REFRESH_ICON, null);
            }
        }
    };

    // 从集合中通过name获取Activity对象
    public static Activity getActivityByName(String name)
    {
        if (name == null || "".equals(name))
        {
            return null;
        }

        for (Activity activity : allActivity)
        {
            if (name.equals(activity.getClass().getSimpleName()))
            {
                return activity;
            }
        }
        return null;
    }

    // 添加任务
    public static void addTask(Task task)
    {
        if (task != null)
        {
            allTask.add(task);
        }
    }

    public static boolean isRun = false;

    @Override
    public void run()
    {
        while (isRun)
        {
            synchronized (allTask)
            {
                if (allTask.size() > 0)
                {
                    doTask(allTask.get(0));
                }
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    public void doTask(Task task)
    {
        Message msg = this.handler.obtainMessage();
        msg.what = task.getTaskId();

        try
        {
            switch (task.getTaskId())
            {
            // 获取用户首页博客
            case Task.TASK_GET_USER_HOMETIMEINLINE:

                int nowPage = Integer.parseInt(task.getTaskParam()
                        .get("nowPage").toString());

                int pageSize = Integer.parseInt(task.getTaskParam()
                        .get("pageSize").toString());

                List<Status> statuses = HttpClient.friends_timeline(nowPage,
                        pageSize);

                if (allUserIcon == null)
                {
                    allUserIcon = new HashMap<Long, BitmapDrawable>();
                }

                if (statuses != null)
                {
                    for (Status status : statuses)
                    {
                        BitmapDrawable bitmapDrawable = allUserIcon.get(status
                                .getUser().getId());

                        if (bitmapDrawable == null)
                        {
                            // 获取该用户的头像
                            Map<String, Object> iconTaskParamMap = new HashMap<String, Object>();

                            iconTaskParamMap.put("userId", status.getUser()
                                    .getId());

                            iconTaskParamMap.put("url", status.getUser()
                                    .getProfile_image_url());

                            Task iconTask = new Task(
                                    Task.TASK_GET_USER_IMAGE_ICON,
                                    iconTaskParamMap);

                            MainService.addTask(iconTask);

                            // 获取微博评论
                            Map<String, Object> commentTaskParamMap = new HashMap<String, Object>();

                            commentTaskParamMap.put("id", status.getId());

                            Task commentTask = new Task(
                                    Task.TASK_GET_USER_HOMETIMEINLINE_COMMENTS,
                                    commentTaskParamMap);

                            MainService.addTask(commentTask);
                        }
                    }
                }

                msg.obj = statuses;

                break;

            // 获取用户头像
            case Task.TASK_GET_USER_IMAGE_ICON:

                allUserIcon.put(
                        (Long) task.getTaskParam().get("userId"),
                        NetUtil.getImageFromUrl(task.getTaskParam().get("url")
                                .toString()));
                break;
            // 获取微博评论
            case Task.TASK_GET_USER_HOMETIMEINLINE_COMMENTS:

                HashMap<String, Object> taskParam = (HashMap<String, Object>) task
                        .getTaskParam();

                List<Comment> commentList = HttpClient.comments_show(
                        Long.parseLong(taskParam.get("id").toString()), 0, 0);

                msg.obj = commentList;

                break;

            case Task.TASK_GET_USER_INFO:

                nowUser = HttpClient.users_show();

                msg.obj = nowUser;

                break;
            }

            msg.sendToTarget();
        }
        catch(Exception e)
        {
            msg.what = -100;

            e.printStackTrace();
        }

        allTask.remove(task);
    }

    // 提示用户网络状态错误
    public static void AlertNetError(final Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.NoRouteToHostException);
        builder.setMessage(R.string.NoSignalException);

        builder.setPositiveButton(R.string.net_setting, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                context.startActivity(new Intent(
                        Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        builder.setNegativeButton(R.string.exit_app, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                exitApp(context);
            }
        });

        builder.create().show();
    }

    public static void exitApp(Context context)
    {
        for (Activity activity : allActivity)
        {
            activity.finish();
        }

        // 停止服务
        Intent intent = new Intent();
        intent.setAction("com.hjs.weibo.logic.MainService");
        context.stopService(intent);

        MainService.isRun = false;
    }

    public static void promptExit(final Context context)
    {
        // 创建对话框
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View exitView = layoutInflater.inflate(R.layout.exitdialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(exitView);

        alertDialogBuilder.setPositiveButton(R.string.exit,
                new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        exitApp(context);
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.cancel, null);

        alertDialogBuilder.show();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        new Thread(this).start();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        MainService.isRun = false;
    }

    public static void main(String args[])
    {
        System.out.println(String.class.getName());
        System.out.println(String.class.getSimpleName());
    }
}
