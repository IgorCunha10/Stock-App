package com.stela.stockapp.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stela.stockapp.R;
import com.stela.stockapp.data.javadb.AppDataBase;
import com.stela.stockapp.data.javadb.HistoryDao;
import com.stela.stockapp.view.adapter.HistoryAdapter;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private HistoryDao historyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        initInsets();

        recyclerView = findViewById(R.id.recyclerHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyAdapter = new HistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(historyAdapter);

        historyDao = AppDataBase.getInstance(this).historyDao();

        historyDao.getAllHistory().observe(this, historyList -> {
            historyAdapter.updateList(historyList);
        });

    }

    public void initInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}