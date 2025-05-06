package com.example.androidproject.modal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;

import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {
    private List<ChatItem> chats;

    public ChatHistoryAdapter(List<ChatItem> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem chat = chats.get(position);
        holder.messageView.setText(chat.message);
        holder.senderView.setText(chat.sender);
        holder.timeView.setText(chat.time);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageView, senderView, timeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.chat_message);
            senderView = itemView.findViewById(R.id.chat_sender);
            timeView = itemView.findViewById(R.id.chat_time);
        }
    }
}