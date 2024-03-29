package com.ahyaobin.mobile.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.SnackBar;
import com.ahyaobin.mobile.R;
import com.ahyaobin.mobile.activity.AntiVirusActivity;
import com.ahyaobin.mobile.activity.AppManagerActivity;
import com.ahyaobin.mobile.activity.LostFindActivity;
import com.ahyaobin.mobile.activity.TaskManagerActivity;
import com.ahyaobin.mobile.utils.GlobalUtils;
import com.ahyaobin.mobile.utils.SharedPreferenceUtil;

import java.util.Random;

/**
 * 安全检查首页
 * Created by weixinfei on 2016/3/20.
 */
public class SafeFragment extends AppBaseFragment implements View.OnClickListener {

    public static final int CHECKEDSAFE = 1;
    public static final int CHECKNOSAFE = 0;
    public static int SAFESCORE = 74;

    private RelativeLayout rlSafeScore;
    private TextView tvSafeScore;

    private SafeRunnable mRunnable;
    private SafeHandler mHandler;
    private Thread mThread;
    private TextView tvLostFind;

    private SnackBar mSnackBar;
    private Dialog.Builder dialogBuilder;
    private TextView tvSafeAntiVirus;
    private TextView tvTaskManager;
    private TextView tvAppManager;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new SafeHandler(Looper.myLooper());
        dialogBuilder = null;
        intent = new Intent();
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_safe;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {
        rlSafeScore = (RelativeLayout)findViewById(R.id.rl_safe_score);
        tvSafeScore = (TextView) findViewById(R.id.tv_safe_score);

        tvLostFind = (TextView) findViewById(R.id.tv_safe_lost_find);
        tvSafeAntiVirus = (TextView) findViewById(R.id.tv_safe_anti_virus);
        tvTaskManager = (TextView) findViewById(R.id.tv_task_manager);
        tvAppManager = (TextView) findViewById(R.id.tv_app_manager);

        setTextIcon();

        mSnackBar = (SnackBar) getActivity().findViewById(R.id.main_sn);
        mSnackBar.applyStyle(R.style.Material_Widget_SnackBar_Tablet);

        tvLostFind.setOnClickListener(this);
        tvSafeAntiVirus.setOnClickListener(this);
        tvTaskManager.setOnClickListener(this);
        tvAppManager.setOnClickListener(this);

        rlSafeScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        initScore();
    }

    private void setTextIcon() {
        TypedValue value = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary,value,true);

        boolean themeMode = SharedPreferenceUtil.getThemeMode();
        Drawable drawable1 = getResources().getDrawable(themeMode ? R.drawable.ic_security_dark : R.drawable.ic_security_light);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLostFind.setCompoundDrawables(null,null,drawable1,null);

        Drawable drawable2 = getResources().getDrawable(themeMode ? R.drawable.ic_antivirus_dark : R.drawable.ic_antivirus_light);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        tvSafeAntiVirus.setCompoundDrawables(null,null,drawable2,null);

        Drawable drawable3 = getResources().getDrawable(themeMode ? R.drawable.ic_app_dark : R.drawable.ic_app_light);
        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
        tvAppManager.setCompoundDrawables(null,null,drawable3,null);

