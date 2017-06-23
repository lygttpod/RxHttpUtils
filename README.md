# 重磅推出 RxHttpUtils 2.x 版本
## RxJava+Retrofit封装，基于RxJava2和Retrofit2重构，便捷使用

继上次[**SuperTextView**](https://github.com/lygttpod/SuperTextView)之后的又一次封装

编写原由：
项目用到RxJava+Retrofit的使用，总是感觉封装的不到位，网上也有很多类此的封装，找来找去没有一款适合自己的，无奈之下只能自己动手封装一个使用起来超级简单的网络框架，个人感觉装装的还是挺不错的，相比网络上其他封装简单了不少，使用起来也很方便，源码很少可以随意修改源码达到自己需要的效果，废话不多说了，请看怎么使用！

> 上次封装的是基于rxjava1版本的，时隔半年多之后现在推出基于rxjava2的版本，相比上一版本提升不少，配置更加灵活，现在放出来供大家学习使用，使用过程中如有问题欢迎提出建议，不断完善这个库！

### 添加Gradle依赖

先在项目根目录的 build.gradle 的 repositories 添加:

     allprojects {
         repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
    
 然后在dependencies添加:
 
        dependencies {
        ...
        compile 'com.github.lygttpod:RxHttpUtils:1.0.2'
        }


# 使用说明

### 1、使用前自己的application类必须继承BaseRxHttpApplication

> ##### 继承BaseRxHttpApplication之后在自己的Application的onCreate方法中进行初始化配置

```
public class MyApplication extends BaseRxHttpApplication {

    Map<String, Object> headerMaps = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        headerMaps.put("client", "android");
        headerMaps.put("version", AppUtils.getAppVersion());
        headerMaps.put("uuid", AppUtils.getUUID());
        headerMaps.put("Content-type", "application/json");

        /**
         * 全局请求的统一配置
         */
        RxHttpUtils.getInstance().config()
                .setBaseUrl(BuildConfig.BASE_URL)//全局的BaseUrl
                .setCache()//开启缓存策略
                .setHeaders(headerMaps)//全局的请求头信息
                .setCookie(true)//全局持久话cookie,保存本地每次都会携带在header中
                .setCertificates(BuildConfig.CertificatesName)//全局ssl证书认证
                .setReadTimeout(10)//全局超时配置
                .setWriteTimeout(10)//全局超时配置
                .setConnectTimeout(10)//全局超时配置
                .setLog(true);//全局是否打开请求log日志

    }
}
```


### 2、自己定义的实体类需要继承BaseResponse基类

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

### 3、全局配置的请求示例
```
                RxHttpUtils
                        .createApi(ApiService.class)//ApiService.class是自己创建的，根据自己需求自行创建
                        .getFreeHero()//自己ApiService中定义的方法
                        .compose(Transformer.<FreeHeroBean>switchSchedulers())//切换线程的操作在里边已经配置好
                        //.compose(Transformer.<FreeHeroBean>switchSchedulers(loading_dialog))//传入自己的dialog会在请求时候显示
                        //.subscribe(new CommonObserver<FreeHeroBean>(loading_dialog) {//会在请求结束关闭loading
                        .subscribe(new CommonObserver<FreeHeroBean>() {
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
                            protected void onSuccess(FreeHeroBean freeHeroBean) {
                                //业务处理
                                showToast(s);
                            }
                        });
```
                
### 4、单个请求配置参数示例
```
                RxHttpUtils
                        //单个请求的实例getSInstance(getSingleInstance的缩写)
                        .getSInstance()
                        //单个请求的baseUrl
                        .baseUrl("https://api.github.com/")
                        //单个请求的header
                        .addHeaders(headerMaps)
                        //单个请求是否开启缓存
                        .cache(true)
                        //单个请求的缓存路径及缓存大小，不设置的话有默认值
                        .cachePath("cachePath",1024*1024*100)
                        //ssl证书验证，放在assets目录下的xxx.cer证书
                        .certificates("xxx.cer")
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
                        .getOctocat()
                        //内部配置了线程切换相关策略
                        //如果需要请求loading需要传入自己的loading_dialog
                        //使用loading的话需要在CommonObserver<XXX>(loading_dialog)中也传去
                        .compose(Transformer.<Octocat>switchSchedulers(loading_dialog))
                        .subscribe(new CommonObserver<Octocat>(loading_dialog) {
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
                            protected void onSuccess(Octocat octocat) {
                                //请求成功
                                showToast(s);
                            }
                        });
```

### 5、链式请求--请求参数是上个请求的结果
```
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
                                 //方法暴露出来使用者根据需求去取消订阅
                                 //d.dispose();在onDestroy方法中调用
                            }

                            @Override
                            protected void onError(String errorMsg) {

                            }

                            @Override
                            protected void onSuccess(HeroListBean heroListBean) {
                                showToast(s);
                            }
                        });
```
### 6、文件下载 ----使用简单粗暴
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
### 7、参数说明

> 全局参数：在application中配置的参数都是以setXXX开头的,根据实际需求配置相应参数即可

```
                //全局的BaseUrl
                .setBaseUrl(BuildConfig.BASE_URL)
                //开启缓存策略
                .setCache()
                //全局的请求头信息
                .setHeaders(headerMaps)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(true)
                //全局ssl证书认证
                .setCertificates(BuildConfig.CertificatesName)
                //全局超时配置
                .setReadTimeout(10)
                .setWriteTimeout(10)
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setLog(true);
```
> 单个请求参数：
```
                        //单个请求的实例getSInstance(getSingleInstance的缩写)
                        .getSInstance()
                        //单个请求的baseUrl
                        .baseUrl("https://api.github.com/")
                        //单个请求的header
                        .addHeaders(headerMaps)
                        //单个请求是否开启缓存
                        .cache(true)
                        //单个请求的缓存路径及缓存大小，不设置的话有默认值
                        .cachePath("cachePath",1024*1024*100)
                        //ssl证书验证，放在assets目录下的xxx.cer证书
                        .certificates("xxx.cer")
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
            
            

### 8、注意事项：
适合请求结果是以下情况的（当然用户可以根据自己的实际需求稍微修改一下代码就能满足自己的需求）

            
            code为错误状态码，为0时表示无错误; msg为错误描述信息
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

### 9、后面会陆续完成上传和下载的封装，敬请期待...

# 更新日志

### V2.0.0
* 基于RxJava2和Retrofit2重构

### V1.0.2
* 	新增网络缓存

# 意见反馈

如果遇到问题或者好的建议，请反馈到我的邮箱：[lygttpod@163.com](mailto:lygttpod@163.com) 或者[lygttpod@gmail.com](mailto:lygttpod@gmail.com)

如果觉得对你有用的话，点一下右上的星星赞一下吧!

# [**传送门**](https://github.com/lygttpod/RxHttpUtils)

#License
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

