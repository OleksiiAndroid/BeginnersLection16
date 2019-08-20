package ua.com.webacademy.beginnerslection16;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewMessage;
    public TextView mTextViewTime;

    public MessageViewHolder(View itemView) {
        super(itemView);

        mTextViewMessage = itemView.findViewById(android.R.id.text1);
        mTextViewTime = itemView.findViewById(android.R.id.text2);
    }
}
