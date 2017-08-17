package com.example.ftkj.wdpointdownload;

import android.webkit.DownloadListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by FTKJ on 2017/8/16.
 */

public class DownInterceptor implements Interceptor {
    private DownloadProgressListener mDownloadListener;
    public DownInterceptor(DownloadProgressListener downloadListener){
        mDownloadListener =downloadListener;

    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response =  warpResponse(chain.proceed(chain.request()));

        return response;
    }

    private Response warpResponse(Response response) {
       return response.newBuilder().body(new DownLoadResponseBody(response.body(),mDownloadListener)).build();


    }
}
