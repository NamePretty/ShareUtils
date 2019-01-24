package com.admin.shareproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.admin.shareproject.permission.PermissionTool;
import com.admin.shareproject.permission.RuntimeRationale;
import com.admin.shareproject.shareUtils.ShareContentType;
import com.admin.shareproject.shareUtils.ShareUtil;
import com.admin.shareproject.utils.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_share)
    TextView tvShare;

    private String filePath="/storage/emulated/0/zcjsc_app/files/1541408507778_305.pdf";//写你自己的本地地址，可以是网络下载的也可以是本地选择的路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestSDPermission();
    }

    private void requestSDPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //做你要做的事
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        ToastUtil.show(MainActivity.this, "SD卡读写权限被拒绝，请手动开启！");
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            PermissionTool.showSettingDialog(MainActivity.this, permissions);
                        }
                    }
                })
                .start();
    }

    /***
     * 目前根据系统版本号做了对应的操作，华为小米5.0 6.0 7.0 8.0 9.0测试都OK
     * 要是有好的方法可以自行修改
     * */
    @OnClick({R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_share:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    AndPermission.with(this)
                            .runtime()
                            .permission(Permission.Group.STORAGE)
                            .rationale(new RuntimeRationale())
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    Uri muri=Uri.parse("file://"+filePath);
                                    new ShareUtil.Builder(MainActivity.this)
                                            .setContentType(ShareContentType.FILE)
                                            .setShareFileUri(muri)
                                            .setTitle("分享文件给好友")
                                            .build()
                                            .shareBySystem();
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(@NonNull List<String> permissions) {
                                    ToastUtil.show(MainActivity.this, "SD卡读写权限被拒绝，请手动开启！");
                                    if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                        PermissionTool.showSettingDialog(MainActivity.this, permissions);
                                    }
                                }
                            })
                            .start();

                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    AndPermission.with(this)
                            .runtime()
                            .permission(Permission.Group.STORAGE)
                            .rationale(new RuntimeRationale())
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    new ShareUtil.Builder(MainActivity.this)
                                            .setContentType(ShareContentType.FILE)
                                            .setShareFileUri(getFileUri(MainActivity.this,ShareContentType.FILE, new File(filePath)))
                                            .setTitle("分享文件给好友")
                                            .build()
                                            .shareBySystem();
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(@NonNull List<String> permissions) {
                                    ToastUtil.show(MainActivity.this, "SD卡读写权限被拒绝，请手动开启！");
                                    if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                        PermissionTool.showSettingDialog(MainActivity.this, permissions);
                                    }

                                }
                            })
                            .start();
                }else{
                    Uri muri=Uri.parse("file://"+filePath);
                    new ShareUtil.Builder(MainActivity.this)
                            .setContentType(ShareContentType.FILE)
                            .setShareFileUri(muri)
                            .setTitle("分享文件给好友")
                            .build()
                            .shareBySystem();
                }
                break;
        }
    }

    public static Uri getFileUri (Context context, @ShareContentType String shareContentType, File file){

        if (context == null) {
            Log.e("TAG","getFileUri current activity is null.");
            return null;
        }

        if (file == null || !file.exists()) {
            Log.e("TAG","getFileUri file is null or not exists.");
            return null;
        }

        Uri uri = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {

            if (TextUtils.isEmpty(shareContentType)) {
                shareContentType = "*/*";
            }

            switch (shareContentType) {

                case ShareContentType.FILE :
                    uri = forceGetFileUri(file);
                    break;
                default: break;
            }
        }

        if (uri == null) {
            uri = forceGetFileUri(file);
        }

        return uri;
    }

    private static Uri forceGetFileUri(File shareFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                @SuppressLint("PrivateApi")
                Method rMethod = StrictMode.class.getDeclaredMethod("disableDeathOnFileUriExposure");
                rMethod.invoke(null);
            } catch (Exception e) {
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }

        return Uri.parse("file://" + shareFile.getAbsolutePath());
    }
}
