[![](https://jitpack.io/v/lygttpod/RxHttpUtils.svg)](https://jitpack.io/#lygttpod/RxHttpUtils)

# 重磅推出 RxHttpUtils 2.x 版本
## RxJava+Retrofit封装，基于RxJava2和Retrofit2重构，便捷使用

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
        compile 'com.github.lygttpod:RxHttpUtils:2.1.6'
        }
```

# 使用说明
### 1、在application类里边进行初始化配置

> ##### 在自己的Application的onCreate方法中进行初始化配置
```
public class MyApplication extends Application {

    Map<String, Object> headerMaps = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

              OkHttpClient okHttpClient = new OkHttpConfig
                .Builder(this)
                //全局的请求头信息
                .setHeaders(headerMaps)
                //开启缓存策略(默认false)
                //1、在有网络的时候，先去读缓存，缓存时间到了，再去访问网络获取数据；
                //2、在没有网络的时候，去读缓存中的数据。
                .setCache(true)
                //全局持久话cookie,保存到内存（new MemoryCookieStore()）或者保存到本地（new SPCookieStore(this)）
                //不设置的话，默认不对cookie做处理
                .setCookieType(new SPCookieStore(this))
                //可以添加自己的拦截器(比如使用自己熟悉三方的缓存库等等)
                //.setAddInterceptor(null)
                //全局ssl证书认证
                //1、信任所有证书,不安全有风险（默认信任所有证书）
                //.setSslSocketFactory()
                //2、使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(cerInputStream)
                //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(bksInputStream,"123456",cerInputStream)
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setDebug(true)
                .build();

        RxHttpUtils
                .getInstance()
                .init(this)
                .config()
                //配置全局baseUrl
                .setBaseUrl("https://api.douban.com/")
                //开启全局配置
                .setOkClient(okHttpClient);
      
    }
}
```

### 2、默认已实现三种数据格式

* 1、CommonObserver  （使用写自己的实体类即可，不用继承任何base）
* 2、StringObserver   (直接String接收数据)
* 3、DataObserver     (适合{"code":200,"msg":"描述",data:{}}这样的格式，需要使用BaseData&lt;T&gt; ,其中T为data中的数据模型)

> 如果以上三种不能满足你的需要，可以分别继承对应的baseObserver方法实现自己的逻辑

# 代码实例

> ## 使用Application里边的全局配置的参数

### 2.1、使用CommonObserver请求示例

```
a、   数据结构
     {
        "code": 0,
        "msg": "success",
        "username":"Allen",
        "job":"Android",
        ...
     }

      备注：TestBean为以上数据结构的模型

b、   @GET("api/test")
      Observable<TestBean> getTestData();

c、   RxHttpUtils
                 .createApi(ApiService.class)
                 .getTestData()
                 .compose(Transformer.<TestBean>switchSchedulers())
                 .subscribe(new CommonObserver<TestBean>() {

                     @Override
                     protected void onError(String errorMsg) {
                          //错误处理
                     }

                     @Override
                     protected void onSuccess(TestBean bookBean) {
                          //业务处理
                     }
                  });
```
### 2.2、使用DataObserver请求示例
```
a、     数据结构
        {
            "code":0,
            "msg":"success",
            "data":{
                "username":"Allen",
                "job":"Android Dev"
                ...
            }
        }

        备注：TestBean为data中的数据模型

b、     @GET("api/test")
        Observable<BaseData<TestBean>> geTestData();

c、
        RxHttpUtils.createApi(ApiServer.class)
                .geTestData()
                .compose(Transformer.<BaseData<TestBean>>switchSchedulers())
                .subscribe(new DataObserver<TestBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        
                    }

                    @Override
                    protected void onSuccess(TestBean data) {

                    }
                });
```

### 2.3、使用StringObserver请求示例
```

a、     @GET("api/test")
        Observable<String> geTestData();

b、
        RxHttpUtils.createApi(ApiServer.class)
                .geTestData()
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {

                    }
                });
