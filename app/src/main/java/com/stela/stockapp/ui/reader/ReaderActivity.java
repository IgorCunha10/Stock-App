package com.stela.stockapp.ui.reader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grotg.hpp.otglibrary.exception.ReaderException;
import com.grotg.hpp.otglibrary.otgreader.OtgReader;
import com.stela.stockapp.R;
import com.stela.stockapp.domain.Tag;

import java.util.ArrayList;

public class ReaderActivity extends AppCompatActivity {

    private OtgReader otgReader;
    private Button btnConnect, btnClear;
    private FloatingActionButton fabScanTag;
    private RecyclerView rvTagList;
    private ReaderAdapter readerAdapter;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reader);

        initReader();
        initView();
        initRecyclerView();
        initListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        btnConnect.setOnClickListener(v -> {
            otgReader.connect((success, message) -> {
                if (success) {
                    Toast.makeText(this, "Leitora Conectada", Toast.LENGTH_SHORT).show();
                    isConnected = true;
                    btnConnect.setText("Conectado");
                } else {
                    Toast.makeText(this, "Falha em conectar leitora", Toast.LENGTH_SHORT).show();
                    isConnected = false;
                    btnConnect.setText("Conectar");
                }
            });
        });

        fabScanTag.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    try {
                        otgReader.ScanTags();
                    } catch (ReaderException e) {
                        Log.e("RFID", "Erro ao iniciar scan", e);
                    }
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    try {
                        otgReader.StopScan();
                    } catch (ReaderException e) {
                        Log.e("RFID", "Erro ao parar scan", e);
                    }

                    v.performClick();
                    return true;
            }
            return false;
        });

        btnClear.setOnClickListener(v -> {
            readerAdapter.clearList();
        });
    }

    private void initView() {
        btnConnect = findViewById(R.id.btnConnect);
        btnClear = findViewById(R.id.btnClear);
        fabScanTag = findViewById(R.id.fabScanTag);
        rvTagList = findViewById(R.id.rvTagList);

        rvTagList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initReader() {
        otgReader = new OtgReader(this);

        otgReader.setreadTagDataCallback(tag ->
                readerAdapter.addTag(new Tag(tag))
        );

    }

    private void initRecyclerView() {
        readerAdapter = new ReaderAdapter(new ArrayList<>());
        rvTagList.setAdapter(readerAdapter);
    }


}