package com.ebridge.tevoi.model;

public interface AudioDownloadListener {

    void onVideoDownloaded();
    void onVideoDownloadError(Exception e);

}
