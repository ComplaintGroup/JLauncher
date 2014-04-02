package complaintgroup.jlauncher;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllAppsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ResolveInfo> mAllApps;

    public AllAppsAdapter(Context context, List<ResolveInfo> allApps) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mAllApps = allApps;
    }

    @Override
    public int getCount() {
        return mAllApps.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;

        if (convertView == null) {
            v = mInflater.inflate(R.layout.all_apps_item, null);
        } else {
            v = convertView;
        }

        ImageView icon = (ImageView) v.findViewById(R.id.icon);
        TextView label = (TextView) v.findViewById(R.id.label);

        PackageManager pm = mContext.getPackageManager();
        ResolveInfo info = mAllApps.get(position);
        icon.setImageDrawable(info.activityInfo.loadIcon(pm));
        label.setText(info.loadLabel(pm));

        return v;
    }

    public Intent getItemIntent(int position) {
        ResolveInfo info = mAllApps.get(position);

        String pkg = info.activityInfo.packageName;
        String cls = info.activityInfo.name;
        ComponentName componet = new ComponentName(pkg, cls);

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setComponent(componet);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return i;
    }
}
