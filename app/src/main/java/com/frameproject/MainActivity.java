package com.frameproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.TextView;

import com.frameproject.host.WebHostDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.base.BaseCompatActivity;
import common.data.AppPref;

/**
 * 应用首页
 */
public class MainActivity extends BaseCompatActivity {


    @BindView(R.id.tvChange)
    TextView tvChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appPref = new AppPref(this, AppPref.APP_PREF);
    }

    AppPref appPref;

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvChange:
                new WebHostDialog(MainActivity.this).show();
                break;


        }
    }

    //请求
//    void request(){
//        HashMap<String, String> params = new HashMap<>();
//        new HttpBuilder(mActivity).setShowLoading(true)
//                .setObservab(APIManager.buildAPI(ZChatService.class).postLogin(params))
//                .setObserverCallback(new ObserverCallback<UserModel>() {
//                    @Override
//                    public void success(UserModel data) {
//                    }
//
//                    @Override
//                    public void failure(Throwable e) {
//                    }
//                });
//    }

    public void reboot() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        Process.killProcess(Process.myPid());
    }
}
