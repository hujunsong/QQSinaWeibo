package com.hjs.weibo.api.model;

public class Remind
{
    int status;// 新微博未读数

    int follower;// 新粉丝数

    int cmt;// 新评论数

    int dm;// 新私信数

    int mention_status;// 新提及我的微博数

    int mention_cmt;// 新提及我的评论数

    int group;// 微群消息未读数

    int private_group;// 私有微群消息未读数

    int notice;// 新通知未读数

    int invite;// 新邀请未读数

    int badge;// 新勋章数

    int photo;// 相册消息未读数

    int msgbox;// {{{3}}}

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getFollower()
    {
        return follower;
    }

    public void setFollower(int follower)
    {
        this.follower = follower;
    }

    public int getCmt()
    {
        return cmt;
    }

    public void setCmt(int cmt)
    {
        this.cmt = cmt;
    }

    public int getDm()
    {
        return dm;
    }

    public void setDm(int dm)
    {
        this.dm = dm;
    }

    public int getMention_status()
    {
        return mention_status;
    }

    public void setMention_status(int mention_status)
    {
        this.mention_status = mention_status;
    }

    public int getMention_cmt()
    {
        return mention_cmt;
    }

    public void setMention_cmt(int mention_cmt)
    {
        this.mention_cmt = mention_cmt;
    }

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }

    public int getPrivate_group()
    {
        return private_group;
    }

    public void setPrivate_group(int private_group)
    {
        this.private_group = private_group;
    }

    public int getNotice()
    {
        return notice;
    }

    public void setNotice(int notice)
    {
        this.notice = notice;
    }

    public int getInvite()
    {
        return invite;
    }

    public void setInvite(int invite)
    {
        this.invite = invite;
    }

    public int getBadge()
    {
        return badge;
    }

    public void setBadge(int badge)
    {
        this.badge = badge;
    }

    public int getPhoto()
    {
        return photo;
    }

    public void setPhoto(int photo)
    {
        this.photo = photo;
    }

    public int getMsgbox()
    {
        return msgbox;
    }

    public void setMsgbox(int msgbox)
    {
        this.msgbox = msgbox;
    }

    @Override
    public String toString()
    {
        return "Remind [status=" + status + ", follower=" + follower + ", cmt="
                + cmt + ", dm=" + dm + ", mention_status=" + mention_status
                + ", mention_cmt=" + mention_cmt + ", group=" + group
                + ", private_group=" + private_group + ", notice=" + notice
                + ", invite=" + invite + ", badge=" + badge + ", photo="
                + photo + ", msgbox=" + msgbox + "]";
    }

}