```

### 2.4、链式请求示例--请求参数是上个请求的结果

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
                         .subscribe(new CommonObserver<Top250Bean>() {

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

# 3、~~单个请求配置(即将废除)~~

> ## 温馨提示：针对某些请求有特殊要求的才建议使用此类方法，没特殊要求不建议使用

### 3.1、单个请求使用默认配置
```
                //单个请求使用默认配置的参数
                RxHttpUtils
                        .getSInstance()
                        .baseUrl("https://api.douban.com/")
                        .createSApi(ApiService.class)
                        .getTop250(10)
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>() {

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
### 3.2、单个请求配置参数示例(可以根据需求选择性的配置)
```
                //单个请求自己配置相关参数
                RxHttpUtils
                        .getSInstance()
                        .baseUrl("https://api.douban.com/")
                        .addHeaders(headerMaps)
                        .cache(true)
                        .cachePath("cachePath", 1024 * 1024 * 100)
                        .sslSocketFactory()
                        .cookieType(new MemoryCookieStore())
                        .writeTimeout(10)
                        .readTimeout(10)
                        .connectTimeout(10)
                        .log(true)
                        .createSApi(ApiService.class)
                        .getTop250(10)
                        .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Top250Bean>() {

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
### 3.3、单个请求返回string
```
                RxHttpUtils.getSInstance()
                        .baseUrl("https://api.douban.com/")
                        //注意这两个配置的顺序
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .createSApi(ApiService.class)
                        .getBookString()
                        .compose(Transformer.<String>switchSchedulers(loading_dialog))
                        .subscribe(new StringObserver() {
                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(String data) {
                                showToast(data);
                                responseTv.setText(data);
                            }
                        });
```

### 4、文件下载 ----使用简单粗暴
```
                String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
                final String fileName = "alipay.apk";

                RxHttpUtils
                        .downloadFile(url)
                        .subscribe(new DownloadObserver(fileName) {
                            //可以通过配置tag用于取消下载请求
                            @Override
                            protected String setTag() {
                                return "download";
                            }

                            @Override
                            protected void onError(String errorMsg) {
                            }

                            @Override
                            protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                                download_http.setText("下 载中：" + progress + "%");
                                if (done) {
                                    responseTv.setText("下载文件路径：" + filePath);
                                }

                            }
                        });
