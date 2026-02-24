package com.stela.stockapp.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.stela.stockapp.data.RfidManager;

public class RfidViewModel extends ViewModel {

private final RfidManager rfidManager = RfidManager.getInstance();

public void conectar(){
    rfidManager.connect();
}

public void iniciarLeitura() {
    rfidManager.startReading();
}

public void pararLeitura() {
    rfidManager.stopReading();
}





}
