package com.frameproject.host;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.frameproject.MainActivity;
import com.frameproject.R;

import java.util.ArrayList;
import java.util.List;

import common.data.APIHostManager;
import common.data.AppPref;


/**
 * desc:  域名管理
 * author:  yangtao
 * <p>
 * creat: 2018/8/29 19:05
 */

public class WebHostDialog extends Dialog {

    private Context mContext;
    private LinearLayout llContent;
    private TextView tvName, tvHost;
    private Button btCancel;
    private AppPref appPref;

    public WebHostDialog(@NonNull Context context) {
        super(context, R.style.map_dialog);
        mContext = context;
        appPref = new AppPref(mContext, AppPref.APP_PREF);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dia_web_host);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = mContext.getResources().getDisplayMetrics().widthPixels;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.alpha = 9f; // 透明度
        //root.measure(0, 0);
        //lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);

        llContent = findViewById(R.id.ll_host);
        btCancel = findViewById(R.id.btn_cancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        List<HostModel> list = new ArrayList();
        String enviroment1 = "开发环境";
        String name11 = "通用";
        String host11 = "https://wwwtest.idasex.com/exchange/api/";
        String name111 = "第三方";
        String host111 = "https://wwwtest.idasex.com/ts/commonality/";
        String game1 = "https://games.pipeapi.cloud/";
        list.add(new HostModel(enviroment1, name11, host11, name111, host111,game1));

        String enviroment2 = "测试环境";
        String name22 = "通用";
        String host22 = "https://apptest.pipeapi.cloud/api/";//https://apptest.antsant.io/sharer/api/
        String name222 = "第三方";
        String host222 = "https://admintest.pipeapi.cloud/";
        String game2 = "https://gamestest.pipeapi.cloud/";
        list.add(new HostModel(enviroment2, name22, host22, name222, host222,game2));

        String enviroment3 = "预发布环境";
        String name33 = "通用";
        String host33 = "https://apppre.pipeapi.cloud/api/";
        String name333 = "第三方";
        String host333 = "https://adminpre.pipeapi.cloud/";
        String game3 = "https://gamespre.pipeapi.cloud";
        list.add(new HostModel(enviroment3, name33, host33, name333, host333,game3));

        String enviroment4 = "生产环境";
        String name44 = "通用";
        String host44 = "https://app.pipeapi.cloud/api/";
        String name444 = "第三方";
        String host444 = "https://app.pipeapi.cloud/";
        String game4 = "https://games.pipeapi.cloud/";
        list.add(new HostModel(enviroment4, name44, host44, name444, host444,game4));

        String enviroment5 = "压测环境";
        String name55 = "通用";
        String host55 = "https://apppressure.antsant.io/sharer/api/";
        String name555 = "第三方";
        String host555 = "https://club.chainplus.us/ts/commonality/";
        String game5 = "https://games.pipeapi.cloud/";
        list.add(new HostModel(enviroment5, name55, host55, name555, host555,game5));
        addContentItem(list);
    }


    /**
     * 添加正文内容
     */
    public void addContentItem(List<HostModel> list) {
        for (HostModel hostModel : list) {
            View addView = LayoutInflater.from(mContext).inflate(R.layout.dia_web_host_item, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 0);
            //环境名称
            TextView environment = addView.findViewById(R.id.environment);
            //环境归属1
            TextView tv_name1 = addView.findViewById(R.id.tv_name1);
            //环境域名1
            TextView tv_host1 = addView.findViewById(R.id.tv_host1);
            //环境归属2
            TextView tv_name2 = addView.findViewById(R.id.tv_name2);
            //环境域名2
            TextView tv_host2 = addView.findViewById(R.id.tv_host2);
            //环境域名3
            TextView tv_host3 = addView.findViewById(R.id.tv_host3);

            environment.setText(hostModel.environment);
            tv_name1.setText(hostModel.name_one);
            tv_host1.setText(hostModel.host_one);
            tv_name2.setText(hostModel.name_two);
            tv_host2.setText(hostModel.host_two);
            tv_host3.setText(hostModel.game);
            addView.setLayoutParams(layoutParams);

            if (APIHostManager.Common_Url.equals(hostModel.host_one)
                    && APIHostManager.IM_Url.equals(hostModel.host_two)
                    && APIHostManager.Game_Url.equals(hostModel.game)){

                addView.findViewById(R.id.rl_item).setBackgroundColor(R.drawable.webhost_dialog);
            }
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof MainActivity) {
                        appPref.setHost(tv_host1.getText().toString().trim() + "," + tv_host2.getText().toString().trim()+ "," + tv_host3.getText().toString().trim());
                        ((MainActivity) mContext).reboot();
                        dismiss();
                    }

                }
            });
            llContent.addView(addView);
        }
    }


}
