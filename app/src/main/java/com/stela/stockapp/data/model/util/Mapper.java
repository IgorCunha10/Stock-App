package com.stela.stockapp.data.model.util;

import com.stela.stockapp.data.model.pojo.ProductTagJoin;
import com.stela.stockapp.data.model.product.ProductDto;
import com.stela.stockapp.data.model.tag.TagDto;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static ProductDto toDto(ProductTagJoin join) {
        return new ProductDto(
                join.getProductId(),
                join.getTagId(),
                join.getProductName(),
                join.getDescription(),
                join.getPrice()

        );
    }

    public static List<ProductDto> toDtoList(List<ProductTagJoin> joins) {
        List<ProductDto> dtos = new ArrayList<>();
        for (ProductTagJoin join : joins) {
            dtos.add(toDto(join));
        }

        return dtos;
    }

}
