package com.stela.stockapp.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.stela.stockapp.R;
import com.stela.stockapp.data.model.product.ProductDto;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductDto> products = new ArrayList<>();
    private OnItemActionListener listener;

    public ProductAdapter(List<ProductDto> products) {

        this.products = products != null ? products : new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDto product = products.get(position);

        holder.productId.setText(String.valueOf(product.getId()));
        holder.productName.setText(product.getProductName());
        holder.productDescription.setText(product.getProductDescription());
        holder.productPrice.setText(String.format("R$ %.2f", product.getProductPrice()));
        if (product.getTagNumber() != null) {
            holder.productTag.setText(product.getTagNumber());
        } else {
            holder.productTag.setText("Produto sem Tag");
        }

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName,
                productDescription,
                productTag,
                productId,
                productPrice;
        ImageButton editBtn, deleteBtn;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.prName);
            productId = itemView.findViewById(R.id.prId);
            productDescription = itemView.findViewById(R.id.prDescription);
            productPrice = itemView.findViewById(R.id.prPrice);
            productTag = itemView.findViewById(R.id.prTag);
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

        //        this.products = products;

        DiffUtil.Callback diffCallback =
                new ProductDiffCallback(this.products, products);

        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(diffCallback);

        this.products.clear();
        this.products.addAll(products);

        diffResult.dispatchUpdatesTo(this);

    }

    public interface OnItemActionListener {
        void onEditClick(ProductDto product);

        void onDeleteClick(ProductDto product);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }
}