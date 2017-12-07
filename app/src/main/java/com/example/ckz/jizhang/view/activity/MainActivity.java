package com.example.ckz.jizhang.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.util.StatusBarUtil;
import com.example.ckz.jizhang.view.view.SkipView;
import com.zhy.changeskin.base.BaseSkinActivity;


public class MainActivity extends BaseSkinActivity {

    private  SkipView skipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        skipView = (SkipView) findViewById(R.id.skip_view);
        StatusBarUtil.setTransparentForImageView(this,skipView);

        skipView.start(new SkipView.OnSkipListener() {
            @Override
            public void onClick(View view) {
                intentToRecond();
            }

            @Override
            public void onFinish() {
                intentToRecond();
            }
        });
    }

    private void intentToRecond(){
        startActivity(new Intent(this,RecondActivity.class));
        finish();
    }
}
