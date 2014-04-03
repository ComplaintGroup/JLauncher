package complaintgroup.jlauncher.ui;

import java.util.ArrayList;
import java.util.List;

import complaintgroup.jlauncher.LauncherApplication;
import complaintgroup.jlauncher.model.AppInfo;
import complaintgroup.jlauncher.model.LauncherModel;
import complaintgroup.jlauncher.model.LauncherModel.Callbacks;
import complaintgroup.jlauncher.R;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

public class MainActivity extends FragmentActivity implements Callbacks {

    private DrawerLayout mRoot;
    private WorkspaceFragment mHomeFragment;
    private AllAppsFragment mAllAppsFragment;
    private LauncherModel mModel;
    List<AppInfo> mAppInfos = null;

    private static final int MSG_ALL_APPS_LOADED = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_ALL_APPS_LOADED:
                mAllAppsFragment.updateAdapter(mAppInfos);
                break;
            default:
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = (DrawerLayout) findViewById(R.id.root);
        mAllAppsFragment = (AllAppsFragment) getSupportFragmentManager().findFragmentById(R.id.all_apps);

        LauncherApplication app = ((LauncherApplication) getApplication());
        mModel = app.setLauncher(this);
        mModel.startLoader();
    }

    @Override
    public void onBackPressed() {
        if (mRoot.isDrawerOpen(Gravity.END)) {
            mRoot.closeDrawer(Gravity.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onAllAppsLoaded() {
        mAppInfos = ResolveInfoToAppInfo(mModel.getAllApps());
        mHandler.sendEmptyMessage(MSG_ALL_APPS_LOADED);
    }

    private List<AppInfo> ResolveInfoToAppInfo(List<ResolveInfo> ResolveInfos) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        if (ResolveInfos != null) {
            PackageManager pm = getPackageManager();
            for (ResolveInfo ri : ResolveInfos) {
                AppInfo ai = new AppInfo(pm, ri);
                appInfos.add(ai);
            }
        }
        return appInfos;
    }
}
