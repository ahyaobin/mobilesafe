package com.ahyaobin.mobile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ahyaobin.mobile.R;
import com.ahyaobin.mobile.utils.GlobalUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我页面
 * Created by weixinfei on 16/5/25.
 */
public class AboutMeActivity extends BaseActivity {
    @Bind(R.id.about_me_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_about_me_github)
    TextView tvGithub;
    @Bind(R.id.tv_about_me_weibo)
    TextView tvWeibo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);

        initActionBar();
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

    @OnClick(R.id.tv_about_me_github)
    public void onGithubClick() {
        String url = tvGithub.getText().toString();
        GlobalUtils.openBrowser(this, url);
    }

    @OnClick(R.id.tv_about_me_weibo)
    public void onWeiboClick() {
        String url = tvWeibo.getText().toString();
        GlobalUtils.openBrowser(this, url);
    }

    //设置menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
