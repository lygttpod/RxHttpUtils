package com.allen.rxhttputils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.RxHttpUtils;
import com.allen.library.bean.BaseData;
import com.allen.library.download.DownloadObserver;
import com.allen.library.factory.ApiFactory;
import com.allen.library.interceptor.Transformer;
import com.allen.library.interfaces.ILoadingView;
import com.allen.library.observer.CommonObserver;
import com.allen.library.observer.DataObserver;
import com.allen.library.observer.StringObserver;
import com.allen.rxhttputils.api.ApiHelper;
import com.allen.rxhttputils.api.DouBanApi;
import com.allen.rxhttputils.api.WanAndroidApi;
import com.allen.rxhttputils.bean.BannerBean;
import com.allen.rxhttputils.bean.Top250Bean;
import com.allen.rxhttputils.url.AppUrlConfig;
import com.allen.rxhttputils.widget.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.allen.library.utils.ToastUtils.showToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button download_http, download_cancel_http;
    private ILoadingView loading_dialog;
    private TextView responseTv;

    private int REQUEST_CODE_CHOOSE = 1;
    private String UPLOAD_URL = "http://t.xinhuo.com/index.php/Api/Pic/uploadPic";
    private RxPermissions permissions;
    private int MAX_SELECTABLE = 1;
    private boolean IS_USE_GLOBAL_CONFIG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading_dialog = new LoadingDialog(this);
        setContentView(R.layout.activity_main);
        responseTv = (TextView) findViewById(R.id.response_tv);
        findViewById(R.id.douan_api_http).setOnClickListener(this);
        findViewById(R.id.other_open_api_http).setOnClickListener(this);
        findViewById(R.id.wanandroid_api_http).setOnClickListener(this);
        findViewById(R.id.global_http).setOnClickListener(this);
        findViewById(R.id.multiple_http).setOnClickListener(this);
        download_http = (Button) findViewById(R.id.download_http);
        download_cancel_http = (Button) findViewById(R.id.download_cancel_http);
        findViewById(R.id.download_http).setOnClickListener(this);
        findViewById(R.id.download_cancel_http).setOnClickListener(this);
        findViewById(R.id.upload_one_pic).setOnClickListener(this);
        findViewById(R.id.upload_more_pic).setOnClickListener(this);
        findViewById(R.id.upload_pic_with_global_config).setOnClickListener(this);
        findViewById(R.id.global_string_http).setOnClickListener(this);

        permissions = new RxPermissions(this);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        responseTv.setText("");

        /**
         * 注意事项：两种写法都可以
         * 1、全局配置并且唯一baseUrl的写法如下
         * RxHttpUtils.createApi(XXXApi.class) 等同于 ApiFactory.getInstance().createApi(XXXApi.class)
         * 2、多个baseUrl写法如下
         * RxHttpUtils.createApi("xxxUrlKey", "xxxUrlValue", XXXApi.class) 等同于 ApiFactory.getInstance().createApi("xxxUrlKey", "xxxUrlValue", XXXApi.class)
         * 3、多baseUrl的情况下可以设置不同retrofit配置,写法如下
         * ApiFactory.getInstance(...).setConverterFactory(...).setCallAdapterFactory(...).setOkClient(...).createApi(...)
         */
        switch (v.getId()) {
            case R.id.global_http:
                RxHttpUtils
                        .createApi(WanAndroidApi.class)
                        .getBanner()
                        .compose(Transformer.<BaseData<List<BannerBean>>>switchSchedulers(loading_dialog))
                        .subscribe(new DataObserver<List<BannerBean>>() {
                            //默认false   隐藏onError的提示
                            @Override
                            protected boolean isHideToast() {
                                return false;
                            }

                            //tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
                            //设置一个tag就行就可以取消当前页面所有请求或者某个请求了
                            @Override
                            protected String setTag() {
                                return "tag1";
                            }

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(List<BannerBean> data) {
                                String s = data.get(0).toString();
                                responseTv.setText(s);
                                showToast(s);
                            }
                        });

                break;
            case R.id.global_string_http:
                RxHttpUtils
                        .createApi(WanAndroidApi.class)
                        .getHotSearchStringData()
                        .compose(Transformer.<String>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<String>() {

                            @Override
                            protected String setTag() {
                                return "tag1";
                            }

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
                        .createApi(WanAndroidApi.class)
                        .getBanner()
                        .flatMap(new Function<BaseData<List<BannerBean>>, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(@NonNull BaseData<List<BannerBean>> baseData) throws Exception {
                                return RxHttpUtils
                                        .createApi(WanAndroidApi.class)
                                        .getHotSearchStringData();
                            }
                        })
                        .compose(Transformer.<String>switchSchedulers(loading_dialog))
                        .subscribe(new StringObserver() {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(String data) {
                                responseTv.setText(data);
                                showToast(data);
                            }
                        });

                break;
            case R.id.download_http:
                String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
                final String fileName = "alipay.apk";
                RxHttpUtils
                        .downloadFile(url)
                        //.subscribe(new DownloadObserver(fileName,destFileDir) 其中 destFileDir是自定义下载存储路径
                        .subscribe(new DownloadObserver(fileName) {
                            //可以去下下载
                            @Override
                            protected String setTag() {
                                return "download";
                            }

                            @Override
                            protected void onError(String errorMsg) {
                                download_cancel_http.setEnabled(false);
                                download_http.setEnabled(true);
                                download_http.setText("文件下载");
                                showToast(errorMsg);
                            }

                            @Override
                            protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                                download_cancel_http.setEnabled(true);
                                download_http.setText("下 载中：" + progress + "%");
                                if (done) {
                                    download_http.setEnabled(true);
                                    download_http.setText("文件下载");
                                    responseTv.setText("下载文件路径：" + filePath);
                                    showToast("下载完成：" + filePath);
                                }

                            }
                        });
                download_http.setEnabled(false);
                break;
            case R.id.download_cancel_http:
                RxHttpUtils.cancel("download");
                break;
            case R.id.upload_one_pic:
                MAX_SELECTABLE = 1;
                IS_USE_GLOBAL_CONFIG = false;
                selectPhotoWithPermission(MAX_SELECTABLE);
                break;
            case R.id.upload_more_pic:
                MAX_SELECTABLE = 9;
                IS_USE_GLOBAL_CONFIG = false;
                selectPhotoWithPermission(MAX_SELECTABLE);
                break;
            case R.id.upload_pic_with_global_config:
                MAX_SELECTABLE = 9;
                IS_USE_GLOBAL_CONFIG = true;
                selectPhotoWithPermission(MAX_SELECTABLE);
                break;
            case R.id.douan_api_http:
                ApiHelper.getDouBanApi()
                        .getTop250(5)
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>() {
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
            case R.id.other_open_api_http:
                ApiHelper.getOtherOpenApi()
                        .getJokesRandom()
                        .compose(Transformer.<String>switchSchedulers(loading_dialog))
                        .subscribe(new StringObserver() {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(String data) {
                                responseTv.setText(data);
                                showToast(data);
                            }
                        });
                break;
            case R.id.wanandroid_api_http:
                ApiHelper.getWanAndroidApi()
                        .getBanner()
                        .compose(Transformer.<BaseData<List<BannerBean>>>switchSchedulers(loading_dialog))
                        .subscribe(new DataObserver<List<BannerBean>>() {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(List<BannerBean> data) {
                                responseTv.setText(data.get(0).getTitle());
                                showToast(data.get(0).getTitle());
                            }
                        });
                break;

            default:
        }
    }

    @SuppressLint("CheckResult")
    private void selectPhotoWithPermission(final int maxSelectable) {
        permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // All requested permissions are granted
                            selectPhoto(maxSelectable);
                        } else {
                            // At least one permission is denied
                            showToast("请授权");
                        }
                    }
                });
    }

    /**
     * 使用全局配置上传图片  demo
     *
     * @param filePaths 图片路径
     */
    private void uploadImgWithGlobalConfig(List<String> filePaths) {

        //以下使用的是全局配置
        RxHttpUtils.createApi(DouBanApi.class)
                .uploadFiles(UPLOAD_URL, getMultipartPart("uploaded_file", null, filePaths))
                .compose(Transformer.<String>switchSchedulers(loading_dialog))
                .subscribe(new StringObserver() {

                    @Override
                    protected String setTag() {
                        return "uploadImg";
                    }

                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        showToast(data);
                        Log.e("allen", "上传完毕: " + data);
                    }
                });
    }

    /**
     * 可以根据需求自行修改(这里只是简单demo示例)
     *
     * @param fileName  后台协定的接受图片的name（没特殊要求就可以随便写）
     * @param paramsMap 普通参数 图文混合参数
     * @param filePaths 图片路径
     * @return List<MultipartBody.Part>
     */
    private List<MultipartBody.Part> getMultipartPart(String fileName, Map<String, Object> paramsMap, List<String> filePaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (null != paramsMap) {
            for (String key : paramsMap.keySet()) {
                builder.addFormDataPart(key, (String) paramsMap.get(key));
            }
        }

        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //"fileName"+i 后台接收图片流的参数名
            builder.addFormDataPart(fileName, file.getName(), imageBody);
        }

        List<MultipartBody.Part> parts = builder.build().parts();

        return parts;
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
                .subscribe(new CommonObserver<ResponseBody>() {

                    @Override
                    protected String setTag() {
                        return "uploadImg";
                    }

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

        RxHttpUtils.uploadImages(UPLOAD_URL, uploadPaths)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {

                    @Override
                    protected String setTag() {
                        return "uploadImg";
                    }

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
                            if (IS_USE_GLOBAL_CONFIG) {
                                uploadImgWithGlobalConfig(paths);
                            } else {
                                uploadImgs(paths);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消某个请求
        //RxHttpUtils.cancel("tag1");
        //取消某些请求
        //RxHttpUtils.cancel("tag1", "tag2", "download", "uploadImg");
        ///取消所有请求
        //RxHttpUtils.cancelAll();
    }
}
