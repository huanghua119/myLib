package com.yuyi.demo.ui.download;

public class DownloadData {

    public static final int STATUS_PROGRESS = 1;
    public static final int STATUS_PAUSED = 2;
    public static final int STATUS_SUCCESS = 3;
    public static final int STATUS_ERROR = 4;

    private int progress;
    private int status;

    public DownloadData(int progress, int status) {
        this.progress = progress;
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }


    public int getStatus() {
        return status;
    }

}