```
### 5、上传图片
上传单张图片的接口
```
        RxHttpUtils.uploadImg(uploadUrl, uploadPath)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {
                    @Override
                    protected void onError(String errorMsg) {
                        Log.e("allen", "上传失败: " + errorMsg);
                        showToast(errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            showToast(responseBody.string());
                            Log.e("allen", "上传完毕: " + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
```
上传多张图片的接口
```
        RxHttpUtils.uploadImgs("yourPicUrl", uploadPaths)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {
                    @Override
                    protected void onError(String errorMsg) {
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                    }
                });
```

### 6、取消请求  相关配置
```
                new XXXObserver<BookBean>() {

                    //重写setTag方法配置当前请求的tag
                    //温馨提示：可以多个请求设置相同的tag自动归为一组，可以一次取消相同tag的所有请求
                    //(适用于一个页面多个请求，配置相同tag，在页面销毁时一次性取消)
                    @Override
                    protected String setTag() {
                        return "yourTag";
                    }

                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(BookBean bookBean) {

                    }
                }

```
```
                //调取如下方法取消某个或某组请求
                RxHttpUtils.cancel("yourTag");

                //调取如下方法取消多个或多组请求
                RxHttpUtils.cancel("yourTag1","yourTag2","yourTag3");
```

### 7、onError中默认Toast显示隐藏的配置

* #### 在CommonObserver或DataObserver或StringObserver中重写isHideToast方法，默认false显示toast
```
                            //默认false   隐藏onError的提示
                            @Override
                            protected boolean isHideToast() {
                                return true;
                            }
```

# 参数说明

> 全局参数：在application中配置的参数都是以setXXX开头的,根据实际需求配置相应参数即可

```


//        获取证书
//        InputStream cerInputStream = null;
//        InputStream bksInputStream = null;
//        try {
//            cerInputStream = getAssets().open("YourSSL.cer");
//            bksInputStream = getAssets().open("your.bks");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

          OkHttpClient okHttpClient = new OkHttpConfig
                .Builder(this)
                //全局的请求头信息
                .setHeaders(headerMaps)
                //开启缓存策略(默认false)
                //1、在有网络的时候，先去读缓存，缓存时间到了，再去访问网络获取数据；
                //2、在没有网络的时候，去读缓存中的数据。
                .setCache(true)
                //全局持久话cookie,保存到内存（new MemoryCookieStore()）或者保存到本地（new SPCookieStore(this)）
                //不设置的话，默认不对cookie做处理
                .setCookieType(new SPCookieStore(this))
                //可以添加自己的拦截器(比如使用自己熟悉三方的缓存库等等)
                //.setAddInterceptor(null)
                //全局ssl证书认证
                //1、信任所有证书,不安全有风险（默认信任所有证书）
                //.setSslSocketFactory()
                //2、使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(cerInputStream)
                //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(bksInputStream,"123456",cerInputStream)
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setDebug(true)
                .build();

        RxHttpUtils
                .getInstance()
                .init(this)
                .config()
                //配置全局baseUrl
                .setBaseUrl("https://api.douban.com/")
                //开启全局配置
                .setOkClient(okHttpClient);

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
```
                new XXXObserver<XXX>() {

                    //重写setTag方法配置当前请求的tag
                    //温馨提示：可以多个请求设置相同的tag自动归为一组，可以一次取消相同tag的所有请求
                    //(适用于一个页面多个请求，配置相同tag，在页面销毁时一次性取消)
                    @Override
                    protected String setTag() {
                        return "yourTag";
                    }

                    //默认false   隐藏onError的提示
                    @Override
                    protected boolean isHideToast() {
                          return false;
                    }

                    //请求错误回调
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    //请求成功回调
                    @Override
                    protected void onSuccess(XXX xxx) {

                    }
                }

```



# 更新日志

### V2.1.6
* 修复日志打印使用StringBuilder导致多线程使用线程不安全的问题（表现在多个请求同时进行的时候出现异常的bug），使用StringBuffer代替StringBuilder
### V2.1.5
* 修复errorBody中为空时候异常信息无法获取的bug

### V2.1.4
* 修复无网络且无缓存时候的依然读取缓存导致504错误的问题;
* 新增对Retrofit进行缓存处理，避免重复创建Retrofit对象;
* 在XXXObserver中去掉对loading的处理，统一放在Transformer中控制


### V2.1.3
* 重写取消请求的方法，通过设置tag可以管理一个请求或一组请求，默认tag为空
```
                new XXXObserver<BookBean>() {

                    //重写setTag方法配置当前请求的tag
                    //温馨提示：可以多个请求设置相同的tag自动归为一组，可以一次取消相同tag的所有请求
                    //(适用于一个页面多个请求，配置相同tag，在页面销毁时一次性取消)
                    @Override
                    protected String setTag() {
                        return "yourTag";
                    }

                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(BookBean bookBean) {

                    }
                }

```
```
                //调取如下方法取消某个或某组请求
                RxHttpUtils.cancel("yourTag");

                //调取如下方法取消多个或多组请求
                RxHttpUtils.cancel("yourTag1","yourTag2","yourTag3");
```

### V2.1.2
* cookie持久化方案去除拦截器方式，使用cookieJar管理
```
    支持两种方式做持久化cookie，默认不设置的话不对cookie做任何处理
    new MemoryCookieStore()  持久化到内存
    new SPCookieStore(this)  持久化到本地SP文件

    使用方式如下
    .setCookieType(new SPCookieStore(this))
```
> 感谢[**OkGo**](https://github.com/jeasonlzy/okhttp-OkGo) 提供的技术方案


### V2.1.1
* 修复header参数类型转换异常的bug

### V2.1.0
* 支持图文上传
* 优化请求日志打印
* 优化缓存
* 全局okhttpclient单例

### V2.0.5
* 新增上传多张图片的接口
* 新增onError中默认Toast的控制显示隐藏的配置
* 在CommonObserver或DataObserver或StringObserver中重写isHideToast方法，默认false显示toast

### V2.0.4
* 新增上传图片功能
* 针对字段为int，double，long类型，如果后台返回""或者null,则对应返回0，0.00，0L

### V2.0.3
* 优化底层代码，去除必须继承BaseRxHttpApplication和baseResponse的限制，使用更灵活

### V2.0.1
* 添加对https的支持

### V2.0.0
* 基于RxJava2和Retrofit2重构

### V1.0.2
* 	新增网络缓存

# 意见反馈

<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=33dacdd367ca0b5a9ba96a196a6658666b442b3ec528850e377d50f3d607f26b"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="STV&amp;RxHttp交流群" title="STV&amp;RxHttp交流群"></a>

或者手动加QQ群：688433795

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

