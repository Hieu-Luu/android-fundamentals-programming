package com.android.persistentdata.ui.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.persistentdata.R;
import com.android.persistentdata.model.room.Word;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        WordViewHolder(@NonNull final View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    private static ClickListener clickListener;

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words

    WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word current = mWords.get(position);
        holder.wordItemView.setText(current.getWord());
    }

    @Override
    public int getItemCount() {
        if (mWords != null) {
            return mWords.size();
        }
        return 0;
    }

    public void setWords(List<Word> words) {
        this.mWords = words;
        notifyDataSetChanged();
    }

    public Word getWordAtPosition (int position) {
        return mWords.get(position);
    }

    public void setOnItemClickListener(ClickListener listener){
        WordListAdapter.clickListener = listener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