        Drawable drawable4 = getResources().getDrawable(themeMode ? R.drawable.ic_tune_dark : R.drawable.ic_tune_light);
        drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
        tvTaskManager.setCompoundDrawables(null,null,drawable4,null);

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

    }

    private void initScore() {
        int safeSettingPrefs = SharedPreferenceUtil.getSafeSettingPrefs();
        if (safeSettingPrefs == CHECKNOSAFE){
            SAFESCORE = 82;
            mRunnable = new SafeRunnable(SAFESCORE);
            mThread = new Thread(mRunnable);
            mThread.start();
        }else if (safeSettingPrefs == CHECKEDSAFE){
            SAFESCORE = 82;
            mRunnable = new SafeRunnable(SAFESCORE+10);
            mThread = new Thread(mRunnable);
            mThread.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_safe_lost_find:
                showLostFindDialog();
                break;
            case R.id.tv_safe_anti_virus:
                intent.setClass(getContext(), AntiVirusActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_task_manager:
                intent.setClass(getContext(), TaskManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_app_manager:
                intent.setClass(getContext(), AppManagerActivity.class);
                startActivity(intent);
                break;
            }
        }

    private void showLostFindDialog() {
        //如果用户没设置过密码 设置密码， 如果设置过 输入密码
        if(!isSetupPwd()){//没设置过密码
            showSetupPwdDialog();
        }else{
            showEnterDialog();
        }
    }

    /**
     * 输入密码进入防盗页面
     */
    private void showEnterDialog() {
        dialogBuilder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
            @Override
            protected void onBuildDone(Dialog dialog) {
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                if (checkPassword(fragment)){
                    super.onPositiveActionClicked(fragment);
                    enterLostFindActivity();
                }else {
                    return;
                }
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        dialogBuilder.title("请输入密码:")
                .positiveAction("确认")
                .negativeAction("取消")
                .contentView(R.layout.dialog_enter_pass);

        DialogFragment fragment = DialogFragment.newInstance(dialogBuilder);
        fragment.show(getFragmentManager(), null);
    }

    /**
     * 设置密码界面
     */
    private void showSetupPwdDialog() {
        dialogBuilder = new SimpleDialog.Builder(R.style.SimpleDialogLight){

            @Override
            protected void onBuildDone(Dialog dialog) {
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                EditText etSafePass = (EditText) fragment.getDialog().findViewById(R.id.et_safe_password);
                EditText etSafePassConfirm = (EditText) fragment.getDialog().findViewById(R.id.et_safe_password_confirm);
                String pwd = etSafePass.getText().toString().trim();
                String pwd_confirm = etSafePassConfirm.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd_confirm)){
                    if(mSnackBar.getState() == SnackBar.STATE_SHOWN)
                        mSnackBar.dismiss();
                    mSnackBar.text("密码不能为空")
                            .actionText("关闭")
                            .duration(1000)
                            .show();
                    return;
                }

                if(pwd.equals(pwd_confirm)){
                    //存储这个密码
                    SharedPreferenceUtil.setSafePasswordPrefs(pwd);

                    enterLostFindActivity();
                    super.onPositiveActionClicked(fragment);
                }else{
                    mSnackBar.text("两次输入密码不一致！")
                            .actionText("关闭")
                            .duration(0)
                            .show();
                }
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        dialogBuilder.title("请输入密码:")
                .positiveAction("确认")
                .negativeAction("取消")
                .contentView(R.layout.dialog_setup_pass);

        DialogFragment fragment = DialogFragment.newInstance(dialogBuilder);
        fragment.show(getFragmentManager(), null);
    }

    /**
     * 进入安全防盗页面
     */
    private void enterLostFindActivity() {
        Intent intent = new Intent(getContext(), LostFindActivity.class);
        startActivity(intent);
    }

    /**
     * 检查密码输入是否正确
     * @return
     */
    private boolean checkPassword(DialogFragment fragment) {
        EditText etSafePass = (EditText) fragment.getDialog().findViewById(R.id.et_safe_password);
        String etPassStr = etSafePass.getText().toString().trim();
        if (TextUtils.isEmpty(etPassStr)){
            if(mSnackBar.getState() == SnackBar.STATE_SHOWN)
                mSnackBar.dismiss();
            mSnackBar.text("请输入密码")
                    .actionText("关闭")
                    .duration(1000)
                    .show();
            return false;
        } else if (!etPassStr.equals(SharedPreferenceUtil.getSafePasswordPrefs())) {
            if(mSnackBar.getState() == SnackBar.STATE_SHOWN)
                mSnackBar.dismiss();
            mSnackBar.text("密码错误")
                    .actionText("重新输入")
                    .duration(1000)
                    .show();
            return false;
        }
        else if (etPassStr.equals(SharedPreferenceUtil.getSafePasswordPrefs())){
            GlobalUtils.showToast(getContext(), "密码正确");
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否设置过密码
     * @return
     */
    private boolean isSetupPwd(){
        String password = SharedPreferenceUtil.getSafePasswordPrefs();
        return !TextUtils.isEmpty(password);
    }

    /**
     * 安全分数变化
     */
    class SafeRunnable implements Runnable{

        private int score;
        public SafeRunnable(int score){
            this.score = score;
        }

        @Override
        public void run() {
            synchronized (this) {
                Message msg;
                score = (new Random().nextInt(4)) + score;
                for (int i = 100; i >= score; i--) {
                    msg = Message.obtain();
                    msg.arg1 = i;
                    msg.what = 101;
                    mHandler.sendMessage(msg);
                    try {
                        mThread.sleep((new Random().nextInt(4)) * 300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class SafeHandler extends Handler{
        public SafeHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.what == 101){
                tvSafeScore.setText(msg.arg1 + "");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        SharedPreferenceUtil.setSafeSettingPrefs(0);
    }
}
