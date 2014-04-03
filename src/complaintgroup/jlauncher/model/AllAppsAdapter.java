package complaintgroup.jlauncher.model;

import java.util.List;

import complaintgroup.jlauncher.R;
import complaintgroup.jlauncher.R.id;
import complaintgroup.jlauncher.R.layout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllAppsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AppInfo> mAllApps;

    public AllAppsAdapter(Context context, List<AppInfo> allApps) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mAllApps = allApps;
    }

    public void setAppsInfo(List<AppInfo> allApps) {
        if (allApps != null) {
            mAllApps = allApps;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mAllApps.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return mAllApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.all_apps_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.label = (TextView) view.findViewById(R.id.label);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AppInfo info = mAllApps.get(position);
        holder.icon.setImageDrawable(info.icon);
        holder.label.setText(info.name);
        return view;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView label;
    }

    public Intent getItemIntent(int position) {
        return mAllApps.get(position).intent;
    }
}
