package com.stela.stockapp.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stela.stockapp.data.repository.ReaderRepository;
import com.stela.stockapp.data.repository.TagRepository;

public class ReaderViewModelFactory implements ViewModelProvider.Factory {

    private final ReaderRepository readerRepository;
    private final TagRepository tagRepository;

    public ReaderViewModelFactory(ReaderRepository readerRepository, TagRepository tagRepository) {
        this.readerRepository = readerRepository;
        this.tagRepository = tagRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReaderViewModel.class)) {
            return (T) new ReaderViewModel(readerRepository, tagRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}