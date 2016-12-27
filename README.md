# RxHttpUtils
Rxjava+Retrofit封装，便捷使用

      RxHttpUtils
                //.getInstance(your_base_url)
                .getInstance()
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
                //传入自己的loading View 会自动在请求开始的时候显示loading
                //.compose(RxHelper.<Banner>io_main(loadingDialog))            
                //传入自己的loading View 会自动在请求开始的时候显示loading
                //.subscribe(new CommonSubscriber<Banner>(loadingDialog) {
                //    @Override
                //    protected void onError(String errorMsg) {
                //    }
                //   @Override
                //    protected void onSuccess(Banner banner) {
                //       Toast.makeText(MainActivity.this, banner.getBanners().get(0).getTitle(), Toast.LENGTH_LONG).show();
                //   }
                //});
