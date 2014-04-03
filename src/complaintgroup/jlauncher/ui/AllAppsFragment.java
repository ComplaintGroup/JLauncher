package complaintgroup.jlauncher.ui;

import java.util.List;

import complaintgroup.jlauncher.R;
import complaintgroup.jlauncher.model.AllAppsAdapter;
import complaintgroup.jlauncher.model.AppInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class AllAppsFragment extends Fragment {
    private static final String TAG = "JLauncher.AllApps";

    private GridView mGridView = null;
    private AllAppsAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.all_apps, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.grid_view);
        if (mAdapter != null) {
            mGridView.setAdapter(mAdapter);
        }
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ((AllAppsAdapter) mGridView.getAdapter()).getItemIntent(position);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateAdapter(List<AppInfo> appInfos) {
        if (mAdapter == null) {
            mAdapter = new AllAppsAdapter(getActivity(), appInfos);
        } else {
            mAdapter.setAppsInfo(appInfos);
        }

        // this function may be called before onCreateView(), so...
        if (mGridView != null) {
            mGridView.setAdapter(mAdapter);
        }
    }
}
