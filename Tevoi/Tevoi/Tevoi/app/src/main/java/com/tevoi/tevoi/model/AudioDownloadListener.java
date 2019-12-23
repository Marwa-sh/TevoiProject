package com.tevoi.tevoi.model;

public interface AudioDownloadListener {

    void onVideoDownloaded();
    void onVideoDownloadError(Exception e);

}
