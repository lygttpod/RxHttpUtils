# RxHttpUtils
##Rxjava+Retrofit封装，便捷使用

###添加Gradle依赖 

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
        compile 'com.github.lygttpod:RxHttpUtils:1.0.1'
        }


#使用说明

###1、使用前自己的application类必须继承BaseApplication

###2、自己定义的实体类需要继承BaseResponse基类

###3、示例

    Map<String ,Object> map = new TreeMap<>();
            map.put("version","8.8");
            map.put("phoneType","android");
        
    RxHttpUtils                
              .getInstance()                              
              .addHeader(map)                             
              .createApi(ApiService.class)                
              .getBanner()                                               
              .compose(RxHelper.<Banner>io_main())        
              .subscribe(new CommonSubscriber<Banner>() {                                   
                    @Override
                    protected void onError(String errorMsg) {
                    }

                    @Override
                    protected void onSuccess(Banner banner) {
                          Toast.makeText(MainActivity.this, banner.getBanners().get(0).getTitle(), Toast.LENGTH_LONG).show();
                    }
                }); 
                
###4、参数说明

            1、getInstance(your_base_url)
            可以动态修改baseUrl
            2、addHeader(map)
            动态设置请求头《可以不添加》
            3、ApiService.class
            是自己定义的
            4、getBanner()
            也是自动定义的方法需要需不需要添加参数根据自己的定义设置
            5、io_main(loadingDialog)
            可以设置自己请求开始的loading对话框
            6、new CommonSubscriber<Banner>(loadingDialog)
            需要显示loading对话框的话这里需要传入自己的Dialog
            
            

###5、注意事项：
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
            修改一下BaseSubscriber和ISubscriber中泛型继承的类就行了

###6、后面会陆续完成上传和下载的封装，敬请期待...


# 意见反馈

如果遇到问题或者好的建议，请反馈到我的邮箱：[lygttpod@163.com](mailto:lygttpod@163.com) 或者[lygttpod@gmail.com](mailto:lygttpod@gmail.com)

如果觉得对你有用的话，点一下右上的星星赞一下吧!

# [**传送门**](https://github.com/lygttpod/RxHttpUtils)

#License
         Copyright 2016 Allen

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

          [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
    limitations under the License.*斜体*
