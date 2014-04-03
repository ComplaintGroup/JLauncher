package complaintgroup.jlauncher.model;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class AppInfo {
    public Drawable icon;
    public CharSequence name;
    public Intent intent;

    public AppInfo(PackageManager pm, ResolveInfo resolveInfo) {
        icon = resolveInfo.activityInfo.loadIcon(pm);
        name = resolveInfo.loadLabel(pm);
        intent = getIntent(resolveInfo);
    }

    public Intent getIntent(ResolveInfo resolveInfo) {
        String pkg = resolveInfo.activityInfo.packageName;
        String cls = resolveInfo.activityInfo.name;
        ComponentName componet = new ComponentName(pkg, cls);

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setComponent(componet);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return i;
    }
}
