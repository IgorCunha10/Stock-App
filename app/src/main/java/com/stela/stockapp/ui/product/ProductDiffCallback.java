package com.stela.stockapp.ui.product;

import androidx.recyclerview.widget.DiffUtil;

import com.stela.stockapp.data.model.product.ProductDto;

import java.util.List;

public class ProductDiffCallback extends DiffUtil.Callback {

    private final List<ProductDto> oldList;
    private final List<ProductDto> newList;

    public ProductDiffCallback(List<ProductDto> oldList, List<ProductDto> newList) {

        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();

    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId()
                == (newList.get(newItemPosition).getId());

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId()
                == (newList.get(newItemPosition).getId());
    }

}
