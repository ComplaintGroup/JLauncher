package complaintgroup.jlauncher.ui;

import complaintgroup.jlauncher.AllAppsAdapter;
import complaintgroup.jlauncher.LauncherApplication;
import complaintgroup.jlauncher.LauncherModel;
import complaintgroup.jlauncher.LauncherModel.Callbacks;
import complaintgroup.jlauncher.R;

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

    private static final int MSG_ALL_APPS_LOADED = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_ALL_APPS_LOADED:
                mAllAppsFragment.setAdapter(new AllAppsAdapter(getApplicationContext(), mModel.getAllApps()));
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
        mHandler.sendEmptyMessage(MSG_ALL_APPS_LOADED);
    }

}
