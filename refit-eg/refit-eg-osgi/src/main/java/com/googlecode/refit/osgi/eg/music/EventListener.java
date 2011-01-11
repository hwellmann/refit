package com.googlecode.refit.osgi.eg.music;

public interface EventListener {
    
    void searchComplete();
    void playStarted();
    void playComplete();
    void loadJammed();

}
