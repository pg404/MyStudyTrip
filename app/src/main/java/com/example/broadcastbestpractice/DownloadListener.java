package com.example.broadcastbestpractice;

/**
 * Created by lenovo on 2017-11-16 0016.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
