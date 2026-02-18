package com.stela.stockapp.ui.movimentation;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stela.stockapp.R;
import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.local.HistoryDao;

import java.util.ArrayList;

public class MovimentationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovimentationAdapter movimentationAdapter;
    private MovimentationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        viewModel = new ViewModelProvider(this)
                .get(MovimentationViewModel.class);

        initInsets();
        initView();
        initRecyclerView();
        observeData();

    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecyclerView() {
        movimentationAdapter = new MovimentationAdapter(new ArrayList<>());
        recyclerView.setAdapter(movimentationAdapter);
    }


    public void initInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void observeData(){
        viewModel.getAllHistory().observe(this, historyList -> {
            movimentationAdapter.updateList(historyList);

        });
    }


}