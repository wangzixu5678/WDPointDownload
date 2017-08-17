package com.example.ftkj.wdpointdownload;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by FTKJ on 2017/8/16.
 */

public class HttpServices {
    private DownInfo mdownInfo;
    private Retrofit mDR;
    private static HttpServices mhttpServices = new HttpServices();
    private Subscriber<ResponseBody> mSubscriber;
    private Context mContext;

    private HttpServices() {

    }

    private OkHttpClient initOKhttp(DownInterceptor downInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(mdownInfo.getDEFAULT_TIMEOUT(), TimeUnit.SECONDS)
                .addInterceptor(downInterceptor)
                .build();
    }

    private Retrofit getDownloadRetrifit(DownInterceptor downInterceptor) {
        OkHttpClient httpClient = initOKhttp(downInterceptor);
        mDR = new Retrofit.Builder()
                .baseUrl(mdownInfo.getBaseUrl())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return mDR;
    }

    public static HttpServices getNewInstance() {
        return mhttpServices;
    }

    public void getDownload(DownInterceptor downInterceptor, DownInfo downInfo, Context c) {
        /**
         * 初始化下载信息
         *
         */
        Log.d("AAA", "call: " + MainActivity.previousByte);
        mContext = c;
        mdownInfo = downInfo;
        mSubscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.d("AAA", "onCompleted: " + "结束");

            }

            @Override
            public void onError(Throwable e) {
                Log.d("AAA", "onCompleted: " + "异常" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                //writeApp(responseBody);
            }
        };

        String sss = String.valueOf(MainActivity.previousByte);
        getDownloadRetrifit(downInterceptor)
                .create(NetApi.class)
                .download("bytes=" + sss + "-",mdownInfo.getUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(new RetryWhenNetworkException())
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        writeCaches(responseBody, new File(mdownInfo.getSavePath()), MainActivity.previousByte);
                        return responseBody;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(mSubscriber);

    }

    public void pause() {
        mSubscriber.unsubscribe();
    }


    private void writeApp(ResponseBody responseBody) {
        try {
            InputStream inputStream = responseBody.byteStream();
            FileOutputStream fileOutputStream = new FileOutputStream(FileUtils.getDiskCacheDir(mContext, "WeChat.apk"));
            int count;
            byte buf[] = new byte[1024 * 8];
            while ((count = inputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, count);
                fileOutputStream.flush();
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件
     *
     * @param file
     * @throws IOException
     */
    public void writeCaches(ResponseBody responseBody, File file, long totleRead) {
        try {
            Log.d("AAA", "writeCaches: " + totleRead);
            RandomAccessFile randomAccessFile = null;
            InputStream inputStream = null;
            try {
                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(totleRead);
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, len);
                }
            } catch (IOException e) {
                throw new HttpTimeException(e.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            throw new HttpTimeException(e.getMessage());
        }
    }


}
