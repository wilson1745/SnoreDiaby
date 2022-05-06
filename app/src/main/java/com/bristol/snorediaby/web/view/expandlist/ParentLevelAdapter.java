package com.bristol.snorediaby.web.view.expandlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bristol.snorediaby.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentLevelAdapter extends BaseExpandableListAdapter {

    private final Context mContext;

    private final List<String> mListDataHeader;

    private final Map<String, List<String>> mListData_SecondLevel_Map;

    private final Map<String, List<String>> mListData_ThirdLevel_Map;

    public ParentLevelAdapter(Context mContext, List<String> mListDataHeader) {
        this.mContext = mContext;
        this.mListDataHeader = new ArrayList<>();
        this.mListDataHeader.addAll(mListDataHeader);

        // SECOND LEVEL
        String[] mItemHeaders;
        mListData_SecondLevel_Map = new HashMap<>();
        for (String content : mListDataHeader) {
            switch (mListDataHeader.indexOf(content)) {
                case 0:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.instructions);
                    break;
                case 1:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.about_snoring);
                    break;
                case 2:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.about_diabetes);
                    break;
                case 3:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.about_light);
                    break;
                case 4:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.faqs);
                    break;
                default:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.items_array_expandable_level_two);
                    break;
            }
            mListData_SecondLevel_Map.put(content, Arrays.asList(mItemHeaders));
        }

        // THIRD LEVEL
        String[] mItemChildOfChild;
        List<String> listChild;
        mListData_ThirdLevel_Map = new HashMap<>();
        for (Object o : mListData_SecondLevel_Map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object object = entry.getValue();
            if (object instanceof List) {
                List<String> stringList = new ArrayList<>();
                Collections.addAll(stringList, (String[]) ((List) object).toArray());
                // Log.e("TAG", "stringList.size: " + stringList.size());

                for (int i = 0; i < stringList.size(); i++) {
                    String content = stringList.get(i);
                    //Log.e("TAG", "content: " + content);

                    switch (content) {
                        case "SnoreDiaby":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.introduction_cont);
                            break;
                        case "Placement":
                        case "手機放置":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.placement_cont);
                            break;
                        case "Results":
                        case "偵測結果":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.results_cont);
                            break;
                        case "Important Disclaimer":
                        case "重要聲明":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.important_disclaimer_cont);
                            break;

                        case "What is Snoring?":
                        case "什麼是打呼?":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.what_cont);
                            break;
                        case "Risk Factors":
                        case "打呼的危險因子":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.risk_cont);
                            break;
                        case "Obstructive Sleep Apnea":
                        case "睡眠呼吸中止(OSA)":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.obstructive_cont);
                            break;
                        case "Snoring Remedies":
                        case "治療打呼":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.remedies_cont);
                            break;

                        case "What is Type 2 Diabetes?":
                        case "什麼是第二型糖尿病?":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.what_t2d);
                            break;
                        case "Sleep & Type 2 Diabetes":
                        case "睡眠 & 第二型糖尿病":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.sleep_t2d);
                            break;
                        case "How To Improve It?":
                        case "如何改善?":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.advice_t2d);
                            break;

                        case "What is Light?":
                        case "什麼是亮度?":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.light_cont);
                            break;
                        case "Light & Type 2 Diabetes":
                        case "亮度 & 第二型糖尿病":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.lightT2D_cont);
                            break;

                        case "Device Positioning":
                        case "設備放置":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.device_cont);
                            break;
                        case "Disk Usage":
                        case "容量使用":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.disk_cont);
                            break;
                        //case "Exporting Sound Files":
                        // mItemChildOfChild = mContext.getResources().getStringArray(R.array.exporting_cont);
                        //break;
                        case "Two Snorers in the Room":
                        case "當有兩名打呼者時":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.snorers_cont);
                            break;
                        case "About SnoreDiaby":
                        case "關於SnoreDiaby":
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.about_cont);
                            break;

                        default:
                            mItemChildOfChild = mContext.getResources().getStringArray(R.array.null_cont);
                            break;
                    }
                    listChild = Arrays.asList(mItemChildOfChild);
                    mListData_ThirdLevel_Map.put(stringList.get(i), listChild);
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        String parentNode = (String) getGroup(groupPosition);

        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.mContext, mListData_SecondLevel_Map.get(parentNode), mListData_ThirdLevel_Map));
        secondLevelExpListView.setGroupIndicator(null);
        // 只能展開一個group 上一個group會關閉
        secondLevelExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup) {
                    secondLevelExpListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }

        });

        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.drawer_list_group, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.explist_indicator);

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        //lblListHeader.setTextColor(Color.BLACK);
        lblListHeader.setText(headerTitle);

        if (getChildrenCount(groupPosition) == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
