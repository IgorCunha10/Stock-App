package com.stela.stockapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stela.stockapp.R;
import com.stela.stockapp.model.history.ProductHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<ProductHistory> historyList;

    public HistoryAdapter(List<ProductHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_view, parent, false);

        return new HistoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ProductHistory history = historyList.get(position);

        holder.productName.setText(history.getProductName());
        holder.productId.setText(String.valueOf(history.getProductId()));
        holder.productDescription.setText(history.getProductDescription());
        holder.productQuantity.setText(String.valueOf(history.getProductQuantity()));
        holder.productPrice.setText(String.format(Locale.getDefault(), "$%.2f", history.getProductPrice()));

        holder.productAction.setText(history.getAction());

        long timestamp = history.getTimeStamp(); // supondo que seja em ms
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.productTimestamp.setText(sdf.format(new Date(timestamp)));

    }

    @Override
    public int getItemCount() {
        return historyList.size();

    }

    public void updateList(List<ProductHistory> newList) {
        historyList = newList;
        notifyDataSetChanged();
    }


    static class HistoryViewHolder extends RecyclerView.ViewHolder {


        TextView productName, productId, productDescription, productQuantity, productPrice, productMovedQntity, productTimestamp, productAction;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.prName);
            productId = itemView.findViewById(R.id.prmvId);
            productDescription = itemView.findViewById(R.id.prmvDescription);
            productQuantity = itemView.findViewById(R.id.prmvQuantity);
            productPrice = itemView.findViewById(R.id.prmvPrice);
            productTimestamp = itemView.findViewById(R.id.prmvDate);
            productAction = itemView.findViewById(R.id.prmvType);


        }
    }

}
