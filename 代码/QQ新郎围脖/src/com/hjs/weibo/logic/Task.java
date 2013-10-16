package com.hjs.weibo.logic;

import java.util.Map;

public class Task
{
    // 任务编号
    private int taskId;

    // 任务参数
    private Map<String, Object> taskParam;

    // 获取用户详细信息
    public static final int TASK_GET_USER_INFO = 0;

    // 获取用户首页博客
    public static final int TASK_GET_USER_HOMETIMEINLINE = 1;

    // 获取用户头像图片
    public static final int TASK_GET_USER_IMAGE_ICON = 2;

    // 获取用户所有好友
    public static final int TASK_GET_USER_FRIEND = 3;

    // 获取用户首页博客下一页
    public static final int TASK_GET_USER_HOMETIMEINLINE_MORE = 4;
    
    // 获取微博评论
    public static final int TASK_GET_USER_HOMETIMEINLINE_COMMENTS = 5;

    // 发布微博
    public static final int TASK_NEW_WEIBO = 6;

    // 用户登录
    public static final int TASK_USER_LOGIN = 7;
    
    public Task(int id, Map<String, Object> param)
    {
        this.taskId = id;
        this.taskParam = param;
    }

    public int getTaskId()
    {
        return this.taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public Map<String, Object> getTaskParam()
    {
        return taskParam;
    }

    public void setTaskParam(Map<String, Object> taskParam)
    {
        this.taskParam = taskParam;
    }
}
