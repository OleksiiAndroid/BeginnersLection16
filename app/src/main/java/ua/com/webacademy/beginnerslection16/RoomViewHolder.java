package ua.com.webacademy.beginnerslection16;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RoomViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewName;
    public TextView mTextViewDescription;

    public RoomViewHolder(View itemView) {
        super(itemView);

        mTextViewName = itemView.findViewById(android.R.id.text1);
        mTextViewDescription = itemView.findViewById(android.R.id.text2);
    }
}
