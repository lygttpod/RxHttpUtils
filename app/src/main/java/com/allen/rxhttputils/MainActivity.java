package com.allen.rxhttputils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.download.DownloadObserver;
import com.allen.library.http.CommonObserver;
import com.allen.library.http.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.allen.rxhttputils.api.ApiService;
import com.allen.rxhttputils.bean.FreeHeroBean;
import com.allen.rxhttputils.bean.HeroListBean;
import com.allen.rxhttputils.bean.Octocat;
import com.allen.rxhttputils.bean.ServerListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button single_http, global_http, multiple_http, download_http, upload_http;
    private Dialog loading_dialog;
    private TextView responseTv;

    private List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        setContentView(R.layout.activity_main);
        responseTv = (TextView) findViewById(R.id.response_tv);
        single_http = (Button) findViewById(R.id.single_http);
        single_http.setOnClickListener(this);
        global_http = (Button) findViewById(R.id.global_http);
        global_http.setOnClickListener(this);
        multiple_http = (Button) findViewById(R.id.multiple_http);
        multiple_http.setOnClickListener(this);
        download_http = (Button) findViewById(R.id.download_http);
        download_http.setOnClickListener(this);
        upload_http = (Button) findViewById(R.id.upload_http);
        upload_http.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> headerMaps = new TreeMap<>();

        headerMaps.put("header1", "header1");
        headerMaps.put("header2", "header2");

        switch (v.getId()) {
            case R.id.single_http:

                RxHttpUtils
                        .getSInstance()
                        .baseUrl("https://api.github.com/")
                        .log(true)
                        .addHeaders(headerMaps)
                        .createSApi(ApiService.class)
                        .getOctocat()
                        .compose(Transformer.<Octocat>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Octocat>(loading_dialog) {
                            @Override
                            protected void getDisposable(Disposable d) {
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(Octocat octocat) {
                                String s = octocat.toString();
                                responseTv.setText(s);
                                showToast(s);
                            }
                        });
                break;
            case R.id.global_http:
                RxHttpUtils
                        .createApi(ApiService.class)
                        .getFreeHero()
                        .compose(Transformer.<FreeHeroBean>switchSchedulers())
                        .subscribe(new CommonObserver<FreeHeroBean>() {
                            @Override
                            protected void getDisposable(Disposable d) {
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(FreeHeroBean freeHeroBean) {
                                String s = freeHeroBean.getData().toString();
                                responseTv.setText(s);
                                showToast(s);
                            }
                        });

                break;

            case R.id.multiple_http:

                RxHttpUtils
                        .createApi(ApiService.class)
                        .getFreeHero()
                        .flatMap(new Function<FreeHeroBean, ObservableSource<HeroListBean>>() {
                            @Override
                            public ObservableSource<HeroListBean> apply(@NonNull FreeHeroBean freeHeroBean) throws Exception {
                                String limit = freeHeroBean.getData().get(0).getId();
                                return RxHttpUtils
                                        .createApi(ApiService.class)
                                        .getHeroList(limit);
                            }
                        })
                        .compose(Transformer.<HeroListBean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<HeroListBean>(loading_dialog) {
                            @Override
                            protected void getDisposable(Disposable d) {
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(HeroListBean heroListBean) {
                                String s = heroListBean.getData().toString();
                                responseTv.setText(s);
                                showToast(s);
                            }
                        });

                break;
            case R.id.download_http:
                String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
                String fileName = "alipay.apk";
                RxHttpUtils
                        .downloadFile(url)
                        .subscribe(new DownloadObserver(fileName) {
                            @Override
                            protected void getDisposable(Disposable d) {
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {
                                download_http.setEnabled(true);
                            }

                            @Override
                            protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                                download_http.setText("下载中：" + progress + "%");
                                if (done) {
                                    download_http.setEnabled(true);
                                    download_http.setText("文件下载");
                                }

                            }
                        });
                download_http.setEnabled(false);
                break;
            case R.id.upload_http:

//                RxHttpUtils
//                        .uploadImg("your_upload_url", "your_filePath")
//                        .subscribe(new Consumer<ResponseBody>() {
//                            @Override
//                            public void accept(@NonNull ResponseBody responseBody) throws Exception {
//                                Log.e("allen", "上传完毕: " + responseBody.string());
//                            }
//                        });

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
            disposables.clear();
        }
    }
}
