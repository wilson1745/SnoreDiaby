package com.bristol.snorediaby.web.view.advance;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.repo.domains.Example;
import com.bristol.snorediaby.repo.domains.Example2;
import com.bristol.snorediaby.repo.domains.Example3;
import com.bristol.snorediaby.repo.domains.Example4;
import com.bristol.snorediaby.repo.sqllite.CustomSqlLite;
import com.bristol.snorediaby.web.view.activities.email.EmailActivity;
import com.bristol.snorediaby.web.view.activities.language.LanguageActivity;
import com.bristol.snorediaby.web.view.activities.personal.PersonalActivity;
import java.text.ParseException;

/**
 * Function: AdvanceFragment
 * Description:
 * Author: wilso
 * Date: 2022/5/6
 * MaintenancePersonnel: wilso
 */
public class AdvanceFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    private Intent intent;

    private View view;

    public AdvanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start AdvanceFragment onCreateView");
            final String[] Item = getResources().getStringArray(R.array.advance);

            context = getActivity();
            view = inflater.inflate(R.layout.fragment_advance, container, false);
            ListView listView = view.findViewById(R.id.ad_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, Item);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), PersonalActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        new AlertDialog.Builder(context)
                            .setTitle(R.string.warning)
                            .setMessage(R.string.delete_rec)
                            .setPositiveButton(R.string.yes_, (dialog, which) -> {
                                CustomSqlLite dbHelp = new CustomSqlLite(getActivity());
                                final SQLiteDatabase db = dbHelp.getWritableDatabase();
                                dbHelp.onUpgrade(db, 1, 2);
                                Toast.makeText(context, R.string.data_gone, Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton(R.string.no_, (dialog, which) -> {
                                //Do nothing
                            }).show();
                        break;
                    case 2:
                        intent = new Intent(getActivity(), LanguageActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Example example = new Example(context);
                        Example2 example2 = new Example2(context);
                        Example3 example3 = new Example3(context);
                        Example4 example4 = new Example4(context);
                        try {
                            example.run();
                            example2.run();
                            example3.run();
                            example4.run();
                            Toast.makeText(context, R.string.example_added, Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        intent = new Intent(getActivity(), EmailActivity.class);
                        startActivity(intent);
                        break;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "AdvanceFragment Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End AdvanceFragment onCreateView");
        }
        return view;
    }

}
