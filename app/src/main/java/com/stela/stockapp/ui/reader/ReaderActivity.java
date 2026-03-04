package com.stela.stockapp.ui.reader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stela.stockapp.R;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.repository.ReaderRepository;
import com.stela.stockapp.domain.Tag;
import com.stela.stockapp.ui.main.MainViewModel;
import com.stela.stockapp.ui.product.NewProductActivity;
import com.stela.stockapp.ui.product.NewProductAdapter;
import com.stela.stockapp.ui.taginfo.TagInfo;
import com.stela.stockapp.ui.viewmodel.ReaderViewModel;
import com.stela.stockapp.ui.viewmodel.ReaderViewModelFactory;

import java.util.ArrayList;


public class ReaderActivity extends AppCompatActivity {

    private Button btnConnect, btnClear;
    private FloatingActionButton fabScanTag;
    private RecyclerView rvTagList;
    private ReaderAdapter readerAdapter;

    private MainViewModel mainViewModel;

    private ActivityResultLauncher<Intent> addProductLauncher;
    private ReaderViewModel viewModel;
    private ToneGenerator toneGenerator;
    private static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String MODE_SELECT = "MODE_SELECT";
    public static final String EXTRA_TAG = "EXTRA_TAG";
    private boolean isSelectMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reader);

        if (getIntent() != null && MODE_SELECT.equals(getIntent().getStringExtra(EXTRA_MODE))) {
            isSelectMode = true;
        }



        initView();

        ReaderRepository repository = new ReaderRepository(this);
        viewModel = new ViewModelProvider(
                this,
                new ReaderViewModelFactory(repository)
        ).get(ReaderViewModel.class);

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        initActivityResults();
        initRecyclerView();
        initObservers();
        initListeners();

        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        viewModel.getError().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void initObservers() {
        viewModel.getTags().observe(this, tags -> readerAdapter.submitList(tags));

        viewModel.isConnected().observe(this, connected -> {
            btnConnect.setText(connected ? "Connected" : "Connect");
            Toast.makeText(this,
                    connected ? "Reader Connected" : "Connect the Reader",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void initActivityResults() {
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null && data.hasExtra("product")) {
                            Product product =
                                    (Product) data.getSerializableExtra("product");

                            mainViewModel.addProduct(product);
                        }
                    }
                }
        );
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
        readerAdapter = new ReaderAdapter();

        readerAdapter.setOnTagActionListener(new ReaderAdapter.OnTagActionListener() {

            @Override
            public void onAddProductClick(Tag tag) {

                if (isSelectMode) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_TAG, tag.getEpc());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Intent intent = new Intent(ReaderActivity.this,
                            NewProductActivity.class);
                    intent.putExtra("epc", tag.getEpc());
                    addProductLauncher.launch(intent);
                }
            }


            @Override
            public void onInfoClick(Tag tag) {
                Intent intent = new Intent(ReaderActivity.this, TagInfo.class);
                startActivity(intent);
            }

        });

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