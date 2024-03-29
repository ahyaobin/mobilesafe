package com.ahyaobin.mobile.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.ahyaobin.mobile.R;
import com.ahyaobin.mobile.db.Dao.AntivirusDao;
import com.ahyaobin.mobile.utils.GlobalUtils;
import com.ahyaobin.mobile.utils.Md5Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 手机杀毒
 * Created by weixinfei on 16/4/30.
 */
public class AntiVirusActivity extends BaseActivity {

    protected static final int SCANING = 1;
    protected static final int SCAN_FINISH = 2;
    protected static final int UPDATE_PROGRESS = 3;
    @Bind(R.id.iv_scan)
    ImageView ivScan;
    @Bind(R.id.tv_scan_status)
    TextView tvScanStatus;
    @Bind(R.id.ll_anti_container)
    LinearLayout llAntiContainer;
    @Bind(R.id.pb_anti_progress)
    ProgressBar pbAntiProgress;
    @Bind(R.id.anti_virus_tool_bar)
    Toolbar mToolbar;

    private AntiHandler mHandler;
    /**
     * 病毒查询的集合
     */
    private List<ScanInfo> virusInfos;
    private Dialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        ButterKnife.bind(this);

        initActionBar();
        initViews();

        initData();

    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.safe_soft_manager));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    //设置menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.men_action_settings:
                SettingActivity.launch(AntiVirusActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        virusInfos = new ArrayList<ScanInfo>();
        mHandler = new AntiHandler(Looper.getMainLooper());

        pbAntiProgress.setProgress(0);
        pbAntiProgress.setSecondaryProgress(0);

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 获取应用程序的特征码。
                PackageManager pm = getPackageManager();
                // 获取所有的应用程序 包括哪些被卸载的但是没有卸载干净 （保留的有数据的应用）
                List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES + PackageManager.GET_SIGNATURES);
                pbAntiProgress.setMax(packinfos.size());
                int progress = 0;
                Random random = new Random();
                for (PackageInfo packinfo : packinfos) {
                    ScanInfo scanInfo = new ScanInfo();
                    scanInfo.packname = packinfo.packageName;
                    scanInfo.appname = packinfo.applicationInfo.loadLabel(pm).toString();
                    String md5 = Md5Utils.encode(packinfo.signatures[0].toCharsString());
                    String result = AntivirusDao.isVirus(md5);
                    if (result != null) {
                        scanInfo.isvirus = true;
                        scanInfo.desc = result;
                        virusInfos.add(scanInfo);
                    } else {
                        scanInfo.isvirus = false;
                        scanInfo.desc = null;
                    }
                    progress++;
                    float percent = progress / packinfos.size();

                    Message msg = Message.obtain();
                    msg.what = SCANING;
                    msg.obj = scanInfo;
                    msg.arg1 = (int) (percent * 100);
                    mHandler.sendMessage(msg);

                    try {
                        Thread.sleep(50 + random.nextInt(50));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = Message.obtain();
                msg.what = SCAN_FINISH;
                mHandler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private void initViews() {
        RotateAnimation ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(Animation.INFINITE);
        ivScan.startAnimation(ra);

        tvScanStatus.setText("正在初始化8核杀毒引擎");
    }

    class ScanInfo {
        String packname;
        boolean isvirus;
        String desc;
        String appname;
    }

    class AntiHandler extends Handler {
        public AntiHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANING:
                    pbAntiProgress.setProgress(msg.arg1);
                    pbAntiProgress.setSecondaryProgress(pbAntiProgress.getSecondaryProgress() + 1);

                    ScanInfo scaninfo = (ScanInfo) msg.obj;
                    tvScanStatus.setText("正在扫描：" + scaninfo.appname);
                    TextView tv = new TextView(getApplicationContext());
                    if (scaninfo.isvirus) {
                        tv.setText("发现病毒：" + scaninfo.appname);
                        tv.setTextColor(Color.RED);
                    } else {
                        tv.setText("扫描安全：" + scaninfo.appname);
                        tv.setTextColor(Color.BLACK);
                    }
                    llAntiContainer.addView(tv, 0);
                    break;
                case SCAN_FINISH:
                    ivScan.clearAnimation();
                    tvScanStatus.setText("扫描完毕");
                    pbAntiProgress.setVisibility(ProgressBar.INVISIBLE);
                    if (virusInfos.size() > 0) {//发现了病毒

                        dialogBuilder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                            @Override
                            public void onPositiveActionClicked(DialogFragment fragment) {
                                super.onPositiveActionClicked(fragment);
                                for (ScanInfo info : virusInfos) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_DELETE);
                                    intent.setData(Uri.parse("package:" + info.packname));
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onNegativeActionClicked(DialogFragment fragment) {
                                super.onNegativeActionClicked(fragment);
                            }
                        };

                        ((SimpleDialog.Builder) dialogBuilder).message("在您的手机里面发现了：" + virusInfos.size() + "个病毒,手机已经十分危险，得分0分，赶紧查杀！！！")
                                .title("警告!!!")
                                .positiveAction("立刻处理")
                                .negativeAction("下次再说");

                        DialogFragment fragment = DialogFragment.newInstance(dialogBuilder);
                        fragment.show(getSupportFragmentManager(), null);
                    } else {
                        GlobalUtils.showToast(getBaseContext(), "您的手机非常安全，继续加油哦");
                    }
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }
}
