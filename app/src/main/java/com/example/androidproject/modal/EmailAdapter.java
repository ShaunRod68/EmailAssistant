package com.example.androidproject.modal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private List<EmailItem> emails;

    public EmailAdapter(List<EmailItem> emails) {
        this.emails = emails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_email_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmailItem email = emails.get(position);
        holder.textView.setText(email.text);
        holder.timeView.setText(email.time);
        holder.lengthView.setText(String.format("%d chars", email.length));
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, timeView, lengthView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.email_text);
            timeView = itemView.findViewById(R.id.email_time);
            lengthView = itemView.findViewById(R.id.email_length);
        }
    }
}
