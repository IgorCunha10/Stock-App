package com.stela.stockapp.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stela.stockapp.data.repository.ProductsRepository;
import com.stela.stockapp.domain.ReaderManager;
import com.stela.stockapp.data.repository.TagRepository;

public class ReaderViewModelFactory implements ViewModelProvider.Factory {

    private final ReaderManager readerManager;
    private final TagRepository tagRepository;
    private final ProductsRepository productsRepository;


    public ReaderViewModelFactory(ReaderManager readerManager, TagRepository tagRepository,
                                  ProductsRepository productsRepository) {
        this.readerManager = readerManager;
        this.tagRepository = tagRepository;
        this.productsRepository = productsRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReaderViewModel.class)) {
            return (T) new ReaderViewModel(readerManager, tagRepository, productsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}