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
import android.widget.ListView;
import android.widget.TextView;

import com.hjs.weibo.R;
import com.hjs.weibo.api.HttpClient;
import com.hjs.weibo.api.model.Comment;
import com.hjs.weibo.api.model.Status;
import com.hjs.weibo.logic.MainService;
import com.hjs.weibo.util.TextAutoLink;

/**
 * 用于显示所有微博信息
 * 
 * @author hjs
 * 
 */
public class StatusListAdapter extends BaseAdapter
{
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            new Locale("zh", "CN"));

    List<Status> statusList = null;

    Context context = null;

    public StatusListAdapter(Context context, List<Status> list)
    {
        this.context = context;
        this.statusList = list;
    }

    @Override
    public int getCount()
    {
        // +2表示添加“刷新”和”更多“两个条目
        return statusList.size() + 2;
    }

    @Override
    public Object getItem(int position)
    {
        return statusList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // 第一个条目是”刷新“
        if (position == 0)
        {
            return 0;
        }
        // 中间条目是微博信息
        else if (position > 0 && (position < this.getCount() - 1))
        {
            return statusList.get(position - 1).getId();
        }
        // 最后一个条目是”更多“
        else
        {
            return -1;
        }
    }

    // 更多
    public void addMoreData(List<Status> moreStatus)
    {
        this.statusList.addAll(moreStatus);

        this.notifyDataSetChanged();
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

        // 第一个条目是”刷新“
        if (position == 0)
        {
            View refreshView = null;

            if (convertView != null
                    && convertView.findViewById(R.id.textView) != null)
            {
                refreshView = convertView;
            }
            else
            {
                refreshView = LayoutInflater.from(context).inflate(
                        R.layout.list_moreitems, null);
            }

            TextView refreshTextView = (TextView) refreshView
                    .findViewById(R.id.textView);

            refreshTextView.setText(R.string.refresh);

            return refreshView;
        }
        // 最后一个条目是”更多“
        else if (position == this.getCount() - 1)
        {
            View moreView = null;

            if (convertView != null
                    && convertView.findViewById(R.id.textView) != null)
            {
                moreView = convertView;
            }
            else
            {
                moreView = LayoutInflater.from(context).inflate(
                        R.layout.list_moreitems, null);
            }

            return moreView;
        }
        // 中间的是微博条目
        else
        {
            View statusView = null;

            if (convertView == null
                    || convertView.findViewById(R.id.ivItemPortrait) == null)
            {
                statusView = LayoutInflater.from(context).inflate(
                        R.layout.itemview, null);
            }
            else
            {
                // 获取内存中的条目
                statusView = convertView;
            }

            ViewHolder holder = new ViewHolder();

            holder.imageViewItemPortrait = (ImageView) statusView
                    .findViewById(R.id.ivItemPortrait);

            holder.textViewName = (TextView) statusView
                    .findViewById(R.id.tvItemName);

            holder.textViewContent = (TextView) statusView
                    .findViewById(R.id.tvItemContent);

            holder.textViewItemDate = (TextView) statusView
                    .findViewById(R.id.tvItemDate);

            holder.subLayout = statusView.findViewById(R.id.subLayout);

            List<Comment> tempCommentList = HttpClient.comments_show(statusList
                    .get(position - 1).getId(), 0, 0);

            // 设置回复
            if (tempCommentList != null && tempCommentList.size() > 0)
            {
                holder.subLayout.setVisibility(View.VISIBLE);

                ((ListView) (holder.subLayout
                        .findViewById(R.id.comment_listview)))
                        .setAdapter(new CommentListAdapter(context,
                                tempCommentList));
            }

            // 设置头像
            if (MainService.allUserIcon.containsKey(statusList
                    .get(position - 1).getUser().getId()))
            {
                holder.imageViewItemPortrait
                        .setImageDrawable(MainService.allUserIcon
                                .get(statusList.get(position - 1).getUser()
                                        .getId()));
            }

            // 设置昵称
            holder.textViewName.setText(statusList.get(position - 1).getUser()
                    .getName());

            // 设置内容
            // holder.textViewContent.setText(statusList.get(position - 1)
            // .getText());

            // 设置带高亮和超链接的内容
            TextAutoLink.addURLSpan(statusList.get(position - 1).getText(),
                    holder.textViewContent);

            // 设置时间
            try
            {
                holder.textViewItemDate.setText(sdf.format(new Date(statusList
                        .get(position - 1).getCreated_at())));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return statusView;
        }
    }

    /**
     * 微博信息数据适配器
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