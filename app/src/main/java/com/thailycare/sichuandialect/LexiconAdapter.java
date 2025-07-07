package com.thailycare.sichuandialect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LexiconAdapter extends RecyclerView.Adapter<LexiconAdapter.ViewHolder> {
    private List<LexiconEntry> entries;

    public LexiconAdapter(List<LexiconEntry> entries) {
        this.entries = entries;
    }

    public void updateEntries(List<LexiconEntry> newEntries) {
        this.entries = newEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lexicon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LexiconEntry entry = entries.get(position);
        holder.tvCharacter.setText("字符: " + entry.character);
        holder.tvTone.setText("音调: " + entry.tone);
        holder.tvParaphrase.setText("释义: " + entry.paraphrase);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCharacter, tvTone, tvParaphrase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCharacter = itemView.findViewById(R.id.tvCharacter);
            tvTone = itemView.findViewById(R.id.tvTone);
            tvParaphrase = itemView.findViewById(R.id.tvParaphrase);
        }
    }
}