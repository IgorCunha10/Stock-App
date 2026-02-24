package com.stela.stockapp.data;

public interface RfidReader {

    void connect();
    void startReading();
    void stopReading();
    void disconnect();


}