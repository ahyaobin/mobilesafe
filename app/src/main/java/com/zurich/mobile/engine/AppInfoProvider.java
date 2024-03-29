package com.ahyaobin.mobile.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.ahyaobin.mobile.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机里面所有的安装的应用程序信息
 * Created by weixinfei on 16/5/3.
 */
public class AppInfoProvider {

    public static List<AppInfo> getAppInfos(Context context){
        //得到包管理器
        PackageManager pm = context.getPackageManager();
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
        List<PackageInfo> packinfos = pm.getInstalledPackages(0);
        for(PackageInfo packinfo : packinfos){
            String packname = packinfo.packageName;
            AppInfo appInfo = new AppInfo();
            Drawable icon = packinfo.applicationInfo.loadIcon(pm);

            String name = packinfo.applicationInfo.loadLabel(pm).toString();
            //应用程序的特征标志。 可以是任意标志的组合
            int flags = packinfo.applicationInfo.flags;//应用交的答题卡
            if((flags & ApplicationInfo.FLAG_SYSTEM)  == 0){
                //用户应用
                appInfo.setUserApp(true);
            }else{
                //系统应用
                appInfo.setUserApp(false);
            }
            if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)  == 0){
                //手机内存
                appInfo.setInRom(true);
            }else{
                //外部存储
                appInfo.setInRom(false);
            }
            appInfo.setIcon(icon);
            appInfo.setName(name);
            appInfo.setPackname(packname);
            appinfos.add(appInfo);
        }
        return appinfos;
    }

}
