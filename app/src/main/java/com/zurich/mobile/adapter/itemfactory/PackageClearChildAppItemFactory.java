package com.ahyaobin.mobile.adapter.itemfactory;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.rey.material.widget.CheckBox;
import com.ahyaobin.mobile.R;
import com.ahyaobin.mobile.adapter.assemblyadapter.AssemblyChildItem;
import com.ahyaobin.mobile.adapter.assemblyadapter.AssemblyChildItemFactory;
import com.ahyaobin.mobile.model.AppPackage;
import com.ahyaobin.mobile.model.PackageClearGroup;

public class PackageClearChildAppItemFactory extends AssemblyChildItemFactory<PackageClearChildAppItemFactory.PackageClearChildAppItem> {
    private EventListener eventListener;

    public PackageClearChildAppItemFactory(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof AppPackage;
    }

    @Override
    public PackageClearChildAppItem createAssemblyItem(ViewGroup parent) {
        return new PackageClearChildAppItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_package_clear_child_app, parent, false), this);
    }

    public interface EventListener {
        void onClickChildAppCheckedButton(PackageClearGroup packageClearGroup, AppPackage appPackage);

        void onClickChildApp(PackageClearGroup packageClearGroup, AppPackage appPackage);
    }

    public class PackageClearChildAppItem extends AssemblyChildItem<AppPackage, PackageClearChildAppItemFactory> {
        private NetworkImageView iconNetworkImageView;
        private TextView appNameTextView;
        private TextView versionNameTextView;
        private TextView descTextView;
        private TextView sizeTextView;
        private CheckBox checkBox;

        protected PackageClearChildAppItem(View convertView, PackageClearChildAppItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            iconNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.image_packageClearChildAppItem_icon);
            appNameTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildAppItem_name);
            versionNameTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildAppItem_versionName);
            descTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildAppItem_desc);
            sizeTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildAppItem_size);
            checkBox = (CheckBox) convertView.findViewById(R.id.image_packageClearChildAppItem_checked);
        }

        @Override
        protected void onConfigViews(Context context) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventListener != null) {
                        eventListener.onClickChildAppCheckedButton((PackageClearGroup) getAdapter().getGroup(getGroupPosition()), getData());
                    }
                }
            });

            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventListener != null) {
                        eventListener.onClickChildApp((PackageClearGroup) getAdapter().getGroup(getGroupPosition()), getData());
                    }
                }
            });

        }

        @Override
        protected void onSetData(int groupPosition, int childPosition, boolean isLastChild, AppPackage appPackage) {
            sizeTextView.setText(Formatter.formatFileSize(sizeTextView.getContext(), appPackage.fileLength));

            if (appPackage.broken) {
                iconNetworkImageView.setImageResource(R.drawable.ic_icon);
                iconNetworkImageView.setTag(null);
                appNameTextView.setText(appPackage.fileName);
                versionNameTextView.setText("未知");
            } else {
//                iconNetworkImageView.setImageUrl(appPackage.filePath, loader);
                appNameTextView.setText(appPackage.appName);
                versionNameTextView.setText(appPackage.appVersionName);

                if (appPackage.installed) {
                    if (appPackage.isNewVersion()) {
                        descTextView.setText("新版本");
                    } else if (appPackage.isOldVersion()) {
                        descTextView.setText("旧版本");
                    } else {
                        descTextView.setText("已安装");
                    }
                } else {
                    descTextView.setText("未安装");
                }
            }

        }
    }
}
