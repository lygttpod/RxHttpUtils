package com.allen.rxhttputils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allen.library.http.CommonSubscriber;
import com.allen.library.http.RxHttpUtils;
import com.allen.library.interceptor.RxHelper;
import com.allen.rxhttputils.api.ApiService;
import com.allen.rxhttputils.bean.Banner;

import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1, button2, button3;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                request1();
                break;
            case R.id.button2:
                request2();
                break;
            case R.id.button3:
                request3();
                break;
        }
    }

    /**
     * 单一请求链式操作，直观明了
     * <p>
     * 无loading对话框
     */
    private void request1() {

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
    }

    /**
     * 带有loading对话框的请求
     */
    private void request2() {
        RxHttpUtils
                .getInstance()
                .createApi(ApiService.class)
                .getBanner()
                .compose(RxHelper.<Banner>io_main(dialog))
                .subscribe(new CommonSubscriber<Banner>(dialog) {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(Banner banner) {
                        Toast.makeText(MainActivity.this, banner.getBanners().get(0).getTitle(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    /**
     * 链式操作，依次发送多个请求
     */
    private void request3() {

//        RxHttpUtils
//                .getInstance()
//                .createApi(ApiService.class)
//                .login(map.getParams())
//                .flatMap(new Func1<User, Observable<Invitation>>() {
//                    @Override
//                    public Observable<Invitation> call(User user) {
//                        SPUtils.put(MainActivity.this, "token", user.getToken());
//                        ParamUtils map = new ParamUtils();
//                        map.addParams("invitationCode", user.getInvitationCode());
//                        return RxHttpUtils.getInstance().createApi(ApiService.class).getInvitation(map.getParams());
//                    }
//                })
//                .compose(RxHelper.<Invitation>io_main(dialog))
//                .subscribe(new BaseSubscriber<Invitation>(dialog) {
//                    @Override
//                    public void doOnError(String errorMsg) {
//
//                    }
//
//                    @Override
//                    public void doOnNext(Invitation invitation) {
//                        Toast.makeText(MainActivity.this, invitation.toString(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void doOnCompleted() {
//
//                    }
//                });
    }


}
