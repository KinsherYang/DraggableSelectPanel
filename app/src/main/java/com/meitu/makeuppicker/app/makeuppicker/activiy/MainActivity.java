package com.meitu.makeuppicker.app.makeuppicker.activiy;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.meitu.makeuppicker.R;
import com.meitu.makeuppicker.app.makeuppicker.widget.makeuppanel.controller.MakeupListPanelController;
import com.meitu.makeuppicker.util.AnimationUtil;
import com.meitu.makeuppicker.util.SurfaceCallback;
import com.yangsq.draggableselectpanel.view.DraggableGroupSelectPanelView;

public class MainActivity extends Activity {

    private DraggableGroupSelectPanelView mMakeupPanel;
    private MakeupListPanelController controller;
    private ImageView mIVToggleShow;

    private boolean mIsPanelShowing = true;
    private SurfaceView mSvCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMakeupPanel = (DraggableGroupSelectPanelView) findViewById(R.id.dgsp_makeup_panel);
        mIVToggleShow = (ImageView) findViewById(R.id.iv_toggle_show);
        mSvCamera = (SurfaceView) findViewById(R.id.sv_camera);
        mSvCamera.getHolder().setKeepScreenOn(true);// 屏幕常亮
        mSvCamera.getHolder().addCallback(new SurfaceCallback(this));
        controller = new MakeupListPanelController(this, mMakeupPanel);
        controller.init();
        initToggleShow();
    }

    private void initToggleShow() {
        mIVToggleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPanelShowing) {
                    mIVToggleShow.setImageResource(R.drawable.makeup_panel_toggle_up);
                    // 隐藏
                    mMakeupPanel.setVisibility(View.GONE);
                    mMakeupPanel.setAnimation(AnimationUtil.moveToViewBottom(300));
                } else {
                    mIVToggleShow.setImageResource(R.drawable.makeup_panel_toggle_down);
                    // 显示
                    mMakeupPanel.setVisibility(View.VISIBLE);
                    mMakeupPanel.setAnimation(AnimationUtil.moveToViewLocation(300));
                }
                mIsPanelShowing = !mIsPanelShowing;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controller != null) {
            controller.onDestroy();
        }
    }
}
