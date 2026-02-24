package com.stela.stockapp.data;

public interface OnTagReadListener {
    void onTagRead(String epc);
    void onError(Throwable error);

}
