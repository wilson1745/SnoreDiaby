package com.bristol.snorediaby.web.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.common.exceptions.SnoreException;
import com.bristol.snorediaby.repo.domain.SnoreStorage;
import com.bristol.snorediaby.repo.domain.beans.OSABarChart;
import java.util.ArrayList;

public class OSAChartFragment extends Fragment {

    private static final String TAG = "OSAChartFragment";

    private View view;

    private ArrayList<SnoreStorage> snoreList;

    public OSAChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Start OSAChartFragment onCreateView");
        try {
            view = inflater.inflate(R.layout.fragment_osachart, container, false);
            Context context = getActivity();

            readSnoreList();
            OSABarChart osaBarChart = new OSABarChart(snoreList, context, view);
            osaBarChart.drawSnoreChart();
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        } finally {
            Log.i(TAG, "End OSAChartFragment onCreateView");
        }
        return view;
    }

    private void readSnoreList() {
        snoreList = new ArrayList<>();
        snoreList = (ArrayList<SnoreStorage>) getArguments().getSerializable("osaList");
    }

}
