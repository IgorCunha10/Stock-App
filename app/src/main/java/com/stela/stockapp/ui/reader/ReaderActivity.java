package com.stela.stockapp.ui.reader;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grotg.hpp.otglibrary.exception.ReaderException;
import com.grotg.hpp.otglibrary.otgreader.OtgReader;
import com.stela.stockapp.R;
import com.stela.stockapp.data.repository.ReaderRepository;
import com.stela.stockapp.domain.Tag;
import com.stela.stockapp.ui.viewmodel.ReaderViewModel;
import com.stela.stockapp.ui.viewmodel.ReaderViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReaderActivity extends AppCompatActivity {

    private Button btnConnect, btnClear;
    private FloatingActionButton fabScanTag;
    private RecyclerView rvTagList;
    private ReaderAdapter readerAdapter;

    private ReaderViewModel viewModel;
    private ToneGenerator toneGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reader);

        initView();
        initRecyclerView();

        ReaderRepository repository = new ReaderRepository(this);
        viewModel = new ViewModelProvider(
                this,
                new ReaderViewModelFactory(repository)
        ).get(ReaderViewModel.class);

        initObservers();
        initListeners();

        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    private void initObservers() {
        viewModel.getTags().observe(this, tags -> readerAdapter.submitList(tags));

        viewModel.isConnected().observe(this, connected -> {
            btnConnect.setText(connected ? "Conectado" : "Conectar");
            Toast.makeText(this,
                    connected ? "Leitora Conectada" : "Falha ao conectar",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        btnConnect.setOnClickListener(v -> viewModel.connect());

        fabScanTag.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    playStartBeep();
                    viewModel.startScan();
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    playStopBeep();
                    viewModel.stopScan();
                    v.performClick();
                    return true;
            }
            return false;
        });

        btnClear.setOnClickListener(v -> viewModel.clearTags());
    }

    private void initView() {
        btnConnect = findViewById(R.id.btnConnect);
        btnClear = findViewById(R.id.btnClear);
        fabScanTag = findViewById(R.id.fabScanTag);
        rvTagList = findViewById(R.id.rvTagList);
    }

    private void initRecyclerView() {
        readerAdapter = new ReaderAdapter(new ArrayList<>());
        rvTagList.setLayoutManager(new LinearLayoutManager(this));
        rvTagList.setAdapter(readerAdapter);
    }

    private void playStartBeep() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 120);
    }

    private void playStopBeep() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 120);
    }
}