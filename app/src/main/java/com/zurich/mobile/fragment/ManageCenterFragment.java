package com.ahyaobin.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.RelativeLayout;
import com.ahyaobin.mobile.R;
import com.ahyaobin.mobile.activity.AppLockActivity;
import com.ahyaobin.mobile.activity.AtoolsActivity;
import com.ahyaobin.mobile.activity.CacheCleanActivity;
import com.ahyaobin.mobile.activity.CallSmsManagerActivity;
import com.ahyaobin.mobile.activity.FindAppActivity;
import com.ahyaobin.mobile.activity.FuliActivity;
import com.ahyaobin.mobile.activity.GoogleInstallerActivity;
import com.ahyaobin.mobile.activity.PackageClearActivity;
import com.ahyaobin.mobile.activity.SettingActivity;

/**
 * 管理中心
 * Created by weixinfei on 2016/3/20.
 */
public class ManageCenterFragment extends AppBaseFragment implements View.OnClickListener {
    private Context mContext;

    private LinearLayout llUpdateIconArea;
    private RelativeLayout rlCallAccess;
    private ContentObserver mDownloadChange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisableDelay(true);
        mContext = getContext();
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_manage_center;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {

        rlCallAccess = (RelativeLayout) findViewById(R.id.rl_call_access);

        rlCallAccess.setOnClickListener(this);

        RelativeLayout rlFindApp = (RelativeLayout) findViewById(R.id.rl_find_app);
        RelativeLayout rlProtect = (RelativeLayout) findViewById(R.id.rl_protect);
        RelativeLayout rlApkClearn = (RelativeLayout) findViewById(R.id.rl_apk_clearn);
        RelativeLayout rlFuliMeizhi = (RelativeLayout) findViewById(R.id.rl_fuli_meizhi);

        rlFindApp.setOnClickListener(this);
        rlProtect.setOnClickListener(this);
        rlApkClearn.setOnClickListener(this);
        rlFuliMeizhi.setOnClickListener(this);

        //4个普通功能
        TextView tvFavorite = (TextView) findViewById(R.id.tv_clean_cache);
        tvFavorite.setOnClickListener(this);
        TextView tvBackup = (TextView) findViewById(R.id.tv_all_tools);
        tvBackup.setOnClickListener(this);
        TextView tvGoogle = (TextView) findViewById(R.id.tv_google);
        tvGoogle.setOnClickListener(this);
        TextView tvSetting = (TextView) findViewById(R.id.tv_setting);
        tvSetting.setOnClickListener(this);

        mDownloadChange = new DownloadCountObserver(new Handler());

    }

    @Override
    public boolean isReadyData() {
        return false;
    }

    @Override
    public void onShowData() {
    }

    @Override
    public void onLoadData() {
        showData();
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_call_access://通信助手
                startActivity(new Intent(mContext, CallSmsManagerActivity.class));
                break;
            case R.id.rl_apk_clearn://安装包清理
                startActivity(new Intent(mContext, PackageClearActivity.class));
                break;
            case R.id.rl_fuli_meizhi://福利
                startActivity(new Intent(mContext, FuliActivity.class));
                break;
            case R.id.tv_all_tools://百宝箱
                startActivity(new Intent(mContext, AtoolsActivity.class));
                break;
            case R.id.tv_google://谷歌检测
                startActivity(new Intent(mContext, GoogleInstallerActivity.class));
                break;
            case R.id.tv_setting://设置
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.rl_protect://隐私保护
                startActivity(new Intent(mContext, AppLockActivity.class));
                break;
            case R.id.rl_find_app://隐私保护
                startActivity(new Intent(mContext, FindAppActivity.class));
                break;
            case R.id.tv_clean_cache://缓存清理
                startActivity(new Intent(mContext, CacheCleanActivity.class));
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        mContext.getContentResolver().unregisterContentObserver(mDownloadChange);
        super.onPause();
    }

    private class DownloadCountObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadCountObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }
}
