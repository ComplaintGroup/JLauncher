package complaintgroup.jlauncher;

import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class LauncherModel extends BroadcastReceiver {
    static final String TAG = "JLauncher.Model";

    private final LauncherApplication mApp;
    private List<ResolveInfo> mAllApps; // LAUNCHER & MAIN activities
    private final Object mLock = new Object();
    private LoaderTask mLoaderTask;
    private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
    static {
        sWorkerThread.start();
    }
    private static final Handler sWorker = new Handler(sWorkerThread.getLooper());

    public interface Callbacks {
        void onAllAppsLoaded();
    }

    private WeakReference<Callbacks> mCallbacks;

    public void initialize(Callbacks callbacks) {
        synchronized (mLock) {
            mCallbacks = new WeakReference<Callbacks>(callbacks);
        }
    }

    public LauncherModel(LauncherApplication app) {
        mApp = app;
    }

    public List<ResolveInfo> getAllApps() {
        return mAllApps;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: action=" + ((intent == null) ? "null" : intent.getAction()));
    }

    /**
     * load apps and/or widgets/shortcuts...
     */
    public void startLoader() {
        synchronized (mLock) {
            if (mCallbacks != null && mCallbacks.get() != null) {
                mLoaderTask = new LoaderTask(mApp);
                sWorkerThread.setPriority(Thread.NORM_PRIORITY);
                sWorker.post(mLoaderTask);
            }
        }
    }

    private class LoaderTask implements Runnable {
        private Context mContext;
        private HashMap<Object, CharSequence> mLabelCache;

        public LoaderTask(Context context) {
            mContext = context;
            mLabelCache = new HashMap<Object, CharSequence>();
        }

        @Override
        public void run() {
            Log.d(TAG, "LoaderTask.run()...");
            loadAllApps();

            // clear reference
            mContext = null;
        }

        private void loadAllApps() {
            final Callbacks oldCallbacks = mCallbacks.get();
            if (oldCallbacks == null) {
                // This launcher has exited and nobody bothered to tell us. Just
                // bail.
                Log.w(TAG, "LoaderTask running with no launcher (loadAllAppsByBatch)");
                return;
            }

            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            final PackageManager pm = mContext.getPackageManager();
            mAllApps = pm.queryIntentActivities(mainIntent, 0);
            // sort labels
            Collections.sort(mAllApps, new LabelComparator(pm, mLabelCache));
            oldCallbacks.onAllAppsLoaded();
        }
    }

    static ComponentName getComponentNameFromResolveInfo(ResolveInfo info) {
        if (info.activityInfo != null) {
            return new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
        } else {
            return new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name);
        }
    }

    private class LabelComparator implements Comparator<ResolveInfo> {
        private Collator mCollator;
        private PackageManager mPackageManager;
        private HashMap<Object, CharSequence> mLabelCache;

        LabelComparator(PackageManager pm, HashMap<Object, CharSequence> cache) {
            mPackageManager = pm;
            mLabelCache = cache;
            mCollator = Collator.getInstance();
        }

        @Override
        public int compare(ResolveInfo a, ResolveInfo b) {
            CharSequence labelA, labelB;
            ComponentName keyA = LauncherModel.getComponentNameFromResolveInfo(a);
            ComponentName keyB = LauncherModel.getComponentNameFromResolveInfo(b);
            if (mLabelCache.containsKey(keyA)) {
                labelA = mLabelCache.get(keyA);
            } else {
                labelA = a.loadLabel(mPackageManager).toString();

                mLabelCache.put(keyA, labelA);
            }
            if (mLabelCache.containsKey(keyB)) {
                labelB = mLabelCache.get(keyB);
            } else {
                labelB = b.loadLabel(mPackageManager).toString();

                mLabelCache.put(keyB, labelB);
            }
            return mCollator.compare(labelA, labelB);
        }

    }
}
