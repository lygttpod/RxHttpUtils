# RxHttpUtils
##Rxjava+Retrofit封装，便捷使用

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
