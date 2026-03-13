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
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stela.stockapp.R;
import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.repository.ProductsRepository;
import com.stela.stockapp.domain.ReaderManager;
import com.stela.stockapp.data.repository.TagRepository;
import com.stela.stockapp.domain.model.Tag;
import com.stela.stockapp.ui.main.MainViewModel;
import com.stela.stockapp.ui.product.NewProductActivity;
import com.stela.stockapp.ui.taginfo.TagInfo;
import com.stela.stockapp.ui.viewmodel.ReaderViewModel;
import com.stela.stockapp.ui.viewmodel.ReaderViewModelFactory;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

import java.util.HashSet;
import java.util.Set;

public class ReaderActivity extends AppCompatActivity {

    private Button btnConnect, btnClear;
    private FloatingActionButton fabScanTag;
    private RecyclerView rvTagList;
    private ReaderAdapter readerAdapter;

    private MainViewModel mainViewModel;
    private ActivityResultLauncher<Intent> addProductLauncher;
    private ReaderViewModel readerViewModel;
    private ToneGenerator toneGenerator;

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String MODE_SELECT = "MODE_SELECT";
    public static final String EXTRA_TAG = "EXTRA_TAG";
    private boolean isSelectMode = false;

    private TextToSpeech tts;

    private final Set<String> processedTags = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reader);

        if (getIntent() != null && MODE_SELECT.equals(getIntent().getStringExtra(EXTRA_MODE))) {
            isSelectMode = true;
        }


        ReaderManager readerManager = new ReaderManager(this);
        AppDataBase db = AppDataBase.getInstance(this);
        TagRepository tagRepository = new TagRepository(db);
        ProductsRepository productRepository = new ProductsRepository(db);


        initView();
        initRecyclerView();
        initListeners();
        initActivityResults();
        initViewModel(readerManager, tagRepository, productRepository);

        initObservers();


        readerViewModel.loadProducts();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);


        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        initTts();

    }

    private void initViewModel(ReaderManager readerManager, TagRepository tagRepository, ProductsRepository
            productsRepository) {
        ReaderViewModelFactory factory =
                new ReaderViewModelFactory(readerManager, tagRepository, productsRepository);

        readerViewModel = new ViewModelProvider(this, factory)
                .get(ReaderViewModel.class);
    }

    private void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tagRead");
        }
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
                    intent.putExtra(EXTRA_TAG, tag.getEpc());
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

    private void initActivityResults() {
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("product")) {
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
        btnConnect.setOnClickListener(v -> readerViewModel.connect());

        fabScanTag.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    playStartBeep();
                    readerViewModel.startScan();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    playStopBeep();
                    readerViewModel.stopScan();
                    v.performClick();
                    return true;
            }
            return false;
        });

        btnClear.setOnClickListener(v -> {
            processedTags.clear();
            readerViewModel.clearTags();
        });
    }

    private void initObservers() {

        readerViewModel.isConnected().observe(this, connected -> {
            btnConnect.setText(connected ? "Connected" : "Connect");
            Toast.makeText(this,
                    connected ? "Reader Connected" : "Connect the Reader",
                    Toast.LENGTH_SHORT).show();
        });

        readerViewModel.getProductLiveData().observe(this, product -> {
            if (product != null) {
                String message = "Tag já cadastrada! Produto: " + product.getProductName();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                speak(message);
            }
        });

        readerViewModel.getProductLiveData().observe(this, product -> {
//            if (product != null) {
            Toast.makeText(this,
                    "Tag já cadastrada! Produto: " + product.getProductName(),
                    Toast.LENGTH_LONG).show();
//           }

        });

        readerViewModel.getError().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            speak(message);
        });


        readerViewModel.getError().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );

        readerViewModel.getTags().observe(this, tags -> {
            readerAdapter.submitList(tags);
        });
    }

    private void initTts() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("pt", "BR"));
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS is not suported in this device", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(this, "Failed to initialize TTs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playStartBeep() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 120);
    }

    private void playStopBeep() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 120);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();

        }
        super.onDestroy();
    }
}