package com.example.ftkj.wdpointdownload;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.ForwardingSource;
import okio.Source;

/**
 * Created by FTKJ on 2017/8/16.
 */

public class WrapSource extends ForwardingSource {
    private DownloadProgressListener mDownloadProgressListener;
    private ResponseBody mResponseBody;
    private long totalBytesRead = 0L;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public WrapSource(Source delegate, ResponseBody responseBody, DownloadProgressListener downloadProgressListener) {
        super(delegate);
        mDownloadProgressListener = downloadProgressListener;
        mResponseBody = responseBody;
    }

    @Override
    public long read(Buffer sink, long byteCount) throws IOException {
        long bytesRead = super.read(sink, byteCount);
        if (bytesRead!=-1){
            totalBytesRead+=bytesRead;
        }else {
            totalBytesRead+=0;
            mDownloadProgressListener.complete();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDownloadProgressListener.update(totalBytesRead,mResponseBody.contentLength());
            }
        });
        return bytesRead;
    }
}
