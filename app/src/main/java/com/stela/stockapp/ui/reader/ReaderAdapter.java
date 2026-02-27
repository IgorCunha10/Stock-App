package com.stela.stockapp.ui.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        public void addTag(@NonNull Tag newTag) {
        int index = findIndexByEpc(newTag.getEpc());

        if (index >= 0) {
            Tag existingTag = tagList.get(index);

            existingTag.setRssi(newTag.getRssi());
            existingTag.incrementReadCount();

            notifyItemChanged(index);

        } else {
            newTag.setReadCount(1);
            tagList.add(newTag);
            notifyItemInserted(tagList.size() - 1);
        }
    }

    private int findIndexByEpc(String epc) {
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getEpc().equals(epc)) {
                return i;
            }
        }
        return -1;
    }

    public void submitList(List<Tag> newList) {
        tagList.clear();
        if (newList != null) {
            tagList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public void clearList() {
        tagList.clear();
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