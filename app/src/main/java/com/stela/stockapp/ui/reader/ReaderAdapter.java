package com.stela.stockapp.ui.reader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stela.stockapp.R;
import com.stela.stockapp.domain.Tag;

public class ReaderAdapter extends ListAdapter<Tag, ReaderAdapter.ReaderViewHolder> {

    public interface OnTagActionListener {
        void onAddProductClick(Tag tag);
        void onInfoClick(Tag tag);
    }

    private OnTagActionListener listener;

    public void setOnTagActionListener(OnTagActionListener listener) {
        this.listener = listener;
    }

    public ReaderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Tag> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Tag>() {
                @Override
                public boolean areItemsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
                    return oldItem.getEpc().equals(newItem.getEpc());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
                    return oldItem.getReadCount() == newItem.getReadCount()
                            && oldItem.getRssi() == newItem.getRssi();
                }
            };

    @NonNull
    @Override
    public ReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reader_view, parent, false);
        return new ReaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReaderViewHolder holder, int position) {
        Tag tag = getItem(position);
        holder.bind(tag, listener);
    }

    static class ReaderViewHolder extends RecyclerView.ViewHolder {

        TextView tagId, signalForce, tagNumber;
        FloatingActionButton fabNewTag, fabTagInfo;

        public ReaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tagId = itemView.findViewById(R.id.tagId);
            signalForce = itemView.findViewById(R.id.signalForce);
            tagNumber = itemView.findViewById(R.id.tagNumber);

            fabNewTag = itemView.findViewById(R.id.fabNewTag);
            fabTagInfo = itemView.findViewById(R.id.fabTagInfo);
        }

        void bind(Tag tag, OnTagActionListener listener) {
            tagId.setText(tag.getEpc());
            signalForce.setText(String.valueOf(tag.getRssi()));
            tagNumber.setText(String.valueOf(tag.getReadCount()));

            fabNewTag.setOnClickListener(v -> {
                if (listener != null) listener.onAddProductClick(tag);
            });

            fabTagInfo.setOnClickListener(v -> {
                if (listener != null) listener.onInfoClick(tag);
            });
        }
    }
}