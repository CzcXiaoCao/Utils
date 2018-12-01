package com.example.myutils.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.czc.httplibrary.OkHttpClientManager;
import com.czc.httplibrary.download.DownLoadResultCallBack;
import com.example.myutils.R;

import java.io.File;

import okhttp3.Request;

public class HttpActivity extends AppCompatActivity {
    private final String TAG = getClass().getName();
    String url = "http://yixin.dl.126.net/update/installer/yixin.apk";
    String rootFilePath = "/storage/emulated/0/";
    String fileName = "yixin.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClientManager.downloadAsyn(url, rootFilePath, new DownLoadResultCallBack<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onProgress(long total, long now, int progress) {
                        Log.e(TAG, "onProgress: " + total + "       " + progress + "        " + now);
                    }

                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        Log.e(TAG, "onResponse: " + response);
                    }
                });
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClientManager.cancelTag(url);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long[] total = {0};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        total[0] = OkHttpClientManager.getContentLength(url);
                        String path = rootFilePath + fileName;
                        File file = new File(path);
                        long nowLength = file.length();
                        Log.e(TAG, "onClick: " + total[0] + "  " + nowLength);
                        OkHttpClientManager.downloadAsyn(url, rootFilePath, nowLength, total[0], new DownLoadResultCallBack<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                super.onError(request, e);
                                Log.e(TAG, "onError: " + e.toString());
                            }

                            @Override
                            public void onProgress(long total, long now, int progress) {
                                Log.e(TAG, "onProgress: " + total + "       " + progress + "        " + now);
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.e(TAG, "onResponse: " + response);
                            }
                        });
                    }
                }).start();

            }
        });

//        OkHttpClientManager.uploadAsyn("",new File(""),new UpLoadResultCallBack(){
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//            }
//
//            @Override
//            public void onResponse(Object response) {
//                super.onResponse(response);
//            }
//
//            @Override
//            public void onProgress(long total, long now, int progress) {
//
//            }
//        });
    }
}
