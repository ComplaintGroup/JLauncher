package complaintgroup.jlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class Launcher extends FragmentActivity implements LauncherModel.Callbacks {
    private static final String TAG = "JLauncher.Launcher";

    private static final int NUM_PAGES = 2;
    private static final int POS_WORKSPACE = 0;
    private static final int POS_ALL_APPS = 1;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private LauncherModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        // setup pager
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LauncherPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // setup model (async, will callback)
        LauncherApplication app = ((LauncherApplication)getApplication());
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

        public LauncherPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == POS_WORKSPACE) {
                return new WorkspaceFragment();
            } else if (position == POS_ALL_APPS) {
                return new AllAppsFragment();
            } else {
                // should not happen
                return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onAllAppsLoaded() {
        Log.d(TAG, "onAllAppsLoaded()...");
        // TODO all apps loaded, init AllApps grid view
    }

}
