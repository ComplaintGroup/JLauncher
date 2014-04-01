package complaintgroup.jlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class AllAppsFragment extends Fragment {
    
    private GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.all_apps, container, false);
        
        mGridView = (GridView) rootView.findViewById(R.id.grid_view);

        return rootView;
    }

}
