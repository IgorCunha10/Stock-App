package com.stela.stockapp.data;

public class RfidManager {

    private static RfidManager instance;
    private RfidReader reader;

    private RfidManager(RfidReader reader) {
        this.reader = reader;
    }

    public static RfidManager getInstance() {

        if (instance = null) {
            instance = new RfidManager();
        }
    return instance;
    }

    public void connect() {
        reader.connect();
    }

    public void startReading() {
        reader.startReading();
    }

    public void stopReading() {
        reader.stopReading();
    }

    public void disconnect() {
        reader.disconnect();
    }

}
