package complaintgroup.jlauncher;

import complaintgroup.jlauncher.model.LauncherModel;
import complaintgroup.jlauncher.model.LauncherModel.Callbacks;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

public class LauncherApplication extends Application {
    private LauncherModel mModel;

    @Override
    public void onCreate() {
        super.onCreate();

        mModel = new LauncherModel(this);
        // Register intent receivers
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        registerReceiver(mModel, filter);
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(mModel);

        super.onTerminate();
    }

    public LauncherModel setLauncher(Callbacks launcher) {
        mModel.initialize(launcher);
        return mModel;
    }

    LauncherModel getModel() {
        return mModel;
    }
}
