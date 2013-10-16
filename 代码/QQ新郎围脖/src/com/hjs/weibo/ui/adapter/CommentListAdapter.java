package com.hjs.weibo.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjs.weibo.R;
import com.hjs.weibo.api.model.Comment;
import com.hjs.weibo.logic.MainService;
import com.hjs.weibo.util.TextAutoLink;

/**
 * 用于显示所有微博回复
 * 
 * @author hjs
 * 
 */
public class CommentListAdapter extends BaseAdapter
{
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            new Locale("zh", "CN"));

    List<Comment> commentList = null;

    Context context = null;

    public CommentListAdapter(Context context, List<Comment> list)
    {
        this.context = context;
        this.commentList = list;
    }

    @Override
    public int getCount()
    {
        return commentList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return commentList.get(position).getId();
    }

    /**
     * 本方法中的第二个参数是缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        /**
         * 从日志中可以看到，当前可见的条目会调用getView方法取得，上下翻动后会重新getView
         */
        // Log.i("listView", "getView" + "-----" + position);

        View commentView = null;

        if (convertView == null
                || convertView.findViewById(R.id.ivItemPortrait) == null)
        {
            commentView = LayoutInflater.from(context).inflate(
                    R.layout.itemview2, null);
        }
        else
        {
            // 获取内存中的条目
            commentView = convertView;
        }

        ViewHolder holder = new ViewHolder();

        holder.imageViewItemPortrait = (ImageView) commentView
                .findViewById(R.id.ivItemPortrait);

        holder.textViewName = (TextView) commentView
                .findViewById(R.id.tvItemName);

        holder.textViewItemDate = (TextView) commentView
                .findViewById(R.id.tvItemDate);

        holder.textViewContent = (TextView) commentView
                .findViewById(R.id.tvItemContent);

        // 设置头像
        if (MainService.allUserIcon.containsKey(commentList.get(position)
                .getUser().getId()))
        {
            holder.imageViewItemPortrait
                    .setImageDrawable(MainService.allUserIcon.get(commentList
                            .get(position).getUser().getId()));
        }

        // 设置昵称
        holder.textViewName.setText(commentList.get(position).getUser()
                .getName());

        // 设置内容
        // holder.textViewContent.setText(statusList.get(position - 1)
        // .getText());

        // 设置带高亮和超链接的内容
        TextAutoLink.addURLSpan(commentList.get(position).getText(),
                holder.textViewContent);

        // 设置时间
        try
        {
            holder.textViewItemDate.setText(sdf.format(new Date(commentList
                    .get(position).getCreated_at())));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return commentView;
    }

    /**
     * 微博评论数据适配器
     * 
     * @author hjs
     * 
     */
    private static class ViewHolder
    {
        // 头像，有默认值
        ImageView imageViewItemPortrait;

        // 昵称
        TextView textViewName;

        // 新浪认证，默认gone
        ImageView imageViewV;

        // 时间
        TextView textViewItemDate;

        // 时间图片，不用修改
        ImageView imageViewItemPic;

        // 内容
        TextView textViewContent;

        // 自己增加的内容图片显示的image
        ImageView contentPic;

        // 回复，默认gone
        View subLayout;

        // 回复内容subLayout显示才可以显示
        TextView textViewSubContent;

        // 自己增加的主要显示回复内容的图片，subLayout显示才可以显示
        ImageView subContentPic;
    }
}