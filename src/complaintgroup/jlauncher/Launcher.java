package complaintgroup.jlauncher;

import complaintgroup.jlauncher.model.LauncherModel;
import complaintgroup.jlauncher.ui.AllAppsFragment;
import complaintgroup.jlauncher.ui.WorkspaceFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class Launcher extends FragmentActivity implements LauncherModel.Callbacks {
    private static final String TAG = "JLauncher.Launcher";

    private static final int NUM_PAGES = 2;
    private static final int POS_WORKSPACE = 0;
    private static final int POS_ALL_APPS = 1;

    private ViewPager mPager;
    private LauncherPagerAdapter mPagerAdapter;
    private LauncherModel mModel;

    private static final int MSG_ALL_APPS_LOADED = 0;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_ALL_APPS_LOADED:
                // mPagerAdapter.getAllAppsFragment().setAdapter(
                // new AllAppsAdapter(getApplicationContext(), mModel.getAllApps()));
                break;

            default:
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        // setup pager
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LauncherPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // setup model (async, will callback)
        LauncherApplication app = ((LauncherApplication) getApplication());
        mModel = app.setLauncher(this);
        mModel.startLoader();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() != POS_WORKSPACE) {
            // back to workspace if it's not
            mPager.setCurrentItem(POS_WORKSPACE);
        } else {
            // otherwise, don't exit activity on back pressed
        }
    }

    private class LauncherPagerAdapter extends FragmentStatePagerAdapter {
        private WorkspaceFragment mWorkspace;
        private AllAppsFragment mAllApps;

        public LauncherPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == POS_WORKSPACE) {
                return getWorkspaceFragment();
            } else if (position == POS_ALL_APPS) {
                return getAllAppsFragment();
            } else {
                // should not happen
                return new Fragment();
            }
        }

        public WorkspaceFragment getWorkspaceFragment() {
            if (mWorkspace == null) {
                mWorkspace = new WorkspaceFragment();
            }
            return mWorkspace;
        }

        public AllAppsFragment getAllAppsFragment() {
            if (mAllApps == null) {
                mAllApps = new AllAppsFragment();
            }
            return mAllApps;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onAllAppsLoaded() {
        // callback is not from UI thread, let handler do the works
        mHandler.sendEmptyMessage(MSG_ALL_APPS_LOADED);
    }

}
