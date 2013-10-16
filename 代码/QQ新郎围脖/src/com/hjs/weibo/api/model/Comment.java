package com.hjs.weibo.api.model;

public class Comment
{
    String created_at;// 评论创建时间

    long id;// 评论的ID

    String text;// 评论的内容

    String source;// 评论的来源

    User user;// 评论作者的用户信息字段 详细

    String mid;// 评论的MID

    String idstr;// 字符串型的评论ID

    Status status;// 评论的微博信息字段 详细

    Comment reply_comment;// 评论来源评论，当本评论属于对另一评论的回复时返回此字段

    public String getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getMid()
    {
        return mid;
    }

    public void setMid(String mid)
    {
        this.mid = mid;
    }

    public String getIdstr()
    {
        return idstr;
    }

    public void setIdstr(String idstr)
    {
        this.idstr = idstr;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Comment getReply_comment()
    {
        return reply_comment;
    }

    public void setReply_comment(Comment reply_comment)
    {
        this.reply_comment = reply_comment;
    }

    @Override
    public String toString()
    {
        return "Comment [created_at=" + created_at + ", id=" + id + ", text="
                + text + ", source=" + source + ", user=" + user + ", mid="
                + mid + ", idstr=" + idstr + ", status=" + status
                + ", reply_comment=" + reply_comment + "]";
    }

}
