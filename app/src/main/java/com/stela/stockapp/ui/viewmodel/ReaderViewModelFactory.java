package com.stela.stockapp.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stela.stockapp.data.repository.ReaderRepository;

public class ReaderViewModelFactory implements ViewModelProvider.Factory {

    private final ReaderRepository repository;

    public ReaderViewModelFactory(ReaderRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReaderViewModel(repository);
    }
}
