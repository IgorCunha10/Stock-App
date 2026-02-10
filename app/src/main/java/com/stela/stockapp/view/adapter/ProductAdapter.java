package com.stela.stockapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stela.stockapp.R;
import com.stela.stockapp.model.Product;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
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
        Product product = products.get(position);

        holder.Product_Name.setText(product.getProductName());
        holder.Product_Id.setText(product.getProductId());
        holder.Product_Description.setText(product.getProductDescription());
        holder.Product_Quantity.setText(
                String.valueOf(product.getProductQuantity())
        );
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView Product_Name, Product_Description, Product_Quantity, Product_Id;


        public ProductViewHolder(View itemView) {
            super(itemView);
            Product_Name = itemView.findViewById(R.id.prName);
            Product_Id = itemView.findViewById(R.id.prId);
            Product_Description = itemView.findViewById(R.id.prDescription);
            Product_Quantity = itemView.findViewById(R.id.prQuantity);


        }

    }





}
