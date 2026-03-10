package com.stela.stockapp.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stela.stockapp.R;
import com.stela.stockapp.data.model.product.ProductDto;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;

    private List<ProductDto> products;
    private OnItemActionListener listener;

    public ProductAdapter(Context context, List<ProductDto> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(context)
                .inflate(R.layout.item_view, parent, false);
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDto product = products.get(position);

        holder.product_Id.setText(String.valueOf(product.getId()));
        holder.product_Name.setText(product.getProductName());
        holder.product_Description.setText(product.getProductDescription());
        holder.product_Price.setText(String.format("R$ %.2f", product.getProductPrice()));
        if (product.getTagNumber() != null) {
            holder.product_Tag.setText(product.getTagNumber());
        } else {
            holder.product_Tag.setText("Produto sem Tag");
        }

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView product_Name,
                 product_Description,
                 product_Tag,
                 product_Id,
                 product_Price;
        ImageButton editBtn, deleteBtn;

        public ProductViewHolder(View itemView) {
            super(itemView);
            product_Name = itemView.findViewById(R.id.prName);
            product_Id = itemView.findViewById(R.id.prId);
            product_Description = itemView.findViewById(R.id.prDescription);
            product_Price = itemView.findViewById(R.id.prPrice);
            product_Tag = itemView.findViewById(R.id.prTag);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(ProductDto product) {
            editBtn.setOnClickListener(view -> {
                if (listener != null) listener.onEditClick(product);
            });
            deleteBtn.setOnClickListener(view -> {
                if (listener != null) listener.onDeleteClick(product);
            });
        }
    }

    public void setProductList(List<ProductDto> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public interface OnItemActionListener {
        void onEditClick(ProductDto product);
        void onDeleteClick(ProductDto product);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }
}