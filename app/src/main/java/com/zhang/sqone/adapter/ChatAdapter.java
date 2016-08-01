package com.zhang.sqone.adapter;

import android.content.Context;

import com.zhang.sqone.R;
import com.zhang.sqone.bean.ChatMessage;

import java.util.List;

/**
*android （万能适配器）对话形式的适配器
*@author ZJP
*created at 2016/3/14 15:02
*/

public class ChatAdapter extends MultiItemCommonAdapter<ChatMessage>
{
    public ChatAdapter(Context context, List<ChatMessage> datas)
    {
        super(context, datas, new MultiItemTypeSupport<ChatMessage>()
        {
            @Override
            public int getLayoutId(int position, ChatMessage msg)
            {
                if (msg.isComMeg())
                {
                    return R.layout.main_chat_from_msg;
                }
                return R.layout.main_chat_send_msg;
            }

            @Override
            public int getViewTypeCount()
            {
                return 2;
            }
            @Override
            public int getItemViewType(int postion, ChatMessage msg)
            {
                if (msg.isComMeg())
                {
                    return ChatMessage.RECIEVE_MSG;
                }
                return ChatMessage.SEND_MSG;
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage)
    {

        switch (holder.getLayoutId())
        {
            case R.layout.main_chat_from_msg:
                holder.setText(R.id.chat_from_content, chatMessage.getContent());
                holder.setText(R.id.chat_from_name, chatMessage.getName());
                holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());
                break;
            case R.layout.main_chat_send_msg:
                holder.setText(R.id.chat_send_content, chatMessage.getContent());
                holder.setText(R.id.chat_send_name, chatMessage.getName());
                holder.setImageResource(R.id.chat_send_icon, chatMessage.getIcon());
                break;
        }
    }
}
