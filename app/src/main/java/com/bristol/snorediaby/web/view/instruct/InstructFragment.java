package com.bristol.snorediaby.web.view.instruct;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.web.view.expandlist.ParentLevelAdapter;
import com.bristol.snorediaby.web.view.instruct.mvp.InstructModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class InstructFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private ExpandableListView mExpandableListView;

    private InstructModel model;

    public InstructFragment() {
        // Required empty public constructor
        this.model = new InstructModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "InstructFragment 初始化");
        View view = inflater.inflate(R.layout.fragment_instruct, container, false);
        Context context = getActivity();

        // Init top level data
        model.setmItemHeaders(this.getResources().getStringArray(R.array.instructions_main));
        List<String> listDataHeader = new ArrayList<>();
        Collections.addAll(listDataHeader, model.getmItemHeaders());

        // expand_main.xml
        mExpandableListView = view.findViewById(R.id.expandableListView_Parent);
        if (!Objects.equals(null, mExpandableListView)) {
            ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(context, listDataHeader);
            mExpandableListView.setAdapter(parentLevelAdapter);
        }

        mExpandableListView.setOnGroupExpandListener(groupPosition -> {
            AtomicInteger index = new AtomicInteger(0);
            for (String header : model.getmItemHeaders()) {
                if (!Objects.equals(index.get(), groupPosition)) {
                    mExpandableListView.collapseGroup(index.getAndIncrement());
                }
            }
        });

        return view;
    }

}
