package com.example.ftkj.wdpointdownload;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements DownloadProgressListener {


    private HttpServices mHttpServices;
    private DownInfo mInfo;

    private ProgressBar mProgressBar;

    public static long previousByte= 0;
    public static long tempbyte = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progressbar);
        mProgressBar.setMax(100);
        mInfo = new DownInfo();
        initInfoData();
        mHttpServices = HttpServices.getNewInstance();

    }

    private void initInfoData() {
        mInfo.setBaseUrl("http://android.xzstatic.com/");
        mInfo.setUrl("http://dldir1.qq.com/weixin/android/weixin6330android920.apk");
        mInfo.setSavePath(FileUtils.getDiskCacheDir(this, "WeChat.apk").getAbsolutePath());
    }

    public void btnDown(View view) {
        mHttpServices.getDownload(new DownInterceptor(this), mInfo,this);
    }




    @Override
    public void update(long read, long count) {
        tempbyte = read;
        int procent = (int) ((read)* 1.0 / count* 100);
        mProgressBar.setProgress(procent);
        Log.d("AAA", "update: "+read + " procent "+procent);




    }

    @Override
    public void complete() {
        Log.d("BBB", "complete: "+"????");
        tempbyte = 0;
        previousByte = 0;

    }

    public void PauseDown(View view) {
        Log.d("AAA", "PauseDown: "+"------------------"+"暂停");
        previousByte = tempbyte+previousByte;
        mHttpServices.pause();

    }




    private void writeApp(ResponseBody responseBody) {
//        try {
//            InputStream inputStream = responseBody.byteStream();
//            FileOutputStream fileOutputStream = new FileOutputStream(FileUtils.getDiskCacheDir(this, "WeChat.apk"));
//            int count;
//            byte buf[] = new byte[1024 * 8];
//            while ((count = inputStream.read(buf)) != -1) {
//                fileOutputStream.write(buf, 0, count);
//                fileOutputStream.flush();
//            }
//            inputStream.close();
//            fileOutputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
