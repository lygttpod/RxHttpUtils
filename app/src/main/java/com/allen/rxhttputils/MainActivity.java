package com.allen.rxhttputils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.RxHttpUtils;
import com.allen.library.download.DownloadObserver;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.allen.library.observer.StringObserver;
import com.allen.rxhttputils.api.ApiService;
import com.allen.rxhttputils.bean.BookBean;
import com.allen.rxhttputils.bean.Top250Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.allen.library.utils.ToastUtils.showToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button single_http_default, single_http, single_string_http, global_http, multiple_http, download_http, upload_http;
    private Dialog loading_dialog;
    private TextView responseTv;

    private List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        setContentView(R.layout.activity_main);
        responseTv = (TextView) findViewById(R.id.response_tv);
        single_http_default = (Button) findViewById(R.id.single_http_default);
        single_http_default.setOnClickListener(this);
        single_http = (Button) findViewById(R.id.single_http);
        single_http.setOnClickListener(this);
        single_string_http = (Button) findViewById(R.id.single_string_http);
        single_string_http.setOnClickListener(this);
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
        responseTv.setText("");

        Map<String, Object> headerMaps = new TreeMap<>();

        headerMaps.put("header1", "header1");
        headerMaps.put("header2", "header2");

        switch (v.getId()) {
            case R.id.single_http_default:
                RxHttpUtils
                        .getSInstance()
                        .baseUrl("https://api.douban.com/")
                        .createSApi(ApiService.class)
                        .getTop250(5)
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>(loading_dialog) {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(Top250Bean top250Bean) {
                                StringBuilder sb = new StringBuilder();
                                for (Top250Bean.SubjectsBean s : top250Bean.getSubjects()) {
                                    sb.append(s.getTitle() + "\n");
                                }
                                responseTv.setText(sb.toString());
                                //请求成功
                                showToast(sb.toString());
                            }
                        });
                break;

            case R.id.single_http:

                RxHttpUtils
                        //单个请求的实例getSInstance(getSingleInstance的缩写)
                        .getSInstance()
                        //单个请求的baseUrl
                        .baseUrl("https://api.douban.com/")
                        //单个请求的header
                        .addHeaders(headerMaps)
                        //根据需求自行配置
                        .addCallAdapterFactory(null)
                        .addConverterFactory(null)
                        //单个请求是否开启缓存
                        .cache(true)
                        //单个请求的缓存路径及缓存大小，不设置的话有默认值
                        .cachePath("cachePath", 1024 * 1024 * 100)
                        //单个请求的ssl证书认证,支持三种认证方式
                        //信任所有证书,不安全有风险
                        .sslSocketFactory()
                        //使用预埋证书，校验服务端证书（自签名证书）
                        //.setSslSocketFactory(getAssets().open("your.cer"))
                        //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                        //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                        //单个请求是否持久化cookie
                        .saveCookie(true)
                        //单个请求超时
                        .writeTimeout(10)
                        .readTimeout(10)
                        .connectTimeout(10)
                        //单个请求是否开启log日志
                        .log(true)
                        //区分全局变量的请求createSApi(createSingleApi的缩写)
                        .createSApi(ApiService.class)
                        //自己ApiService中的方法名
                        .getTop250(10)
                        //内部配置了线程切换相关策略
                        //如果需要请求loading需要传入自己的loading_dialog
                        //使用loading的话需要在CommonObserver<XXX>(loading_dialog)中也传去
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>(loading_dialog) {
                            @Override
                            protected void onError(String errorMsg) {
                                //错误处理
                            }

                            @Override
                            protected void onSuccess(Top250Bean top250Bean) {
                                StringBuilder sb = new StringBuilder();
                                for (Top250Bean.SubjectsBean s : top250Bean.getSubjects()) {
                                    sb.append(s.getTitle() + "\n");
                                }
                                responseTv.setText(sb.toString());
                                //请求成功
                                showToast(sb.toString());
                            }
                        });
                break;
            case R.id.single_string_http:
                RxHttpUtils.getSInstance()
                        .baseUrl("https://api.douban.com/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .createSApi(ApiService.class)
                        .getBookString()
                        .compose(Transformer.<String>switchSchedulers(loading_dialog))
                        .subscribe(new StringObserver(loading_dialog) {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(String data) {
                                showToast(data);
                                responseTv.setText(data);
                            }
                        });

                break;
            case R.id.global_http:
                RxHttpUtils
                        .createApi(ApiService.class)
                        .getBook()
                        .compose(Transformer.<BookBean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<BookBean>(loading_dialog) {

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(BookBean bookBean) {
                                String s = bookBean.getSummary();
                                responseTv.setText(s);
                                showToast(s);
                            }
                        });
                break;

            case R.id.multiple_http:

                RxHttpUtils
                        .createApi(ApiService.class)
                        .getBook()
                        .flatMap(new Function<BookBean, ObservableSource<Top250Bean>>() {
                            @Override
                            public ObservableSource<Top250Bean> apply(@NonNull BookBean bookBean) throws Exception {
                                return RxHttpUtils
                                        .createApi(ApiService.class)
                                        .getTop250(20);
                            }
                        })
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>(loading_dialog) {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(Top250Bean top250Bean) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(top250Bean.getTitle() + "\n");

                                for (Top250Bean.SubjectsBean s : top250Bean.getSubjects()) {
                                    sb.append(s.getTitle() + "\n");
                                }
                                responseTv.setText(sb.toString());
                                //请求成功
                                showToast(sb.toString());
                            }
                        });

                break;
            case R.id.download_http:
                String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
                final String fileName = "alipay.apk";
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
                                    responseTv.setText("下载文件路径：" + filePath);

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
            default:
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxHttpUtils.cancelAllRequest();
    }
}
