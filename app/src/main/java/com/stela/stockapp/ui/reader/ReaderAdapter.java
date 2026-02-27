package com.stela.stockapp.ui.reader;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.stela.stockapp.R;
import com.stela.stockapp.domain.Tag;

import java.util.ArrayList;
import java.util.List;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.ReaderViewHolder> {

    private final List<Tag> tagList;

    public ReaderAdapter(List<Tag> tagList) {
        this.tagList = tagList != null ? tagList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tagView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reader_view, parent, false);
        return new ReaderViewHolder(tagView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReaderViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.bind(tag);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }


    public void submitList(List<Tag> newList) {
        tagList.clear();
        if (newList != null) {
            tagList.addAll(newList);
        }
        notifyDataSetChanged();
    }


    class ReaderViewHolder extends RecyclerView.ViewHolder {

        TextView tagId, signalForce, tagNumber;

        public ReaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tagId = itemView.findViewById(R.id.tagId);
            signalForce = itemView.findViewById(R.id.signalForce);
            tagNumber = itemView.findViewById(R.id.tagNumber);
        }

        public void bind(Tag tag) {
            tagId.setText(tag.getEpc());
            signalForce.setText(String.valueOf(tag.getRssi()));
            tagNumber.setText(String.valueOf(tag.getReadCount()));
        }
    }
}