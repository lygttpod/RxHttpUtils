[![](https://jitpack.io/v/lygttpod/RxHttpUtils.svg)](https://jitpack.io/#lygttpod/RxHttpUtils)

# 重磅推出 RxHttpUtils 2.x 版本
## RxJava+Retrofit封装，基于RxJava2和Retrofit2重构，便捷使用

> ## 上次封装的是基于RxJava1版本的，时隔半年多之后现在推出基于RxJava2的版本，相比上一版本提升不少，配置更加灵活，现在放出来供大家学习使用，使用过程中如有问题欢迎提出建议，不断完善这个库！


### 添加Gradle依赖

先在项目根目录的 build.gradle 的 repositories 添加:
```
     allprojects {
         repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
 然后在dependencies添加:
 
 ```
        dependencies {
        ...
        compile 'com.github.lygttpod:RxHttpUtils:2.0.1'
        }
```

# 使用说明

### 1、使用前自己的application类必须继承BaseRxHttpApplication

> ##### 继承BaseRxHttpApplication之后在自己的Application的onCreate方法中进行初始化配置

```
public class MyApplication extends BaseRxHttpApplication {

    Map<String, Object> headerMaps = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        headerMaps.put("header1", "header1");
        headerMaps.put("header1", "header1");

        /**
         * 全局请求的统一配置
         */
        RxHttpUtils
                .getInstance()
                //开启全局配置
                .config()
                //全局的BaseUrl,必须配置(baseurl以 / 结尾)
                .setBaseUrl(BuildConfig.BASE_URL)
                //开启缓存策略
                .setCache()
                //全局的请求头信息
                .setHeaders(headerMaps)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证，支持三种方式
                //1、信任所有证书,不安全有风险
                .setSslSocketFactory()
                //2、使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.cer"))
                //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setLog(true);

    }
}
```


### 2、自己定义的实体类必须继承BaseResponse基类

```
/**
 * Created by allen on 2017/6/23.
 * <p>
 * 请求结果基类   所有请求结果继承此类
 */

public class BaseResponse {

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误描述
     */
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
```


# 代码实例-----具体参数意义请看下边的参数说明

### 1、使用Application里边的全局配置参数的请求示例
```
                RxHttpUtils
                        .createApi(ApiService.class)
                        .getBook()
                        .compose(Transformer.<BookBean>switchSchedulers())
                        .subscribe(new CommonObserver<BookBean>() {
                            @Override
                            protected void getDisposable(Disposable d) {
                                 //方法暴露出来使用者根据需求去取消订阅
                                 //d.dispose();在onDestroy方法中调用
                            }

                            @Override
                            protected void onError(String errorMsg) {
                                //错误处理
                            }

                            @Override
                            protected void onSuccess(BookBean bookBean) {
                                //业务处理
                            }
                        });
```
                
### 2、单个请求配置参数示例(可以根据需求选择性的配置)
```
                //单个请求使用默认配置的参数
                RxHttpUtils
                        .getSInstance()
                        .baseUrl("https://api.douban.com/")
                        .createSApi(ApiService.class)
                        .getTop250(10)
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>(loading_dialog) {
                            @Override
                            protected void getDisposable(Disposable d) {
                                //方法暴露出来使用者根据需求去取消订阅
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {
                                //错误处理
                            }

                            @Override
                            protected void onSuccess(Top250Bean top250Bean) {
                               //业务处理
                            }
                        });


                //单个请求自己配置相关参数
                RxHttpUtils
                        .getSInstance()
                        .baseUrl("https://api.douban.com/")
                        .addHeaders(headerMaps)
                        .cache(true)
                        .cachePath("cachePath", 1024 * 1024 * 100)
                        .sslSocketFactory()
                        .saveCookie(true)
                        .writeTimeout(10)
                        .readTimeout(10)
                        .connectTimeout(10)
                        .log(true)
                        .createSApi(ApiService.class)
                        .getTop250(10)
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>(loading_dialog) {
                            @Override
                            protected void getDisposable(Disposable d) {
                                //方法暴露出来使用者根据需求去取消订阅
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {
                                //错误处理
                            }

                            @Override
                            protected void onSuccess(Top250Bean top250Bean) {
                               //业务处理
                            }
                        });
```

### 3、链式请求--请求参数是上个请求的结果
```
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
                            protected void getDisposable(Disposable d) {
                                //方法暴露出来使用者根据需求去取消订阅
                                disposables.add(d);
                            }

                            @Override
                            protected void onError(String errorMsg) {
                                //错误处理
                            }

                            @Override
                            protected void onSuccess(Top250Bean top250Bean) {
                               //业务处理
                            }
                        });
```
### 4、文件下载 ----使用简单粗暴
```
                String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
                String fileName = "alipay.apk";

                RxHttpUtils
                        .downloadFile(url)
                        .subscribe(new DownloadObserver(fileName) {
                            @Override
                            protected void getDisposable(Disposable d) {
                                //方法暴露出来使用者根据需求去取消订阅
                                //d.dispose();在onDestroy方法中调用
                            }

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                                log.d("allen","下载中：" + progress + "%");
                                if (done) {
                                    showToast("下载完成---文件路径"+filePath);
                                }

                            }
                        });
```
# 参数说明

> 全局参数：在application中配置的参数都是以setXXX开头的,根据实际需求配置相应参数即可

```
                //开启全局配置
                .config()
                //全局的BaseUrl
                .setBaseUrl(BuildConfig.BASE_URL)
                //开启缓存策略
                .setCache()
                //全局的请求头信息
                .setHeaders(headerMaps)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证，支持三种方式
                //信任所有证书,不安全有风险
                .setSslSocketFactory()
                //使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.cer"))
                //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setLog(true);
```
> 单个请求参数：
```
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
                        //单个请求的ssl证书认证，支持三种方式
                        //1、信任所有证书,不安全有风险
                        .setSslSocketFactory()
                        //2、使用预埋证书，校验服务端证书（自签名证书）
                        //.setSslSocketFactory(getAssets().open("your.cer"))
                        //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
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
```
            
            

# 注意事项：
适合请求结果是以下情况的（当然用户可以根据自己的实际需求稍微修改一下代码就能满足自己的需求）

            
            code为错误状态码 ; msg为错误描述信息
            注意：请求成功时，msg字段可有可无。
            {
            code: 0/400/401...,
            msg: 错误描述...,
            ...
            ...
            ...
            }
            如果你的服务器返回不是以上格式不要惊慌，下载源码，源码其实很简单，自己重写一个BaseResponse基类，根据自己需求处理，
            修改一下BaseObserver和ISubscriber中泛型继承的类就行了

# 后面会陆续完成文件上传的封装，敬请期待...

# 更新日志

### V2.0.1
* 添加对https的支持

### V2.0.0
* 基于RxJava2和Retrofit2重构

### V1.0.2
* 	新增网络缓存

# 意见反馈

如果遇到问题或者好的建议，请反馈到：[issue](https://github.com/lygttpod/RxHttpUtils/issues)、[lygttpod@163.com](mailto:lygttpod@163.com) 或者[lygttpod@gmail.com](mailto:lygttpod@gmail.com)

如果觉得对你有用的话，点一下右上的星星赞一下吧!


# [**Demo下载地址**](https://fir.im/dzm1) 或者扫码下载demo

<div  align="center">   
<img src="https://github.com/lygttpod/RxHttpUtils/blob/2.x/rxhttputils.png" width = "480" height = "480" alt="Demo下载地址" align=center /></div>
</div>

# License
         Copyright 2016 Allen

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.

