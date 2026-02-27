package com.stela.stockapp.data.repository;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.grotg.hpp.otglibrary.exception.ReaderException;
import com.grotg.hpp.otglibrary.otgreader.OtgReader;
import com.grotg.hpp.otglibrary.param.EpcBean;

public class ReaderRepository {

    private final OtgReader otgReader;

    public interface TagCallback {
        void onTagRead(EpcBean epcBean);
    }

    public ReaderRepository(Activity activity) {
        otgReader = new OtgReader(activity);
    }

    public void connect(OtgReader.ConnectCallback callback) {
        otgReader.connect(callback);
    }

    public void startScan() {
        try {
            otgReader.ScanTags();
        } catch (ReaderException e) {
            Log.e("RFID", "Erro ao iniciar scan", e);
        }
    }

    public void stopScan() {
        try {
            otgReader.StopScan();
        } catch (ReaderException e) {
            Log.e("RFID", "Erro ao parar scan", e);
        }
    }

    public void setOnTagRead(TagCallback callback) {
        otgReader.setreadTagDataCallback(callback::onTagRead);
    }

    public void release() {
        try {
            otgReader.StopScan();
        } catch (ReaderException ignored) {
        }
    }
}