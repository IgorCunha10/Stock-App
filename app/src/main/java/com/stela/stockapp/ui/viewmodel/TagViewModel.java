package com.stela.stockapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.repository.TagRepository;

public class TagViewModel extends AndroidViewModel {

    private final TagRepository tagRepository;

    public TagViewModel(@NonNull Application application) {
        super(application);

        AppDataBase db = AppDataBase.getInstance(application);
        tagRepository = new TagRepository(db);

    }

}
