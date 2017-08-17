package com.example.ftkj.wdpointdownload;

import android.support.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by FTKJ on 2017/8/16.
 */

public class DownLoadResponseBody extends ResponseBody {
    private ResponseBody mResponseBody;
    private DownloadProgressListener mDownloadProgressListener;
    private BufferedSource mBufferedSource;
    public DownLoadResponseBody(ResponseBody responseBody,DownloadProgressListener downloadProgressListener){
        mResponseBody = responseBody;
        mDownloadProgressListener = downloadProgressListener;
    }
    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource==null){
            mBufferedSource = Okio.buffer(new WrapSource(mResponseBody.source(),mResponseBody,mDownloadProgressListener));
        }
        return mBufferedSource;
    }
}
