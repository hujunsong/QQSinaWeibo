package com.hjs.weibo.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

/**
 * 文字自动链接
 * 
 * @author hjs
 * 
 */
public class TextAutoLink
{
    // 加入话题、好友、URL的链接
    public static char strArray[];

    /**
     * 处理要高亮显示的微博内容
     * 
     * @param str
     * @param textView
     */
    public static void addURLSpan(String str, TextView textView)
    {
        SpannableString spannableString = new SpannableString(str);

        strArray = str.toCharArray();

        // =========== 处理http链接 ===================
        int l = str.length() - 10;

        for (int i = 0; i < l; i++)
        {
            // 以"http://"开头
            if (i + 7 < l && strArray[i] == 'h' && strArray[i + 1] == 't'
                    && strArray[i + 2] == 't' && strArray[i + 3] == 'p'
                    && strArray[i + 4] == ':' && strArray[i + 5] == '/'
                    && strArray[i + 6] == '/')
            {
                StringBuffer stringBuffer = new StringBuffer("http://");

                for (int j = i + 7; true; j++)
                {
                    if (strArray[j] != ' ' && j < str.length() - 1)
                    {
                        stringBuffer.append(strArray[j]);
                    }
                    else
                    {
                        // 高亮显示
                        spannableString.setSpan(
                                new URLSpan(stringBuffer.toString()), i, j + 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        i = j;
                        break;
                    }
                }
            }
        }

        // =========== 处理好友，以‘@’开头，以‘ ’结束===================
        l = str.length();

        StringBuffer stringBuffer = null;

        boolean start = false;

        int startIndex = 0;

        for (int i = 0; i < l; i++)
        {
            if (strArray[i] == '@')
            {
                start = true;

                /*
                 * 这里的webo即为清单文件中，intentFilter的data的scheme
                 * weibo.View即为清单文件中，intentFilter的data的host
                 */
                stringBuffer = new StringBuffer("weibo://weibo.View/");

                startIndex = i;
            }
            else
            {
                if (start)
                {
                    if (strArray[i] == ' ')
                    {
                        spannableString
                                .setSpan(new URLSpan(stringBuffer.toString()),
                                        startIndex, i,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        stringBuffer = null;
                        start = false;
                    }
                    else
                    {
                        stringBuffer.append(strArray[i]);
                    }
                }
            }
        }

        // ================= 处理话题 ==============
        start = false;
        startIndex = 0;
        for (int i = 0; i < l; i++)
        {
            if (strArray[i] == '#')
            {
                if (!start)
                {
                    start = true;

                    stringBuffer = new StringBuffer("weibo://weibo.View/");

                    startIndex = i;
                }
                else
                {
                    stringBuffer.append('#');

                    spannableString.setSpan(
                            new URLSpan(stringBuffer.toString()), startIndex,
                            i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    stringBuffer = null;
                    start = false;
                }
            }
            else
            {
                if (start)
                {
                    stringBuffer.append(strArray[i]);
                }
            }
        }

        // 设定TextView内容的话题、URL和好友的链接
        textView.setText(spannableString);

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        strArray = null;
    }
}
