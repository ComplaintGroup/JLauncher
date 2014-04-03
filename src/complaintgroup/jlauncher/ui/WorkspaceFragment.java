package complaintgroup.jlauncher.ui;

import complaintgroup.jlauncher.R;
import complaintgroup.jlauncher.R.layout;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkspaceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.workspace, container, false);

        return rootView;

    }

}
