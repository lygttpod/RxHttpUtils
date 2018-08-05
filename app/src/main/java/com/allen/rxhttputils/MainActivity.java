package com.allen.rxhttputils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.RxHttpUtils;
import com.allen.library.cookie.store.MemoryCookieStore;
import com.allen.library.cookie.store.SPCookieStore;
import com.allen.library.download.DownloadObserver;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.allen.library.observer.StringObserver;
import com.allen.rxhttputils.api.ApiService;
import com.allen.rxhttputils.bean.BookBean;
import com.allen.rxhttputils.bean.Top250Bean;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.allen.library.utils.ToastUtils.showToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button download_http;
    private Dialog loading_dialog;
    private TextView responseTv;

    private List<Disposable> disposables = new ArrayList<>();

    private int REQUEST_CODE_CHOOSE = 1;
    private String uploadUrl = "http://server.jeasonlzy.com/OkHttpUtils/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        setContentView(R.layout.activity_main);
        responseTv = (TextView) findViewById(R.id.response_tv);
        findViewById(R.id.single_http_default).setOnClickListener(this);
        findViewById(R.id.single_http).setOnClickListener(this);
        findViewById(R.id.single_string_http).setOnClickListener(this);
        findViewById(R.id.global_http).setOnClickListener(this);
        findViewById(R.id.multiple_http).setOnClickListener(this);
        download_http = (Button) findViewById(R.id.download_http);
        findViewById(R.id.download_http).setOnClickListener(this);
        findViewById(R.id.upload_http).setOnClickListener(this);
        findViewById(R.id.upload_imgs).setOnClickListener(this);
        findViewById(R.id.global_string_http).setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                        .cookieType(new MemoryCookieStore())
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
                            //默认false
//                            @Override
//                            public boolean isHideToast() {
//                                return true;
//                            }

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
                            //默认false   是否隐藏onError的提示
                            @Override
                            protected boolean isHideToast() {
                                return false;
                            }

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

                            //默认false   隐藏onError的提示
                            @Override
                            protected boolean isHideToast() {
                                return true;
                            }

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
            case R.id.global_string_http:
                RxHttpUtils
                        .createApi(ApiService.class)
                        .getBookString()
                        .compose(Transformer.<String>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<String>(loading_dialog) {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(String s) {
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

                RxPermissions permissions = new RxPermissions(this);
                permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    // All requested permissions are granted
                                    selectPhoto(1);
                                } else {
                                    // At least one permission is denied
                                    showToast("请授权");
                                }
                            }
                        });
                break;
            case R.id.upload_imgs:
                RxPermissions permissions1 = new RxPermissions(this);
                permissions1.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    // All requested permissions are granted
                                    selectPhoto(9);
                                } else {
                                    // At least one permission is denied
                                    showToast("请授权");
                                }
                            }
                        });
                break;
            default:
        }
    }

    /**
     * 上传单张图片
     *
     * @param uploadUrl  地址
     * @param uploadPath 文件路径
     */
    private void uploadImg(String uploadUrl, String uploadPath) {

        RxHttpUtils.uploadImg(uploadUrl, uploadPath)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>(loading_dialog) {
                    @Override
                    protected void onError(String errorMsg) {
                        Log.e("allen", "上传失败: " + errorMsg);
                        showToast(errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String msg = responseBody.string();
                            showToast(msg);
                            Log.e("allen", "上传完毕: " + msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 一次上传多张图片
     *
     * @param uploadPaths 图片路径
     */
    private void uploadImgs(List<String> uploadPaths) {

        RxHttpUtils.uploadImgs("http://t.xinhuo.com/index.php/Api/Pic/uploadPic", uploadPaths)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>(loading_dialog) {
                    @Override
                    protected void onError(String errorMsg) {
                        Log.e("allen", "上传失败: " + errorMsg);
                        showToast(errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String msg = responseBody.string();
                            Log.e("allen", "上传完毕: " + msg);
                            showToast(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 选择图片
     */
    private void selectPhoto(int maxSelectable) {
        Matisse.from(MainActivity.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(maxSelectable)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    private List<Uri> mSelected;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            final List<String> paths = new ArrayList<>();
            Log.d("Matisse", "mSelected: " + mSelected);
            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            for (int i = 0; i < mSelected.size(); i++) {
                Tiny.getInstance().source(mSelected.get(i)).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        paths.add(outfile);
                        if (paths.size() == mSelected.size()) {
                            uploadImgs(paths);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxHttpUtils.clearAllCompositeDisposable();
    }
}
