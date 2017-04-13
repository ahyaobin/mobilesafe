package com.ahyaobin.mobile.skin;

import android.app.Activity;

import com.ahyaobin.mobile.R;
import com.ahyaobin.mobile.utils.SharedPreferenceUtil;

/**
 * 更换皮肤工具类
 * Created by weixinfei on 16/5/14.
 */
public class ThemeTool {
    public static void changeTheme(Activity activity) {
        if (SharedPreferenceUtil.getThemeMode()) {
            activity.setTheme(R.style.AppThemeDark);
        }
    }
}
